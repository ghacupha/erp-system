package io.github.erp.internal.service.rou;

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
import io.github.erp.service.dto.RouDepreciationRequestDTO;
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

import static io.github.erp.internal.service.rou.batch.ROUDepreciationEntryBatchConfigs.PERSISTENCE_JOB_NAME;

@Slf4j
@Service
public class ROUDepreciationEntryCompilationJobImpl implements ROUDepreciationEntryCompilationJob {

    private final JobLauncher jobLauncher;

    private final Job rouDepreciationEntryPersistenceJob;

    private final InternalRouDepreciationRequestService internalRouDepreciationRequestService;

    public ROUDepreciationEntryCompilationJobImpl(
        JobLauncher jobLauncher,
        @Qualifier(PERSISTENCE_JOB_NAME) Job rouDepreciationEntryPersistenceJob,
        InternalRouDepreciationRequestService internalRouDepreciationRequestService) {
        this.jobLauncher = jobLauncher;
        this.rouDepreciationEntryPersistenceJob = rouDepreciationEntryPersistenceJob;
        this.internalRouDepreciationRequestService = internalRouDepreciationRequestService;
    }

    /**
     * Initiate a rou depreciation job
     *
     * @param requestDTO Request entity
     */
    @Async
    @Override
    public void compileROUDepreciationEntries(RouDepreciationRequestDTO requestDTO) {
        // Trigger the Spring Batch job
        try {
            String batchJobIdentifier = UUID.randomUUID().toString();
            JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobToken", String.valueOf(System.currentTimeMillis()))
                .addString("batchJobIdentifier", batchJobIdentifier)
                .addLong("rouDepreciationRequestId", requestDTO.getId())
                .toJobParameters();
            jobLauncher.run(rouDepreciationEntryPersistenceJob, jobParameters);

            RouDepreciationRequestDTO completed = internalRouDepreciationRequestService.saveIdentifier(requestDTO, UUID.fromString(batchJobIdentifier));

            internalRouDepreciationRequestService.markRequestComplete(completed);

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
