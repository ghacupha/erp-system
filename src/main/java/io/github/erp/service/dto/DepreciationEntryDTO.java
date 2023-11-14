package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.DepreciationEntry} entity.
 */
public class DepreciationEntryDTO implements Serializable {

    private Long id;

    private ZonedDateTime postedAt;

    private BigDecimal depreciationAmount;

    private Long assetNumber;

    private ServiceOutletDTO serviceOutlet;

    private AssetCategoryDTO assetCategory;

    private DepreciationMethodDTO depreciationMethod;

    private AssetRegistrationDTO assetRegistration;

    private DepreciationPeriodDTO depreciationPeriod;

    private FiscalMonthDTO fiscalMonth;

    private FiscalQuarterDTO fiscalQuarter;

    private FiscalYearDTO fiscalYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(ZonedDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public Long getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
    }

    public ServiceOutletDTO getServiceOutlet() {
        return serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutletDTO serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public AssetCategoryDTO getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategoryDTO assetCategory) {
        this.assetCategory = assetCategory;
    }

    public DepreciationMethodDTO getDepreciationMethod() {
        return depreciationMethod;
    }

    public void setDepreciationMethod(DepreciationMethodDTO depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public AssetRegistrationDTO getAssetRegistration() {
        return assetRegistration;
    }

    public void setAssetRegistration(AssetRegistrationDTO assetRegistration) {
        this.assetRegistration = assetRegistration;
    }

    public DepreciationPeriodDTO getDepreciationPeriod() {
        return depreciationPeriod;
    }

    public void setDepreciationPeriod(DepreciationPeriodDTO depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
    }

    public FiscalMonthDTO getFiscalMonth() {
        return fiscalMonth;
    }

    public void setFiscalMonth(FiscalMonthDTO fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public FiscalQuarterDTO getFiscalQuarter() {
        return fiscalQuarter;
    }

    public void setFiscalQuarter(FiscalQuarterDTO fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public FiscalYearDTO getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYearDTO fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationEntryDTO)) {
            return false;
        }

        DepreciationEntryDTO depreciationEntryDTO = (DepreciationEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depreciationEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationEntryDTO{" +
            "id=" + getId() +
            ", postedAt='" + getPostedAt() + "'" +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", assetNumber=" + getAssetNumber() +
            ", serviceOutlet=" + getServiceOutlet() +
            ", assetCategory=" + getAssetCategory() +
            ", depreciationMethod=" + getDepreciationMethod() +
            ", assetRegistration=" + getAssetRegistration() +
            ", depreciationPeriod=" + getDepreciationPeriod() +
            ", fiscalMonth=" + getFiscalMonth() +
            ", fiscalQuarter=" + getFiscalQuarter() +
            ", fiscalYear=" + getFiscalYear() +
            "}";
    }
}
