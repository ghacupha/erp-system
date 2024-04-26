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
