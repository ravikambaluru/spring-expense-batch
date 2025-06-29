package com.raviteja.expense.batchprocessor;

import com.raviteja.expense.infrastructure.domain.entity.TransItemEntity;
import com.raviteja.expense.infrastructure.domain.repository.TransactionItemRepository;
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
import org.springframework.transaction.PlatformTransactionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    private ExpenseJobWriter expenseJobWriter;
    @Autowired
    private TransactionItemRepository transactionItemRepository;

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
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date startDate=formatter.parse("2025-05-01 00:00:00");
                    Date endDate=formatter.parse("2025-05-31 23:59:59");
                    List<TransItemEntity> activeTransactions = transactionItemRepository.findByTransactionDateBetween(startDate, endDate);
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
                .<Transaction, TransItemEntity>chunk(10, transactionManager)
                .reader(expenseJobReader())
                .processor(expenseJobProcessor())
                .writer(expenseJobWriter)
                .build();
    }

    @Bean
    public ExpenseJobReader expenseJobReader() throws Exception {
        System.out.println("======= in reader bean =======");
        return new ExpenseJobReader("statements/statement-may.pdf");
    }

    @Bean
    public ExpenseJobProcessor expenseJobProcessor() {
        return new ExpenseJobProcessor();
    }
}
