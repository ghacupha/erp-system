package io.github.erp.erp.assets.depreciation.adjustments;

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
