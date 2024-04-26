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
import io.github.erp.domain.AssetWriteOffInternal;
import io.github.erp.internal.service.InternalAssetGeneralAdjustmentService;
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
