package io.github.erp.domain;

import java.math.BigDecimal;

public interface DepreciationEntryInternal {

    Long getGetId();

    String getAssetRegistrationDetails();

    String getPostedAt();

    String getAssetNumber();

    String getServiceOutletCode();

    String getAssetCategory();

    String getDepreciationMethod();

    String getDepreciationPeriodCode();

    String getFiscalMonthCode();

    BigDecimal getAssetRegistrationCost();

    BigDecimal getDepreciationAmount();
}
