package io.github.erp.internal.framework.batch;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
        TokenGenerator token = new TokenGenerator();
        String newStepName = this.stepName + "_"+ token.generateBase64Token(16);
        log.debug("Creating read-file-step of id : {}", newStepName);
        return newStepName;
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
