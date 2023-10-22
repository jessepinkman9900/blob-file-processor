package com.demo.fileprocessing.controllers.models;

import com.demo.fileprocessing.controllers.models.enums.FileType;
import lombok.Data;

@Data
public class JobRequest {
  FileType fileType;
  // blob storage
  String sourceFilePath;
  String destinationFilePath;
}
