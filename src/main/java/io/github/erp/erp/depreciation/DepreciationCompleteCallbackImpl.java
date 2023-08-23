package io.github.erp.erp.depreciation;

import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.DepreciationBatchSequenceService;
import org.springframework.stereotype.Service;

@Service
public class DepreciationCompleteCallbackImpl implements DepreciationCompleteCallback {

    private final DepreciationBatchSequenceService depreciationBatchSequenceService;

    public DepreciationCompleteCallbackImpl(DepreciationBatchSequenceService depreciationBatchSequenceService) {
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
    }

    @Override
    public void onComplete(DepreciationBatchMessage message) {
        // This method will be called after each depreciation is completed
        depreciationBatchSequenceService.findOne(Long.valueOf(message.getBatchId())).ifPresent(depreciationBatch -> {
            if (depreciationBatch.getDepreciationBatchStatus() != DepreciationBatchStatusType.COMPLETED) {
                depreciationBatch.setDepreciationBatchStatus(DepreciationBatchStatusType.COMPLETED);
                depreciationBatchSequenceService.save(depreciationBatch);
            }
        });

    }
}

