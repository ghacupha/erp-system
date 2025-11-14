package io.github.erp.erp.leases.liability.schedule.upload;

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

import io.github.erp.domain.LeaseLiabilityScheduleFileUpload;
import io.github.erp.erp.leases.liability.schedule.batch.LeaseLiabilityScheduleBatchJobConfiguration;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class LeaseLiabilityScheduleUploadJobLauncher {

    private static final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleUploadJobLauncher.class);

    private final JobLauncher jobLauncher;
    private final Job job;

    public LeaseLiabilityScheduleUploadJobLauncher(
        JobLauncher jobLauncher,
        @Qualifier(LeaseLiabilityScheduleBatchJobConfiguration.JOB_NAME) Job leaseLiabilityScheduleUploadJob
    ) {
        this.jobLauncher = jobLauncher;
        this.job = leaseLiabilityScheduleUploadJob;
    }

    public void launch(LeaseLiabilityScheduleFileUpload upload) {
        try {
            JobParameters parameters = new JobParametersBuilder()
                .addString("run.id", UUID.randomUUID().toString())
                .addLong("fileId", upload.getCsvFileUpload().getId())
                .addString("filePath", upload.getCsvFileUpload().getFilePath())
                .addLong("leaseLiabilityId", upload.getLeaseLiabilityId())
                .addLong("leaseLiabilityCompilationId", upload.getLeaseLiabilityCompilationId())
                .addLong("uploadId", upload.getId())
                .addLong("startUpTime", Instant.now().toEpochMilli())
                .toJobParameters();

            if (upload.getLeaseAmortizationScheduleId() != null) {
                parameters = new JobParametersBuilder(parameters)
                    .addLong("leaseAmortizationScheduleId", upload.getLeaseAmortizationScheduleId())
                    .toJobParameters();
            }

            jobLauncher.run(job, parameters);
        } catch (Exception e) {
            log.error("Failed to launch lease liability schedule upload job", e);
            throw new IllegalStateException("Unable to launch lease liability schedule upload job", e);
        }
    }
}

