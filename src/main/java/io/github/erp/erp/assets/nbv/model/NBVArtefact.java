package io.github.erp.erp.assets.nbv.model;

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
public class NBVArtefact {

    private BigDecimal netBookValueAmount;

    private BigDecimal previousNetBookValueAmount;

    private Long elapsedMonths;

    private Long priorMonths;

    private BigDecimal usefulLifeYears;

    private LocalDate activePeriodStartDate;

    private LocalDate activePeriodEndDate;

    private LocalDate capitalizationDate;
}
