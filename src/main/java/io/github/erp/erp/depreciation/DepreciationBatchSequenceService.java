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

import io.github.erp.domain.*;
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.erp.depreciation.calculation.CalculatesDepreciation;
import io.github.erp.erp.depreciation.calculation.DepreciationCalculatorService;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * the DepreciationBatchService class triggers the depreciation process
 * when the triggerDepreciation method is called.
 * The service depends on repositories (DepreciationJobRepository, AssetRepository, DepreciationBatchSequenceRepository)
 * to access the database and the DepreciationCalculator for performing the depreciation calculations.
 *
 * The runDepreciation method performs the following steps:
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
public class DepreciationBatchSequenceService {

    private static final Logger log = LoggerFactory.getLogger(DepreciationJobSequenceService.class);

    private final DepreciationJobRepository depreciationJobRepository;
    private final AssetRegistrationRepository assetRepository;
    private final DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;
    private final DepreciationCalculatorService depreciationCalculatorService;
    private final AssetCategoryRepository assetCategoryRepository;
    private final DepreciationMethodRepository depreciationMethodRepository;
    private final ServiceOutletRepository serviceOutletRepository;
    private final DepreciationEntryRepository depreciationEntryRepository;

    public DepreciationBatchSequenceService(DepreciationJobRepository depreciationJobRepository, AssetRegistrationRepository assetRepository, DepreciationBatchSequenceRepository depreciationBatchSequenceRepository, DepreciationCalculatorService depreciationCalculatorService, AssetCategoryRepository assetCategoryRepository, DepreciationMethodRepository depreciationMethodRepository, ServiceOutletRepository serviceOutletRepository, DepreciationEntryRepository depreciationEntryRepository) {
        this.depreciationJobRepository = depreciationJobRepository;
        this.assetRepository = assetRepository;
        this.depreciationBatchSequenceRepository = depreciationBatchSequenceRepository;
        this.depreciationCalculatorService = depreciationCalculatorService;
        this.assetCategoryRepository = assetCategoryRepository;
        this.depreciationMethodRepository = depreciationMethodRepository;
        this.serviceOutletRepository = serviceOutletRepository;
        this.depreciationEntryRepository = depreciationEntryRepository;
    }

    /**
     * Triggers the depreciation process by creating a new DepreciationRun entity and processing the assets in batches.
     * This method retrieves the assets from the database, performs the depreciation calculations, and updates the necessary records.
     */
    public void runDepreciation(DepreciationBatchMessage message) {

        log.debug("Running depreciation for batch-id {}, standby...", message.getBatchId());

        String jobId = message.getJobId();
        List<String> assetIds = message.getAssetIds();
        BigDecimal initialCost = message.getInitialCost();

        // fetch depreciationJob and depreciationPeriod from repo
        DepreciationJob depreciationJob = depreciationJobRepository.getById(Long.valueOf(jobId));
        DepreciationPeriod depreciationPeriod = depreciationJob.getDepreciationPeriod();

        // TODO Update batch-sequence data in the caller

        // OPT OUT
        if (depreciationJob.getDepreciationJobStatus() == DepreciationJobStatusType.COMPLETE) {

            log.warn("DepreciationJob id {} is status COMPLETE for period id {}. System opting out the depreciation sequence. Standby", depreciationJob.getId(), depreciationPeriod.getId());
            return;
        }

        // TODO opt out if period is CLOSED

        log.debug("Standby for depreciation sequence on {} assets for batch id{}", assetIds.size(), message.getBatchId());
        // Perform the depreciation calculations for the batch of assets
        for (String assetId : assetIds) {

            // TODO opt out if depreciated for a given period

            // assetRepository.getById()

            // Retrieve the asset from the database using the assetId
           assetRepository.findById(Long.valueOf(assetId)).ifPresentOrElse(
               assetRegistration -> {

                   log.debug("Asset id {} ready for depreciation sequence, standby for next update", assetRegistration.getId());

                   AssetCategory assetCategory = assetCategoryRepository.getById(assetRegistration.getAssetCategory().getId());
                   ServiceOutlet serviceOutlet = serviceOutletRepository.getById(assetRegistration.getMainServiceOutlet().getId());

                   DepreciationMethod depreciationMethod = depreciationMethodRepository.getById(assetCategory.getDepreciationMethod().getId());

                   // Calculate the depreciation amount using the DepreciationCalculator
                   BigDecimal depreciationAmount = depreciationCalculatorService.calculateDepreciation(assetRegistration, depreciationPeriod, assetCategory, depreciationMethod);

                   // Update the asset's net book value and any other relevant data
                   // ...

                   // Save the depreciation to the database
                   DepreciationEntry depreciationEntry =
                       new DepreciationEntry()
                           .depreciationAmount(depreciationAmount)
                           .depreciationMethod(depreciationMethod)
                           .depreciationPeriod(depreciationPeriod)
                           .assetCategory(assetCategory)
                           .assetNumber(Long.valueOf(assetRegistration.getAssetNumber()))
                           .assetRegistration(assetRegistration)
                           .postedAt(ZonedDateTime.now())
                           .serviceOutlet(serviceOutlet);

                   DepreciationEntry depreciation = depreciationEntryRepository.save(depreciationEntry);

                   log.debug("depreciation-entry id {} saved to the database, standby for next update", depreciation.getId());
           },
           () -> log.warn("Asset-Registration id {} was not found...", assetId)
          );
            // TODO Update the asset's net book value and any other relevant data
            // TODO Create and update depreciation-entry
            // TODO Create and update netbook-value-entry
        }

        // DepreciationBatchSequence depreciationBatchSequence = depreciationBatchSequenceRepository.getById(Long.valueOf(message.getBatchId()));
        // depreciationBatchSequence.depreciationBatchStatus(DepreciationBatchStatusType.COMPLETED);

        // Save the DepreciationBatchSequence entity to the database
        // depreciationBatchSequenceRepository.save(depreciationBatchSequence);

        // TODO Update if batch is final
        // log.info("Initiating DepreciationJob id {}, for depreciation-period id {}. Standby", depreciationJob.getId(), depreciationPeriod.getId());

        // depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.RUNNING);

        // depreciationJobRepository.save(depreciationJob);
    }
}
