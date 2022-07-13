package io.github.erp.service.dto;

/*-
 * Erp System - Mark II No 19 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
 * A DTO for the {@link io.github.erp.domain.Settlement} entity.
 */
public class SettlementDTO implements Serializable {

    private Long id;

    private String paymentNumber;

    private LocalDate paymentDate;

    private BigDecimal paymentAmount;

    private String description;

    private String notes;

    @Lob
    private byte[] calculationFile;

    private String calculationFileContentType;
    private String fileUploadToken;

    private String compilationToken;

    @Lob
    private String remarks;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private SettlementCurrencyDTO settlementCurrency;

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private PaymentCategoryDTO paymentCategory;

    private SettlementDTO groupSettlement;

    private DealerDTO biller;

    private Set<PaymentInvoiceDTO> paymentInvoices = new HashSet<>();

    private Set<DealerDTO> signatories = new HashSet<>();

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public SettlementDTO getGroupSettlement() {
        return groupSettlement;
    }

    public void setGroupSettlement(SettlementDTO groupSettlement) {
        this.groupSettlement = groupSettlement;
    }

    public DealerDTO getBiller() {
        return biller;
    }

    public void setBiller(DealerDTO biller) {
        this.biller = biller;
    }

    public Set<PaymentInvoiceDTO> getPaymentInvoices() {
        return paymentInvoices;
    }

    public void setPaymentInvoices(Set<PaymentInvoiceDTO> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }

    public Set<DealerDTO> getSignatories() {
        return signatories;
    }

    public void setSignatories(Set<DealerDTO> signatories) {
        this.signatories = signatories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettlementDTO)) {
            return false;
        }

        SettlementDTO settlementDTO = (SettlementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, settlementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettlementDTO{" +
            "id=" + getId() +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", calculationFile='" + getCalculationFile() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", paymentLabels=" + getPaymentLabels() +
            ", paymentCategory=" + getPaymentCategory() +
            ", groupSettlement=" + getGroupSettlement() +
            ", biller=" + getBiller() +
            ", paymentInvoices=" + getPaymentInvoices() +
            ", signatories=" + getSignatories() +
            "}";
    }
}
