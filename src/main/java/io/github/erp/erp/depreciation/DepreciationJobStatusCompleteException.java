package io.github.erp.erp.depreciation;

import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;

public class DepreciationJobStatusCompleteException extends IllegalStateException {
    public DepreciationJobStatusCompleteException(DepreciationBatchMessage message) {
    }
}
