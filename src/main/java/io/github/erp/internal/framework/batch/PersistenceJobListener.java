package io.github.erp.internal.framework.batch;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Scope;

/**
 * This class can be used to configure batch related actions before, and after but only within this job
 */
@Scope("job")
public class PersistenceJobListener implements JobExecutionListener {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PersistenceJobListener.class);

    private final long fileId;
    private final long startUpTime;

    public PersistenceJobListener(final long fileId, final long startUpTime) {
        this.fileId = fileId;
        this.startUpTime = startUpTime;
    }

    /**
     * Callback before a job executes.
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @Override
    public void beforeJob(final JobExecution jobExecution) {

        log.info("New job id : {} has started for file id : {}, with start time : {}", jobExecution.getJobId(), fileId, startUpTime);

    }

    /**
     * Callback after completion of a job. Called after both both successful and failed executions. To perform logic on a particular status, use "if (jobExecution.getStatus() == BatchStatus.X)".
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @Override
    public void afterJob(final JobExecution jobExecution) {

        String exitStatus = jobExecution.getExitStatus().getExitCode();

        log.info("Job Id {}, for file-id : {} completed in : {}ms with status {}", jobExecution.getJobId(), fileId, System.currentTimeMillis() - startUpTime, exitStatus);
    }
}
