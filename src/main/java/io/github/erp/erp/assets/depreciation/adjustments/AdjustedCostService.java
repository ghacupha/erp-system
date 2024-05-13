package io.github.erp.erp.assets.depreciation.adjustments;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import java.math.BigDecimal;

/**
 * Derives a cost adjustment for the depreciation depending on presence of asset write-off,
 * disposal events or audit and other revaluation adjustments during the lifetime of the asset.
 * The adjustment amount is subtracted from the cost item itself, prior to running the depreciation
 * sequence at a specified point in time after which the adjustment is effective. The implementation
 * allows for multiple events which means possibility of staggered write-offs.
 * Because we are using point-in-time calculations which depend solely on the asset cost at
 * capitalization date, the adjustment is applied to the asset cost at capitalization date
 * as though we were revising the initial value of the asset before calculating depreciation. Such
 * revaluation is irrelevant until the adjustment's effective period aligns with the current
 * depreciation-job's depreciation period.
 */
public interface AdjustedCostService {

    /**
     *
     * @param depreciationPeriod Specified for the current Job
     * @param assetId of the asset whose depreciation we are calculating
     * @return The amount of adjustment to the asset cost
     */
    BigDecimal getAssetAmountAdjustment(DepreciationPeriodDTO depreciationPeriod, String assetId);
}
