package com.demo.fileprocessing.jobs.readers;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.specialized.BlobInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;

public class BlobStorageItemReader implements ItemReader<String>, StepExecutionListener {

  private BlobClient blobClient;
  private BlobInputStream is;
  private BufferedReader br;

  public BlobStorageItemReader(BlobClient blobClient) {
    this.blobClient = blobClient;
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {
    BlobInputStream is = blobClient.openInputStream();
    br = new BufferedReader(new InputStreamReader(is));
    System.out.println("Opened input stream");
  }

  @Override
  public String read() {
    try {
      return br.readLine();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public org.springframework.batch.core.ExitStatus afterStep(StepExecution stepExecution) {
    try {
      br.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return null;
  }
}
