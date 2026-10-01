package io.github.erp.erp.leases.payments.upload;

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
import io.github.erp.erp.leases.payments.upload.batch.LeasePaymentUploadBatchConfiguration;
import io.github.erp.domain.LeasePaymentUpload;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class LeasePaymentUploadJobLauncher {

    private static final Logger log = LoggerFactory.getLogger(LeasePaymentUploadJobLauncher.class);

    private final JobLauncher jobLauncher;
    private final Job job;

    public LeasePaymentUploadJobLauncher(
        JobLauncher jobLauncher,
        @Qualifier(LeasePaymentUploadBatchConfiguration.JOB_NAME) Job job
    ) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public void launch(LeasePaymentUpload upload) {
        try {
            Map<String, JobParameter> parameters = new HashMap<>();
            parameters.put("filePath", new JobParameter(upload.getCsvFileUpload().getFilePath()));
            parameters.put("leaseContractId", new JobParameter(upload.getLeaseContract().getId()));
            parameters.put("uploadId", new JobParameter(upload.getId()));
            JobParameters jobParameters = new JobParameters(parameters);
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            log.error("Failed to launch lease payment upload job for upload {}", upload.getId(), e);
            throw new IllegalStateException("Unable to launch lease payment upload job", e);
        }
    }
}
