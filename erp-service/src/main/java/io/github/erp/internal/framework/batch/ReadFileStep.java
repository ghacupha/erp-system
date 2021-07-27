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
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This read-step represents the common structure of how collections are passed from the reader to the writer
 * in a read file step
 *
 * @param <EVM>
 * @param <DTO>
 */
public class ReadFileStep<EVM, DTO> implements Step {

    private static final Logger log = getLogger(ReadFileStep.class);

    private final Step theStep;
    private final String stepName;

    public ReadFileStep(
        final String stepName,
        final ItemReader<List<EVM>> itemReader,
        final ItemProcessor<List<EVM>, List<DTO>> itemProcessor,
        final ItemWriter<List<DTO>> itemWriter,
        final StepBuilderFactory stepBuilderFactory
    ) {
        theStep =
            stepBuilderFactory
                .get(stepName)
                .<List<EVM>, List<DTO>>chunk(2)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
        this.stepName = stepName;
    }

    @Override
    public String getName() {
        log.debug("Creating read-file-step of id : {}", stepName);
        return this.theStep.getName();
    }

    @Override
    public boolean isAllowStartIfComplete() {
        return this.theStep.isAllowStartIfComplete();
    }

    @Override
    public int getStartLimit() {
        return this.theStep.getStartLimit();
    }

    @Override
    public void execute(StepExecution stepExecution) throws JobInterruptedException {
        log.debug("Commencing read-file batch step execution for batch step id {}", stepName);
        this.theStep.execute(stepExecution);
    }
}
