package io.github.erp.internal.model;

import io.github.erp.domain.enumeration.CurrencyTypes;
import io.github.erp.internal.framework.batch.BatchEntity;
import io.github.erp.service.dto.PaymentCategoryDTO;
import io.github.erp.service.dto.PaymentDTO;
import io.github.erp.service.dto.PaymentLabelDTO;
import io.github.erp.service.dto.PlaceholderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentBEO implements BatchEntity {

    private Long id;

    private String paymentNumber;

    private LocalDate paymentDate;

    private BigDecimal invoicedAmount;

    private BigDecimal paymentAmount;

    private String description;

    private CurrencyTypes settlementCurrency;

    private String dealerName;

    private String purchaseOrderNumber;

    private String fileUploadToken;

    private String compilationToken;

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private PaymentCategoryDTO paymentCategory;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private PaymentDTO paymentGroup;

    @Override
    public void setUploadToken(String uploadToken) {
        this.fileUploadToken = uploadToken;
    }

    @Override
    public String getUploadToken() {
        return this.fileUploadToken;
    }
}
