package com.demo.fileprocessing.jobs.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StringProcessor implements ItemProcessor<String, String> {
  @Override
  public String process(String item) {
    System.out.println("Processing item: " + item);
    if (item.isEmpty()) {
      return item;
    }

    // Update the item as required
    item = item.toUpperCase();

    // reader removes \n
    // append new line character
    item = item.concat("\n");
    return item;
  }
}
