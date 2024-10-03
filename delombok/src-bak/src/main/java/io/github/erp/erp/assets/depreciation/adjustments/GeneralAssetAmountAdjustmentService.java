package io.github.erp.erp.assets.depreciation.adjustments;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.internal.service.assets.InternalAssetGeneralAdjustmentService;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * General adjustment to asset amounts triggered by audit process and corrections
 */
@Service("generalAssetAmountAdjustmentService")
public class GeneralAssetAmountAdjustmentService implements AdjustedCostService {

    private final InternalAssetGeneralAdjustmentService internalAssetGeneralAdjustmentService;

    public GeneralAssetAmountAdjustmentService(InternalAssetGeneralAdjustmentService internalAssetGeneralAdjustmentService) {
        this.internalAssetGeneralAdjustmentService = internalAssetGeneralAdjustmentService;
    }

    /**
     * @param depreciationPeriod Specified for the current Job
     * @param assetId            of the asset whose depreciation we are calculating
     * @return The amount of adjustment to the asset cost
     */
    @Override
    public BigDecimal getAssetAmountAdjustment(DepreciationPeriodDTO depreciationPeriod, String assetId) {

        final BigDecimal[] revaluationAmount = {BigDecimal.ZERO};

        internalAssetGeneralAdjustmentService.findAdjustmentItems(Long.valueOf(assetId), depreciationPeriod.getStartDate())
            .ifPresent(revaluationEvents -> revaluationEvents.forEach(revaluationEvent ->  revaluationAmount[0] = revaluationAmount[0].add(revaluationEvent.getDevaluationAmount())));

        return revaluationAmount[0];
    }
}
