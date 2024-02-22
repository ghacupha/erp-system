package io.github.erp.erp.assets.nbv;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.domain.enumeration.CompilationBatchStatusTypes;
import io.github.erp.erp.assets.nbv.buffer.BufferedSinkProcessor;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.erp.assets.nbv.queue.NBVBatchProducer;
import io.github.erp.internal.repository.InternalAssetRegistrationRepository;
import io.github.erp.service.NbvCompilationBatchService;
import io.github.erp.service.NbvCompilationJobService;
import io.github.erp.service.dto.NbvCompilationBatchDTO;
import io.github.erp.service.dto.NbvCompilationJobDTO;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import static io.github.erp.domain.enumeration.NVBCompilationStatus.*;
import static io.github.erp.erp.assets.depreciation.DepreciationJobSequenceServiceImpl.PREFERRED_BATCH_SIZE;

@Service
public class NBVJobSequenceServiceImpl implements NBVJobSequenceService<NbvCompilationJobDTO> {

    private final static Logger log = LoggerFactory.getLogger(NBVJobSequenceServiceImpl.class);
    private final NbvCompilationJobService nbvCompilationJobService;
    private final InternalAssetRegistrationRepository internalAssetRegistrationRepository;
    private final NBVBatchProducer nbvBatchProducer;

    private final NbvCompilationBatchService nbvCompilationBatchService;

    private final BufferedSinkProcessor<NetBookValueEntry> netBookValueEntryBufferedSinkProcessor;

    public NBVJobSequenceServiceImpl(
        NbvCompilationJobService nbvCompilationJobService,
        InternalAssetRegistrationRepository internalAssetRegistrationRepository,
        NBVBatchProducer nbvBatchProducer,
        NbvCompilationBatchService nbvCompilationBatchService,
        BufferedSinkProcessor<NetBookValueEntry> netBookValueEntryBufferedSinkProcessor) {
        this.nbvCompilationJobService = nbvCompilationJobService;
        this.internalAssetRegistrationRepository = internalAssetRegistrationRepository;
        this.nbvBatchProducer = nbvBatchProducer;
        this.nbvCompilationBatchService = nbvCompilationBatchService;
        this.netBookValueEntryBufferedSinkProcessor = netBookValueEntryBufferedSinkProcessor;
    }

    @Override
    public void triggerJobStart(NbvCompilationJobDTO nbvCompilationJobDTO) {
        netBookValueEntryBufferedSinkProcessor.startup();
        // TODO Implement batch processing sequence
        // TODO review opt out conditions

        if (nbvCompilationJobDTO.getCompilationStatus() == COMPLETE) {

            log.warn("NBVCompilation job id: {} is status COMPLETE, running processor shutdown", nbvCompilationJobDTO.getId());

            netBookValueEntryBufferedSinkProcessor.flushStuckTaskComplete();

            netBookValueEntryBufferedSinkProcessor.shutdown();

            return;
        }


        if (checkOptOutConditions(nbvCompilationJobDTO)) {

            log.warn("This compilation has tested positive for opt out conditions, and will now terminate. Terminating the job...");

            netBookValueEntryBufferedSinkProcessor.shutdown();
            // TODO sinkProcessor.shutDown();

            return;
        }

        long startUpTime = System.currentTimeMillis();

        log.info("Initiating NBV job sequence, standby for status update...");

        nbvCompilationJobDTO.setCompilationStatus(RUNNING);
        nbvCompilationJobService.save(nbvCompilationJobDTO);

        log.info("NBVCompilationJob status update complete, fetching assets for valuation...");

        // Fetch assets from the database for depreciation processing
        List<Long> assetsIds = new LinkedList<>(fetchAssets(nbvCompilationJobDTO));

        log.info("{} asset-ids acquiring for processing. Generating batches...", assetsIds.size());

        // Process the assets in batches
        int numberOfBatches = processAssetsInBatches(nbvCompilationJobDTO, assetsIds, PREFERRED_BATCH_SIZE);

        log.info("{} batches generated for async queue execution", numberOfBatches);

        nbvCompilationJobDTO.setCompilationStatus(ENQUEUED);
        nbvCompilationJobService.save(nbvCompilationJobDTO);
    }

