package io.github.erp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkInProgressOverviewDTO {

    private String currencyCode;

    private BigDecimal instalmentAmount;

    private BigDecimal totalTransferAmount;

    private BigDecimal outstandingAmount;
}
