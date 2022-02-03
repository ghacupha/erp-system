package io.github.erp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.PaymentInvoice} entity.
 */
public class PaymentInvoiceDTO implements Serializable {

    private Long id;

    @NotNull
    private String invoiceNumber;

    private LocalDate invoiceDate;

    private BigDecimal invoiceAmount;

    private String paymentReference;

    private String dealerName;

    private String fileUploadToken;

    private String compilationToken;

    private Set<PurchaseOrderDTO> purchaseOrders = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private SettlementCurrencyDTO settlementCurrency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getFileUploadToken() {
        return fileUploadToken;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return compilationToken;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public Set<PurchaseOrderDTO> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrderDTO> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<PaymentLabelDTO> getPaymentLabels() {
        return paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabelDTO> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public SettlementCurrencyDTO getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrencyDTO settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentInvoiceDTO)) {
            return false;
        }

        PaymentInvoiceDTO paymentInvoiceDTO = (PaymentInvoiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentInvoiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentInvoiceDTO{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", invoiceAmount=" + getInvoiceAmount() +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", dealerName='" + getDealerName() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            ", purchaseOrders=" + getPurchaseOrders() +
            ", placeholders=" + getPlaceholders() +
            ", paymentLabels=" + getPaymentLabels() +
            ", settlementCurrency=" + getSettlementCurrency() +
            "}";
    }
}
