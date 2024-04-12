package io.github.erp.erp.assets.depreciation.adjustments;

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


    public AggregateAssetCostAdjustmentService(AdjustedCostService assetDisposalAdjustmentService, AdjustedCostService assetWriteOffAdjustmentService) {
        this.assetDisposalAdjustmentService = assetDisposalAdjustmentService;
        this.assetWriteOffAdjustmentService = assetWriteOffAdjustmentService;
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
        // TODO adjustment = adjustment.add(assetRevaluationAdjustmentService.getAssetAmountAdjustment(depreciationPeriod, assetId));
        // TODO adjustment = adjustment.add(assetGeneralAdjustmentService.getAssetAmountAdjustment(depreciationPeriod, assetId));

        return adjustment;
    }
}
