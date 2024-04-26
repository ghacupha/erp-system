package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.RouDepreciationEntryReportItem} entity.
 */
public class RouDepreciationEntryReportItemDTO implements Serializable {

    private Long id;

    private String leaseContractNumber;

    private String fiscalPeriodCode;

    private LocalDate fiscalPeriodEndDate;

    private String assetCategoryName;

    private String debitAccountNumber;

    private String creditAccountNumber;

    private String description;

    private String shortTitle;

    private String rouAssetIdentifier;

    private Integer sequenceNumber;

    private BigDecimal depreciationAmount;

    private BigDecimal outstandingAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaseContractNumber() {
        return leaseContractNumber;
    }

    public void setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public String getFiscalPeriodCode() {
        return fiscalPeriodCode;
    }

    public void setFiscalPeriodCode(String fiscalPeriodCode) {
        this.fiscalPeriodCode = fiscalPeriodCode;
    }

    public LocalDate getFiscalPeriodEndDate() {
        return fiscalPeriodEndDate;
    }

    public void setFiscalPeriodEndDate(LocalDate fiscalPeriodEndDate) {
        this.fiscalPeriodEndDate = fiscalPeriodEndDate;
    }

    public String getAssetCategoryName() {
        return assetCategoryName;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public String getDebitAccountNumber() {
        return debitAccountNumber;
    }

    public void setDebitAccountNumber(String debitAccountNumber) {
        this.debitAccountNumber = debitAccountNumber;
    }

    public String getCreditAccountNumber() {
        return creditAccountNumber;
    }

    public void setCreditAccountNumber(String creditAccountNumber) {
        this.creditAccountNumber = creditAccountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getRouAssetIdentifier() {
        return rouAssetIdentifier;
    }

    public void setRouAssetIdentifier(String rouAssetIdentifier) {
        this.rouAssetIdentifier = rouAssetIdentifier;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouDepreciationEntryReportItemDTO)) {
            return false;
        }

        RouDepreciationEntryReportItemDTO rouDepreciationEntryReportItemDTO = (RouDepreciationEntryReportItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rouDepreciationEntryReportItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationEntryReportItemDTO{" +
            "id=" + getId() +
            ", leaseContractNumber='" + getLeaseContractNumber() + "'" +
            ", fiscalPeriodCode='" + getFiscalPeriodCode() + "'" +
            ", fiscalPeriodEndDate='" + getFiscalPeriodEndDate() + "'" +
            ", assetCategoryName='" + getAssetCategoryName() + "'" +
            ", debitAccountNumber='" + getDebitAccountNumber() + "'" +
            ", creditAccountNumber='" + getCreditAccountNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", shortTitle='" + getShortTitle() + "'" +
            ", rouAssetIdentifier='" + getRouAssetIdentifier() + "'" +
            ", sequenceNumber=" + getSequenceNumber() +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            "}";
    }
}
