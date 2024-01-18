package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.enumeration.CurrencyTypes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.Payment} entity.
 */
public class PaymentDTO implements Serializable {

    private Long id;

    private String paymentNumber;

    private LocalDate paymentDate;

    private BigDecimal invoicedAmount;

    private BigDecimal paymentAmount;

    private String description;

    @NotNull
    private CurrencyTypes settlementCurrency;

    @Lob
    private byte[] calculationFile;

    private String calculationFileContentType;
    private String dealerName;

    private String purchaseOrderNumber;

    private String fileUploadToken;

    private String compilationToken;

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private PaymentCategoryDTO paymentCategory;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private PaymentDTO paymentGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CurrencyTypes getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(CurrencyTypes settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public byte[] getCalculationFile() {
        return calculationFile;
    }

    public void setCalculationFile(byte[] calculationFile) {
        this.calculationFile = calculationFile;
    }

    public String getCalculationFileContentType() {
        return calculationFileContentType;
    }

    public void setCalculationFileContentType(String calculationFileContentType) {
        this.calculationFileContentType = calculationFileContentType;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
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

    public Set<PaymentLabelDTO> getPaymentLabels() {
        return paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabelDTO> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public PaymentCategoryDTO getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(PaymentCategoryDTO paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public PaymentDTO getPaymentGroup() {
        return paymentGroup;
    }

    public void setPaymentGroup(PaymentDTO paymentGroup) {
        this.paymentGroup = paymentGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", invoicedAmount=" + getInvoicedAmount() +
            ", paymentAmount=" + getPaymentAmount() +
            ", description='" + getDescription() + "'" +
            ", settlementCurrency='" + getSettlementCurrency() + "'" +
            ", calculationFile='" + getCalculationFile() + "'" +
            ", dealerName='" + getDealerName() + "'" +
            ", purchaseOrderNumber='" + getPurchaseOrderNumber() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            ", paymentLabels=" + getPaymentLabels() +
            ", paymentCategory=" + getPaymentCategory() +
            ", placeholders=" + getPlaceholders() +
            ", paymentGroup=" + getPaymentGroup() +
            "}";
    }
}
