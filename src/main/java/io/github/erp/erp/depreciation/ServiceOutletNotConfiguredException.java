package io.github.erp.erp.depreciation;

import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.dto.AssetRegistrationDTO;

public class ServiceOutletNotConfiguredException extends IllegalStateException {
    public ServiceOutletNotConfiguredException(AssetRegistrationDTO assetRegistration, DepreciationBatchMessage message) {
    }
}
