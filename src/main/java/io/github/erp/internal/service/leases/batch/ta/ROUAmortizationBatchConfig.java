package io.github.erp.internal.service.leases.batch.ta;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.internal.service.leases.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ROUAmortizationBatchConfig {

    public static final String LEASES_COMPILATION_JOB_NAME = "taLeasesCompilationJob";
    private static final String ROU_AMORTIZATION_STEP_NAME = "taROUAmortizationCompilationStep";
    private static final String LEASE_INTEREST_ACCRUAL_STEP_NAME = "taLeaseInterestAccrualCompilationStep";
    private static final String LEASE_INTEREST_PAID_TRANSFER_STEP_NAME = "taLeaseInterestPaidTransferCompilationStep";
    private static final String LEASE_REPAYMENT_STEP_NAME = "taLeaseRepaymentCompilationStep";
    private static final String LEASE_LIABILITY_RECOGNITION_STEP_NAME = "taLeaseLiabilityRecognitionCompilationStep";
    private static final String LEASE_ROU_RECOGNITION_STEP_NAME = "taLeaseROURecognitionCompilationStep";
    private static final String ROU_AMORTIZATION_TASKLET_NAME = "taROUAmortizationCompilationTaskLet";
    private static final String LEASE_INTEREST_ACCRUAL_TASKLET_NAME = "taLeaseInterestAccrualCompilationTaskLet";
    private static final String LEASE_INTEREST_PAID_TRANSFER_TASKLET_NAME = "taLeaseInterestPaidTransferCompilationTaskLet";
    private static final String LEASE_REPAYMENT_TASKLET_NAME = "taLeaseRepaymentCompilationTaskLet";
    private static final String LEASE_LIABILITY_RECOGNITION_TASKLET_NAME = "taLeaseLiabilityRecognitionCompilationTaskLet";
    private static final String LEASE_ROU_RECOGNITION_TASKLET_NAME = "taLeaseROURecognitionCompilationTaskLet";

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['requisitionId']}")
    private static String requisitionId;

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['postedById']}")
    private static long postedById;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ROUAmortizationTransactionDetailsService rouAmortizationTransactionDetailsService;

    @Autowired
    private LeaseInterestAccrualTransactionDetailsService leaseInterestAccrualTransactionDetailsService;

    @Autowired
    private LeaseInterestPaidTransferTransactionDetailsService leaseInterestPaidTransferTransactionDetailsService;

    @Autowired
    private LeaseRepaymentTransactionDetailsService leaseRepaymentTransactionDetailsService;

    @Autowired
    private LeaseLiabilityRecognitionTransactionDetailsService leaseLiabilityRecognitionTransactionDetailsService;

    @Autowired
    private LeaseRouRecognitionTransactionDetailsService leaseRouRecognitionTransactionDetailsService;

    @Bean(LEASES_COMPILATION_JOB_NAME)
    public Job leaseAmortizationJob() {
        return jobBuilderFactory.get(LEASES_COMPILATION_JOB_NAME)
            .start(leaseLiabilityRecognitionStep())
            .next(leaseRouRecognitionStep())
            .next(leaseAmortizationStep())
            .next(leaseInterestAccrualStep())
            .next(leaseInterestPaidTransferStep())
            .next(leaseRepaymentStep())
            .build();
    }

    @Bean(ROU_AMORTIZATION_STEP_NAME)
    public Step leaseAmortizationStep() {
        return stepBuilderFactory.get(ROU_AMORTIZATION_STEP_NAME)
            .tasklet(rouAmortizationTasklet(requisitionId, postedById))
            .build();
    }

    @Bean(LEASE_INTEREST_ACCRUAL_STEP_NAME)
    public Step leaseInterestAccrualStep() {
        return stepBuilderFactory.get(LEASE_INTEREST_ACCRUAL_STEP_NAME)
            .tasklet(leaseInterestAccrualTasklet(requisitionId, postedById))
            .build();
    }

    @Bean(LEASE_INTEREST_PAID_TRANSFER_STEP_NAME)
    public Step leaseInterestPaidTransferStep() {
        return stepBuilderFactory.get(LEASE_INTEREST_PAID_TRANSFER_STEP_NAME)
            .tasklet(leaseInterestPaidTransferTasklet(requisitionId, postedById))
            .build();
    }

    @Bean(LEASE_REPAYMENT_STEP_NAME)
    public Step leaseRepaymentStep() {
        return stepBuilderFactory.get(LEASE_REPAYMENT_STEP_NAME)
            .tasklet(leaseRepaymentTasklet(requisitionId, postedById))
            .build();
    }

    @Bean(LEASE_LIABILITY_RECOGNITION_STEP_NAME)
    public Step leaseLiabilityRecognitionStep() {
        return stepBuilderFactory.get(LEASE_LIABILITY_RECOGNITION_STEP_NAME)
            .tasklet(leaseLiabilityRecognitionTasklet(requisitionId, postedById))
            .build();
    }

    @Bean(LEASE_ROU_RECOGNITION_STEP_NAME)
    public Step leaseRouRecognitionStep() {
        return stepBuilderFactory.get(LEASE_ROU_RECOGNITION_STEP_NAME)
            .tasklet(leaseRouRecognitionTasklet(requisitionId, postedById))
            .build();
    }

    @Bean(ROU_AMORTIZATION_TASKLET_NAME)
    @StepScope
    public Tasklet rouAmortizationTasklet(@Value("#{jobParameters['requisitionId']}") String requisitionId, @Value("#{jobParameters['postedById']}") long postedById) {
        return new ROUAmortizationTasklet(rouAmortizationTransactionDetailsService, UUID.fromString(requisitionId), postedById);
    }

    @Bean(LEASE_INTEREST_ACCRUAL_TASKLET_NAME)
    @StepScope
    public Tasklet leaseInterestAccrualTasklet(@Value("#{jobParameters['requisitionId']}") String requisitionId, @Value("#{jobParameters['postedById']}") long postedById) {
        return new LeaseInterestAccrualTasklet(leaseInterestAccrualTransactionDetailsService, UUID.fromString(requisitionId), postedById);
    }

    @Bean(LEASE_INTEREST_PAID_TRANSFER_TASKLET_NAME)
    @StepScope
    public Tasklet leaseInterestPaidTransferTasklet(@Value("#{jobParameters['requisitionId']}") String requisitionId, @Value("#{jobParameters['postedById']}") long postedById) {
        return new LeaseInterestPaidTransferTasklet(leaseInterestPaidTransferTransactionDetailsService, UUID.fromString(requisitionId), postedById);
    }

    @Bean(LEASE_REPAYMENT_TASKLET_NAME)
    @StepScope
    public Tasklet leaseRepaymentTasklet(@Value("#{jobParameters['requisitionId']}") String requisitionId, @Value("#{jobParameters['postedById']}") long postedById) {
        return new LeaseRepaymentTasklet(leaseRepaymentTransactionDetailsService, UUID.fromString(requisitionId), postedById);
    }

    @Bean(LEASE_LIABILITY_RECOGNITION_TASKLET_NAME)
    @StepScope
    public Tasklet leaseLiabilityRecognitionTasklet(@Value("#{jobParameters['requisitionId']}") String requisitionId, @Value("#{jobParameters['postedById']}") long postedById) {
        return new LeaseLiabilityRecognitionTasklet(leaseLiabilityRecognitionTransactionDetailsService, UUID.fromString(requisitionId), postedById);
    }

    @Bean(LEASE_ROU_RECOGNITION_TASKLET_NAME)
    @StepScope
    public Tasklet leaseRouRecognitionTasklet(@Value("#{jobParameters['requisitionId']}") String requisitionId, @Value("#{jobParameters['postedById']}") long postedById) {
        return new LeaseRouRecognitionTasklet(leaseRouRecognitionTransactionDetailsService, UUID.fromString(requisitionId), postedById);
    }
}
