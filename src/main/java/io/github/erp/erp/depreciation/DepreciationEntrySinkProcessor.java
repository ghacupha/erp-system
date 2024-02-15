package io.github.erp.erp.depreciation;

import io.github.erp.domain.DepreciationEntry;

public interface DepreciationEntrySinkProcessor {

    void addDepreciationEntry(DepreciationEntry entry);
}
