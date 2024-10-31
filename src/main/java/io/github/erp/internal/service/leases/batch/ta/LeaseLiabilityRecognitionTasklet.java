package io.github.erp.internal.service.leases.batch.ta;

import io.github.erp.internal.service.leases.LeaseLiabilityRecognitionTransactionDetailsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import java.util.UUID;

public class LeaseLiabilityRecognitionTasklet implements Tasklet, InitializingBean {

    private final LeaseLiabilityRecognitionTransactionDetailsService transactionDetailsService;

    private final UUID requisitionId;

    private final Long postedById;

    public LeaseLiabilityRecognitionTasklet(LeaseLiabilityRecognitionTransactionDetailsService leaseLiabilityRecognitionTransactionDetailsService, UUID requisitionId, Long postedById) {
        this.transactionDetailsService = leaseLiabilityRecognitionTransactionDetailsService;
        this.requisitionId = requisitionId;
        this.postedById = postedById;
    }

    /**
     * Given the current context in the form of a step contribution, do whatever
     * is necessary to process this unit inside a transaction. Implementations
     * return {@link RepeatStatus#FINISHED} if finished. If not they return
     * {@link RepeatStatus#CONTINUABLE}. On failure throws an exception.
     *
     * @param contribution mutable state to be passed back to update the current
     * step execution
     * @param chunkContext attributes shared between invocations but not between
     * restarts
     * @return an {@link RepeatStatus} indicating whether processing is
     * continuable. Returning {@code null} is interpreted as {@link RepeatStatus#FINISHED}
     *
     * @throws Exception thrown if error occurs during execution.
     */
    @Override
    public RepeatStatus execute(@NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {

        transactionDetailsService.createTransactionDetails(requisitionId, postedById);

        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        // TODO check repo states org.springframework.util.Assert.state(directory != null, "Directory must be set");
        // TODO Check availability of rules
        // Check availability of lease contract mappings
    }
}
