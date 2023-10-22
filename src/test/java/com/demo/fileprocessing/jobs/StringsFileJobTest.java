package com.demo.fileprocessing.jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.fileprocessing.services.azure.BlobService;
import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBatchTest
@EnableAutoConfiguration
@TestExecutionListeners({
  DependencyInjectionTestExecutionListener.class,
  DirtiesContextTestExecutionListener.class
})
@Testcontainers
public class StringsFileJobTest {
  @Container
  public static final DockerComposeContainer dockerComposeContainer =
      new DockerComposeContainer(new File("docker-compose.yaml"))
          .withExposedService("postgres", 5432)
          .withExposedService("azurite", 10000)
          .withLocalCompose(false);

  @Autowired private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  @Qualifier(value = "syncJobLauncher")
  private JobLauncher jobLauncher;

  @Autowired private BlobService blobService;

  @BeforeEach
  public void loadFile() throws FileNotFoundException {
    blobService.uploadFile(
        "src/main/resources/data/input/fixed_length.txt", "test-data/fixed_length.txt");
  }

  @AfterEach
  public void deleteFile() {
    blobService.deleteFile("test-data/fixed_length_destination.txt");
  }

  @AfterAll
  public static void cleanUp() {
    dockerComposeContainer.stop();
  }

  @Test
  public void testStringBlobFileJob(@Autowired Job stringBlobFlatFileJob) throws Exception {
    JobParameters jobParameters =
        new JobParametersBuilder()
            .addString("file_type", "FIXED")
            .addString("source", "test-data/" + "fixed_length.txt")
            .addString("target", "test-data/" + "fixed_length_destination.txt")
            .toJobParameters();

    jobLauncherTestUtils.setJobLauncher(jobLauncher);
    jobLauncherTestUtils.setJob(stringBlobFlatFileJob);
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
    assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
  }
}
