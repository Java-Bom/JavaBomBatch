package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.config.datasource.CustomDataSourceTemplate;
import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@BatchSpringTest
class JdbcCustomerPagingReadJobConfigurationTest {

    private final TestJobLauncher jobLauncher;
    private final CustomDataSourceTemplate customDataSourceTemplate;

    public JdbcCustomerPagingReadJobConfigurationTest(TestJobLauncher jobLauncher, CustomDataSourceTemplate customDataSourceTemplate) {
        this.jobLauncher = jobLauncher;
        this.customDataSourceTemplate = customDataSourceTemplate;
    }

    @AfterEach
    void tearDown() {
        customDataSourceTemplate.runScripts("delete from customer_jdbc");
    }

    @Test
    void JDBC_페이징방식으로_읽기() {
        //given
        for (int i = 0; i < 100; i++) {
            customDataSourceTemplate.runScripts(
                    "insert into customer_jdbc (" +
                            "firstName, " +
                            "middleInitial, " +
                            "lastName, " +
                            "address, " +
                            "street, " +
                            "city, " +
                            "state, " +
                            "zipCode" +
                            ")" +
                            "values ('name','middle','last" + i + "'" + ",'address','street','city','state','zipcode')"
            );
        }

        //when
        JobExecution execution = jobLauncher.launchJob(JdbcCustomerPagingReadJobConfiguration.JOB_NAME,
                new JobParametersBuilder()
                        .addString("city", "city")
                        .toJobParameters());

        //then
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}
