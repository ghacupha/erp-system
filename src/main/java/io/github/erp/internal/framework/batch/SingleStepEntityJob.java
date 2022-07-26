package io.github.erp.internal.framework.batch;

/*-
 * Erp System - Mark II No 21 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.internal.framework.util.TokenGenerator;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

/**
 * This is a general configuration of a single step job
 */
public class SingleStepEntityJob implements Job {

    private final Job job;

    public SingleStepEntityJob(String jobName, JobExecutionListener jobExecutionListener, Step step, JobBuilderFactory jobBuilderFactory) {
        this.job =
            jobBuilderFactory
                .get(jobName)
                .preventRestart()
                .listener(jobExecutionListener)
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Override
    public String getName() {
        TokenGenerator token = new TokenGenerator();
        return this.job.getName() + "_" + token.generateBase64Token(16);
    }

    @Override
    public boolean isRestartable() {
        return this.job.isRestartable();
    }

    @Override
    public void execute(JobExecution jobExecution) {
        this.job.execute(jobExecution);
    }

    @Override
    public JobParametersIncrementer getJobParametersIncrementer() {
        return this.job.getJobParametersIncrementer();
    }

    @Override
    public JobParametersValidator getJobParametersValidator() {
        return this.job.getJobParametersValidator();
    }
}
