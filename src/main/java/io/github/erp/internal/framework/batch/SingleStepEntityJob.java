package io.github.erp.internal.framework.batch;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
