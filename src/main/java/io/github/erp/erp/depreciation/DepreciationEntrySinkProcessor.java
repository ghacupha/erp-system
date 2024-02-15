package io.github.erp.erp.depreciation;

import io.github.erp.domain.DepreciationEntry;

import java.util.UUID;

public interface DepreciationEntrySinkProcessor {

    void addDepreciationEntry(DepreciationEntry entry, UUID depreciationJobCountDownContextId);

    void flushRemainingItems(UUID depreciationJobCountDownContextId);
}
