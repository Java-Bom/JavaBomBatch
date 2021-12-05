package com.javabom.definitiveguide.chap8;

import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@BatchSpringTest
class ItemProcessorAdapterConfigurationTest {


    @Autowired
    private TestJobLauncher testJobLauncher;
    private final String fileName = "testFile.csv";
    private File setupFile;

    @BeforeEach
    void setUp() {
        this.setupFile = new File(fileName);
    }

    @AfterEach
    public void tearDown() {
        this.setupFile.delete();
    }

    @Test
    @DisplayName("service 로직을 processor로 재활용할 수 있다 - 대문자 변환")
    void test(){
        //given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fileName", fileName)
                .toJobParameters();
        writeContents("cys,lastName");

        //when
        //then
        JobExecution jobExecution = testJobLauncher.launchJob(ItemProcessorAdapterConfiguration.JOB_NAME, jobParameters);
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }


    void writeContents(String... contents) {
        try (FileWriter pw = new FileWriter(fileName, true)) {
            for (String line : contents) {
                pw.write(line);
            }
            pw.flush();
        } catch (Exception e) {
            throw new IllegalStateException("write file");
        }
    }
}