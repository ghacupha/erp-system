package io.github.erp.internal.framework.batch;

/*-
 * ERP System - ERP data management platform
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
