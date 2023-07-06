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
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.repository.AssetRegistrationRepository;
import io.github.erp.repository.DepreciationBatchSequenceRepository;
import io.github.erp.repository.DepreciationJobRepository;
import io.github.erp.service.dto.DepreciationJobDTO;
import io.github.erp.service.mapper.DepreciationJobMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DepreciationService {

    private final DepreciationJobMapper depreciationJobMapper;
    private final DepreciationJobRepository depreciationRunRepository;
    private final AssetRegistrationRepository assetRepository;
    private final DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;
    // TODO private final DepreciationCalculator depreciationCalculator;

    public DepreciationService(DepreciationJobMapper depreciationJobMapper,
                               DepreciationJobRepository depreciationJobRepository,
                               AssetRegistrationRepository assetRepository,
                               DepreciationBatchSequenceRepository depreciationBatchSequenceRepository) {
        this.depreciationJobMapper = depreciationJobMapper;
        // todo DepreciationCalculator depreciationCalculator) {
        this.depreciationRunRepository = depreciationJobRepository;
        this.assetRepository = assetRepository;
        this.depreciationBatchSequenceRepository = depreciationBatchSequenceRepository;
        // TODO this.depreciationCalculator = depreciationCalculator;
    }

    public void triggerDepreciation(DepreciationJobDTO depreciationJob) {

        // Save the DepreciationRun entity to the database
        // depreciationRun = depreciationRunRepository.save(depreciationRun);

        // Retrieve the assets from the database
        List<AssetRegistration> assets = assetRepository.findAll();

        // Process the assets in batches
        int batchSize = 100; // Set the batch size as desired
        int totalAssets = assets.size();
        int processedCount = 0;

        while (processedCount < totalAssets) {
            int startIndex = processedCount;
            int endIndex = Math.min(processedCount + batchSize, totalAssets);

            // Get the current batch of assets
            List<AssetRegistration> currentBatch = assets.subList(startIndex, endIndex);

            // Perform the depreciation calculations for the current batch
            for (AssetRegistration asset : currentBatch) {
                // Calculate the depreciation amount using the DepreciationCalculator
                // TODO BigDecimal depreciationAmount = depreciationCalculator.calculateStraightLineDepreciation(asset.getAssetCost(), asset.getResidualValue(), asset.getUsefulLifeYears());
                BigDecimal depreciationAmount = BigDecimal.ZERO;

                // Update the asset's net book value and any other relevant data
                // ...

                // Save the updated asset to the database
                assetRepository.save(asset);
            }

            // Create a DepreciationBatchSequence entity to track the processed batch
            DepreciationBatchSequence batchSequence = new DepreciationBatchSequence();
            batchSequence.setDepreciationJob(depreciationJobMapper.toEntity(depreciationJob));
            batchSequence.setStartIndex(startIndex);
            batchSequence.setEndIndex(endIndex);
            batchSequence.depreciationBatchStatus(DepreciationBatchStatusType.COMPLETED);

            // Save the DepreciationBatchSequence entity to the database
            depreciationBatchSequenceRepository.save(batchSequence);

            processedCount += batchSize;
        }

        // Mark the depreciation run as completed
        depreciationJob.setDepreciationJobStatus(DepreciationJobStatusType.COMPLETE);
        depreciationRunRepository.save(depreciationJobMapper.toEntity(depreciationJob));
    }
}

