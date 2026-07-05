package io.github.erp.internal.service.leases.batch.ta;

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
import io.github.erp.internal.service.leases.trxAccounts.LeaseInterestAccrualTransactionDetailsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import java.util.UUID;

public class LeaseInterestAccrualTasklet implements Tasklet, InitializingBean {

    private final LeaseInterestAccrualTransactionDetailsService transactionDetailsService;

    private final UUID requisitionId;

    private final Long postedById;

    public LeaseInterestAccrualTasklet(LeaseInterestAccrualTransactionDetailsService leaseInterestAccrualTransactionDetailsService, UUID requisitionId, Long postedById) {
        this.transactionDetailsService = leaseInterestAccrualTransactionDetailsService;
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
