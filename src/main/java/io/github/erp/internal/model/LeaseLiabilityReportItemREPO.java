package io.github.erp.internal.model;

import java.math.BigDecimal;

public interface LeaseLiabilityReportItemREPO {

    Long getId();

    String getBookingId();

    String getLeaseTitle();

    String getLiabilityAccountNumber();

    BigDecimal getLiabilityAmount();

    String getInterestPayableAccountNumber();

    BigDecimal getInterestPayableAmount();
}
