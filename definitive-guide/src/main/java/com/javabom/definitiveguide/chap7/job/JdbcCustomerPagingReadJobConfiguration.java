package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.chap7.mapper.CustomerJdbcRowMapper;
import com.javabom.definitiveguide.chap7.model.CustomerJdbc;
import com.sun.istack.internal.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JdbcCustomerPagingReadJobConfiguration {
    public static final String JOB_NAME = "JdbcCustomerPagingReadJob";
    private static final String STEP_NAME = JOB_NAME + "Step";
    private static final String PAGING_READER_NAME = STEP_NAME + "Reader";
    private static final String WRITER_READER_NAME = STEP_NAME + "Writer";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(JOB_NAME)
    public Job jdbcCustomerPagingReadJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(jdbcCustomerPagingReadStep())
                .build();
    }

    @Bean(STEP_NAME)
    public Step jdbcCustomerPagingReadStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<CustomerJdbc, CustomerJdbc>chunk(100)
                .reader(customerJdbcPagingItemReader(null, null, null))
                .writer(customerJdbcItemWriter())
                .build();
    }

    @StepScope
    @Bean(PAGING_READER_NAME)
    public JdbcPagingItemReader<CustomerJdbc> customerJdbcPagingItemReader(
            DataSource dataSource,
            PagingQueryProvider queryProvider,
            @Value("#{jobParameters['city']}") String city
    ) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("city", city);

        return new JdbcPagingItemReaderBuilder<CustomerJdbc>()
                .name(PAGING_READER_NAME)
                .dataSource(dataSource)
                .queryProvider(queryProvider)
                .parameterValues(parameters)
                .pageSize(10)
                .rowMapper(new CustomerJdbcRowMapper())
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean pagingQueryProviderFactoryBean(DataSource dataSource) {
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();

        factoryBean.setSelectClause("SELECT *");
        factoryBean.setFromClause("FROM customer_jdbc");
        factoryBean.setWhereClause("WHERE city = :city");
        factoryBean.setSortKey("lastName");
        factoryBean.setDataSource(dataSource);
        //factoryBean.setDatabaseType("H2");

        return factoryBean;
    }

    @Bean(WRITER_READER_NAME)
    public ItemWriter<CustomerJdbc> customerJdbcItemWriter() {
        return items -> items.forEach(item -> log.info(item.toString()));
    }
}
