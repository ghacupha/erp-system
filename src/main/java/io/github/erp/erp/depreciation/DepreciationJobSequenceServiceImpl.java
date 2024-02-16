package io.github.erp.erp.depreciation;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.domain.enumeration.DepreciationPeriodStatusTypes;
import io.github.erp.erp.depreciation.context.DepreciationAmountContext;
import io.github.erp.erp.depreciation.context.DepreciationContextInstance;
import io.github.erp.erp.depreciation.context.DepreciationJobContext;
import io.github.erp.erp.depreciation.queue.DepreciationBatchProducer;
import io.github.erp.internal.service.InternalAssetRegistrationService;
import io.github.erp.service.*;
import io.github.erp.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The DepreciationJobSequenceService class manages the depreciation process for a given DepreciationJob. It is responsible for
 * triggering the depreciation sequence, processing assets in batches, and updating the depreciation job status accordingly.
 * The class interfaces with various services to fetch assets, create depreciation batch sequences, and enqueue depreciation
 * batch messages for processing.
 *
 * The DepreciationJobSequenceService class provides a way to initiate the depreciation process and handle different scenarios
 * such as closed depreciation periods, completed depreciation jobs, and missing depreciation periods. It takes care of creating
 * depreciation batch sequences and monitoring the enqueuing of batches.
 */
@Service
public class DepreciationJobSequenceServiceImpl implements DepreciationJobSequenceService<DepreciationJobDTO> {

    private static final Logger log = LoggerFactory.getLogger(DepreciationJobSequenceServiceImpl.class);
    public static final int PREFERRED_BATCH_SIZE = 650;

    // private final AssetRegistrationService assetRegistrationService;
    private final DepreciationBatchProducer depreciationBatchProducer;
    private final DepreciationJobService depreciationJobService;
    private final DepreciationBatchSequenceService depreciationBatchSequenceService;
    private final DepreciationPeriodService depreciationPeriodService;

    private final InternalAssetRegistrationService internalAssetRegistrationService;

    private final DepreciationEntrySinkProcessor depreciationEntrySinkProcessor;

    public DepreciationJobSequenceServiceImpl(
        // AssetRegistrationService assetRegistrationService,
        DepreciationBatchProducer depreciationBatchProducer,
        DepreciationJobService depreciationJobService,
        DepreciationBatchSequenceService depreciationBatchSequenceService,
        DepreciationPeriodService depreciationPeriodService,
        InternalAssetRegistrationService internalAssetRegistrationService,
        DepreciationEntrySinkProcessor depreciationEntrySinkProcessor) {
        // this.assetRegistrationService = assetRegistrationService;
        this.depreciationBatchProducer = depreciationBatchProducer;
        this.depreciationJobService = depreciationJobService;
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
        this.depreciationPeriodService = depreciationPeriodService;
        this.internalAssetRegistrationService = internalAssetRegistrationService;
        this.depreciationEntrySinkProcessor = depreciationEntrySinkProcessor;
    }

    /**
     * Triggers the depreciation process for a given DepreciationJobDTO by processing assets in batches. This method fetches assets
     * from the database, checks for any opt-out conditions, and initiates the depreciation sequence by enqueuing depreciation batch
     * messages for processing.
     *
     * @param depreciationJob The DepreciationJobDTO containing the details of the depreciation job to be processed.
     */
    @Override
    public void triggerDepreciation(DepreciationJobDTO depreciationJob) {

        log.info("Commencing depreciation-job id: {} depreciation-processor startup in progress", depreciationJob.getId());

        depreciationEntrySinkProcessor.startup();

        // Check if any conditions require the system to opt out of the depreciation process
        boolean shouldOptOut = checkOptOutConditions(depreciationJob);
        if (shouldOptOut) {

            log.warn("The opt-out conditions have returned positive. Depreciation Job is now terminating at the processor shutting down");
            depreciationEntrySinkProcessor.shutdown();
            return;
        }

        // Depreciation process is initiated
        log.info("Initiating DepreciationJob id {}, for depreciation-period id {}. Standby...", depreciationJob.getId(), depreciationJob.getDepreciationPeriod().getId());

        // Update the status of the depreciation job as running
        depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.RUNNING);
        depreciationJobService.save(depreciationJob);

        log.info("DepreciationJob status update complete, fetching assets for depreciation...");
        // Fetch assets from the database for depreciation processing
        List<AssetRegistrationDTO> assets = fetchAssets(depreciationJob);

        log.info("{} items extracted for depreciation, preferred batch size is {}", assets.size(), PREFERRED_BATCH_SIZE);

        // Process the assets in batches
        // TODO externalize this setting, you definitely would not do this for 10,000 items
        int numberOfBatches = processAssetsInBatches(depreciationJob, assets, PREFERRED_BATCH_SIZE);

