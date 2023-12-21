package io.github.erp.internal.service.autonomousReport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WIPByDealerProjectDTO {

    private Long id;
    private String projectTitle;
    private String dealerName;
    private Long numberOfItems;
    private BigDecimal instalmentAmount;
    private BigDecimal transferAmount;
    private BigDecimal outstandingAmount;
}
