package io.github.erp.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AssetDisposalInternal {

    Long getId();

    String getDescription();

    BigDecimal getAssetCost();

    BigDecimal getHistoricalCost();

    BigDecimal getAccruedDepreciation();

    BigDecimal getNetBookValue();

    LocalDate getDecommissioningDate();

    LocalDate getDisposalDate();

    LocalDate getDisposalPeriodStartDate();

    LocalDate getDisposalPeriodEndDate();

    String getAssetNumber();
}
