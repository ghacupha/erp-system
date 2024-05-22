package io.github.erp.internal.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RouDepreciationEntryReportItemInternal {

    Long getId();

    String getLeaseContractNumber();

    String getFiscalPeriodCode();

    LocalDate getFiscalPeriodEndDate();

    String getAssetCategoryName();

    String getDebitAccountNumber();

    String getCreditAccountNumber();

    String getDescription();

    String getShortTitle();

    String getRouAssetIdentifier();

    Integer getSequenceNumber();

    BigDecimal getDepreciationAmount();

    BigDecimal getOutstandingAmount();
}
