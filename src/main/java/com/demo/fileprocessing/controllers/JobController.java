package com.demo.fileprocessing.controllers;

import com.demo.fileprocessing.controllers.models.JobRequest;
import com.demo.fileprocessing.services.jobs.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {
  Logger logger = LoggerFactory.getLogger(JobController.class);
  JobService jobService;

  @Autowired
  public JobController(JobService jobService) {
    this.jobService = jobService;
  }

  @PostMapping
  public JobInstance createJob(@RequestBody JobRequest request) {
    try {
      return jobService.buildAndTriggerJob(request);
    } catch (Exception e) {
      logger.error(e.toString());
      throw new RuntimeException(e);
    }
  }
}
