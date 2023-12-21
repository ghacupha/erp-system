package io.github.erp.domain;

import java.math.BigDecimal;

public interface AmortizationPostingReportInternal {

    Long getId();

    String getCatalogueNumber();

    String getDebitAccount();

    String getCreditAccount();

    String getDescription();

    BigDecimal getAmortizationAmount();

}
