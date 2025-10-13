package io.github.erp.erp.assets.depreciation.adjustments;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
