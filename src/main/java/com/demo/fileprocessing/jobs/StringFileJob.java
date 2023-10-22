package com.demo.fileprocessing.jobs;

import com.demo.fileprocessing.jobs.readers.BlobStorageEncryptionItemReader;
import com.demo.fileprocessing.jobs.readers.BlobStorageItemReader;
import com.demo.fileprocessing.jobs.writers.BlobStorageEncryptionItemWriter;
import com.demo.fileprocessing.jobs.writers.BlobStorageItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StringFileJob {
  PlatformTransactionManager transactionManager;
  BlobStorageItemReader stringBlobItemReader;
  BlobStorageEncryptionItemReader stringBlobEncryptionItemReader;
  BlobStorageItemWriter stringBlobItemWriter;
  BlobStorageEncryptionItemWriter stringBlobEncryptionItemWriter;
  ItemProcessor<String, String> stringProcessor;

  @Autowired
  public StringFileJob(
      PlatformTransactionManager transactionManager,
      BlobStorageItemReader stringBlobItemReader,
      BlobStorageEncryptionItemReader stringBlobEncryptionItemReader,
      BlobStorageItemWriter stringBlobItemWriter,
      BlobStorageEncryptionItemWriter stringBlobEncryptionItemWriter,
      ItemProcessor<String, String> stringProcessor) {
    this.transactionManager = transactionManager;
    this.stringBlobItemReader = stringBlobItemReader;
    this.stringBlobEncryptionItemReader = stringBlobEncryptionItemReader;
    this.stringBlobItemWriter = stringBlobItemWriter;
    this.stringBlobEncryptionItemWriter = stringBlobEncryptionItemWriter;
    this.stringProcessor = stringProcessor;
  }

  @Bean
  public Job stringBlobFlatFileJob(JobRepository jobRepository) {
    return new JobBuilder("stringFileJob", jobRepository)
        .start(stringBlobCapitalisationStep(jobRepository))
        .build();
  }

  @Bean
  public Step stringBlobCapitalisationStep(JobRepository jobRepository) {
    return new StepBuilder("stringCapitalisationStep", jobRepository)
        .<String, String>chunk(5, transactionManager)
        .reader(stringBlobItemReader)
        .processor(stringProcessor)
        .writer(stringBlobItemWriter)
        .faultTolerant()
        .retry(Exception.class)
        .retryLimit(3)
        .build();
  }
}
