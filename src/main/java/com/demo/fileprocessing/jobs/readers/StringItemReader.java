package com.demo.fileprocessing.jobs.readers;

import com.demo.fileprocessing.services.azure.BlobService;
import com.azure.storage.blob.BlobClient;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StringItemReader {
  @Autowired
  BlobService blobService;

  @Bean
  @StepScope
  public BlobStorageItemReader stringBlobItemReader(
      @Value("#{jobParameters['source']}") String source) {
    BlobClient blobClient = blobService.getBlobClient(source);
    return new BlobStorageItemReader(blobClient);
  }

  @Bean
  @StepScope
  public BlobStorageEncryptionItemReader stringBlobEncryptionItemReader(
      @Value("#{jobParameters['source']}") String source) {
    BlobClient blobClient = blobService.getBlobClient(source);
    return new BlobStorageEncryptionItemReader(blobClient);
  }
}