        // Mark the depreciation job as complete
        markDepreciationJobAsEnqueued(depreciationJob, numberOfBatches);
    }

    /**
     * Checks if any conditions require the system to opt out of the depreciation process.
     * @return true if the system should opt out, false otherwise.
     */
    private boolean checkOptOutConditions(DepreciationJobDTO depreciationJob) {
        // Check if the depreciation period exists
        boolean depreciationPeriodDoesNotExist = depreciationPeriodService.findOne(depreciationJob.getDepreciationPeriod().getId()).isEmpty();

        if (depreciationPeriodDoesNotExist) {
            log.debug("We are opting out of the depreciation-job id {} because depreciation-period {} does not exist", depreciationJob.getId(), depreciationJob.getDepreciationPeriod().getId());
            String message = "DepreciationJob id: " + depreciationJob.getDepreciationPeriod().getId() + " does not exist";
            // TODO DEPRECIATION NOTICE OF DUE TO PERIOD STATE
            // TODO Update notice depreciationJobNoticeService.save(new DepreciationJobNoticeDTO(message, depreciationJob));
            return true;
        }

        // Get the depreciation period details
        DepreciationPeriodDTO depreciationPeriod = depreciationPeriodService.findOne(depreciationJob.getDepreciationPeriod().getId()).get();

        // Check if the depreciation period is closed
        if (depreciationPeriod.getDepreciationPeriodStatus() == DepreciationPeriodStatusTypes.CLOSED) {
            log.info("DepreciationPeriod id {} is status CLOSED for job id {}. System opting out the depreciation sequence. Standby", depreciationPeriod.getId(), depreciationJob.getId());
            depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.ERRORED);

            String message = "DepreciationPeriod id: " + depreciationJob.getDepreciationPeriod().getId() + " is closed";
            // TODO DEPRECIATION NOTICE OF DUE TO PERIOD STATE
            // TODO Update notice depreciationJobNoticeService.save(new DepreciationJobNoticeDTO(message, depreciationJob));
            return true;
        }

        // Check if the depreciation job is already complete
        if (depreciationJob.getDepreciationJobStatus() == DepreciationJobStatusType.COMPLETE) {
            log.info("DepreciationJob id {} is status COMPLETE for period id {}. System opting out the depreciation sequence. Standby", depreciationJob.getId(), depreciationPeriod.getId());

            String message = "DepreciationJob id: " + depreciationJob.getId() + " is COMPLETE";
            // TODO DEPRECIATION NOTICE OF DUE TO PERIOD STATE
            // TODO Update notice depreciationJobNoticeService.save(new DepreciationJobNoticeDTO(message, depreciationJob));
            return true;
        }

        return false;
    }

    /**
     * Fetches the assets from the database for depreciation processing.
     * @return List of AssetRegistrationDTO containing the fetched assets.
     */
    private List<AssetRegistrationDTO> fetchAssets(DepreciationJobDTO depreciationJob) {
        // List<AssetRegistrationDTO> assets = assetRegistrationService.findAll(Pageable.unpaged()).toList();
        List<AssetRegistrationDTO> assets = internalAssetRegistrationService.findByCapitalizationDateBefore(depreciationJob.getDepreciationPeriod().getEndDate());
        log.info("System has retrieved {} assets for depreciation.", assets.size());
        return assets;
    }

    /**
     * Processes the assets in batches for depreciation.
     */
    private int processAssetsInBatches(DepreciationJobDTO depreciationJob, List<AssetRegistrationDTO> assets, int batchSize) {
         int totalAssets = assets.size();
         int processedCount = 0;
         int sequenceCounter = 0;
         int numberOfBatches = totalAssets / batchSize + (totalAssets % batchSize == 0 ? 0 : 1);

         DepreciationJobContext contextManager = DepreciationJobContext.getInstance();
         UUID depreciationJobCountUpContextId = contextManager.createContext(0);
         UUID depreciationJobCountDownContextId = contextManager.createContext(totalAssets);
         UUID depreciationBatchCountUpContextId = contextManager.createContext(0);
         UUID messageCountContextId = contextManager.createContext(0);
         UUID depreciationBatchCountDownContextId = contextManager.createContext(totalAssets);

         DepreciationAmountContext depreciationAmountContext = DepreciationAmountContext.getInstance();
         UUID depreciationAmountContextId = depreciationAmountContext.createDepreciationAmountContext();

         log.info("System is processing {} assets in batches of {}", totalAssets, batchSize);

         // TODO check if the we can implement this lists on the repository itself using pages
         // TODO so that we can pull only the size of the assets needed not the whole kingdom
         while (processedCount < totalAssets) {

            ++sequenceCounter;

            int startIndex = processedCount;
            int endIndex = Math.min(processedCount + batchSize, totalAssets);

            // Get the current batch of assets
            List<AssetRegistrationDTO> currentBatch = assets.subList(startIndex, endIndex);

            // Check if this is the last batch
            boolean isLastBatch = processedCount + batchSize >= totalAssets;
            DepreciationBatchSequenceDTO batchSequence = createDepreciationBatchSequence(depreciationJob, startIndex, endIndex, isLastBatch, processedCount, currentBatch.size(), sequenceCounter, totalAssets);

            DepreciationContextInstance contextInstance = DepreciationContextInstance.builder()
                .depreciationJobCountUpContextId(depreciationJobCountUpContextId)
                .depreciationBatchCountUpContextId(depreciationBatchCountUpContextId)
                .depreciationJobCountDownContextId(depreciationJobCountDownContextId)
                .depreciationBatchCountDownContextId(depreciationBatchCountDownContextId)
                .depreciationAmountContextId(depreciationAmountContextId)
                .messageCountContextId(messageCountContextId)
                .build();

            BigDecimal initialCost = currentBatch.stream().map(AssetRegistrationDTO::getAssetCost).reduce(BigDecimal::add).get();

            List<Long> assetIds = currentBatch.stream().map(AssetRegistrationDTO::getId).collect(Collectors.toList());

            // Process and enqueue the current batch
            processAndEnqueueBatch(depreciationJob, assetIds, batchSequence, isLastBatch, processedCount, sequenceCounter, totalAssets, contextInstance, numberOfBatches, initialCost);

            processedCount += batchSize;
        }
        return numberOfBatches;
    }

    /**
     * Creates a DepreciationBatchSequence entity to track the processed batch.
     */
    private DepreciationBatchSequenceDTO createDepreciationBatchSequence(DepreciationJobDTO depreciationJob, int startIndex, int endIndex, boolean isLastBatch, int processedItems, int batchSize, int sequenceNumber, int totalItems) {
        DepreciationBatchSequenceDTO batchSequence = new DepreciationBatchSequenceDTO();
        batchSequence.setDepreciationBatchStatus(DepreciationBatchStatusType.CREATED);
        batchSequence.setDepreciationJob(depreciationJob);
        batchSequence.setStartIndex(startIndex);
        batchSequence.setEndIndex(endIndex);
        batchSequence.setSequenceNumber(sequenceNumber);
        batchSequence.setBatchSize(batchSize);
        batchSequence.setProcessedItems(processedItems);
        batchSequence.setIsLastBatch(isLastBatch);
        batchSequence.setTotalItems(totalItems);
        // TODO batchSequence.setProcessingIdentifier(/* Extracted from DepreciationContext*/);
        // TODO SET identifier to compute processing time

        DepreciationBatchSequenceDTO createdBatchSequence = depreciationBatchSequenceService.save(batchSequence);

        log.debug("Initiating batch sequence id {}, for depreciation-job id {}, depreciation-period id {}. Standby", createdBatchSequence.getId(), depreciationJob.getId(), depreciationJob.getDepreciationPeriod().getId());

        return createdBatchSequence;
    }

    /**
     * Processes and enqueues the current batch for depreciation.
     */
    private void processAndEnqueueBatch(DepreciationJobDTO depreciationJob, List<Long> currentBatch, DepreciationBatchSequenceDTO batchSequence, boolean isLastBatch, int processedCount, int sequenceNumber, int totalItems, DepreciationContextInstance contextInstance, int numberOfBatches, BigDecimal initialCost) {

        log.info("Batch # {} of {} items received for processing under job id {}; {} items processed out of {}", sequenceNumber, currentBatch.size(),depreciationJob.getDescription(),processedCount, totalItems);
        // Enqueuing the DepreciationBatchMessage
        depreciationBatchProducer.sendDepreciationJobMessage(depreciationJob, currentBatch, batchSequence, isLastBatch, processedCount, sequenceNumber, totalItems, contextInstance, numberOfBatches, initialCost);
        // Update the batch sequence status to enqueued
        batchSequence.setDepreciationBatchStatus(DepreciationBatchStatusType.ENQUEUED);
        depreciationBatchSequenceService.save(batchSequence);
    }

    /**
     * Marks the depreciation job as complete.
     */
    @Async
    void markDepreciationJobAsEnqueued(DepreciationJobDTO depreciationJob, int numberOfBatches) {
        depreciationJob.setNumberOfBatches(numberOfBatches);
        depreciationJob.setTimeOfCommencement(ZonedDateTime.now());
        depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.ENQUEUED);
        depreciationJobService.save(depreciationJob);
    }
}

