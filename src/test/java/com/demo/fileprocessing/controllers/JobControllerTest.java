package com.demo.fileprocessing.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.fileprocessing.controllers.models.JobRequest;
import com.demo.fileprocessing.controllers.models.enums.FileType;
import com.demo.fileprocessing.services.azure.BlobService;
import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class JobControllerTest {

  @Container
  public static final DockerComposeContainer dockerComposeContainer =
      new DockerComposeContainer(new File("docker-compose.yaml"))
          .withExposedService("postgres", 5432)
          .withExposedService("azurite", 10000)
          .withLocalCompose(false);

  @Autowired private MockMvc mvc;

  @Autowired private BlobService blobService;

  @BeforeEach
  public void loadFile() throws FileNotFoundException {
    blobService.uploadFile(
        "src/main/resources/data/input/fixed_length.txt", "test-data/fixed_length.txt");
  }

  @AfterAll
  public static void cleanUp() {
    dockerComposeContainer.stop();
  }

  @Test
  public void testFlatFileJobController() throws Exception {
    JobRequest jobRequest = new JobRequest();
    jobRequest.setFileType(FileType.FIXED);
    jobRequest.setSourceFilePath("test-data/fixed_length.txt");
    jobRequest.setDestinationFilePath("test-data/fixed_length_destination.txt");

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json = ow.writeValueAsString(jobRequest);

    mvc.perform(
            MockMvcRequestBuilders.post("/job")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(System.out::println)
        .andExpect(status().isOk());
  }
}
