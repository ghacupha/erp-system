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

import io.github.erp.internal.service.leases.ROUAmortizationTransactionDetailsService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ROUAmortizationBatchConfig {

    public static final String ROU_AMORTIZATION_JOB_NAME = "taROUAmortizationJob";
    private static final String STEP_NAME = "taROUAmortizationCompilationStep";
    private static final String TASKLET_NAME = "taROUAmortizationCompilationTaskLet";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ROUAmortizationTransactionDetailsService rouAmortizationTransactionDetailsService;

    @Bean(ROU_AMORTIZATION_JOB_NAME)
    public Job leaseAmortizationJob() {
        return jobBuilderFactory.get(ROU_AMORTIZATION_JOB_NAME)
            .start(leaseAmortizationStep())
            .build();
    }

    @Bean(STEP_NAME)
    public Step leaseAmortizationStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .tasklet(rouAmortizationTasklet())
            .build();
    }

    @Bean(TASKLET_NAME)
    public Tasklet rouAmortizationTasklet() {
        return new ROUAmortizationTasklet(rouAmortizationTransactionDetailsService);
    }
}
