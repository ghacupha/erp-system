package io.github.erp.erp.depreciation;

import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;

public class DepreciationJobNotFoundException extends IllegalStateException {
    public DepreciationJobNotFoundException(DepreciationBatchMessage message) {
    }
}
