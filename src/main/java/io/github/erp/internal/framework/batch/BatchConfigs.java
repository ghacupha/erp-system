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
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Contains general configurations for batch related entities and objects
 */
@Configuration
@EnableBatchProcessing
public class BatchConfigs {

    @Bean("persistenceJobListener")
    @JobScope
    public JobExecutionListener persistenceJobListener(@Value("#{jobParameters['fileId']}") long fileId, @Value("#{jobParameters['startUpTime']}") long startUpTime) {
        return new PersistenceJobListener(fileId, startUpTime);
    }
}
