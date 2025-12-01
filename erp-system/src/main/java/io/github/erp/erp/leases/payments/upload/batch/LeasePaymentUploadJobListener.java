package io.github.erp.erp.leases.payments.upload.batch;

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
