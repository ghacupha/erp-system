package io.github.erp.internal.service.rou;

import io.github.erp.domain.RouDepreciationRequest;
import io.github.erp.domain.enumeration.depreciationProcessStatusTypes;
import io.github.erp.internal.repository.InternalRouDepreciationRequestRepository;
import io.github.erp.service.dto.RouDepreciationRequestDTO;
import io.github.erp.service.mapper.RouDepreciationRequestMapper;
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

import static io.github.erp.internal.service.rou.batch.InvalidateDepreciationBatchConfig.INVALIDATE_DEPRECIATION_JOB_NAME;

@Slf4j
@Service
public class RouDepreciationValidationServiceImpl implements RouDepreciationValidationService {

    private final InternalRouDepreciationRequestRepository rouDepreciationRequestRepository;
    private final InternalRouDepreciationRequestService internalRouDepreciationRequestService;
    private final RouDepreciationRequestMapper rouDepreciationRequestMapper;

    public final Job invalidateDepreciationJob;

    private final JobLauncher jobLauncher;

    public RouDepreciationValidationServiceImpl(
        InternalRouDepreciationRequestRepository rouDepreciationRequestRepository,
        InternalRouDepreciationRequestService internalRouDepreciationRequestService,
        RouDepreciationRequestMapper rouDepreciationRequestMapper,
        @Qualifier(INVALIDATE_DEPRECIATION_JOB_NAME) Job invalidateDepreciationJob,
        JobLauncher jobLauncher) {
        this.rouDepreciationRequestRepository = rouDepreciationRequestRepository;
        this.internalRouDepreciationRequestService = internalRouDepreciationRequestService;
        this.rouDepreciationRequestMapper = rouDepreciationRequestMapper;
        this.invalidateDepreciationJob = invalidateDepreciationJob;
        this.jobLauncher = jobLauncher;
    }

    /**
     * Changes the state of the request to deactivated
     *
     * @param rouDepreciationRequestDTO
     * @return
     */
    @Override
    public RouDepreciationRequestDTO invalidate(RouDepreciationRequestDTO rouDepreciationRequestDTO) {

        RouDepreciationRequest requestEntity = rouDepreciationRequestRepository.findById(rouDepreciationRequestDTO.getId()).orElseThrow();

        requestEntity.setInvalidated(true);

        RouDepreciationRequestDTO requestDTO = internalRouDepreciationRequestService.save(rouDepreciationRequestMapper.toDto(requestEntity));

        runInvalidationJob(requestDTO);

        return requestDTO;
    }

    @Async
    void runInvalidationJob(RouDepreciationRequestDTO requestDTO) {

        // Trigger the Spring Batch job
        try {
            String batchJobIdentifier = UUID.randomUUID().toString();

            JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobToken", String.valueOf(System.currentTimeMillis()))
                .addString("batchJobIdentifier", batchJobIdentifier)
                .addString("previousBatchJobIdentifier", requestDTO.getBatchJobIdentifier().toString())
                .toJobParameters();
            jobLauncher.run(invalidateDepreciationJob, jobParameters);

            RouDepreciationRequestDTO completed = internalRouDepreciationRequestService.saveIdentifier(requestDTO, UUID.fromString(batchJobIdentifier));
            markJobComplete(completed);

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

    private void markJobComplete(RouDepreciationRequestDTO requestDTO) {

        requestDTO.setDepreciationProcessStatus(depreciationProcessStatusTypes.INVALIDATED);

        internalRouDepreciationRequestService.save(requestDTO);
    }
}
