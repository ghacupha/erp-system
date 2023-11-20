package io.github.erp.erp.resources;

import java.math.BigDecimal;

public interface WorkInProgressOutstandingReportTuple {

    long getId();

    String getSequenceNumber();

    String getParticulars();

    String getDealerName();

    String getIso4217Code();

    BigDecimal getInstalmentAmount();

    BigDecimal getTotalTransferAmount();

    BigDecimal getOutstandingAmount();
}
