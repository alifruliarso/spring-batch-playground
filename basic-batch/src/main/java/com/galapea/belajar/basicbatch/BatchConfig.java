package com.galapea.belajar.basicbatch;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<Customer> customerReader() {
        return new FlatFileItemReaderBuilder<Customer>().name("customerItemReader")
                .resource(new ClassPathResource("sample-data.csv")).delimited()
                .names(new String[] {"firstName", "lastName"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {
                    {
                        setTargetType(Customer.class);
                    }
                }).build();
    }

    @Bean
    public CustomerItemProcessor processor() {
        return new CustomerItemProcessor();

    }

    @Bean
    public JdbcBatchItemWriter<Customer> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO customer (first_name, last_name) VALUES(:firstName, :lastName)")
                .dataSource(dataSource).build();
    }

    @Bean
    public Job importCustomerJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importCustomerJob").listener(listener)
                .incrementer(new RunIdIncrementer()).flow(step1).end().build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Customer> writer) {
        return stepBuilderFactory.get("step1").<Customer, Customer>chunk(10)
                .reader(customerReader()).processor(processor()).writer(writer).build();
    }
}
