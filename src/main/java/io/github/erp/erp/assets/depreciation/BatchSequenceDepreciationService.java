package io.github.erp.erp.assets.depreciation;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.domain.enumeration.DepreciationNoticeStatusType;
import io.github.erp.domain.enumeration.DepreciationPeriodStatusTypes;
import io.github.erp.erp.assets.depreciation.adjustments.AdjustedCostService;
import io.github.erp.erp.assets.depreciation.calculation.DepreciationCalculatorService;
import io.github.erp.erp.assets.depreciation.context.DepreciationAmountContext;
import io.github.erp.erp.assets.depreciation.context.DepreciationJobContext;
import io.github.erp.erp.assets.depreciation.exceptions.AssetCategoryNotConfiguredException;
import io.github.erp.erp.assets.depreciation.exceptions.DepreciationBatchSequenceNotFound;
import io.github.erp.erp.assets.depreciation.exceptions.DepreciationPeriodClosedException;
import io.github.erp.erp.assets.depreciation.exceptions.DepreciationPeriodNotConfiguredException;
import io.github.erp.erp.assets.depreciation.exceptions.FiscalMonthNotConfiguredException;
import io.github.erp.erp.assets.depreciation.exceptions.ServiceOutletNotConfiguredException;
import io.github.erp.erp.assets.depreciation.model.DepreciationArtefact;
import io.github.erp.erp.assets.depreciation.model.DepreciationBatchMessage;
import io.github.erp.internal.service.InternalAssetDisposalService;
import io.github.erp.internal.service.InternalAssetWriteOffService;
import io.github.erp.service.*;
import io.github.erp.service.dto.*;
import io.github.erp.service.mapper.DepreciationEntryMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

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
@Transactional
public class BatchSequenceDepreciationService {

    private static final Logger log = LoggerFactory.getLogger(DepreciationJobSequenceServiceImpl.class);

    private final DepreciationCalculatorService depreciationCalculatorService;
    private final DepreciationJobService depreciationJobService;
    private final DepreciationPeriodService depreciationPeriodService;
    private final AssetCategoryService assetCategoryService;
    private final ServiceOutletService serviceOutletService;
    private final AssetRegistrationService assetRegistrationService;
    private final DepreciationMethodService depreciationMethodService;
    private final FiscalMonthService fiscalMonthService;
    private final DepreciationJobNoticeService depreciationJobNoticeService;
    private final io.github.erp.service.DepreciationBatchSequenceService batchSequenceService;

    private final DepreciationEntryMapper depreciationEntryMapper;
    private final DepreciationEntrySinkProcessor depreciationEntrySinkProcessor;

    private final InternalAssetDisposalService internalAssetDisposalService;
    private final InternalAssetWriteOffService internalAssetWriteOffService;

    private final AdjustedCostService adjustedCostService;

    public BatchSequenceDepreciationService(
        DepreciationCalculatorService depreciationCalculatorService,
        DepreciationJobService depreciationJobService,
        DepreciationPeriodService depreciationPeriodService,
        AssetCategoryService assetCategoryService,
        ServiceOutletService serviceOutletService,
        AssetRegistrationService assetRegistrationService,
        DepreciationMethodService depreciationMethodService,
        FiscalMonthService fiscalMonthService,
        DepreciationJobNoticeService depreciationJobNoticeService,
        DepreciationBatchSequenceService batchSequenceService,
        DepreciationEntryMapper depreciationEntryMapper,
        DepreciationEntrySinkProcessor depreciationEntrySinkProcessor,
        InternalAssetDisposalService internalAssetDisposalService,
        InternalAssetWriteOffService internalAssetWriteOffService,
        @Qualifier("aggregateAssetCostAdjustmentService") AdjustedCostService adjustedCostService) {
        this.depreciationCalculatorService = depreciationCalculatorService;
        this.depreciationJobService = depreciationJobService;
        this.depreciationPeriodService = depreciationPeriodService;
        this.assetCategoryService = assetCategoryService;
        this.serviceOutletService = serviceOutletService;
        this.assetRegistrationService = assetRegistrationService;
        this.depreciationMethodService = depreciationMethodService;
        this.fiscalMonthService = fiscalMonthService;
        this.depreciationJobNoticeService = depreciationJobNoticeService;
        this.batchSequenceService = batchSequenceService;
        this.depreciationEntryMapper = depreciationEntryMapper;
        this.depreciationEntrySinkProcessor = depreciationEntrySinkProcessor;
        this.internalAssetDisposalService = internalAssetDisposalService;
        this.internalAssetWriteOffService = internalAssetWriteOffService;
        this.adjustedCostService = adjustedCostService;
    }

