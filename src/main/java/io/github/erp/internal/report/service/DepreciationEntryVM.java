package io.github.erp.internal.report.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepreciationEntryVM implements Serializable {

    private Long id;

    private String assetRegistrationDetails;

    private String postedAt;

    private String assetNumber;

    private String serviceOutletCode;

    private String assetCategory;

    private String depreciationMethod;

    private String depreciationPeriodCode;

    private String fiscalMonthCode;

    private BigDecimal assetRegistrationCost;

    private BigDecimal depreciationAmount;
}
