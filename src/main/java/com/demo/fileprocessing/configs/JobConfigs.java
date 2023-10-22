package com.demo.fileprocessing.configs;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing(tablePrefix = "BATCH_")
public class JobConfigs {

  // @Bean
  // public DataSource dataSource() {
  // return DataSourceBuilder.create().build();
  // }

  // @Bean
  // public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource
  // dataSource) {
  // return new JdbcTransactionManager(dataSource);
  // }

  // @Bean
  // public JobRepository jobRepository(@Qualifier("dataSource") DataSource dataSource,
  // PlatformTransactionManager
  // transactionManager) throws Exception {
  // JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
  // factory.setDataSource(dataSource);
  // factory.setTransactionManager(transactionManager);
  // factory.afterPropertiesSet();
  // return factory.getObject();
  // }

  @Bean
  public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
    TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
    jobLauncher.setJobRepository(jobRepository);
    jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }
}
