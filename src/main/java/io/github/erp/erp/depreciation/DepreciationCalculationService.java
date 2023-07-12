package io.github.erp.erp.depreciation;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.1
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
import io.github.erp.repository.AssetRegistrationRepository;
import io.github.erp.repository.DepreciationBatchSequenceRepository;
import io.github.erp.repository.DepreciationJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * the DepreciationService class triggers the depreciation process
 * when the triggerDepreciation method is called.
 * The service depends on repositories (DepreciationJobRepository, AssetRepository, DepreciationBatchSequenceRepository)
 * to access the database and the DepreciationCalculator for performing the depreciation calculations.
 *
 * The triggerDepreciation method performs the following steps:
 *
 * Uses the newly created DepreciationJob entity with the current run date and any other relevant properties.
 * Saves the DepreciationJob entity to the database.
 * Retrieves all the assets from the database.
 * Processes the assets in batches, with a specified batch size.
 * Calculates the depreciation amount for each asset using the DepreciationCalculator.
 * Updates the asset's net book value and other relevant data.
 * Saves the updated asset to the database.
 * Creates a DepreciationBatchSequence entity to track the processed batch and saves it to the database.
 * Repeats the process until all assets are processed.
 * Marks the depreciation run as completed and saves the DepreciationRun entity.
 */
@Service
public class DepreciationCalculationService {

    private static final Logger log = LoggerFactory.getLogger(DepreciationCalculationService.class);

    private final DepreciationJobRepository depreciationJobRepository;
    private final AssetRegistrationRepository assetRepository;
    private final DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;
    private final CalculatesDepreciation depreciationCalculator;

    public DepreciationCalculationService(DepreciationJobRepository depreciationJobRepository,
                                          AssetRegistrationRepository assetRepository,
                                          DepreciationBatchSequenceRepository depreciationBatchSequenceRepository,
                                          CalculatesDepreciation depreciationCalculator) {

        this.depreciationJobRepository = depreciationJobRepository;
        this.assetRepository = assetRepository;
        this.depreciationBatchSequenceRepository = depreciationBatchSequenceRepository;
        this.depreciationCalculator = depreciationCalculator;
    }

    /**
     * Triggers the depreciation process by creating a new DepreciationRun entity and processing the assets in batches.
     * This method retrieves the assets from the database, performs the depreciation calculations, and updates the necessary records.
     */
    public void triggerDepreciation(DepreciationJob depreciationJob) {

        DepreciationPeriod depreciationPeriod = depreciationJob.getDepreciationPeriod();

        // OPT OUT
        if (depreciationJob.getDepreciationJobStatus() == DepreciationJobStatusType.COMPLETE) {

            log.info("DepreciationJob id {} is status COMPLETE for period id {}. System opting out the depreciation sequence. Standby", depreciationJob.getId(), depreciationPeriod.getId());
            return;
        }

        log.info("Initiating DepreciationJob id {}, for depreciation-period id {}. Standby", depreciationJob.getId(), depreciationPeriod.getId());

        depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.RUNNING);

        depreciationJobRepository.save(depreciationJob);

        // Retrieve the assets from the database
        List<AssetRegistration> assets = assetRepository.findAll();

        // Process the assets in batches
        // TODO Set the batch size as desired from properties file
        int batchSize = 100;
        int totalAssets = assets.size();
        int processedCount = 0;

        log.info("System has retrieved {} assets for depreciation in batches of {}", assets.size(), batchSize);

        while (processedCount < totalAssets) {
            int startIndex = processedCount;
            int endIndex = Math.min(processedCount + batchSize, totalAssets);

            // Get the current batch of assets
            List<AssetRegistration> currentBatch = assets.subList(startIndex, endIndex);

            // Create a DepreciationBatchSequence entity to track the processed batch
            DepreciationBatchSequence batchSequence = depreciationBatchSequenceRepository.save(new DepreciationBatchSequence());
            batchSequence.depreciationBatchStatus(DepreciationBatchStatusType.CREATED);
            batchSequence.setDepreciationJob(depreciationJob);
            batchSequence.setStartIndex(startIndex);
            batchSequence.setEndIndex(endIndex);

            depreciationBatchSequenceRepository.save(batchSequence);

            log.debug("Initiating batch sequence id {}, for depreciation-job id {}, depreciation-period id {}. Standby", batchSequence.getId(), depreciationJob.getId(), depreciationPeriod.getId());

            batchSequence.depreciationBatchStatus(DepreciationBatchStatusType.RUNNING);
            depreciationBatchSequenceRepository.save(batchSequence);

            // Perform the depreciation calculations for the current batch
            for (AssetRegistration asset : currentBatch) {
                // Calculate the depreciation amount using the DepreciationCalculator
                BigDecimal depreciationAmount = depreciationCalculator.calculateDepreciation(asset, depreciationPeriod, asset.getAssetCategory(), asset.getAssetCategory().getDepreciationMethod());

                // TODO Update the asset's net book value and any other relevant data
                // TODO Create and update depreciation-entry
                // TODO Create and update netbook-value-entry

                // Save the updated asset to the database
                assetRepository.save(asset);
            }

            batchSequence.depreciationBatchStatus(DepreciationBatchStatusType.COMPLETED);

            // Save the DepreciationBatchSequence entity to the database
            depreciationBatchSequenceRepository.save(batchSequence);

            processedCount += batchSize;
        }

        // Mark the depreciation run as completed
        depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.COMPLETE);
        depreciationJobRepository.save(depreciationJob);
    }
}

