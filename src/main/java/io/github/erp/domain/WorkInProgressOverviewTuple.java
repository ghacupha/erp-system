package io.github.erp.domain;

import java.math.BigDecimal;

public interface WorkInProgressOverviewTuple {

    long getNumberOfItems();
    BigDecimal getInstalmentAmount();
    BigDecimal getTotalTransferAmount();
    BigDecimal getOutstandingAmount();
}