    private int processAssetsInBatches(NbvCompilationJobDTO nbvCompilationJobDTO, List<Long> assetsIds, int preferredBatchSize) {

        // TODO Implement and persist compilation batches

        final int[] count = {0};
        final int[] processedItems = {0};

        Observable.fromIterable(assetsIds)
            .buffer(PREFERRED_BATCH_SIZE)
            .subscribe(batchList -> {

                int batchSize = PREFERRED_BATCH_SIZE;

                ++count[0];
                processedItems[0] += batchList.size();

                int numberOfBatches = assetsIds.size() / batchSize + (assetsIds.size() % batchSize == 0 ? 0 : 1);

                boolean isLastBatch = processedItems[0] + batchSize >= assetsIds.size();

                enqueueBatch(
                    batchList,
                    nbvCompilationJobDTO.getId(),
                    assetsIds.indexOf(batchList.get(0)),
                    assetsIds.indexOf(batchList.get(batchList.size()-1)),
                    nbvCompilationJobDTO,
                    assetsIds.size(),
                    count[0],
                    processedItems[0],
                    numberOfBatches,
                    isLastBatch
                    );
            });

        /* TODO nbvBatchProducer.sendJobMessage(); */

        return 0;
    }

    private void enqueueBatch(List<Long> assetIds, Long jobId, Integer startIndex, Integer endIndex, NbvCompilationJobDTO nbvCompilationJobDTO, int totalItems, int processedBatches, int processedItems, int numberOfBatches, boolean isLastBatch) {

        NbvCompilationBatchDTO nbvCompilationBatch = new NbvCompilationBatchDTO();
        nbvCompilationBatch.setStartIndex(startIndex);
        nbvCompilationBatch.setEndIndex(endIndex);
        nbvCompilationBatch.setCompilationBatchStatus(CompilationBatchStatusTypes.ENQUEUED);
        nbvCompilationBatch.setCompilationBatchIdentifier(UUID.randomUUID());
        nbvCompilationBatch.setCompilationJobidentifier(nbvCompilationJobDTO.getCompilationJobIdentifier());
        // TODO nbvCompilationBatch.setCompilationPeriodIdentifier()
        // TODO nbvCompilationBatch.setFiscalMonthIdentifier()
        nbvCompilationBatch.setBatchSize(assetIds.size());
        nbvCompilationBatch.setSequenceNumber(processedBatches);
        nbvCompilationBatch.setProcessedItems(processedItems);
        nbvCompilationBatch.setIsLastBatch(isLastBatch);
        // TODO processingTime()
        nbvCompilationBatch.setTotalItems(totalItems);
        nbvCompilationBatch.setNbvCompilationJob(nbvCompilationJobDTO);

        NbvCompilationBatchDTO nbvBatch = nbvCompilationBatchService.save(nbvCompilationBatch);


        NBVBatchMessage nbvBatchMessage = NBVBatchMessage
            .builder()
            .messageCorrelationId(nbvBatch.getCompilationBatchIdentifier())
            .jobId(jobId)
            .batchId(nbvBatch.getId())
            .batchSize(nbvBatch.getBatchSize())
            .assetIds(assetIds)
            .createdAt(LocalDateTime.now())
            .startIndex(nbvBatch.getStartIndex())
            .endIndex(nbvBatch.getEndIndex())
            .isLastBatch(nbvCompilationBatch.getIsLastBatch())
            .enqueuedCount(processedItems)
            .sequenceNumber(nbvCompilationBatch.getSequenceNumber())
            .numberOfBatches(numberOfBatches)

            .build();

        nbvBatchProducer.sendJobMessage(nbvBatchMessage);
    }


    private List<Long> fetchAssets(NbvCompilationJobDTO nbvCompilationJobDTO) {
        return internalAssetRegistrationRepository.getAssetIdsByCapitalizationDateBefore(nbvCompilationJobDTO.getActivePeriod().getEndDate());
    }

    private boolean checkOptOutConditions(NbvCompilationJobDTO nbvCompilationJobDTO) {

        // MAKE SURE THE PERIOD IS NOT CLOSED
        // TODO due for implementation
        return false;
    }
}
