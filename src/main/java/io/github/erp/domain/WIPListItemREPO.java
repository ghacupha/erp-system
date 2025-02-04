package io.github.erp.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface WIPListItemREPO  {

    Long getId();

    String getSequenceNumber();

    String getParticulars();

    LocalDate getInstalmentDate();

    BigDecimal getInstalmentAmount();

    String getSettlementCurrency();

    String getOutletCode();

    String getSettlementTransaction();

    LocalDate getSettlementTransactionDate();

    String getDealerName();

    String getWorkProject();
}
