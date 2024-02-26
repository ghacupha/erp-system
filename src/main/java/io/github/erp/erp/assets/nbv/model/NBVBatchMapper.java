package io.github.erp.erp.assets.nbv.model;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
