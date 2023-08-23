package io.github.erp.erp.depreciation;

import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;

public class DepreciationPeriodClosedException extends IllegalStateException {
    public DepreciationPeriodClosedException(DepreciationBatchMessage message) {
    }
}
