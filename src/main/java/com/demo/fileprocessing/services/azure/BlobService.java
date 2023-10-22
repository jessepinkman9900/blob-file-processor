package com.demo.fileprocessing.services.azure;

import com.demo.fileprocessing.utils.azure.PathUtils;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.specialized.AppendBlobClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlobService {
  @Autowired BlobServiceClient client;

  public BlobClient getBlobClient(String path) {
    String container = PathUtils.getContainerName(path);
    String blobName = PathUtils.getBlobName(path);
    BlobContainerClient containerClient = client.getBlobContainerClient(container);
    return containerClient.getBlobClient(blobName);
  }

  public AppendBlobClient getAppendBlobClient(String path) {
    String container = PathUtils.getContainerName(path);
    String blobName = PathUtils.getBlobName(path);
    BlobContainerClient containerClient = client.getBlobContainerClient(container);
    BlobClient blobClient = containerClient.getBlobClient(blobName);
    AppendBlobClient appendBlobClient = blobClient.getAppendBlobClient();
    appendBlobClient.createIfNotExists();
    return appendBlobClient;
  }

  public void uploadFile(String sourcePath, String path) throws FileNotFoundException {
    String container = PathUtils.getContainerName(path);
    String blobName = PathUtils.getBlobName(path);
    BlobContainerClient containerClient = client.createBlobContainerIfNotExists(container);

    File file = new File(sourcePath);
    FileInputStream fis = new FileInputStream(file);
    BlobClient blobClient = containerClient.getBlobClient(blobName);
    blobClient.upload(fis);
  }

  public void deleteFile(String path) {
    String container = PathUtils.getContainerName(path);
    String blobName = PathUtils.getBlobName(path);
    BlobContainerClient containerClient = client.createBlobContainerIfNotExists(container);
    BlobClient blobClient = containerClient.getBlobClient(blobName);
    blobClient.delete();
  }
}
