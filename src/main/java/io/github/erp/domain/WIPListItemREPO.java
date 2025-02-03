package io.github.erp.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class WIPListItemREPO implements Serializable {


    private static final long serialVersionUID = 1L;

    private Long id;

    private String sequenceNumber;

    private String particulars;

    private LocalDate instalmentDate;

    private BigDecimal instalmentAmount;

    private String settlementCurrency;

    private String outletCode;

    private String settlementTransaction;

    private LocalDate settlementTransactionDate;

    private String dealerName;

    private String workProject;
}
