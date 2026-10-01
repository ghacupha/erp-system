package io.github.erp.erp.leases.payments.upload.batch;

/*-
 * Erp System - Mark X No 12 (Kadar Series) Server ver 1.9.0
 * Copyright © 2021 - 2026 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.repository.LeasePaymentUploadRepository;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("leasePaymentUploadJobListener")
@JobScope
public class LeasePaymentUploadJobListener implements JobExecutionListener {

    private final LeasePaymentUploadRepository repository;
    private final Long uploadId;

    public LeasePaymentUploadJobListener(
        LeasePaymentUploadRepository repository,
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
                    if (upload.getCsvFileUpload() != null) {
                        upload.getCsvFileUpload().setProcessed(Boolean.TRUE);
                    }
                } else {
                    upload.setUploadStatus("FAILED");
                }
                repository.save(upload);
            });
    }
}
