package com.javabom.definitiveguide.chap13.test;

import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.BatchStepTest;
import com.javabom.definitiveguide.test.TestJobLauncher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@BatchStepTest
public class FileReadBatchStepTest {

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
    void stepTest() {
        //given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fileName", fileName)
                .toJobParameters();
        writeContents("test,1");

        //when
        JobExecution execution = testJobLauncher.launchStep(FileReadBatchConfiguration.JOB_NAME,
                FileReadBatchConfiguration.STEP_NAME,
                jobParameters);

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
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
