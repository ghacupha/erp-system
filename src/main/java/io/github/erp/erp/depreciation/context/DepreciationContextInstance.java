package io.github.erp.erp.depreciation.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepreciationContextInstance implements Serializable {

    private UUID depreciationJobCountUpContextId;
    private UUID depreciationJobCountDownContextId;
    private UUID depreciationBatchCountUpContextId;
    private UUID depreciationBatchCountDownContextId;
    private UUID messageCountContextId;
    private List<UUID> cumulativeDepreciationContextIds; //for storing items by sol and by category
    private List<UUID> netBookValueContextIds; //for storing items by sol and by category
}
