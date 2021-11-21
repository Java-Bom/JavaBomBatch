package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.chap7.model.CustomerXML;
import com.javabom.definitiveguide.chap7.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@RequiredArgsConstructor
public class XMLJobConfiguration {

    public static final String JOB_NAME = "chap7_XML_job";
    public static final String STEP_NAME = "chap7_XML_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job job() {
        return this.jobBuilderFactory.get("job")
                .start(XMLStep())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step XMLStep() {
        return this.stepBuilderFactory.get(STEP_NAME)
                .<CustomerXML, CustomerXML>chunk(10)
                .reader(XMLFileReader(null))
                .writer(XMLitemWriter())
                .build();
    }

    @Bean
    @StepScope
    public StaxEventItemReader<CustomerXML> XMLFileReader(
            @Value("#{jobParameters['customerFile']}") ClassPathResource inputFile) {

        return new StaxEventItemReaderBuilder<CustomerXML>()
                .name("XMLFileReader")
                .resource(inputFile)
                .addFragmentRootElements("customer")
                .unmarshaller(customerMarshaller())
                .build();
    }

    @Bean
    public Jaxb2Marshaller customerMarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(new Class[]{CustomerXML.class, Transaction.class});
        return jaxb2Marshaller;
    }

    @Bean
    public ItemWriter XMLitemWriter() {
        return (items) -> items.forEach(System.out::println);
    }
}
