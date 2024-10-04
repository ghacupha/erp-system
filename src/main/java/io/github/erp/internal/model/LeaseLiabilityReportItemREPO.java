package io.github.erp.internal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaseLiabilityReportItemREPO {

    private Long id;

    private String bookingId;

    private String leaseTitle;

    private String liabilityAccountNumber;

    private BigDecimal liabilityAmount;

    private String interestPayableAccountNumber;

    private BigDecimal interestPayableAmount;
}
