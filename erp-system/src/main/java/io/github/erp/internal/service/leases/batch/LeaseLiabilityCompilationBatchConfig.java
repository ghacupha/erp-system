package io.github.erp.internal.service.leases.batch;

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
import io.github.erp.internal.service.leases.InternalLeaseLiabilityScheduleItemService;
import io.github.erp.internal.service.leases.InternalLeaseLiabilityService;
import io.github.erp.internal.service.leases.LeaseAmortizationCompilationService;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LeaseLiabilityCompilationBatchConfig {

    public static final String JOB_NAME = "leaseLiabilityCompilationJob";
    private static final String STEP_NAME = "leaseLiabilityCompilationStep";
    private static final String ITEM_READER_NAME = "leaseLiabilityCompilationReader";
    private static final String PROCESSOR_NAME = "leaseLiabilityCompilationProcessor";
    private static final String WRITE_NAME = "leaseLiabilityCompilationWriter";

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['batchJobIdentifier']}")
    private static String batchJobIdentifier;

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['leaseLiabilityCompilationRequestId']}")
    private static long leaseLiabilityCompilationRequestId;

    @Autowired
    private InternalLeaseLiabilityService internalLeaseLiabilityService;

    @Autowired
    private LeaseAmortizationCompilationService leaseAmortizationCompilationService;

    @Autowired
    private InternalLeaseLiabilityScheduleItemService internalLeaseLiabilityScheduleItemService;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean(JOB_NAME)
    public Job leaseLiabilityCompilationJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(revalidateEntriesStep())
            .build();
    }

    @Bean(STEP_NAME)
    public Step revalidateEntriesStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .<LeaseLiabilityDTO, List<LeaseLiabilityScheduleItemDTO>>chunk(24) // Assuming processing of 2 years at a time
            .reader(leaseLiabilityCompilationReader(batchJobIdentifier, leaseLiabilityCompilationRequestId))
            .processor(leaseLiabilityCompilationProcessor(batchJobIdentifier, leaseLiabilityCompilationRequestId))
            .writer(leaseLiabilityCompilationWriterName(leaseLiabilityCompilationRequestId))
            .build();
    }

    @Bean(ITEM_READER_NAME)
    @StepScope
    public ItemReader<LeaseLiabilityDTO> leaseLiabilityCompilationReader(@Value("#{jobParameters['batchJobIdentifier']}") String batchJobIdentifier, @Value("#{jobParameters['leaseLiabilityCompilationRequestId']}") long leaseLiabilityCompilationRequestId) {
        return new LeaseLiabilityCompilationItemReader(internalLeaseLiabilityService, leaseLiabilityCompilationRequestId, batchJobIdentifier);
    }

    @Bean(PROCESSOR_NAME)
    @StepScope
    public ItemProcessor<LeaseLiabilityDTO, List<LeaseLiabilityScheduleItemDTO>> leaseLiabilityCompilationProcessor(
        @Value("#{jobParameters['batchJobIdentifier']}") String batchJobIdentifier,
        @Value("#{jobParameters['leaseLiabilityCompilationRequestId']}") long leaseLiabilityCompilationRequestId
    ) {
        return new LeaseLiabilityCompilationItemProcessor(batchJobIdentifier, leaseLiabilityCompilationRequestId, leaseAmortizationCompilationService);
    }

    @Bean(WRITE_NAME)
    @StepScope
    public ItemWriter<List<LeaseLiabilityScheduleItemDTO>> leaseLiabilityCompilationWriterName(
        @Value("#{jobParameters['leaseLiabilityCompilationRequestId']}") long leaseLiabilityCompilationRequestId
    ) {
        return new LeaseLiabilityCompilationItemWriter(internalLeaseLiabilityScheduleItemService, leaseLiabilityCompilationRequestId);
    }
}
