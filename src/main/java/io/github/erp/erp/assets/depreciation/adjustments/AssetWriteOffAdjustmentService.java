package io.github.erp.erp.assets.depreciation.adjustments;

import io.github.erp.domain.AssetWriteOffInternal;
import io.github.erp.internal.service.InternalAssetWriteOffService;
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
