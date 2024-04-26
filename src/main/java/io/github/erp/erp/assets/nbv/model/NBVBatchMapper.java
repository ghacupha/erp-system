package io.github.erp.erp.assets.nbv.model;

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
import io.github.erp.erp.assets.depreciation.context.ContextInstance;
import io.github.erp.erp.assets.depreciation.model.DepreciationBatchMessage;
import io.github.erp.internal.framework.Mapping;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class NBVBatchMapper implements Mapping<DepreciationBatchMessage, NBVBatchMessage> {

    @Override
    public DepreciationBatchMessage toValue1(NBVBatchMessage vs) {
        return DepreciationBatchMessage.builder()
            .messageCorrelationId(vs.getMessageCorrelationId())
            .jobId(String.valueOf(vs.getJobId()))
            .batchId(String.valueOf(vs.getBatchId()))
            .batchSize(vs.getBatchSize())
            .assetIds(vs.getAssetIds().stream().map(String::valueOf).collect(Collectors.toList()))
            .initialCost(vs.getInitialCost())
            .createdAt(vs.getCreatedAt())
            .startIndex(vs.getStartIndex())
            .endIndex(vs.getEndIndex())
            .isLastBatch(vs.isLastBatch())
            .enqueuedCount(vs.getEnqueuedCount())
            .sequenceNumber(vs.getSequenceNumber())
            .totalItems(vs.getTotalItems())
            .contextInstance(vs.getContextInstance())
            .processed(vs.isProcessed())
            .processedMessagesCount(vs.getProcessedMessagesCount())
            .numberOfBatches(vs.getNumberOfBatches())
            .build();
    }

    @Override
    public NBVBatchMessage toValue2(DepreciationBatchMessage vs) {
        return NBVBatchMessage.builder()
            .messageCorrelationId(vs.getMessageCorrelationId())
            .jobId(Long.parseLong(vs.getJobId()))
            .batchId(Long.parseLong(vs.getBatchId()))
            .batchSize(vs.getBatchSize())
            .assetIds(vs.getAssetIds().stream().map(Long::valueOf).collect(Collectors.toList()))
            .initialCost(vs.getInitialCost())
            .createdAt(vs.getCreatedAt())
            .startIndex(vs.getStartIndex())
            .endIndex(vs.getEndIndex())
            .isLastBatch(vs.isLastBatch())
            .enqueuedCount(vs.getEnqueuedCount())
            .sequenceNumber(vs.getSequenceNumber())
            .totalItems(vs.getTotalItems())
            .contextInstance(vs.getContextInstance())
            .processed(vs.isProcessed())
            .processedMessagesCount(vs.getProcessedMessagesCount())
            .numberOfBatches(vs.getNumberOfBatches())
            .build();
    }
}
