package io.github.erp.service.dto;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    private SettlementCurrencyDTO settlementCurrency;

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
            ", settlementCurrency=" + getSettlementCurrency() +
            "}";
    }
}
