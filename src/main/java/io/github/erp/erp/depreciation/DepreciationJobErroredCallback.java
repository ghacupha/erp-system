package io.github.erp.erp.depreciation;

import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;

@FunctionalInterface
public interface DepreciationJobErroredCallback {

    void onError(DepreciationBatchMessage message, Exception e);
}
