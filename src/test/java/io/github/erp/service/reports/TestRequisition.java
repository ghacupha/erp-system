package io.github.erp.service.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
class TestRequisition {
    private long id;
    private String biller;
    private String currencyCode;
    private BigDecimal paymentAmount;
    private String currentOwner;

}
