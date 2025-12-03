package io.github.erp.erp.leases.liability.enumeration.batch;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationRequest;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.ResourcelessTransactionManager;


@Configuration
public class LiabilityEnumerationBatchConfiguration {

    public static final String JOB_NAME = "liabilityEnumerationJob";
    public static final String STEP_NAME = "liabilityEnumerationStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public LiabilityEnumerationBatchConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager != null ? transactionManager : new ResourcelessTransactionManager();
    }

    @Bean
    @StepScope
    public ListItemReader<LiabilityEnumerationBatchItem> liabilityEnumerationReader(
        @Value("#{jobParameters['leaseContractId']}") Long leaseContractId,
        @Value("#{jobParameters['leasePaymentUploadId']}") Long leasePaymentUploadId,
        @Value("#{jobParameters['interestRate']}") String interestRate,
        @Value("#{jobParameters['timeGranularity']}") String timeGranularity,
        @Value("#{jobParameters['active']}") String active
    ) {
        LiabilityEnumerationBatchItem item = new LiabilityEnumerationBatchItem(new LiabilityEnumerationRequest());
        item.getRequest().setLeaseContractId(leaseContractId);
        item.getRequest().setLeasePaymentUploadId(leasePaymentUploadId);
        item.getRequest().setInterestRate(interestRate);
        item.getRequest().setTimeGranularity(timeGranularity);
        item.getRequest().setActive(Boolean.valueOf(active));
        return new ListItemReader<>(java.util.List.of(item));
    }

    @Bean
    public CompositeItemProcessor<LiabilityEnumerationBatchItem, LiabilityEnumerationBatchItem> liabilityEnumerationProcessor(
        @Qualifier("liabilityEnumerationValidationProcessor") ItemProcessor<LiabilityEnumerationBatchItem, LiabilityEnumerationBatchItem> validationProcessor,
        @Qualifier("liabilityEnumerationLookupProcessor") ItemProcessor<LiabilityEnumerationBatchItem, LiabilityEnumerationBatchItem> lookupProcessor,
        @Qualifier("liabilityEnumerationQueueProcessor") ItemProcessor<LiabilityEnumerationBatchItem, LiabilityEnumerationBatchItem> queueProcessor
    ) {
        CompositeItemProcessor<LiabilityEnumerationBatchItem, LiabilityEnumerationBatchItem> processor = new CompositeItemProcessor<>();
        processor.setDelegates(java.util.List.of(validationProcessor, lookupProcessor, queueProcessor));
        return processor;
    }

    @Bean(STEP_NAME)
    public Step liabilityEnumerationStep(
        ListItemReader<LiabilityEnumerationBatchItem> reader,
        CompositeItemProcessor<LiabilityEnumerationBatchItem, LiabilityEnumerationBatchItem> processor,
        @Qualifier("liabilityEnumerationAmortizationWriter") LiabilityEnumerationAmortizationWriter writer
    ) {
        return new StepBuilder(STEP_NAME)
            .repository(jobRepository)
            .<LiabilityEnumerationBatchItem, LiabilityEnumerationBatchItem>chunk(1)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .transactionManager(transactionManager)
            .listener(writer)
            .build();
    }

    @Bean(JOB_NAME)
    public Job liabilityEnumerationJob(Step liabilityEnumerationStep) {
        return new JobBuilder(JOB_NAME).repository(jobRepository).start(liabilityEnumerationStep).incrementer(new RunIdIncrementer()).build();
    }
}
