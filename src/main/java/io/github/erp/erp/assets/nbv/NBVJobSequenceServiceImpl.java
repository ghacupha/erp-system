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

import io.github.erp.internal.repository.InternalAssetRegistrationRepository;
import io.github.erp.service.NbvCompilationJobService;
import io.github.erp.service.dto.NbvCompilationJobDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.erp.domain.enumeration.NVBCompilationStatus.ENQUEUED;
import static io.github.erp.domain.enumeration.NVBCompilationStatus.RUNNING;
import static io.github.erp.erp.assets.depreciation.DepreciationJobSequenceServiceImpl.PREFERRED_BATCH_SIZE;

@Service
public class NBVJobSequenceServiceImpl implements NBVJobSequenceService<NbvCompilationJobDTO> {

    private final static Logger log = LoggerFactory.getLogger(NBVJobSequenceServiceImpl.class);
    private final NbvCompilationJobService nbvCompilationJobService;
    private final InternalAssetRegistrationRepository internalAssetRegistrationRepository;

    public NBVJobSequenceServiceImpl(
        NbvCompilationJobService nbvCompilationJobService,
        InternalAssetRegistrationRepository internalAssetRegistrationRepository) {
        this.nbvCompilationJobService = nbvCompilationJobService;
        this.internalAssetRegistrationRepository = internalAssetRegistrationRepository;
    }

    @Override
    public void triggerJobStart(NbvCompilationJobDTO nbvCompilationJobDTO) {
        // TODO Implement sink processor
        // TODO Implement batch processing sequence
        // TODO review opt out conditions

        // todo sinkProcessor.Startup()

        if (checkOptOutConditions(nbvCompilationJobDTO)) {

            log.warn("This compilation has tested positive for opt out conditions, and will now terminate. Terminating the job...");

            // TODO sinkProcessor.shutDown();

            return;
        }

        long startUpTime = System.currentTimeMillis();

        log.info("Initiating NBV job sequence, standby for status update...");

        nbvCompilationJobDTO.setCompilationStatus(RUNNING);
        nbvCompilationJobService.save(nbvCompilationJobDTO);

        log.info("NBVCompilationJob status update complete, fetching assets for valuation...");
        // Fetch assets from the database for depreciation processing
        List<Long> assetsIds = fetchAssets(nbvCompilationJobDTO);

        log.info("{} asset-ids acquiring for processing. Generating batches...", assetsIds.size());

        // Process the assets in batches
        int numberOfBatches = processAssetsInBatches(nbvCompilationJobDTO, assetsIds, PREFERRED_BATCH_SIZE);

        log.info("{} batches generated for asynch queue execution", numberOfBatches);

        nbvCompilationJobDTO.setCompilationStatus(ENQUEUED);
        nbvCompilationJobService.save(nbvCompilationJobDTO);
    }

    private int processAssetsInBatches(NbvCompilationJobDTO nbvCompilationJobDTO, List<Long> assetsIds, int preferredBatchSize) {

        // TODO Implement and persist compilation batches

        return 0;
    }


    private List<Long> fetchAssets(NbvCompilationJobDTO nbvCompilationJobDTO) {
        return internalAssetRegistrationRepository.getAssetIdsByCapitalizationDateBefore(nbvCompilationJobDTO.getActivePeriod().getEndDate());
    }

    private boolean checkOptOutConditions(NbvCompilationJobDTO nbvCompilationJobDTO) {

        // TODO due for implementation
        throw new UnsupportedOperationException();
    }
}
