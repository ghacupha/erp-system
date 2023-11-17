package io.github.erp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkInProgressOutstandingOptionalReport {

    private long id;

    private String sequenceNumber;

    private String paymentNumber;

    private LocalDate paymentDate;

    private String particulars;

    private String dealerName;

    private String iso4217Code;

    private BigDecimal instalmentAmount;

    private BigDecimal totalTransferAmount;

    private BigDecimal outstandingAmount;
}
