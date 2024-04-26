package io.github.erp.erp.assets.depreciation.adjustments;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Calculates the aggregate amount of adjustment to be subtracted from the asset
 */
@Service("aggregateAssetCostAdjustmentService")
public class AggregateAssetCostAdjustmentService implements AdjustedCostService {

    private final AdjustedCostService assetDisposalAdjustmentService;
    private final AdjustedCostService assetWriteOffAdjustmentService;
    private final AdjustedCostService assetRevaluationAdjustmentService;
    private final AdjustedCostService generalAssetAmountAdjustmentService;


    public AggregateAssetCostAdjustmentService(
        AdjustedCostService assetDisposalAdjustmentService,
        AdjustedCostService assetWriteOffAdjustmentService,
        AdjustedCostService assetRevaluationAdjustmentService,
        AdjustedCostService generalAssetAmountAdjustmentService) {
        this.assetDisposalAdjustmentService = assetDisposalAdjustmentService;
        this.assetWriteOffAdjustmentService = assetWriteOffAdjustmentService;
        this.assetRevaluationAdjustmentService = assetRevaluationAdjustmentService;
        this.generalAssetAmountAdjustmentService = generalAssetAmountAdjustmentService;
    }

    /**
     * @param depreciationPeriod Specified for the current Job
     * @param assetId            of the asset whose depreciation we are calculating
     * @return The amount of adjustment to the asset cost
     */
    @Override
    public BigDecimal getAssetAmountAdjustment(DepreciationPeriodDTO depreciationPeriod, String assetId) {
        BigDecimal adjustment = BigDecimal.ZERO;

        adjustment = adjustment.add(assetDisposalAdjustmentService.getAssetAmountAdjustment(depreciationPeriod, assetId));
        adjustment = adjustment.add(assetWriteOffAdjustmentService.getAssetAmountAdjustment(depreciationPeriod, assetId));
        adjustment = adjustment.add(assetRevaluationAdjustmentService.getAssetAmountAdjustment(depreciationPeriod, assetId));
        adjustment = adjustment.add(generalAssetAmountAdjustmentService.getAssetAmountAdjustment(depreciationPeriod, assetId));

        return adjustment;
    }
}
