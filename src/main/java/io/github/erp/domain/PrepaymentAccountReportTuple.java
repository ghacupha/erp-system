package io.github.erp.domain;

import java.math.BigDecimal;

public interface PrepaymentAccountReportTuple {

    Long getId();

    String getPrepaymentAccount();

    BigDecimal getPrepaymentAmount();

    BigDecimal getAmortisedAmount();

    BigDecimal getOutstandingAmount();

    Integer getNumberOfPrepaymentAccounts();

    Integer getNumberOfAmortisedItems();
}
