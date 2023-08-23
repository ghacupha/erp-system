package io.github.erp.erp.depreciation;

import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;

public class FiscalYearNotConfiguredException extends IllegalStateException {
    public FiscalYearNotConfiguredException(DepreciationBatchMessage message) {
    }
}
