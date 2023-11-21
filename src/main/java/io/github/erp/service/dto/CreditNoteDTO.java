package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
