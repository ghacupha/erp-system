package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.DepreciationEntryReportItem} entity.
 */
public class DepreciationEntryReportItemDTO implements Serializable {

    private Long id;

    private String assetRegistrationDetails;

    private String postedAt;

    private Long assetNumber;

    private String serviceOutletCode;

    private String assetCategory;

    private String depreciationMethod;

    private String depreciationPeriodCode;

    private String fiscalMonthCode;

    private BigDecimal assetRegistrationCost;

    private BigDecimal depreciationAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetRegistrationDetails() {
        return assetRegistrationDetails;
    }

    public void setAssetRegistrationDetails(String assetRegistrationDetails) {
        this.assetRegistrationDetails = assetRegistrationDetails;
    }

    public String getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(String postedAt) {
        this.postedAt = postedAt;
    }

    public Long getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getServiceOutletCode() {
        return serviceOutletCode;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public String getDepreciationMethod() {
        return depreciationMethod;
    }

    public void setDepreciationMethod(String depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public String getDepreciationPeriodCode() {
        return depreciationPeriodCode;
    }

    public void setDepreciationPeriodCode(String depreciationPeriodCode) {
        this.depreciationPeriodCode = depreciationPeriodCode;
    }

    public String getFiscalMonthCode() {
        return fiscalMonthCode;
    }

    public void setFiscalMonthCode(String fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public BigDecimal getAssetRegistrationCost() {
        return assetRegistrationCost;
    }

    public void setAssetRegistrationCost(BigDecimal assetRegistrationCost) {
        this.assetRegistrationCost = assetRegistrationCost;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationEntryReportItemDTO)) {
            return false;
        }

        DepreciationEntryReportItemDTO depreciationEntryReportItemDTO = (DepreciationEntryReportItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depreciationEntryReportItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationEntryReportItemDTO{" +
            "id=" + getId() +
            ", assetRegistrationDetails='" + getAssetRegistrationDetails() + "'" +
            ", postedAt='" + getPostedAt() + "'" +
            ", assetNumber=" + getAssetNumber() +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            ", assetCategory='" + getAssetCategory() + "'" +
            ", depreciationMethod='" + getDepreciationMethod() + "'" +
            ", depreciationPeriodCode='" + getDepreciationPeriodCode() + "'" +
            ", fiscalMonthCode='" + getFiscalMonthCode() + "'" +
            ", assetRegistrationCost=" + getAssetRegistrationCost() +
            ", depreciationAmount=" + getDepreciationAmount() +
            "}";
    }
}
