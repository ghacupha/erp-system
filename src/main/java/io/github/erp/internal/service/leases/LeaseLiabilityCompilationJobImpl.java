package io.github.erp.internal.service.leases;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.github.erp.internal.service.leases.batch.LeaseLiabilityCompilationBatchConfig.JOB_NAME;


@Slf4j
@Service
public class LeaseLiabilityCompilationJobImpl implements LeaseLiabilityCompilationJob {

    private final JobLauncher jobLauncher;

    private final Job leaseLiabilityCompilationJob;

    private final InternalLeaseLiabilityService internalLeaseLiabilityService;

    public LeaseLiabilityCompilationJobImpl(
        JobLauncher jobLauncher,
        @Qualifier(JOB_NAME) Job leaseLiabilityCompilationJob,
        InternalLeaseLiabilityService internalLeaseLiabilityService) {
        this.jobLauncher = jobLauncher;
        this.leaseLiabilityCompilationJob = leaseLiabilityCompilationJob;
        this.internalLeaseLiabilityService = internalLeaseLiabilityService;
    }

    @Async
    @Override
    public void compileLeaseLiabilitySchedule(LeaseLiabilityCompilationDTO requestDTO) {
        // Trigger the Spring Batch job
        try {
            String batchJobIdentifier = UUID.randomUUID().toString();
            JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobToken", String.valueOf(System.currentTimeMillis()))
                .addString("batchJobIdentifier", batchJobIdentifier)
                .addLong("leaseLiabilityCompilationRequestId", requestDTO.getId())
                .toJobParameters();
            jobLauncher.run(leaseLiabilityCompilationJob, jobParameters);

            // TODO
            // TODO LeaseLiabilityCompilationDTO completed = internalLeaseLiabilityService.saveIdentifier(requestDTO, UUID.fromString(batchJobIdentifier));

            // TODO internalRouDepreciationRequestService.markRequestComplete(completed);

        } catch (JobExecutionAlreadyRunningException alreadyRunningException) {
            log.error("The JobInstance identified by the properties already has an execution running.", alreadyRunningException);
        } catch (IllegalArgumentException args) {
            log.error("Either the job or the job instances are null", args);
        } catch (JobRestartException jre) {
            log.error("Job has been run before and circumstances that preclude a re-start", jre);
        } catch (JobInstanceAlreadyCompleteException jic) {
            log.error("The job has been run before with the same parameters and completed successfull", jic);
        } catch (JobParametersInvalidException jpi) {
            log.error("Job parameters are not valid for this job", jpi);
        }
    }
}
