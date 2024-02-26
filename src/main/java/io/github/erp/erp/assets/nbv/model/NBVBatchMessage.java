package io.github.erp.erp.assets.nbv.model;

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
