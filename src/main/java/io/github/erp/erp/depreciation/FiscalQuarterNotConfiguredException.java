package io.github.erp.erp.depreciation;

import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;

public class FiscalQuarterNotConfiguredException extends IllegalStateException {
    public FiscalQuarterNotConfiguredException(DepreciationBatchMessage message) {
    }
}
