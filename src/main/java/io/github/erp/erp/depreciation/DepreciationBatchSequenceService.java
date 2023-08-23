package io.github.erp.erp.depreciation;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.5
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
import io.github.erp.domain.enumeration.DepreciationNoticeStatusType;
import io.github.erp.domain.enumeration.DepreciationPeriodStatusTypes;
import io.github.erp.erp.depreciation.calculation.DepreciationCalculatorService;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.AssetCategoryService;
import io.github.erp.service.AssetRegistrationService;
import io.github.erp.service.DepreciationEntryService;
import io.github.erp.service.DepreciationJobNoticeService;
import io.github.erp.service.DepreciationJobService;
import io.github.erp.service.DepreciationMethodService;
import io.github.erp.service.DepreciationPeriodService;
import io.github.erp.service.FiscalMonthService;
import io.github.erp.service.FiscalQuarterService;
import io.github.erp.service.FiscalYearService;
import io.github.erp.service.ServiceOutletService;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import io.github.erp.service.dto.DepreciationEntryDTO;
import io.github.erp.service.dto.DepreciationJobDTO;
import io.github.erp.service.dto.DepreciationJobNoticeDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.dto.FiscalMonthDTO;
import io.github.erp.service.dto.FiscalQuarterDTO;
import io.github.erp.service.dto.FiscalYearDTO;
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

    private final DepreciationCalculatorService depreciationCalculatorService;
    private final DepreciationJobService depreciationJobService;
    private final DepreciationPeriodService depreciationPeriodService;
    private final AssetCategoryService assetCategoryService;
    private final ServiceOutletService serviceOutletService;
    private final AssetRegistrationService assetRegistrationService;
    private final DepreciationMethodService depreciationMethodService;
    private final DepreciationEntryService depreciationEntryService;
    private final FiscalYearService fiscalYearService;
    private final FiscalQuarterService fiscalQuarterService;
    private final FiscalMonthService fiscalMonthService;
    private final DepreciationJobNoticeService depreciationJobNoticeService;
    private final io.github.erp.service.DepreciationBatchSequenceService batchSequenceService;

    public DepreciationBatchSequenceService(
        DepreciationCalculatorService depreciationCalculatorService,
        DepreciationJobService depreciationJobService,
        DepreciationPeriodService depreciationPeriodService,
        AssetCategoryService assetCategoryService,
        ServiceOutletService serviceOutletService,
        AssetRegistrationService assetRegistrationService,
        DepreciationMethodService depreciationMethodService,
        DepreciationEntryService depreciationEntryService,
        FiscalYearService fiscalYearService,
        FiscalQuarterService fiscalQuarterService,
        FiscalMonthService fiscalMonthService,
        DepreciationJobNoticeService depreciationJobNoticeService,
        io.github.erp.service.DepreciationBatchSequenceService batchSequenceService) {
        this.depreciationCalculatorService = depreciationCalculatorService;
        this.depreciationJobService = depreciationJobService;
        this.depreciationPeriodService = depreciationPeriodService;
        this.assetCategoryService = assetCategoryService;
        this.serviceOutletService = serviceOutletService;
        this.assetRegistrationService = assetRegistrationService;
        this.depreciationMethodService = depreciationMethodService;
        this.depreciationEntryService = depreciationEntryService;
        this.fiscalYearService = fiscalYearService;
        this.fiscalQuarterService = fiscalQuarterService;
        this.fiscalMonthService = fiscalMonthService;
        this.depreciationJobNoticeService = depreciationJobNoticeService;
        this.batchSequenceService = batchSequenceService;
    }

    /**
     * Triggers the depreciation process by creating a new DepreciationRun entity and processing the assets in batches.
     * This method retrieves the assets from the database, performs the depreciation calculations, and updates the necessary records.
     */
    public void runDepreciation(DepreciationBatchMessage message) {

        batchSequenceService.findOne(Long.valueOf(message.getBatchId())).ifPresentOrElse(batchSequence -> {

            log.debug("Running depreciation for batch-id {}, standby...", message.getBatchId());

            String jobId = message.getJobId();
            List<String> assetIds = message.getAssetIds();
            // BigDecimal initialCost = message.getInitialCost();

            // fetch depreciationJob and depreciationPeriod from repo
            // depreciationJobRepository.findById(Long.valueOf(jobId)).ifPresent(depreciationJob -> {
            boolean depreciationJobDoesntExist = depreciationJobService.findOne(Long.valueOf(jobId)).isEmpty();

            if (depreciationJobDoesntExist) {
                log.warn("Depreciation job id {} does not exist", jobId);

                DepreciationJobNoticeDTO notice = new DepreciationJobNoticeDTO();
                notice.setDepreciationNoticeStatus(DepreciationNoticeStatusType.ERROR);
                notice.setEventNarrative("Depreciation job id: " + jobId + " doesn't exist");
                notice.setEventTimeStamp(ZonedDateTime.now());
                notice.setDepreciationBatchSequence(batchSequence);
                depreciationJobNoticeService.save(notice);

                return;
            }

            DepreciationJobDTO depreciationJob = depreciationJobService.findOne(Long.valueOf(jobId)).get();

            log.debug("Commencing depreciation for depreciation-job id {}", depreciationJob.getId());

            boolean depreciationPeriodDoesntExist = depreciationPeriodService.findOne(depreciationJob.getDepreciationPeriod().getId()).isEmpty();

            if (depreciationPeriodDoesntExist) {
                log.warn("Depreciation period id {} does not exist, for depreciation-job id {}", depreciationJob.getDepreciationPeriod().getId(), depreciationJob.getId());

                DepreciationJobNoticeDTO notice = new DepreciationJobNoticeDTO();
                notice.setDepreciationJob(depreciationJob);
                notice.setDepreciationNoticeStatus(DepreciationNoticeStatusType.ERROR);
                notice.setEventNarrative("Depreciation period id: " + depreciationJob.getDepreciationPeriod().getId() + " doesn't exist");
                notice.setEventTimeStamp(ZonedDateTime.now());
                notice.setDepreciationBatchSequence(batchSequence);
                depreciationJobNoticeService.save(notice);

                return;
            }

            DepreciationPeriodDTO depreciationPeriod = depreciationPeriodService.findOne(depreciationJob.getDepreciationPeriod().getId()).get();

            log.debug("Commencing depreciation for depreciation-job id {}, for depreciation-period {}. Standby...", depreciationJob.getId(), depreciationPeriod.getId());

            if (depreciationPeriod.getDepreciationPeriodStatus() == DepreciationPeriodStatusTypes.CLOSED) {

                log.warn("Depreciation-period id {} is status CLOSED for depreciation-period id {}. System opting out the depreciation sequence. Standby", depreciationPeriod.getId(), depreciationJob.getId());

                DepreciationJobNoticeDTO notice = new DepreciationJobNoticeDTO();
                notice.setDepreciationJob(depreciationJob);
                notice.setDepreciationPeriod(depreciationPeriod);
                notice.setDepreciationNoticeStatus(DepreciationNoticeStatusType.ERROR);
                notice.setEventNarrative("Depreciation period id: " + depreciationPeriod.getPeriodCode() + " is closed");
                notice.setEventTimeStamp(ZonedDateTime.now());
                notice.setDepreciationBatchSequence(batchSequence);
                depreciationJobNoticeService.save(notice);

                return;
            }
            boolean fiscalYearDoesntExist = fiscalYearService.findOne(depreciationPeriod.getFiscalYear().getId()).isEmpty();

            if (fiscalYearDoesntExist) {
                // opt out
                DepreciationJobNoticeDTO notice = new DepreciationJobNoticeDTO();
                notice.setErrorMessage("Fiscal year id: " + depreciationPeriod.getFiscalYear().getId() + " doesn't exist");
                notice.setDepreciationJob(depreciationJob);
                notice.setDepreciationPeriod(depreciationPeriod);
                notice.setDepreciationNoticeStatus(DepreciationNoticeStatusType.ERROR);
                notice.setEventNarrative("Fiscal year id: " + depreciationPeriod.getFiscalYear().getId() + " doesn't exist");
                notice.setEventTimeStamp(ZonedDateTime.now());
                notice.setDepreciationBatchSequence(batchSequence);
                depreciationJobNoticeService.save(notice);

                return;
            }
            FiscalYearDTO fiscalYear = fiscalYearService.findOne(depreciationPeriod.getFiscalYear().getId()).get();

            boolean fiscalMonthDoesntExist = fiscalMonthService.findOne(depreciationPeriod.getFiscalMonth().getId()).isEmpty();

            if (fiscalMonthDoesntExist) {
                // opt out
                DepreciationJobNoticeDTO notice = new DepreciationJobNoticeDTO();
                notice.setErrorMessage("Fiscal month id: " + depreciationPeriod.getFiscalMonth().getId() + " doesn't exist");
                notice.setDepreciationJob(depreciationJob);
                notice.setDepreciationPeriod(depreciationPeriod);
                notice.setDepreciationNoticeStatus(DepreciationNoticeStatusType.ERROR);
                notice.setEventNarrative("Fiscal month id: " + depreciationPeriod.getFiscalMonth().getId() + " doesn't exist");
                notice.setEventTimeStamp(ZonedDateTime.now());
                notice.setDepreciationBatchSequence(batchSequence);
                depreciationJobNoticeService.save(notice);

                return;
            }


            FiscalMonthDTO fiscalMonth = fiscalMonthService.findOne(depreciationPeriod.getFiscalMonth().getId()).get();

            boolean fiscalQuarterDoesntExist = fiscalQuarterService.findOne(depreciationPeriod.getFiscalQuarter().getId()).isEmpty();

            if (fiscalQuarterDoesntExist) {
                // opt out
                DepreciationJobNoticeDTO notice = new DepreciationJobNoticeDTO();
                notice.setErrorMessage("Fiscal quarter id: " + depreciationPeriod.getFiscalQuarter().getId() + " doesn't exist");
                notice.setDepreciationJob(depreciationJob);
                notice.setDepreciationPeriod(depreciationPeriod);
                notice.setDepreciationNoticeStatus(DepreciationNoticeStatusType.ERROR);
                notice.setEventNarrative("Fiscal quarter id: " + depreciationPeriod.getFiscalQuarter().getId() + " doesn't exist");
                notice.setEventTimeStamp(ZonedDateTime.now());
                notice.setDepreciationBatchSequence(batchSequence);
                depreciationJobNoticeService.save(notice);

                return;
            }

            FiscalQuarterDTO fiscalQuarter = fiscalQuarterService.findOne(depreciationPeriod.getFiscalQuarter().getId()).get();

            // OPT OUT
            if (depreciationJob.getDepreciationJobStatus() == DepreciationJobStatusType.COMPLETE) {

                log.warn("DepreciationJob id {} is status COMPLETE for period id {}. System opting out the depreciation sequence. Standby", depreciationJob.getId(), depreciationPeriod.getId());

                DepreciationJobNoticeDTO notice = new DepreciationJobNoticeDTO();
                notice.setErrorMessage("Depreciation Job id: " + depreciationJob.getId() + " is status COMPLETE");
                notice.setDepreciationJob(depreciationJob);
                notice.setDepreciationPeriod(depreciationPeriod);
                notice.setDepreciationNoticeStatus(DepreciationNoticeStatusType.ERROR);
                notice.setEventNarrative("Depreciation Job id: " + depreciationJob.getId() + " is status COMPLETE");
                notice.setEventTimeStamp(ZonedDateTime.now());
                // TODO update batch sequence
                notice.setDepreciationBatchSequence(batchSequence);
                depreciationJobNoticeService.save(notice);

                return;
            }

            log.debug("Standby for depreciation sequence on {} assets for batch id{}", assetIds.size(), message.getBatchId());
            // Perform the depreciation calculations for the batch of assets
            for (String assetId : assetIds) {

                // TODO opt out if depreciated for a given period
                // TODO add depreciation period UNIQUE relation with fiscal-month

                // Retrieve the asset from the database using the assetId
                assetRegistrationService.findOne(Long.valueOf(assetId)).ifPresent(
                    assetRegistration -> {

                        log.debug("Asset id {} ready for depreciation sequence, standby for next update", assetRegistration.getId());

                        boolean assetCategoryDoesNotExist = assetCategoryService.findOne(assetRegistration.getAssetCategory().getId()).isEmpty();

                        if (assetCategoryDoesNotExist) {

                            log.warn("Asset Category id {} not found", assetRegistration.getAssetCategory().getId());

                            DepreciationJobNoticeDTO notice = new DepreciationJobNoticeDTO();
                            notice.setErrorMessage("Asset Category id: " + assetRegistration.getAssetCategory().getId() + " not found");
                            notice.setDepreciationJob(depreciationJob);
                            notice.setDepreciationPeriod(depreciationPeriod);
                            notice.setDepreciationNoticeStatus(DepreciationNoticeStatusType.ERROR);
                            notice.setEventNarrative("Asset Category id: " + assetRegistration.getAssetCategory().getId() + " not found");
                            notice.setEventTimeStamp(ZonedDateTime.now());
                            notice.setDepreciationBatchSequence(batchSequence);
                            depreciationJobNoticeService.save(notice);

                            return;
                        }

                        AssetCategoryDTO assetCategory = assetCategoryService.findOne(assetRegistration.getAssetCategory().getId()).get();

                        boolean serviceOutletDoesNotExist = serviceOutletService.findOne(assetRegistration.getMainServiceOutlet().getId()).isEmpty();

                        if (serviceOutletDoesNotExist) {

                            log.warn("Service outlet id {} not found", assetRegistration.getMainServiceOutlet().getId());

                            DepreciationJobNoticeDTO notice = new DepreciationJobNoticeDTO();
                            notice.setErrorMessage("Service outlet id: " + assetRegistration.getMainServiceOutlet().getId() + " not found");
                            notice.setDepreciationJob(depreciationJob);
                            notice.setDepreciationPeriod(depreciationPeriod);
                            notice.setDepreciationNoticeStatus(DepreciationNoticeStatusType.ERROR);
                            notice.setEventNarrative("Service outlet id: " + assetRegistration.getMainServiceOutlet().getId() + " not found");
                            notice.setEventTimeStamp(ZonedDateTime.now());
                            notice.setDepreciationBatchSequence(batchSequence);
                            depreciationJobNoticeService.save(notice);
                            return;
                        }

                        ServiceOutletDTO serviceOutlet = serviceOutletService.findOne(assetRegistration.getMainServiceOutlet().getId()).get();

                        depreciationMethodService.findOne(assetCategory.getDepreciationMethod().getId()).ifPresent(
                            depreciationMethod -> {
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
                                // TODO change this field to string
                                depreciationEntry.setAssetNumber(Long.valueOf(assetRegistration.getAssetNumber()));
                                depreciationEntry.setAssetRegistration(assetRegistration);
                                depreciationEntry.setPostedAt(ZonedDateTime.now());
                                depreciationEntry.setServiceOutlet(serviceOutlet);
                                depreciationEntry.setFiscalYear(fiscalYear);
                                depreciationEntry.setFiscalMonth(fiscalMonth);
                                depreciationEntry.setFiscalQuarter(fiscalQuarter);

                                DepreciationEntryDTO depreciation = depreciationEntryService.save(depreciationEntry);

                                log.debug("depreciation-entry id {} saved to the database, standby for next update", depreciation.getId());
                            });
                    });
                // TODO Update the asset's net book value and any other relevant data
                // TODO Create and update depreciation-entry
                // TODO Create and update netbook-value-entry
            }

            updateBatchSequenceStatus(batchSequence, DepreciationBatchStatusType.COMPLETED);
       },
            () -> {
            throw new DepreciationBatchSequenceNotFound(message.getBatchId());
        });
    }

    private void updateBatchSequenceStatus(DepreciationBatchSequenceDTO depreciationBatchSequence, DepreciationBatchStatusType status) {
        if (depreciationBatchSequence.getDepreciationBatchStatus() != status) {
            depreciationBatchSequence.setDepreciationBatchStatus(status);
            batchSequenceService.save(depreciationBatchSequence);
        }
    }
}
