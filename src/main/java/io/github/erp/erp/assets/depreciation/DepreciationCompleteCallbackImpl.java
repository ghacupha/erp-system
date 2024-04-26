package io.github.erp.erp.assets.depreciation;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.erp.assets.depreciation.context.DepreciationJobContext;
import io.github.erp.erp.assets.depreciation.model.DepreciationBatchMessage;
import io.github.erp.service.DepreciationBatchSequenceService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DepreciationCompleteCallbackImpl implements DepreciationCompleteCallback {

    private final DepreciationBatchSequenceService depreciationBatchSequenceService;

    public DepreciationCompleteCallbackImpl(DepreciationBatchSequenceService depreciationBatchSequenceService) {
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
    }

    @Override
    public void onComplete(DepreciationBatchMessage message) {
        // Check if batch-size agrees with the counter first
        UUID batchCountDownContextId = message.getContextInstance().getDepreciationBatchCountDownContextId();
        DepreciationJobContext contextManager = DepreciationJobContext.getInstance();

        // This should now be zero
        int pendingItems = contextManager.getNumberOfProcessedItems(batchCountDownContextId);

        if (pendingItems == 0) {
            // This method will be called after each depreciation batch is completed
            depreciationBatchSequenceService.findOne(Long.valueOf(message.getBatchId())).ifPresent(depreciationBatch -> {
                if (depreciationBatch.getDepreciationBatchStatus() != DepreciationBatchStatusType.COMPLETED) {
                    depreciationBatch.setDepreciationBatchStatus(DepreciationBatchStatusType.COMPLETED);
                    depreciationBatchSequenceService.save(depreciationBatch);
                }
            });
        }

        // Else check sequentially on a timer

    }
}

