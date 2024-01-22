package io.github.erp.erp.depreciation.model;

import liquibase.pro.packaged.B;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DepreciationArtefact {

    private BigDecimal depreciationAmount;

    private Long elapsedMonths;

    private Long priorMonths;

    private BigDecimal usefulLifeYears;

    private BigDecimal nbvBeforeDepreciation;

    private BigDecimal nbv;

    private LocalDate depreciationPeriodStartDate;

    private LocalDate depreciationPeriodEndDate;
}
