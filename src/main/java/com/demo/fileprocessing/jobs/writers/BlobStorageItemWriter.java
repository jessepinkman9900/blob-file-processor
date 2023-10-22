package com.demo.fileprocessing.jobs.writers;

import com.azure.storage.blob.specialized.AppendBlobClient;
import com.azure.storage.blob.specialized.BlobOutputStream;
import java.io.IOException;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class BlobStorageItemWriter implements ItemWriter<String>, StepExecutionListener {
  private AppendBlobClient blobClient;
  private BlobOutputStream os;

  public BlobStorageItemWriter(AppendBlobClient blobClient) {
    this.blobClient = blobClient;
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {
    os = blobClient.getBlobOutputStream();
  }

  @Override
  public void write(Chunk<? extends String> chunk) {
    for (String item : chunk.getItems()) {
      os.write(item.getBytes());
    }
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    try {
      os.flush();
      os.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return null;
  }
}
