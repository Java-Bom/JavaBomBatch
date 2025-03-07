package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.chap7.job.reader.CustomerFileReader;
import com.javabom.definitiveguide.chap7.mapper.TransactionFieldSetMapper;
import com.javabom.definitiveguide.chap7.model.CustomerTransactions;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
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
public class CustomReaderJobConfiguration {

    public static final String JOB_NAME = "chap7_custom_reader_job";
    public static final String STEP_NAME = "chap7_custom_reader_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job job() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(customTransactionLineTokenizerFileStep())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step customTransactionLineTokenizerFileStep() {
        return this.stepBuilderFactory.get(STEP_NAME)
                .<CustomerTransactions, CustomerTransactions>chunk(10)
                .reader(customerFileReader())
                .writer(patternMatchingItemWriter())
                .build();
    }

    @Bean
    public CustomerFileReader customerFileReader() {
        return new CustomerFileReader(customerTransactionsItemReader(null));
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

    @Bean(name = "custom_reader_line_tokenizer")
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

    @Bean(name = "custom_reader_transaction_line_tokenizer")
    public DelimitedLineTokenizer transactionLineTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("prefix", "accountNumber", "transactionDate", "amount");
        return lineTokenizer;
    }

    @Bean(name = "custom_item_writer")
    public ItemWriter patternMatchingItemWriter() {
        return (items -> items.forEach(System.out::println));
    }
}
