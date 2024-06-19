package io.github.erp.internal.service.leases;

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
