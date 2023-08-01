package io.github.erp.erp.depreciation;

/*-
 * Erp System - Mark IV No 3 (Ehud Series) Server ver 1.3.3
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.domain.enumeration.DepreciationPeriodStatusTypes;
import io.github.erp.erp.depreciation.queue.DepreciationBatchProducer;
import io.github.erp.service.AssetRegistrationService;
import io.github.erp.service.DepreciationBatchSequenceService;
import io.github.erp.service.DepreciationJobService;
import io.github.erp.service.DepreciationPeriodService;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import io.github.erp.service.dto.DepreciationJobDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Run the overall depreciation-sequence for a given depreciation-job request. The job is done in batches
 * whose ordering is executed through the queue.
 */
@Service
public class DepreciationJobSequenceService {

    private static final Logger log = LoggerFactory.getLogger(DepreciationJobSequenceService.class);

    private final AssetRegistrationService assetRegistrationService;
    private final DepreciationBatchProducer depreciationBatchProducer;
    private final DepreciationJobService depreciationJobService;
    private final DepreciationBatchSequenceService depreciationBatchSequenceService;
    private final DepreciationPeriodService depreciationPeriodService;

    public DepreciationJobSequenceService(AssetRegistrationService assetRegistrationService, DepreciationBatchProducer depreciationBatchProducer, DepreciationJobService depreciationJobService, DepreciationBatchSequenceService depreciationBatchSequenceService, DepreciationPeriodService depreciationPeriodService) {
        this.assetRegistrationService = assetRegistrationService;
        this.depreciationBatchProducer = depreciationBatchProducer;
        this.depreciationJobService = depreciationJobService;
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
        this.depreciationPeriodService = depreciationPeriodService;
    }

    /**
     * Triggers the depreciation process by creating a new DepreciationRun entity and processing the assets in batches.
     * This method retrieves the assets from the database, performs the depreciation calculations, and updates the necessary records.
     */
    public void triggerDepreciation(DepreciationJobDTO depreciationJob) {
        // !!! Check that the depreciationJob status

        // todo migrate to depreciation-period-dto
        boolean depreciationPeriodExists = depreciationPeriodService.findOne(depreciationJob.getDepreciationPeriod().getId()).isPresent();

        // Highly unlikely, but let's get pendantic and check
        if (!depreciationPeriodExists) {

            log.debug("We are opting out of the depreciation-job id {} because depreciation-period {} does not exist", depreciationJob.getId(), depreciationJob.getDepreciationPeriod().getId());
            String message = "DepreciationJob id: " + depreciationJob.getDepreciationPeriod().getId() + " doth not exist";
            // TODO DEPRECIATION NOTICE OF DUE TO PERIOD STATE
            // TODO Update notice depreciationJobNoticeService.save(new DepreciationJobNoticeDTO(message, depreciationJob));
            return;
        }


        DepreciationPeriodDTO depreciationPeriod = depreciationPeriodService.findOne(depreciationJob.getDepreciationPeriod().getId()).get();

        // OPT OUT
        if (depreciationPeriod.getDepreciationPeriodStatus() == DepreciationPeriodStatusTypes.CLOSED) {
            log.info("DepreciationPeriod id {} is status CLOSED for job id {}. System opting out the depreciation sequence. Standby", depreciationPeriod.getId(), depreciationJob.getId());
            depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.ERRORED);

            String message = "DepreciationPeriod id: " + depreciationJob.getDepreciationPeriod().getId() + " is closed";
            // TODO DEPRECIATION NOTICE OF DUE TO PERIOD STATE
            // TODO Update notice depreciationJobNoticeService.save(new DepreciationJobNoticeDTO(message, depreciationJob));
            return;
        }

        // OPT OUT
        if (depreciationJob.getDepreciationJobStatus() == DepreciationJobStatusType.COMPLETE) {
            log.info("DepreciationJob id {} is status COMPLETE for period id {}. System opting out the depreciation sequence. Standby", depreciationJob.getId(), depreciationPeriod.getId());

            String message = "DepreciationJob id: " + depreciationJob.getId() + " is COMPLETE";
            // TODO DEPRECIATION NOTICE OF DUE TO PERIOD STATE
            // TODO Update notice depreciationJobNoticeService.save(new DepreciationJobNoticeDTO(message, depreciationJob));
            return;
        }

        log.info("Initiating DepreciationJob id {}, for depreciation-period id {}. Standby...", depreciationJob.getId(), depreciationPeriod.getId());

        depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.RUNNING);

        depreciationJobService.save(depreciationJob);

        // TODO room for improvement in memory-management issues . Retrieve the assets from the database
        List<AssetRegistrationDTO> assets = assetRegistrationService.findAll(Pageable.unpaged()).toList();

        // Process the assets in batches
        // TODO Set the batch size as desired from properties file
        int batchSize = 10;
        int totalAssets = assets.size();
        int processedCount = 0;

        log.info("System has retrieved {} assets for depreciation in batches of {}", assets.size(), batchSize);

        while (processedCount < totalAssets) {
            int startIndex = processedCount;
            int endIndex = Math.min(processedCount + batchSize, totalAssets);

            // Get the current batch of assets
            List<AssetRegistrationDTO> currentBatch = assets.subList(startIndex, endIndex);

            // TODO track last-batch
            boolean isLastBatch = processedCount + batchSize >= totalAssets;

            // Create a DepreciationBatchSequence entity to track the processed batch
            DepreciationBatchSequenceDTO batchSequence = new DepreciationBatchSequenceDTO();
            batchSequence.setDepreciationBatchStatus(DepreciationBatchStatusType.CREATED);
            batchSequence.setCreatedAt(ZonedDateTime.now());
            batchSequence.setDepreciationJob(depreciationJob);
            batchSequence.setStartIndex(startIndex);
            batchSequence.setEndIndex(endIndex);
            // TODO track last-batch batchSequence.isLastBatch(isLastBatch);

            DepreciationBatchSequenceDTO createdBatchSequence = depreciationBatchSequenceService.save(batchSequence);

            log.debug("Initiating batch sequence id {}, for depreciation-job id {}, depreciation-period id {}. Standby", createdBatchSequence.getId(), depreciationJob.getId(), depreciationPeriod.getId());

            // Enqueuing the DepreciationBatchMessage
            depreciationBatchProducer.sendDepreciationJobMessage(depreciationJob, currentBatch, createdBatchSequence);

            createdBatchSequence.setDepreciationBatchStatus(DepreciationBatchStatusType.ENQUEUED);
            depreciationBatchSequenceService.save(createdBatchSequence);

            if (isLastBatch) {
                log.debug("The last depreciation-batch-sequence has been enqueued");
                depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.ENQUEUED);
                depreciationJobService.save(depreciationJob);

            }

            processedCount += batchSize;
        }

        // Mark the depreciation run as completed
        // depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.COMPLETE);
        // depreciationJobRepository.save(depreciationJob);
    }
}

