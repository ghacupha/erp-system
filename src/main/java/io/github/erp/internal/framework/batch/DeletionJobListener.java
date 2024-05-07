package io.github.erp.internal.framework.batch;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.internal.framework.service.FileUploadDeletionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

@Scope("job")
@Slf4j
public class DeletionJobListener implements JobExecutionListener {
    private final long fileId;
    private final long startUpTime;
    private final FileUploadDeletionService<? extends HasIndex> fileUploadDeletionService;

    public DeletionJobListener(
        final @Value("#{jobParameters['fileId']}") long fileId,
        final long startUpTime,
        final FileUploadDeletionService<? extends HasIndex> granularFileUploadService
    ) {
        this.fileId = fileId;
        this.startUpTime = startUpTime;
        this.fileUploadDeletionService = granularFileUploadService;
    }

    /**
     * Callback before a job executes.
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @Override
    public void beforeJob(final JobExecution jobExecution) {
        log.info(
            "Starting deletion sequence ...   New job id : {} has started for file id : {}, with start time : {}",
            jobExecution.getJobId(),
            fileId,
            startUpTime
        );
    }

    /**
     * Callback after completion of a job. Called after both both successful and failed executions. To perform logic on a particular status, use "if (jobExecution.getStatus() == BatchStatus.X)".
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @Override
    public void afterJob(final JobExecution jobExecution) {
        log.info(
            "Concluding deletion sequence ... job id : {} for file id : {}, with start time : {}",
            jobExecution.getJobId(),
            fileId,
            startUpTime
        );
        // delete the physical file from the DB
        fileUploadDeletionService
            .findOne(fileId)
            .ifPresent(
                file -> {
                    fileUploadDeletionService.delete(file.getId());
                }
            );

        String exitStatus = jobExecution.getExitStatus().getExitCode();

        log.info(
            "Job Id {}, for file-id : {} completed in : {}ms with status {}",
            jobExecution.getJobId(),
            fileId,
            System.currentTimeMillis() - startUpTime,
            exitStatus
        );
    }
}
