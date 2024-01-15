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
    private UUID depreciationAmountContextId;
}
