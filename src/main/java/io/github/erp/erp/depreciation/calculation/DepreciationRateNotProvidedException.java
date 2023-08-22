package io.github.erp.erp.depreciation.calculation;

import io.github.erp.service.dto.AssetCategoryDTO;

/**
 * Thrown when the asset category is not configured with the yearly depreciation rate
 */
public class DepreciationRateNotProvidedException extends IllegalStateException {
    public DepreciationRateNotProvidedException(String errorMessage, AssetCategoryDTO assetCategory) {
        super(errorMessage + " please ensure the yearly depreciation rate for category id : " + assetCategory.getAssetCategoryName() + " is provided in decimals");
    }
}
