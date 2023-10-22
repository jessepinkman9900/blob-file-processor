package com.demo.fileprocessing.jobs.writers;

import com.demo.fileprocessing.services.azure.BlobService;
import com.azure.storage.blob.specialized.AppendBlobClient;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StringItemWriter {
  @Autowired BlobService blobService;

  @Bean
  @StepScope
  public BlobStorageItemWriter stringBlobItemWriter(
      @Value("#{jobParameters['target']}") String target) {
    AppendBlobClient appendBlobClient = blobService.getAppendBlobClient(target);
    return new BlobStorageItemWriter(appendBlobClient);
  }

  @Bean
  @StepScope
  public BlobStorageEncryptionItemWriter stringBlobEncryptionItemWriter(
      @Value("#{jobParameters['target']}") String target) {
    AppendBlobClient appendBlobClient = blobService.getAppendBlobClient(target);
    return new BlobStorageEncryptionItemWriter(appendBlobClient);
  }
}