    /**
     * Triggers the depreciation process by creating a new DepreciationRun entity and processing the assets in batches.
     * This method retrieves the assets from the database, performs the depreciation calculations, and updates the necessary records.
     */
    public DepreciationBatchMessage runDepreciation(DepreciationBatchMessage message) {

        batchSequenceService.findOne(Long.valueOf(message.getBatchId())).ifPresentOrElse(batchSequence -> {

                log.debug("Running depreciation for batch-id {}, standby...", message.getBatchId());

                String jobId = message.getJobId();
                List<String> assetIds = message.getAssetIds();

                UUID depreciationAmountContextId = message.getContextInstance().getDepreciationAmountContextId();

                DepreciationJobDTO depreciationJob = getDepreciationJob(batchSequence, jobId);
                if (depreciationJob == null) throw new DepreciationJobNotFoundException(message);

                log.debug("Commencing depreciation for depreciation-job id {}", depreciationJob.getId());


                DepreciationPeriodDTO depreciationPeriod = getDepreciationPeriod(batchSequence, depreciationJob);
                if (depreciationPeriod == null) throw new DepreciationPeriodNotConfiguredException(message);

                // OPT OUT
                if (depreciationJobCompletionStatusCheck(message, batchSequence, depreciationJob, depreciationPeriod)) return;

                log.debug("Commencing depreciation for depreciation-job id {}, for depreciation-period {}. Standby...", depreciationJob.getId(), depreciationPeriod.getId());

                if (depreciationPeriodIsClosed(batchSequence, depreciationJob, depreciationPeriod))
                    throw new DepreciationPeriodClosedException(message);

                FiscalMonthDTO fiscalMonth = getFiscalMonth(batchSequence, depreciationJob, depreciationPeriod);
                if (fiscalMonth == null) throw new FiscalMonthNotConfiguredException(message);

                log.debug("Standby for depreciation sequence on {} assets for batch id{}", assetIds.size(), message.getBatchId());

                DepreciationAmountContext depreciationAmountContext
                    = DepreciationAmountContext.getDepreciationAmountContext(depreciationAmountContextId);

                // Increment number of pending items
                depreciationAmountContext.addNumberOfPendingItems(assetIds.size());

                // Perform the depreciation calculations for the batch of assets
                for (String assetId : assetIds) {

                    depreciateAssetId(message, batchSequence, depreciationJob, depreciationPeriod, fiscalMonth, depreciationAmountContext, assetId);
                }

                message.setProcessed(true);
            },
            () -> {
                throw new DepreciationBatchSequenceNotFound(message.getBatchId());
            });

        return message;
    }

    private void depreciateAssetId(DepreciationBatchMessage message, DepreciationBatchSequenceDTO batchSequence, DepreciationJobDTO depreciationJob, DepreciationPeriodDTO depreciationPeriod, FiscalMonthDTO fiscalMonth, DepreciationAmountContext depreciationAmountContext, String assetId) {

        DepreciationJobContext contextManager = DepreciationJobContext.getInstance();

        UUID depreciationJobCountUpContextId = message.getContextInstance().getDepreciationJobCountUpContextId();
        UUID depreciationJobCountDownContextId = message.getContextInstance().getDepreciationJobCountDownContextId();
        UUID depreciationBatchCountUpContextId = message.getContextInstance().getDepreciationBatchCountUpContextId();
        UUID depreciationBatchCountDownContextId = message.getContextInstance().getDepreciationBatchCountDownContextId();


        // Retrieve the asset from the database using the assetId
        assetRegistrationService.findOne(Long.valueOf(assetId)).ifPresent(
            assetRegistration -> {

                log.debug("Asset id {} ready for depreciation sequence, standby for next update", assetRegistration.getId());

                if (assetCategoryNotConfigured(batchSequence, depreciationJob, depreciationPeriod, assetRegistration))
                    throw new AssetCategoryNotConfiguredException(assetRegistration, message);

                assetCategoryService.findOne(assetRegistration.getAssetCategory().getId()).ifPresent(assetCategory -> {

                    if (serviceOutletNotConfigured(batchSequence, depreciationJob, depreciationPeriod, assetRegistration))
                        throw new ServiceOutletNotConfiguredException(assetRegistration, message);

                    serviceOutletService.findOne(assetRegistration.getMainServiceOutlet().getId()).ifPresent(serviceOutlet -> {

                        depreciationMethodService.findOne(assetCategory.getDepreciationMethod().getId()).ifPresent(
                            depreciationMethod -> {

                                final BigDecimal costAdjustment = adjustedCostService.getAssetAmountAdjustment(depreciationPeriod, assetId);

                                    // Calculate the depreciation amount using the DepreciationCalculator
                                    DepreciationArtefact depreciationArtefact = depreciationCalculatorService.calculateDepreciation(assetRegistration, depreciationPeriod, assetCategory, depreciationMethod, costAdjustment);

                                    recordDepreciationEntry(
                                        depreciationPeriod,
                                        fiscalMonth,
                                        assetRegistration,
                                        assetCategory,
                                        serviceOutlet,
                                        depreciationMethod,
                                        depreciationArtefact,
                                        depreciationJob,
                                        batchSequence,
                                        depreciationJobCountDownContextId
                                    );

                                depreciationAmountContext.setNumberOfProcessedItems(depreciationAmountContext.getNumberOfProcessedItems() + 1);

                                // update depreciation amount
                                depreciationAmountContext.updateAmountForServiceOutlet(assetCategory.getAssetCategoryName(), serviceOutlet.getOutletCode(), depreciationArtefact.getDepreciationAmount().doubleValue());
                            });

                        contextManager.updateNumberOfProcessedItems(depreciationJobCountUpContextId, 1);
                        contextManager.updateNumberOfProcessedItems(depreciationBatchCountUpContextId, 1);
                        contextManager.updateNumberOfProcessedItems(depreciationBatchCountDownContextId, -1);
                    });
                });
            });
    }

