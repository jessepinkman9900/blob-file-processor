package com.demo.fileprocessing.services.jobs;

import com.demo.fileprocessing.controllers.models.JobRequest;
import com.demo.fileprocessing.controllers.models.enums.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {
  Logger logger = LoggerFactory.getLogger(JobService.class);
  Job stringBlobFlatFileJob;
  JobLauncher jobLauncher;

  @Autowired
  public JobService(Job stringBlobFlatFileJob, JobLauncher jobLauncher) {
    this.stringBlobFlatFileJob = stringBlobFlatFileJob;
    this.jobLauncher = jobLauncher;
  }

  public JobInstance buildAndTriggerJob(JobRequest jobRequest) {
    JobParameters jobParameters = buildJobParameters(jobRequest);

    try {
      final JobExecution run = jobLauncher.run(stringBlobFlatFileJob, jobParameters);
      return run.getJobInstance();
    } catch (Exception e) {
      logger.error("Error running job: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private JobParameters buildJobParameters(JobRequest jobRequest) {
    if (jobRequest.getFileType().equals(FileType.FIXED)) {
      return buildFixedFileJobParameters(jobRequest);
    } else {
      throw new RuntimeException(
          "Cannot build JobParameters for unsupported file type: " + jobRequest.getFileType());
    }
  }

  private JobParameters buildFixedFileJobParameters(JobRequest jobRequest) {
    // get metadata
    JobParameters jobParameters =
        new JobParametersBuilder()
            .addString("file_type", jobRequest.getFileType().toString())
            .addString("source", jobRequest.getSourceFilePath())
            .addString("target", jobRequest.getDestinationFilePath())
            .toJobParameters();
    // modify job parameters to add additional metadata for downstream steps
    return jobParameters;
  }
}
