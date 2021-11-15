package com.javabom.definitiveguide.chap5.job;

import com.javabom.definitiveguide.chap6.domain.Transaction;
import com.javabom.definitiveguide.chap6.job.TransactionProcessingJob;
import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("mysql")
@BatchSpringTest
public class TransactionProcessingJobTest {

    private static final String INSERT_SQL = "INSERT INTO ACCOUNT_SUMMARY(account_number, current_balance) VALUES (?, ?)";
    @Autowired
    private TestJobLauncher testJobLauncher;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void migrationTransactionAccountNumber() throws IOException {
        Path path = Paths.get("src/main/resources/input/transactionFile.csv");
        File transactionFile = new ClassPathResource("input/transactionFile.csv").getFile();
        String[] transactions = new String(Files.readAllBytes(transactionFile.toPath())).split("\n");
        for (String transaction : transactions) {
            String[] row = transaction.split(",");
            Transaction t = new Transaction();
            t.setAccountNumber(row[0]);
            t.setAmount(0);
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement preparedStatement = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, row[0]);
                    preparedStatement.setDouble(2, 0);
                    return preparedStatement;
                }
            });
        }
    }

    @Test
    public void transactionTest() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("transactionFile", "input/transactionFile.csv")
                .addString("summaryFile", "file:///{summaryFilePath}")
                .toJobParameters();
        JobExecution jobExecution = testJobLauncher.launchJob(TransactionProcessingJob.JOB_NAME, jobParameters);

        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}