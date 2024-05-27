package io.github.erp.internal.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RouAssetNBVReportItemInternal {

    Long getId();

    String getModelTitle();

    BigDecimal getModelVersion();

    String getDescription();

    String getRouModelReference();

    LocalDate getCommencementDate();

    LocalDate getExpirationDate();

    String getAssetCategoryName();

    String getAssetAccountNumber();

    String getDepreciationAccountNumber();

    LocalDate getFiscalPeriodEndDate();

    BigDecimal getLeaseAmount();

    BigDecimal getNetBookValue();
}
