package io.github.erp.erp.depreciation;

import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;

@FunctionalInterface
public interface DepreciationJobCompleteCallback {

    /**
     * Called when process is complete
     *
     * @param message
     */
    void onComplete(DepreciationBatchMessage message);
}
