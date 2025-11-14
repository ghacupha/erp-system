package io.github.erp.erp.leases.liability.schedule.batch;

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

import io.github.erp.repository.LeaseLiabilityScheduleFileUploadRepository;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Updates the status of a lease liability schedule upload as the batch job
 * progresses.
 */
@Component("leaseLiabilityScheduleUploadJobListener")
@Scope("job")
public class LeaseLiabilityScheduleUploadJobListener implements JobExecutionListener {

    private final LeaseLiabilityScheduleFileUploadRepository repository;
    private final Long uploadId;

    public LeaseLiabilityScheduleUploadJobListener(
        LeaseLiabilityScheduleFileUploadRepository repository,
        @Value("#{jobParameters['uploadId']}") Long uploadId
    ) {
        this.repository = repository;
        this.uploadId = uploadId;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        repository
            .findById(uploadId)
            .ifPresent(upload -> {
                upload.setUploadStatus("PROCESSING");
                repository.save(upload);
            });
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        repository
            .findById(uploadId)
            .ifPresent(upload -> {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    upload.setUploadStatus("COMPLETED");
                } else {
                    upload.setUploadStatus("FAILED");
                }
                repository.save(upload);
            });
    }
}

