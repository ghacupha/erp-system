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

import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.domain.enumeration.DepreciationPeriodStatusTypes;
import io.github.erp.erp.depreciation.calculation.DepreciationCalculatorService;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.AssetCategoryService;
import io.github.erp.service.AssetRegistrationService;
import io.github.erp.service.DepreciationEntryService;
import io.github.erp.service.DepreciationJobService;
import io.github.erp.service.DepreciationMethodService;
import io.github.erp.service.DepreciationPeriodService;
import io.github.erp.service.ServiceOutletService;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.DepreciationEntryDTO;
import io.github.erp.service.dto.DepreciationJobDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.dto.ServiceOutletDTO;
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
 * <p>
 * The runDepreciation method performs the following steps:
 * <p>
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

    private static final Logger log = LoggerFactory.getLogger(DepreciationJobSequenceServiceImpl.class);

    // TODO replace repositories with indexed services
    private final DepreciationCalculatorService depreciationCalculatorService;
    private final DepreciationJobService depreciationJobService;
    private final DepreciationPeriodService depreciationPeriodService;
    private final AssetCategoryService assetCategoryService;
    private final ServiceOutletService serviceOutletService;
    private final AssetRegistrationService assetRegistrationService;
    private final DepreciationMethodService depreciationMethodService;
    private final DepreciationEntryService depreciationEntryService;

    public DepreciationBatchSequenceService(DepreciationCalculatorService depreciationCalculatorService, DepreciationJobService depreciationJobService, DepreciationPeriodService depreciationPeriodService, AssetCategoryService assetCategoryService, ServiceOutletService serviceOutletService, AssetRegistrationService assetRegistrationService, DepreciationMethodService depreciationMethodService, DepreciationEntryService depreciationEntryService) {
        this.depreciationCalculatorService = depreciationCalculatorService;
        this.depreciationJobService = depreciationJobService;
        this.depreciationPeriodService = depreciationPeriodService;
        this.assetCategoryService = assetCategoryService;
        this.serviceOutletService = serviceOutletService;
        this.assetRegistrationService = assetRegistrationService;
        this.depreciationMethodService = depreciationMethodService;
        this.depreciationEntryService = depreciationEntryService;
    }

    /**
     * Triggers the depreciation process by creating a new DepreciationRun entity and processing the assets in batches.
     * This method retrieves the assets from the database, performs the depreciation calculations, and updates the necessary records.
     */
    public void runDepreciation(DepreciationBatchMessage message) {

        log.debug("Running depreciation for batch-id {}, standby...", message.getBatchId());

        String jobId = message.getJobId();
        List<String> assetIds = message.getAssetIds();
        // BigDecimal initialCost = message.getInitialCost();

        // fetch depreciationJob and depreciationPeriod from repo
        // depreciationJobRepository.findById(Long.valueOf(jobId)).ifPresent(depreciationJob -> {
        boolean depreciationJobDoesntExist = depreciationJobService.findOne(Long.valueOf(jobId)).isEmpty();

        if (depreciationJobDoesntExist) {
            log.warn("Depreciation job id {} does not exist", jobId);
            // TODO update the notification

            return;
        }

        DepreciationJobDTO depreciationJob = depreciationJobService.findOne(Long.valueOf(jobId)).get();

        log.debug("Commencing depreciation for depreciation-job id {}", depreciationJob.getId());

        boolean depreciationPeriodDoesntExist = depreciationPeriodService.findOne(depreciationJob.getDepreciationPeriod().getId()).isEmpty();

        if (depreciationPeriodDoesntExist) {
            log.warn("Depreciation period id {} does not exist, for depreciation-job id {}", depreciationJob.getDepreciationPeriod().getId(), depreciationJob.getId());
            // TODO update the notification

            return;
        }

        DepreciationPeriodDTO depreciationPeriod = depreciationPeriodService.findOne(depreciationJob.getDepreciationPeriod().getId()).get();

        log.debug("Commencing depreciation for depreciation-job id {}, for depreciation-period {}. Standby...", depreciationJob.getId(), depreciationPeriod.getId());

        if (depreciationPeriod.getDepreciationPeriodStatus() == DepreciationPeriodStatusTypes.CLOSED) {

            log.warn("Depreciation-period id {} is status CLOSED for depreciation-period id {}. System opting out the depreciation sequence. Standby", depreciationPeriod.getId(), depreciationJob.getId());
            // TODO update the notification

            return;
        }
        // TODO Update batch-sequence data in the caller

        // OPT OUT
        if (depreciationJob.getDepreciationJobStatus() == DepreciationJobStatusType.COMPLETE) {

            log.warn("DepreciationJob id {} is status COMPLETE for period id {}. System opting out the depreciation sequence. Standby", depreciationJob.getId(), depreciationPeriod.getId());
            // TODO update notification
            return;
        }

        log.debug("Standby for depreciation sequence on {} assets for batch id{}", assetIds.size(), message.getBatchId());
        // Perform the depreciation calculations for the batch of assets
        for (String assetId : assetIds) {

            // TODO opt out if depreciated for a given period
            // TODO add depreciation period relation with an asset

            // Retrieve the asset from the database using the assetId
            assetRegistrationService.findOne(Long.valueOf(assetId)).ifPresent(
                assetRegistration -> {

                    log.debug("Asset id {} ready for depreciation sequence, standby for next update", assetRegistration.getId());

                    // assetCategoryRepository.findById(assetRegistration.getAssetCategory().getId()).ifPresent(assetCategory -> {

                    boolean assetCategoryDoesNotExist = assetCategoryService.findOne(assetRegistration.getAssetCategory().getId()).isEmpty();

                    if (assetCategoryDoesNotExist) {

                        log.warn("Asset Category id {} not found", assetRegistration.getAssetCategory().getId());
                        // TODO update notification
                        return;
                    }

                    AssetCategoryDTO assetCategory = assetCategoryService.findOne(assetRegistration.getAssetCategory().getId()).get();

                    boolean serviceOutletDoesNotExist = serviceOutletService.findOne(assetRegistration.getMainServiceOutlet().getId()).isEmpty();

                    if (serviceOutletDoesNotExist) {

                        log.warn("Service outlet id {} not found", assetRegistration.getMainServiceOutlet().getId());

                        // TODO update notification
                        return;
                    }

                    ServiceOutletDTO serviceOutlet = serviceOutletService.findOne(assetRegistration.getMainServiceOutlet().getId()).get();

                    depreciationMethodService.findOne(assetCategory.getDepreciationMethod().getId()).ifPresent(
                        depreciationMethod -> {
                            // TODO note asset-category has null fields
                            // TODO note ensure relevant fields are not null
                            // Calculate the depreciation amount using the DepreciationCalculator
                            BigDecimal depreciationAmount = depreciationCalculatorService.calculateDepreciation(assetRegistration, depreciationPeriod, assetCategory, depreciationMethod);

                            // TODO Update the asset's net book value and any other relevant data
                            // NetBookValueDTO netBookValue = new NetBookValueDTO(depreciationAmount, assetRegistration, assetCategory, serviceOutlet, depreciationPeriod, depreciationJob)
                            // NetBookValueDTO = netBookValueService.save(netBookValue)

                            // Save the depreciation to the database
                            DepreciationEntryDTO depreciationEntry = new DepreciationEntryDTO();
                            depreciationEntry.setDepreciationAmount(depreciationAmount);
                            depreciationEntry.setDepreciationMethod(depreciationMethod);
                            depreciationEntry.setDepreciationPeriod(depreciationPeriod);
                            depreciationEntry.setAssetCategory(assetCategory);
                            depreciationEntry.setAssetNumber(Long.valueOf(assetRegistration.getAssetNumber()));
                            depreciationEntry.setAssetRegistration(assetRegistration);
                            depreciationEntry.setPostedAt(ZonedDateTime.now());
                            depreciationEntry.setServiceOutlet(serviceOutlet);

                            DepreciationEntryDTO depreciation = depreciationEntryService.save(depreciationEntry);

                            log.debug("depreciation-entry id {} saved to the database, standby for next update", depreciation.getId());
                        });
                });
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
