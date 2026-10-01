package io.github.erp.erp.leases.liability.enumeration.batch;

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
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationRequest;
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationResponse;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;

public class LiabilityEnumerationJobLauncher {

    private static final Logger log = LoggerFactory.getLogger(LiabilityEnumerationJobLauncher.class);

    private final JobLauncher jobLauncher;
    private final Job job;

    public LiabilityEnumerationJobLauncher(
        JobLauncher jobLauncher,
        @Qualifier(LiabilityEnumerationBatchConfiguration.JOB_NAME) Job job
    ) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public LiabilityEnumerationResponse launch(LiabilityEnumerationRequest request) {
        try {
            Map<String, JobParameter> parameters = new HashMap<>();
            parameters.put("leaseContractId", new JobParameter(request.getLeaseContractId()));
            parameters.put("leasePaymentUploadId", new JobParameter(request.getLeasePaymentUploadId()));
            parameters.put("interestRate", new JobParameter(request.getInterestRate()));
            parameters.put("timeGranularity", new JobParameter(request.getTimeGranularity()));
            parameters.put("active", new JobParameter(Boolean.TRUE.equals(request.getActive()) ? "true" : "false"));
            JobParameters jobParameters = new JobParameters(parameters);
            JobExecution execution = jobLauncher.run(job, jobParameters);

            if (!execution.getAllFailureExceptions().isEmpty()) {
                Throwable cause = execution.getAllFailureExceptions().get(0);
                throw new IllegalStateException("Liability enumeration batch failed: " + cause.getMessage(), cause);
            }

            Object response = execution.getExecutionContext().get(LiabilityEnumerationAmortizationWriter.RESPONSE_CONTEXT_KEY);
            if (response instanceof LiabilityEnumerationResponse) {
                return (LiabilityEnumerationResponse) response;
            }
            throw new IllegalStateException("Liability enumeration batch completed without a response payload");
        } catch (Exception e) {
            log.error("Failed to launch liability enumeration batch", e);
            if (e instanceof IllegalStateException) {
                throw (IllegalStateException) e;
            }
            throw new IllegalStateException("Unable to launch liability enumeration job", e);
        }
    }
}
