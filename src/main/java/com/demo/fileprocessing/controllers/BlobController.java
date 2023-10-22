package com.demo.fileprocessing.controllers;

import com.demo.fileprocessing.controllers.models.JobRequest;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

/*
ONLY for testing
 */
@Profile("local")
@RestController
@RequestMapping("/blob")
public class BlobController {
  Logger logger = LoggerFactory.getLogger(JobController.class);
  BlobServiceClient client;

  @Autowired
  public BlobController(BlobServiceClient client) {
    this.client = client;
  }

  @PostMapping("/upload")
  public void uploadFile(@RequestBody JobRequest jobRequest) throws FileNotFoundException {
    String container =
        jobRequest
            .getDestinationFilePath()
            .substring(0, jobRequest.getDestinationFilePath().indexOf("/"));
    String name =
        jobRequest
            .getDestinationFilePath()
            .substring(jobRequest.getDestinationFilePath().lastIndexOf("/") + 1);
    BlobContainerClient containerClient = client.createBlobContainerIfNotExists(container);

    File file = new File(jobRequest.getSourceFilePath());
    FileInputStream fis = new FileInputStream(file);
    BlobClient blobClient = containerClient.getBlobClient(name);
    blobClient.upload(fis);
  }

  @GetMapping("/list/{container}")
  public List<String> listBlobs(@PathVariable String container) {
    List<String> blobs = new ArrayList<>();
    BlobContainerClient containerClient = client.getBlobContainerClient(container);
    containerClient.listBlobs().forEach(blob -> blobs.add(blob.getName()));
    return blobs;
  }
}
