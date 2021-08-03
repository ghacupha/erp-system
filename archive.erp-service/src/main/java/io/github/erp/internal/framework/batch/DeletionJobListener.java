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
            "Commencing deletion sequence ...   New job id : {} has started for file id : {}, with start time : {}",
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
            .ifPresentOrElse(
                file -> {
                    fileUploadDeletionService.delete(file.getId());
                },
                () -> {
                    throw new IllegalArgumentException("File-Id " + fileId + "doth not exist");
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
