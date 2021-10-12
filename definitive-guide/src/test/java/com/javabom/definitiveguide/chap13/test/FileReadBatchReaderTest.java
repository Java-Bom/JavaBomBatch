package com.javabom.definitiveguide.chap13.test;

import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.BatchStepTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@BatchStepTest
class FileReadBatchReaderTest {

    @Autowired
    private FlatFileItemReader<Member> csvReader;
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
    void csvReader() throws Exception {
        //given
        writeContents("test1,19\n","test2,20\n");

        //when
        csvReader.open(new ExecutionContext());

        //then
        assertNotNull(this.csvReader.read());
        assertNotNull(this.csvReader.read());
    }

    public StepExecution getStepExecution() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fileName", fileName)
                .toJobParameters();
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
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