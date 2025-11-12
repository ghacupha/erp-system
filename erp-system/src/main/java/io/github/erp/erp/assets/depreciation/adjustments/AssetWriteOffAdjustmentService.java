package io.github.erp.erp.assets.depreciation.adjustments;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.internal.service.assets.InternalAssetWriteOffService;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Adjustments triggered by write-off of an asset
 */
@Service("assetWriteOffAdjustmentService")
public class AssetWriteOffAdjustmentService implements AdjustedCostService {

    private final InternalAssetWriteOffService internalAssetWriteOffService;

    public AssetWriteOffAdjustmentService(InternalAssetWriteOffService internalAssetWriteOffService) {
        this.internalAssetWriteOffService = internalAssetWriteOffService;
    }

    /**
     * @param depreciationPeriod Specified for the current Job
     * @param assetId            of the asset whose depreciation we are calculating
     * @return The amount of adjustment to the asset cost
     */
    @Override
    public BigDecimal getAssetAmountAdjustment(DepreciationPeriodDTO depreciationPeriod, String assetId) {

        final BigDecimal[] writeOffAmount = {BigDecimal.ZERO};

        internalAssetWriteOffService.findAssetWriteOffByIdAndPeriod(depreciationPeriod, Long.valueOf(assetId))
            .ifPresent(writeOffEvents -> writeOffEvents.forEach(writeOffEvent ->  writeOffAmount[0] = writeOffAmount[0].add(writeOffEvent.getWriteOffAmount())));

        return writeOffAmount[0];
    }
}
