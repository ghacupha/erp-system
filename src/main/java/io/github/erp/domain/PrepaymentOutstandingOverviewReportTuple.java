package io.github.erp.domain;

import java.math.BigDecimal;

public interface PrepaymentOutstandingOverviewReportTuple {

    BigDecimal getTotalPrepaymentAmount();

    BigDecimal getTotalAmortisedAmount();

    BigDecimal getTotalOutstandingAmount();

    BigDecimal getNumberOfPrepaymentAccounts();
}
