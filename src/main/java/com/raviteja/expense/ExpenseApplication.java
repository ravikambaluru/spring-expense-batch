package com.raviteja.expense;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpenseApplication implements CommandLineRunner {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job expenseJob;

	public static void main(String[] args) {
		SpringApplication.run(ExpenseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		JobParameters params = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
		jobLauncher.run(expenseJob, params);
	}
}
