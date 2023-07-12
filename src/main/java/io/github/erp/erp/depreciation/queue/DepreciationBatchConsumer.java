package io.github.erp.erp.depreciation.queue;

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
import io.github.erp.erp.depreciation.DepreciationCalculationService;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.repository.AssetRegistrationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static io.github.erp.erp.depreciation.queue.DepreciationBatchProducer.DEPRECIATION_BATCH_TOPIC;

@Component
public class DepreciationBatchConsumer {

    private final AssetRegistrationRepository assetRegistrationRepository;
    private final DepreciationCalculationService depreciationCalculationService;

    public DepreciationBatchConsumer(AssetRegistrationRepository assetRegistrationRepository, DepreciationCalculationService depreciationCalculationService) {
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.depreciationCalculationService = depreciationCalculationService;
    }

    @KafkaListener(topics = DEPRECIATION_BATCH_TOPIC, groupId = "erp-system")
    public void processDepreciationJobMessages(List<DepreciationBatchMessage> messages) {
        // Process the batch of depreciation job messages
        for (DepreciationBatchMessage message : messages) {
            // Extract the necessary details from the message
            String jobId = message.getJobId();
            List<String> assetIds = message.getAssetIds();
            BigDecimal initialCost = message.getInitialCost();
            BigDecimal residualValue = message.getResidualValue();

            depreciationCalculationService.depreciateBatch(message);

            // Perform the depreciation calculations for the batch of assets
            for (String assetId : assetIds) {
                // Retrieve the asset from the database using the assetId
                AssetRegistration asset = assetRegistrationRepository.findById(assetId).orElse(null);

                if (asset != null) {
                    // Calculate the depreciation amount using the DepreciationCalculator
                    BigDecimal depreciationAmount = depreciationCalculator.calculateStraightLineDepreciation(initialCost, residualValue, usefulLifeYears);

                    // Update the asset's net book value and any other relevant data
                    // ...

                    // Save the updated asset to the database
                    assetRegistrationRepository.save(asset);
                }
            }

            // Update the status or progress of the depreciation job, if needed
            // ...
        }
    }
}


