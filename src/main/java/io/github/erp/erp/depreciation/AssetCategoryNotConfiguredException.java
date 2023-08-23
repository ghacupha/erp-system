package io.github.erp.erp.depreciation;

import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.dto.AssetRegistrationDTO;

public class AssetCategoryNotConfiguredException extends IllegalStateException {
    public AssetCategoryNotConfiguredException(AssetRegistrationDTO assetRegistration, DepreciationBatchMessage message) {
    }
}
