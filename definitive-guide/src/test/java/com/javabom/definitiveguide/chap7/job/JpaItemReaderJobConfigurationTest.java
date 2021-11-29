package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;
import com.javabom.service.domain.customer.entity.CustomerEntity;
import com.javabom.service.domain.customer.repository.CustomerJpaRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@BatchSpringTest
class JpaItemReaderJobConfigurationTest {
    private final TestJobLauncher jobLauncher;
    private final CustomerJpaRepository customerRepository;

    public JpaItemReaderJobConfigurationTest(TestJobLauncher jobLauncher, CustomerJpaRepository customerRepository) {
        this.jobLauncher = jobLauncher;
        this.customerRepository = customerRepository;
    }

    @BeforeEach
    void setUp() {
        List<String> cities = Lists.list("인천", "서울", "부산");

        for (int i = 0; i < 100; i++) {
            int cityIndex = i % 3;
            CustomerEntity customerEntity = CustomerEntity.builder()
                    .city(cities.get(cityIndex))
                    .build();
            customerRepository.save(customerEntity);
        }
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAllInBatch();
    }

    @Test
    void JPA_커서_리더_테스트() {
        //when
        JobExecution execution = jobLauncher.launchJob(JpaCursorItemReaderJobConfiguration.JOB_NAME,
                new JobParametersBuilder()
                        .addString("city", "인천")
                        .toJobParameters()
        );

        //then
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    void JPA_페이징_리더_테스트() {
        //when
        JobExecution execution = jobLauncher.launchJob(JpaPagingItemReaderJobConfiguration.JOB_NAME,
                new JobParametersBuilder()
                        .addString("city", "인천")
                        .toJobParameters()
        );

        //then
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}

