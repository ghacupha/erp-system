package io.github.erp.erp.depreciation;

import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;

public class FiscalMonthNotConfiguredException extends IllegalStateException {
    public FiscalMonthNotConfiguredException(DepreciationBatchMessage message) {
    }
}
