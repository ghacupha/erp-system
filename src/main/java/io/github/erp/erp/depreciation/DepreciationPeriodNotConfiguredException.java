package io.github.erp.erp.depreciation;

import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;

public class DepreciationPeriodNotConfiguredException extends IllegalStateException {
    public DepreciationPeriodNotConfiguredException(DepreciationBatchMessage message) {
    }
}
