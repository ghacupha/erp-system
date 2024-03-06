package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.AssetAdditionsReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.AssetAdditionsReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-additions-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetAdditionsReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter assetNumber;

    private StringFilter assetTag;

    private StringFilter serviceOutletCode;

    private StringFilter transactionId;

    private LocalDateFilter transactionDate;

    private LocalDateFilter capitalizationDate;

    private StringFilter assetCategory;

    private StringFilter assetDetails;

    private BigDecimalFilter assetCost;

    private StringFilter supplier;

    private BigDecimalFilter historicalCost;

    private LocalDateFilter registrationDate;

    private Boolean distinct;

    public AssetAdditionsReportItemCriteria() {}

    public AssetAdditionsReportItemCriteria(AssetAdditionsReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetNumber = other.assetNumber == null ? null : other.assetNumber.copy();
        this.assetTag = other.assetTag == null ? null : other.assetTag.copy();
        this.serviceOutletCode = other.serviceOutletCode == null ? null : other.serviceOutletCode.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.capitalizationDate = other.capitalizationDate == null ? null : other.capitalizationDate.copy();
        this.assetCategory = other.assetCategory == null ? null : other.assetCategory.copy();
        this.assetDetails = other.assetDetails == null ? null : other.assetDetails.copy();
        this.assetCost = other.assetCost == null ? null : other.assetCost.copy();
        this.supplier = other.supplier == null ? null : other.supplier.copy();
        this.historicalCost = other.historicalCost == null ? null : other.historicalCost.copy();
        this.registrationDate = other.registrationDate == null ? null : other.registrationDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetAdditionsReportItemCriteria copy() {
        return new AssetAdditionsReportItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAssetNumber() {
        return assetNumber;
    }

    public StringFilter assetNumber() {
        if (assetNumber == null) {
            assetNumber = new StringFilter();
        }
        return assetNumber;
    }

    public void setAssetNumber(StringFilter assetNumber) {
        this.assetNumber = assetNumber;
    }

    public StringFilter getAssetTag() {
        return assetTag;
    }

    public StringFilter assetTag() {
        if (assetTag == null) {
            assetTag = new StringFilter();
        }
        return assetTag;
    }

    public void setAssetTag(StringFilter assetTag) {
        this.assetTag = assetTag;
    }

    public StringFilter getServiceOutletCode() {
        return serviceOutletCode;
    }

    public StringFilter serviceOutletCode() {
        if (serviceOutletCode == null) {
            serviceOutletCode = new StringFilter();
        }
        return serviceOutletCode;
    }

    public void setServiceOutletCode(StringFilter serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public StringFilter getTransactionId() {
        return transactionId;
    }

    public StringFilter transactionId() {
        if (transactionId == null) {
            transactionId = new StringFilter();
        }
        return transactionId;
    }

    public void setTransactionId(StringFilter transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateFilter getTransactionDate() {
        return transactionDate;
    }

    public LocalDateFilter transactionDate() {
        if (transactionDate == null) {
            transactionDate = new LocalDateFilter();
        }
        return transactionDate;
    }

    public void setTransactionDate(LocalDateFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDateFilter getCapitalizationDate() {
        return capitalizationDate;
    }

    public LocalDateFilter capitalizationDate() {
        if (capitalizationDate == null) {
            capitalizationDate = new LocalDateFilter();
        }
        return capitalizationDate;
    }

    public void setCapitalizationDate(LocalDateFilter capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public StringFilter getAssetCategory() {
        return assetCategory;
    }

    public StringFilter assetCategory() {
        if (assetCategory == null) {
            assetCategory = new StringFilter();
        }
        return assetCategory;
    }

    public void setAssetCategory(StringFilter assetCategory) {
        this.assetCategory = assetCategory;
    }

    public StringFilter getAssetDetails() {
        return assetDetails;
    }

    public StringFilter assetDetails() {
        if (assetDetails == null) {
            assetDetails = new StringFilter();
        }
        return assetDetails;
    }

    public void setAssetDetails(StringFilter assetDetails) {
        this.assetDetails = assetDetails;
    }

    public BigDecimalFilter getAssetCost() {
        return assetCost;
    }

    public BigDecimalFilter assetCost() {
        if (assetCost == null) {
            assetCost = new BigDecimalFilter();
        }
        return assetCost;
    }

    public void setAssetCost(BigDecimalFilter assetCost) {
        this.assetCost = assetCost;
    }

    public StringFilter getSupplier() {
        return supplier;
    }

    public StringFilter supplier() {
        if (supplier == null) {
            supplier = new StringFilter();
        }
        return supplier;
    }

    public void setSupplier(StringFilter supplier) {
        this.supplier = supplier;
    }

    public BigDecimalFilter getHistoricalCost() {
        return historicalCost;
    }

    public BigDecimalFilter historicalCost() {
        if (historicalCost == null) {
            historicalCost = new BigDecimalFilter();
        }
        return historicalCost;
    }

    public void setHistoricalCost(BigDecimalFilter historicalCost) {
        this.historicalCost = historicalCost;
    }

    public LocalDateFilter getRegistrationDate() {
        return registrationDate;
    }

    public LocalDateFilter registrationDate() {
        if (registrationDate == null) {
            registrationDate = new LocalDateFilter();
        }
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AssetAdditionsReportItemCriteria that = (AssetAdditionsReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetNumber, that.assetNumber) &&
            Objects.equals(assetTag, that.assetTag) &&
            Objects.equals(serviceOutletCode, that.serviceOutletCode) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(capitalizationDate, that.capitalizationDate) &&
            Objects.equals(assetCategory, that.assetCategory) &&
            Objects.equals(assetDetails, that.assetDetails) &&
            Objects.equals(assetCost, that.assetCost) &&
            Objects.equals(supplier, that.supplier) &&
            Objects.equals(historicalCost, that.historicalCost) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetNumber,
            assetTag,
            serviceOutletCode,
            transactionId,
            transactionDate,
            capitalizationDate,
            assetCategory,
            assetDetails,
            assetCost,
            supplier,
            historicalCost,
            registrationDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetAdditionsReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetNumber != null ? "assetNumber=" + assetNumber + ", " : "") +
            (assetTag != null ? "assetTag=" + assetTag + ", " : "") +
            (serviceOutletCode != null ? "serviceOutletCode=" + serviceOutletCode + ", " : "") +
            (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
            (capitalizationDate != null ? "capitalizationDate=" + capitalizationDate + ", " : "") +
            (assetCategory != null ? "assetCategory=" + assetCategory + ", " : "") +
            (assetDetails != null ? "assetDetails=" + assetDetails + ", " : "") +
            (assetCost != null ? "assetCost=" + assetCost + ", " : "") +
            (supplier != null ? "supplier=" + supplier + ", " : "") +
            (historicalCost != null ? "historicalCost=" + historicalCost + ", " : "") +
            (registrationDate != null ? "registrationDate=" + registrationDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
