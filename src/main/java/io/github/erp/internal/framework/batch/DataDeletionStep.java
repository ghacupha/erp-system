package io.github.erp.internal.framework.batch;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * Abstract data deletion step. It uses the step-builder-factory to create a step whose
 * configuration generates a {@link List} of Long items which mark the indices of instances
 * of Entity E to be deleted from the persistence layer.
 *
 * @param <E> Entity whose table is to be deleted
 */
public class DataDeletionStep<E> implements Step {

    private static final Logger log = LoggerFactory.getLogger(DataDeletionStep.class);

    private final Step step;
    private final String stepName;

    public DataDeletionStep(
        final StepBuilderFactory stepBuilderFactory,
        final String stepName,
        final ItemReader<List<Long>> longListReader,
        final ItemProcessor<List<Long>, List<E>> longListProcessor,
        final ItemWriter<? super List<E>> entityListWriter
    ) {
        step =
            stepBuilderFactory
                .get(stepName)
                .<List<Long>, List<E>>chunk(2)
                .reader(longListReader)
                .processor(longListProcessor)
                .writer(entityListWriter)
                .build();
        this.stepName = stepName;
    }

    @Override
    public String getName() {
        log.debug("Creating batch step id : {} ...", stepName);
        return step.getName();
    }

    @Override
    public boolean isAllowStartIfComplete() {
        return step.isAllowStartIfComplete();
    }

    @Override
    public int getStartLimit() {
        return step.getStartLimit();
    }

    @Override
    public void execute(StepExecution stepExecution) throws JobInterruptedException {
        log.debug("Commencing execution of batch step id : {}", stepName);
        step.execute(stepExecution);
    }
}
