package io.github.erp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WorkInProgressReportREPO implements Serializable {

    private String projectTitle;
    private String dealerName;
    private long numberOfItems;
    private BigDecimal instalmentAmount;
    private BigDecimal transferAmount;
    private BigDecimal outstandingAmount;
}
