package com.javabom.definitiveguide.chap8;

import com.javabom.definitiveguide.chap8.ValidatingConfiguration;
import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;
import org.junit.jupiter.api.*;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@BatchSpringTest
class ValidatingConfigurationTest {


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
    @DisplayName("bean valid 확인")
   void test(){
        //given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fileName", fileName)
                .toJobParameters();
        writeContents("사이즈가 10을 넘는다,lastName");

        //when
        //then
        JobExecution jobExecution = testJobLauncher.launchJob(ValidatingConfiguration.JOB_NAME, jobParameters);
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.FAILED);
    }

    @Test
    @DisplayName("custom valid 확인")
    void test2(){
        //given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fileName", fileName)
                .addString("processorType","UniqueLastNameValid")
                .toJobParameters();
        writeContents("cys,lastName\n","cys2,lastName");

        //when
        //then
        JobExecution jobExecution = testJobLauncher.launchJob(ValidatingConfiguration.JOB_NAME, jobParameters);
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.FAILED);
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