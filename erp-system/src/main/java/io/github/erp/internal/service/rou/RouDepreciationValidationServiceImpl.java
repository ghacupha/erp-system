package io.github.erp.internal.service.rou;

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
import io.github.erp.domain.RouDepreciationRequest;
import io.github.erp.domain.enumeration.depreciationProcessStatusTypes;
import io.github.erp.internal.repository.InternalRouDepreciationRequestRepository;
import io.github.erp.service.dto.RouDepreciationRequestDTO;
import io.github.erp.service.mapper.RouDepreciationRequestMapper;
import io.github.erp.internal.service.rou.rules.RouDepreciationValidationResult;
import io.github.erp.internal.service.rou.rules.RouDepreciationValidationRuleEngine;
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

import java.util.Optional;
import java.util.UUID;

import static io.github.erp.internal.service.rou.batch.InvalidateDepreciationBatchConfig.INVALIDATE_DEPRECIATION_JOB_NAME;
import static io.github.erp.internal.service.rou.batch.RevalidateDepreciationBatchConfig.REVALIDATE_DEPRECIATION_JOB_NAME;

@Slf4j
@Service
public class RouDepreciationValidationServiceImpl implements RouDepreciationValidationService {

    private final InternalRouDepreciationRequestRepository rouDepreciationRequestRepository;
    private final InternalRouDepreciationRequestService internalRouDepreciationRequestService;
    private final RouDepreciationRequestMapper rouDepreciationRequestMapper;

    public final Job invalidateDepreciationJob;

    public final Job revalidateDepreciationJob;

    private final JobLauncher jobLauncher;

    private final RouDepreciationValidationRuleEngine validationRuleEngine;

    public RouDepreciationValidationServiceImpl(
        InternalRouDepreciationRequestRepository rouDepreciationRequestRepository,
        InternalRouDepreciationRequestService internalRouDepreciationRequestService,
        RouDepreciationRequestMapper rouDepreciationRequestMapper,
        @Qualifier(INVALIDATE_DEPRECIATION_JOB_NAME) Job invalidateDepreciationJob,
        @Qualifier(REVALIDATE_DEPRECIATION_JOB_NAME) Job revalidateDepreciationJob,
        JobLauncher jobLauncher,
        RouDepreciationValidationRuleEngine validationRuleEngine) {
        this.rouDepreciationRequestRepository = rouDepreciationRequestRepository;
        this.internalRouDepreciationRequestService = internalRouDepreciationRequestService;
        this.rouDepreciationRequestMapper = rouDepreciationRequestMapper;
        this.invalidateDepreciationJob = invalidateDepreciationJob;
        this.revalidateDepreciationJob = revalidateDepreciationJob;
        this.jobLauncher = jobLauncher;
        this.validationRuleEngine = validationRuleEngine;
    }

    /**
     * Changes the state of the request to deactivated
     *
     * @param rouDepreciationRequestDTO
     * @return
     */
    @Override
    public RouDepreciationRequestDTO invalidate(RouDepreciationRequestDTO rouDepreciationRequestDTO) {

        RouDepreciationRequest requestEntity = rouDepreciationRequestRepository
            .findById(rouDepreciationRequestDTO.getId())
            .orElseThrow();

        RouDepreciationRequestDTO currentState = rouDepreciationRequestMapper.toDto(requestEntity);
        RouDepreciationValidationResult validationResult = validationRuleEngine.evaluateInvalidation(currentState);

        if (!validationResult.isValid()) {
            throw new IllegalStateException(String.join(" ", validationResult.getMessages()));
        }

        logRuleMessages(validationResult);

        currentState.setInvalidated(true);

        RouDepreciationRequestDTO requestDTO = internalRouDepreciationRequestService.save(currentState);

        runInvalidationJob(requestDTO, validationResult);

        return requestDTO;
    }

    /**
     * Changes the state of the request to activated
     *
     * @param rouDepreciationRequestDTO
     * @return
     */
    @Override
    public RouDepreciationRequestDTO revalidate(RouDepreciationRequestDTO rouDepreciationRequestDTO) {

        RouDepreciationRequest requestEntity = rouDepreciationRequestRepository
            .findById(rouDepreciationRequestDTO.getId())
            .orElseThrow();

        RouDepreciationRequestDTO currentState = rouDepreciationRequestMapper.toDto(requestEntity);
        RouDepreciationValidationResult validationResult = validationRuleEngine.evaluateRevalidation(currentState);

        if (!validationResult.isValid()) {
            throw new IllegalStateException(String.join(" ", validationResult.getMessages()));
        }

        logRuleMessages(validationResult);

        currentState.setInvalidated(false);

        RouDepreciationRequestDTO requestDTO = internalRouDepreciationRequestService.save(currentState);

        runRevalidationJob(requestDTO, validationResult);

        return requestDTO;
    }

    @Async
    void runInvalidationJob(RouDepreciationRequestDTO requestDTO, RouDepreciationValidationResult validationResult) {

        // Trigger the Spring Batch job
        try {
            String batchJobIdentifier = UUID.randomUUID().toString();

            JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobToken", String.valueOf(System.currentTimeMillis()))
                .addString("batchJobIdentifier", batchJobIdentifier)
                .addString("previousBatchJobIdentifier", resolvePreviousBatchIdentifier(requestDTO, validationResult).toString())
                .toJobParameters();
            jobLauncher.run(invalidateDepreciationJob, jobParameters);

            RouDepreciationRequestDTO completed = internalRouDepreciationRequestService.saveIdentifier(requestDTO, UUID.fromString(batchJobIdentifier));
            markRequestInvalidated(completed);

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

    private void markRequestInvalidated(RouDepreciationRequestDTO requestDTO) {

        requestDTO.setDepreciationProcessStatus(depreciationProcessStatusTypes.INVALIDATED);

        internalRouDepreciationRequestService.save(requestDTO);
    }

    @Async
    void runRevalidationJob(RouDepreciationRequestDTO requestDTO, RouDepreciationValidationResult validationResult) {

        // Trigger the Spring Batch job
        try {
            String batchJobIdentifier = UUID.randomUUID().toString();

            JobParameters jobParameters = new JobParametersBuilder()
                .addString("jobToken", String.valueOf(System.currentTimeMillis()))
                .addString("batchJobIdentifier", batchJobIdentifier)
                .addString("previousBatchJobIdentifier", resolvePreviousBatchIdentifier(requestDTO, validationResult).toString())
                .toJobParameters();
            jobLauncher.run(revalidateDepreciationJob, jobParameters);

            RouDepreciationRequestDTO completed = internalRouDepreciationRequestService.saveIdentifier(requestDTO, UUID.fromString(batchJobIdentifier));
            markRequestRevalidated(completed);

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

    private void markRequestRevalidated(RouDepreciationRequestDTO requestDTO) {

        requestDTO.setDepreciationProcessStatus(depreciationProcessStatusTypes.COMPLETE);

        internalRouDepreciationRequestService.save(requestDTO);
    }

    private void logRuleMessages(RouDepreciationValidationResult validationResult) {

        validationResult.getMessages().forEach(message -> log.info("ROU depreciation validation: {}", message));
    }

    private UUID resolvePreviousBatchIdentifier(
        RouDepreciationRequestDTO requestDTO,
        RouDepreciationValidationResult validationResult
    ) {
        return validationResult
            .getPreviousBatchJobIdentifier()
            .or(() -> Optional.ofNullable(requestDTO.getBatchJobIdentifier()))
            .orElseThrow(() -> new IllegalStateException("Unable to resolve previous batch job identifier for depreciation job"));
    }
}
