package com.raviteja.expense.batchprocessor;

import com.raviteja.expense.infrastructure.domain.entity.TransactionEntity;
import com.raviteja.expense.infrastructure.domain.repository.TransactionItemRepository;
import com.raviteja.expense.infrastructure.domain.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.PlatformTransactionManager;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    public static final String START_DATE = "2025-09-29 00:00:00";
    public static final String END_DATE = "2025-09-30 23:59:59";
    public static final String FILE_PATH = "statements/statement-sept-29-30.pdf";
    @Autowired
    private ExpenseJobWriter expenseJobWriter;
    @Autowired
    private TransactionItemRepository transactionItemRepository;
    @Autowired
    private UserRepository userRepository;

    @Bean
    public Job expenseJob(JobRepository jobRepository,
                          Step deleteActiveMonthTransactions,
                          Step expenseJobStep) {
        System.out.println("======== Job config is being called =========");
        return new JobBuilder("expenseJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(deleteActiveMonthTransactions)
                .next(expenseJobStep)
                .build();
    }

    @Bean
    public Step deleteActiveMonthTransactions(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("deleteActiveMonthTransactions", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("=== Deleting active month transactions ===");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDate startDate=LocalDate.parse(START_DATE,formatter);
                    LocalDate endDate=LocalDate.parse(END_DATE,formatter);
                    List<TransactionEntity> activeTransactions = transactionItemRepository.findByTransactionDateBetween(startDate, endDate);
                    System.out.println("=========== retrieved active transactions of size "+activeTransactions.size());
                    transactionItemRepository.deleteAll(activeTransactions);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step expenseJobStep(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager) throws Exception {
        System.out.println("========= step is being executed ===========");
        return new StepBuilder("expenseJobStep", jobRepository)
                .<Transaction, TransactionEntity>chunk(10, transactionManager)
                .reader(expenseJobReader())
                .processor(expenseJobProcessor())
                .writer(expenseJobWriter)
                .build();
    }

    @Bean
    public ExpenseJobReader expenseJobReader() throws Exception {
        System.out.println("======= in reader bean =======");
        return new ExpenseJobReader(FILE_PATH);
    }

    @Bean
    public ExpenseJobProcessor expenseJobProcessor() {
        return new ExpenseJobProcessor(userRepository);
    }
}
