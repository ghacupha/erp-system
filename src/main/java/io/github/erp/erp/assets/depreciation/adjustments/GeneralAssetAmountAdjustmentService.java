package io.github.erp.erp.assets.depreciation.adjustments;

import io.github.erp.domain.AssetWriteOffInternal;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * General adjustment to asset amounts triggered by audit process and corrections
 */
@Service("generalAssetAmountAdjustmentService")
public class GeneralAssetAmountAdjustmentService implements AdjustedCostService {

    /**
     * @param depreciationPeriod Specified for the current Job
     * @param assetId            of the asset whose depreciation we are calculating
     * @return The amount of adjustment to the asset cost
     */
    @Override
    public BigDecimal getAssetAmountAdjustment(DepreciationPeriodDTO depreciationPeriod, String assetId) {
        return null;
    }
}
