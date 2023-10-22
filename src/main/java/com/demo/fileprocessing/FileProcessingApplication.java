package com.demo.fileprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FileProcessingApplication {

  /*
   * todo: use tasklets - LineReader with BeforeStep decryption
   * todo: use takslets - LineWriter with AfterStep encryption
   */
  public static void main(String[] args) {
    SpringApplication.run(FileProcessingApplication.class, args);
  }
}
