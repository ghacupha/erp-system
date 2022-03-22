package io.github.erp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CreditNote} entity.
 */
public class CreditNoteDTO implements Serializable {

    private Long id;

    @NotNull
    private String creditNumber;

    @NotNull
    private LocalDate creditNoteDate;

    @NotNull
    private BigDecimal creditAmount;

    @Lob
    private String remarks;

    private Set<PurchaseOrderDTO> purchaseOrders = new HashSet<>();

    private Set<PaymentInvoiceDTO> invoices = new HashSet<>();

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(String creditNumber) {
        this.creditNumber = creditNumber;
    }

    public LocalDate getCreditNoteDate() {
        return creditNoteDate;
    }

    public void setCreditNoteDate(LocalDate creditNoteDate) {
        this.creditNoteDate = creditNoteDate;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Set<PurchaseOrderDTO> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrderDTO> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Set<PaymentInvoiceDTO> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<PaymentInvoiceDTO> invoices) {
        this.invoices = invoices;
    }

    public Set<PaymentLabelDTO> getPaymentLabels() {
        return paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabelDTO> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreditNoteDTO)) {
            return false;
        }

        CreditNoteDTO creditNoteDTO = (CreditNoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, creditNoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreditNoteDTO{" +
            "id=" + getId() +
            ", creditNumber='" + getCreditNumber() + "'" +
            ", creditNoteDate='" + getCreditNoteDate() + "'" +
            ", creditAmount=" + getCreditAmount() +
            ", remarks='" + getRemarks() + "'" +
            ", purchaseOrders=" + getPurchaseOrders() +
            ", invoices=" + getInvoices() +
            ", paymentLabels=" + getPaymentLabels() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