    private boolean depreciationJobCompletionStatusCheck(DepreciationBatchMessage message, DepreciationBatchSequenceDTO batchSequence, DepreciationJobDTO depreciationJob, DepreciationPeriodDTO depreciationPeriod) {
        if (depreciationJobStatusIsComplete(batchSequence, depreciationJob, depreciationPeriod, message)) {
            // TODO Mark job complete
            depreciationJob.setProcessingTime(Duration.ofNanos(System.nanoTime() - depreciationJob.getTimeOfCommencement().getNano()));
            depreciationJob.setProcessedItems(depreciationJob.getProcessedItems() + message.getBatchSize());
            depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.COMPLETE);
            depreciationJobService.save(depreciationJob);

            return true;
        }
        return false;
    }

    @NotNull
    private BigDecimal updateDisposedAssetAmount(DepreciationPeriodDTO depreciationPeriod, String assetId) {
        final BigDecimal[] disposalAmount = {BigDecimal.ZERO};

        internalAssetDisposalService.findDisposedItems(Long.valueOf(assetId), depreciationPeriod.getStartDate())
            .ifPresent(disposalEvents -> disposalEvents.forEach(disposalEvent ->  disposalAmount[0] = disposalAmount[0].add(disposalEvent.getAssetCost())));

        return disposalAmount[0];
    }

    // Send to Queue
    private void recordDepreciationEntry(
        DepreciationPeriodDTO depreciationPeriod,
        FiscalMonthDTO fiscalMonth,
        AssetRegistrationDTO assetRegistration,
        AssetCategoryDTO assetCategory,
        ServiceOutletDTO serviceOutlet,
        DepreciationMethodDTO depreciationMethod,
        DepreciationArtefact depreciationArtefact,
        DepreciationJobDTO depreciationJobDTO,
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO,
        UUID depreciationJobCountDownContextId
        ) {
        // Save the depreciation to the database
        DepreciationEntryDTO depreciationEntry = new DepreciationEntryDTO();
        depreciationEntry.setDepreciationAmount(depreciationArtefact.getDepreciationAmount());
        depreciationEntry.setDepreciationMethod(depreciationMethod);
        depreciationEntry.setDepreciationPeriod(depreciationPeriod);
        depreciationEntry.setAssetCategory(assetCategory);
        depreciationEntry.setAssetNumber(Long.valueOf(assetRegistration.getAssetNumber()));
        depreciationEntry.setAssetRegistration(assetRegistration);
        depreciationEntry.setPostedAt(ZonedDateTime.now());
        depreciationEntry.setServiceOutlet(serviceOutlet);
        depreciationEntry.setFiscalYear(fiscalMonth.getFiscalYear());
        depreciationEntry.setFiscalMonth(fiscalMonth);
        depreciationEntry.setFiscalQuarter(fiscalMonth.getFiscalQuarter());
        depreciationEntry.setDepreciationJob(depreciationJobDTO);
        depreciationEntry.setDepreciationBatchSequence(depreciationBatchSequenceDTO );

        depreciationEntry.setElapsedMonths(depreciationArtefact.getElapsedMonths());
        depreciationEntry.setPriorMonths(depreciationArtefact.getPriorMonths());
        depreciationEntry.setUsefulLifeYears(depreciationArtefact.getUsefulLifeYears());
        depreciationEntry.setPreviousNBV(depreciationArtefact.getNbvBeforeDepreciation());
        depreciationEntry.setNetBookValue(depreciationArtefact.getNbv());
        depreciationEntry.setDepreciationPeriodStartDate(depreciationArtefact.getDepreciationPeriodStartDate());
        depreciationEntry.setDepreciationPeriodEndDate(depreciationArtefact.getDepreciationPeriodEndDate());

        depreciationEntry.setCapitalizationDate(depreciationArtefact.getCapitalizationDate());

        depreciationEntrySinkProcessor.addDepreciationEntry(depreciationEntryMapper.toEntity(depreciationEntry), depreciationJobCountDownContextId);
    }

    private boolean serviceOutletNotConfigured(DepreciationBatchSequenceDTO batchSequence, DepreciationJobDTO depreciationJob, DepreciationPeriodDTO depreciationPeriod, io.github.erp.service.dto.AssetRegistrationDTO assetRegistration) {
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
            return true;
        }
        return false;
    }

    private boolean assetCategoryNotConfigured(DepreciationBatchSequenceDTO batchSequence, DepreciationJobDTO depreciationJob, DepreciationPeriodDTO depreciationPeriod, io.github.erp.service.dto.AssetRegistrationDTO assetRegistration) {
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

            return true;
        }
        return false;
    }

    private boolean depreciationJobStatusIsComplete(DepreciationBatchSequenceDTO batchSequence, DepreciationJobDTO depreciationJob, DepreciationPeriodDTO depreciationPeriod, DepreciationBatchMessage message) {

        // Check if batch-size agrees with the counter first
        UUID jobCountDownContextId = message.getContextInstance().getDepreciationJobCountDownContextId();
        DepreciationJobContext contextManager = DepreciationJobContext.getInstance();

        // This should now be zero
        int pendingItems = contextManager.getNumberOfProcessedItems(jobCountDownContextId);

        if (pendingItems == 0 | pendingItems < 0) {

            log.warn("DepreciationJob id {} is status COMPLETE for period id {}. System opting out the depreciation sequence. Standby", depreciationJob.getId(), depreciationPeriod.getId());

            // update job as complete
            depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.COMPLETE);
            depreciationJobService.save(depreciationJob);

            return true;
        }

        return false;
    }

    @Nullable
    private FiscalMonthDTO getFiscalMonth(DepreciationBatchSequenceDTO batchSequence, DepreciationJobDTO depreciationJob, DepreciationPeriodDTO depreciationPeriod) {
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

            return null;
        }


        return fiscalMonthService.findOne(depreciationPeriod.getFiscalMonth().getId()).get();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Nullable
    private DepreciationPeriodDTO getDepreciationPeriod(DepreciationBatchSequenceDTO batchSequence, DepreciationJobDTO depreciationJob) {
        if (depreciationPeriodDoesntExist(batchSequence, depreciationJob)) return null;

        return depreciationPeriodService.findOne(depreciationJob.getDepreciationPeriod().getId()).get();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Nullable
    private DepreciationJobDTO getDepreciationJob(DepreciationBatchSequenceDTO batchSequence, String jobId) {
        if (depreciationJobDoesntExist(batchSequence, jobId)) return null;

        return depreciationJobService.findOne(Long.valueOf(jobId)).get();
    }

    private boolean depreciationPeriodIsClosed(DepreciationBatchSequenceDTO batchSequence, DepreciationJobDTO depreciationJob, DepreciationPeriodDTO depreciationPeriod) {
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

            return true;
        }
        return false;
    }

    private boolean depreciationPeriodDoesntExist(DepreciationBatchSequenceDTO batchSequence, DepreciationJobDTO depreciationJob) {
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

            return true;
        }
        return false;
    }

    private boolean depreciationJobDoesntExist(DepreciationBatchSequenceDTO batchSequence, String jobId) {

        boolean depreciationJobDoesntExist = depreciationJobService.findOne(Long.valueOf(jobId)).isEmpty();

        if (depreciationJobDoesntExist) {
            log.warn("Depreciation job id {} does not exist", jobId);

            DepreciationJobNoticeDTO notice = new DepreciationJobNoticeDTO();
            notice.setDepreciationNoticeStatus(DepreciationNoticeStatusType.ERROR);
            notice.setEventNarrative("Depreciation job id: " + jobId + " doesn't exist");
            notice.setEventTimeStamp(ZonedDateTime.now());
            notice.setDepreciationBatchSequence(batchSequence);
            depreciationJobNoticeService.save(notice);

            return true;
        }
        return false;
    }
}
