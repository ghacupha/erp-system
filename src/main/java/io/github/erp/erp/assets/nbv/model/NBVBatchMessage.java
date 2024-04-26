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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NBVBatchMessage implements Serializable {

    private boolean processed;
    private int processedMessagesCount;
    private int numberOfBatches;
    private BigDecimal initialCost;
    private UUID messageCorrelationId;
    private long jobId;
    private long batchId;
    private int batchSize;
    private List<Long> assetIds;
    private LocalDateTime createdAt;
    private int startIndex;
    private int endIndex;
    private boolean isLastBatch;
    private int enqueuedCount;
    private int sequenceNumber;
    private int totalItems;
    private ContextInstance contextInstance;


}
