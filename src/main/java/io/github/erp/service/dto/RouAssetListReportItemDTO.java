package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import java.util.UUID;

/**
 * A DTO for the {@link io.github.erp.domain.RouAssetListReportItem} entity.
 */
public class RouAssetListReportItemDTO implements Serializable {

    private Long id;

    private String modelTitle;

    private BigDecimal modelVersion;

    private String description;

    private Integer leaseTermPeriods;

    private UUID rouModelReference;

    private LocalDate commencementDate;

    private LocalDate expirationDate;

    private String leaseContractTitle;

    private String assetAccountNumber;

    private String depreciationAccountNumber;

    private String accruedDepreciationAccountNumber;

    private String assetCategoryName;

    private BigDecimal leaseAmount;

    private String leaseContractSerialNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelTitle() {
        return modelTitle;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    public BigDecimal getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(BigDecimal modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLeaseTermPeriods() {
        return leaseTermPeriods;
    }

    public void setLeaseTermPeriods(Integer leaseTermPeriods) {
        this.leaseTermPeriods = leaseTermPeriods;
    }

    public UUID getRouModelReference() {
        return rouModelReference;
    }

    public void setRouModelReference(UUID rouModelReference) {
        this.rouModelReference = rouModelReference;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLeaseContractTitle() {
        return leaseContractTitle;
    }

    public void setLeaseContractTitle(String leaseContractTitle) {
        this.leaseContractTitle = leaseContractTitle;
    }

    public String getAssetAccountNumber() {
        return assetAccountNumber;
    }

    public void setAssetAccountNumber(String assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
    }

    public String getDepreciationAccountNumber() {
        return depreciationAccountNumber;
    }

    public void setDepreciationAccountNumber(String depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
    }

    public String getAccruedDepreciationAccountNumber() {
        return accruedDepreciationAccountNumber;
    }

    public void setAccruedDepreciationAccountNumber(String accruedDepreciationAccountNumber) {
        this.accruedDepreciationAccountNumber = accruedDepreciationAccountNumber;
    }

    public String getAssetCategoryName() {
        return assetCategoryName;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public BigDecimal getLeaseAmount() {
        return leaseAmount;
    }

    public void setLeaseAmount(BigDecimal leaseAmount) {
        this.leaseAmount = leaseAmount;
    }

    public String getLeaseContractSerialNumber() {
        return leaseContractSerialNumber;
    }

    public void setLeaseContractSerialNumber(String leaseContractSerialNumber) {
        this.leaseContractSerialNumber = leaseContractSerialNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouAssetListReportItemDTO)) {
            return false;
        }

        RouAssetListReportItemDTO rouAssetListReportItemDTO = (RouAssetListReportItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rouAssetListReportItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouAssetListReportItemDTO{" +
            "id=" + getId() +
            ", modelTitle='" + getModelTitle() + "'" +
            ", modelVersion=" + getModelVersion() +
            ", description='" + getDescription() + "'" +
            ", leaseTermPeriods=" + getLeaseTermPeriods() +
            ", rouModelReference='" + getRouModelReference() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", leaseContractTitle='" + getLeaseContractTitle() + "'" +
            ", assetAccountNumber='" + getAssetAccountNumber() + "'" +
            ", depreciationAccountNumber='" + getDepreciationAccountNumber() + "'" +
            ", accruedDepreciationAccountNumber='" + getAccruedDepreciationAccountNumber() + "'" +
            ", assetCategoryName='" + getAssetCategoryName() + "'" +
            ", leaseAmount=" + getLeaseAmount() +
            ", leaseContractSerialNumber='" + getLeaseContractSerialNumber() + "'" +
            "}";
    }
}
