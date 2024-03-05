package io.github.erp.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface MonthlyPrepaymentOutstandingReportItemInternal {

    Long getId();

    LocalDate getFiscalMonthEndDate();

    BigDecimal getTotalPrepaymentAmount();

    BigDecimal getTotalAmortisedAmount();

    BigDecimal getTotalOutstandingAmount();

    Integer getNumberOfPrepaymentAccounts();
}
