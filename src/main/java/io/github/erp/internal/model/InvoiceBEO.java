package io.github.erp.internal.model;

import io.github.erp.domain.enumeration.CurrencyTypes;
import io.github.erp.internal.framework.batch.BatchEntity;
import io.github.erp.service.dto.PaymentLabelDTO;
import io.github.erp.service.dto.PlaceholderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceBEO implements BatchEntity {


    private Long id;

    @NotNull
    private String invoiceNumber;

    private LocalDate invoiceDate;

    private BigDecimal invoiceAmount;

    @NotNull
    private CurrencyTypes currency;

    private String paymentReference;

    private String dealerName;

    private String fileUploadToken;

    private String compilationToken;

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    @Override
    public void setUploadToken(String uploadToken) {
        this.fileUploadToken = uploadToken;
    }

    @Override
    public String getUploadToken() {
        return this.fileUploadToken;
    }
}
