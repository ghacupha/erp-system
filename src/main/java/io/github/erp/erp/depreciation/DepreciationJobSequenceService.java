package io.github.erp.erp.depreciation;

/*-
 * Erp System - Mark IV No 2 (Ehud Series) Server ver 1.3.2
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
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.DepreciationBatchSequence;
import io.github.erp.domain.DepreciationJob;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.erp.depreciation.calculation.CalculatesDepreciation;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.erp.depreciation.queue.DepreciationBatchProducer;
import io.github.erp.repository.AssetRegistrationRepository;
import io.github.erp.repository.DepreciationBatchSequenceRepository;
import io.github.erp.repository.DepreciationJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Run the overall depreciation-sequence for a given depreciation-job request. The job is done in batches
 * whose ordering is executed through the queue.
 */
@Service
public class DepreciationJobSequenceService {

    private static final Logger log = LoggerFactory.getLogger(DepreciationJobSequenceService.class);

    private final DepreciationJobRepository depreciationJobRepository;
    private final AssetRegistrationRepository assetRepository;
    private final DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;
    private final DepreciationBatchProducer depreciationBatchProducer;

    public DepreciationJobSequenceService(DepreciationJobRepository depreciationJobRepository, AssetRegistrationRepository assetRepository, DepreciationBatchSequenceRepository depreciationBatchSequenceRepository, DepreciationBatchProducer depreciationBatchProducer) {
        this.depreciationJobRepository = depreciationJobRepository;
        this.assetRepository = assetRepository;
        this.depreciationBatchSequenceRepository = depreciationBatchSequenceRepository;
        this.depreciationBatchProducer = depreciationBatchProducer;
    }

    /**
     * Triggers the depreciation process by creating a new DepreciationRun entity and processing the assets in batches.
     * This method retrieves the assets from the database, performs the depreciation calculations, and updates the necessary records.
     */
    public void triggerDepreciation(DepreciationJob depreciationJob) {
        // !!! Check that the depreciationJob status

        DepreciationPeriod depreciationPeriod = depreciationJob.getDepreciationPeriod();

        // TODO OPT OUT
        // TODO if (depreciationPeriod.depreciationPeriodStatus == COMPLETE) {
        // TODO     log.info("DepreciationPeriod id {} is status COMPLETE for job id {}. System opting out the depreciation sequence. Standby", depreciationPeriod.getId(), depreciationJob.getId(),);
        // TODO     depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.COMPLETE);
        //  todo return;
        // TODO }

        // OPT OUT
        if (depreciationJob.getDepreciationJobStatus() == DepreciationJobStatusType.COMPLETE) {

            log.info("DepreciationJob id {} is status COMPLETE for period id {}. System opting out the depreciation sequence. Standby", depreciationJob.getId(), depreciationPeriod.getId());
            return;
        }

        log.info("Initiating DepreciationJob id {}, for depreciation-period id {}. Standby", depreciationJob.getId(), depreciationPeriod.getId());

        depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.RUNNING);

        depreciationJobRepository.save(depreciationJob);

        // TODO room for improvement in memory-management issues . Retrieve the assets from the database
        List<AssetRegistration> assets = assetRepository.findAll();

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
            List<AssetRegistration> currentBatch = assets.subList(startIndex, endIndex);

            // Create a DepreciationBatchSequence entity to track the processed batch
            DepreciationBatchSequence batchSequence = new DepreciationBatchSequence();
            batchSequence.depreciationBatchStatus(DepreciationBatchStatusType.CREATED);
            batchSequence.setDepreciationJob(depreciationJob);
            batchSequence.setStartIndex(startIndex);
            batchSequence.setEndIndex(endIndex);
            // TODO track last-batch batchSequence.lastBatchSequence(processedCount + batchSize < totalAssets);

            DepreciationBatchSequence createdBatchSequence = depreciationBatchSequenceRepository.save(batchSequence);

            log.debug("Initiating batch sequence id {}, for depreciation-job id {}, depreciation-period id {}. Standby", createdBatchSequence.getId(), depreciationJob.getId(), depreciationPeriod.getId());

            // Enqueuing the DepreciationBatchMessage
            depreciationBatchProducer.sendDepreciationJobMessage(depreciationJob, currentBatch, createdBatchSequence);

            createdBatchSequence.depreciationBatchStatus(DepreciationBatchStatusType.RUNNING);
            depreciationBatchSequenceRepository.save(createdBatchSequence);

            processedCount += batchSize;
        }

        // Mark the depreciation run as completed
        // depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.COMPLETE);
        // depreciationJobRepository.save(depreciationJob);
    }
}

