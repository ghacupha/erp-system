package io.github.erp.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AssetWriteOffInternal {

    Long getId();

    String getDescription();

    BigDecimal getWriteOffAmount();

    LocalDate getWriteOffDate();

    LocalDate getEffectivePeriodStartDate();

    LocalDate getEffectivePeriodEndDate();

    String getAssetNumber();
}
