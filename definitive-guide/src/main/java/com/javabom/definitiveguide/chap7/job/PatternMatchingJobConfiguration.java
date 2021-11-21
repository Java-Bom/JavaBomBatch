package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.chap7.job.reader.CustomerFileReader;
import com.javabom.definitiveguide.chap7.mapper.TransactionFieldSetMapper;
import com.javabom.definitiveguide.chap7.model.CustomerAddress;
import com.javabom.definitiveguide.chap7.model.CustomerTransactions;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PatternMatchingJobConfiguration {

    public static final String JOB_NAME = "chap7_pattern_matching_job";
    public static final String STEP_NAME = "chap7_pattern_matching_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job job() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(customMultiFileStep())
                .build();
    }

    @Bean(name = "first" + STEP_NAME)
    public Step customAddressLineTokenizerFileStep() {
        return this.stepBuilderFactory.get("first" + STEP_NAME)
                .<CustomerAddress, CustomerAddress>chunk(10)
                .reader(patternMatchingItemReader(null))
                .writer(patternMatchingItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader patternMatchingItemReader(
            @Value("#{jobParameters['customerFile']}") ClassPathResource inputFile) {
        return new FlatFileItemReaderBuilder<CustomerAddress>()
                .name("patternMatchingItemReader")
                .lineMapper(patternMatchingLineMapper())
                .resource(inputFile)
                .build();
    }

    @Bean
    public PatternMatchingCompositeLineMapper patternMatchingLineMapper() {
        Map<String, LineTokenizer> lineTokenizerMap = new HashMap<>(2);
        lineTokenizerMap.put("CUST*", customerLineTokenizer()); //고객 정보 포맷
        lineTokenizerMap.put("TRANS*", transactionLineTokenizer()); //거래 레코드 포맷

        Map<String, FieldSetMapper> fieldSetMapperMap = new HashMap<>(2);
        BeanWrapperFieldSetMapper<CustomerAddress> customerFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        customerFieldSetMapper.setTargetType(CustomerAddress.class);

        fieldSetMapperMap.put("CUST*", customerFieldSetMapper);
        fieldSetMapperMap.put("TRANS*", new TransactionFieldSetMapper());

        PatternMatchingCompositeLineMapper lineMapper = new PatternMatchingCompositeLineMapper();
        lineMapper.setTokenizers(lineTokenizerMap);
        lineMapper.setFieldSetMappers(fieldSetMapperMap);

        return lineMapper;
    }

    @Bean(name = "second" + STEP_NAME)
    public Step customTransactionLineTokenizerFileStep() {
        return this.stepBuilderFactory.get("second" + STEP_NAME)
                .<CustomerTransactions, CustomerTransactions>chunk(10)
                .reader(customerFileReader())
                .writer(patternMatchingItemWriter())
                .build();
    }

    @Bean
    public CustomerFileReader customerFileReader() {
        return new CustomerFileReader(customerMultiItemReader());
    }

    @Bean
    public PatternMatchingCompositeLineMapper customerTransactionsLineMapper() {
        Map<String, LineTokenizer> lineTokenizerMap = new HashMap<>(2);
        lineTokenizerMap.put("CUST*", customerLineTokenizer()); //고객 정보 포맷
        lineTokenizerMap.put("TRANS*", transactionLineTokenizer()); //거래 레코드 포맷

        Map<String, FieldSetMapper> fieldSetMapperMap = new HashMap<>(2);
        BeanWrapperFieldSetMapper<CustomerTransactions> customerFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        customerFieldSetMapper.setTargetType(CustomerTransactions.class);

        fieldSetMapperMap.put("CUST*", customerFieldSetMapper);
        fieldSetMapperMap.put("TRANS*", new TransactionFieldSetMapper());

        PatternMatchingCompositeLineMapper lineMapper = new PatternMatchingCompositeLineMapper();
        lineMapper.setTokenizers(lineTokenizerMap);
        lineMapper.setFieldSetMappers(fieldSetMapperMap);

        return lineMapper;
    }

    @Bean
    public FlatFileItemReader customerMultiItemReader() {
        return new FlatFileItemReaderBuilder<CustomerTransactions>()
                .name("customerMultiItemReader")
                .lineMapper(customerTransactionsLineMapper())
                .build();
    }

    @Bean(name = "third" + STEP_NAME)
    public Step customMultiFileStep() {
        return this.stepBuilderFactory.get("third" + STEP_NAME)
                .<CustomerTransactions, CustomerTransactions>chunk(10)
                .reader(customerMultiFileItemReader(null))
                .writer(patternMatchingItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public MultiResourceItemReader customerMultiFileItemReader(
            @Value("#{jobParameters['customerFile']}") ClassPathResource[] inputFiles) {
        return new MultiResourceItemReaderBuilder<>()
                .name("customerMultiFileItemReader")
                .resources(inputFiles)
                .delegate(customerMultiFileReader())
                .build();
    }

    @Bean
    public CustomerFileReader customerMultiFileReader() {
        return new CustomerFileReader(customerMultiItemReader());
    }

    @Bean
    @StepScope
    public FlatFileItemReader customerTransactionsItemReader(
            @Value("#{jobParameters['customerFile']}") ClassPathResource inputFile) {
        return new FlatFileItemReaderBuilder<CustomerTransactions>()
                .name("customerTransactionsItemReader")
                .lineMapper(customerTransactionsLineMapper())
                .resource(inputFile)
                .build();
    }

    @Bean
    public DelimitedLineTokenizer customerLineTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("firstName",
                "middleInitial",
                "lastName",
                "address",
                "city",
                "state",
                "zipCode");
        lineTokenizer.setIncludedFields(1, 2, 3, 4, 5, 6, 7); //prefix 무시 (인덱스 시작: 0)
        return lineTokenizer;
    }

    @Bean
    public DelimitedLineTokenizer transactionLineTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("prefix", "accountNumber", "transactionDate", "amount");
        return lineTokenizer;
    }

    @Bean(name = "pattern_matching_item_writer")
    public ItemWriter patternMatchingItemWriter() {
        return (items -> items.forEach(System.out::println));
    }
}
