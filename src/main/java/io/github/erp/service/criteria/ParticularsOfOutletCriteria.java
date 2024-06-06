package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
 * Criteria class for the {@link io.github.erp.domain.ParticularsOfOutlet} entity. This class is used
 * in {@link io.github.erp.web.rest.ParticularsOfOutletResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /particulars-of-outlets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ParticularsOfOutletCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter businessReportingDate;

    private StringFilter outletName;

    private StringFilter town;

    private DoubleFilter iso6709Latitute;

    private DoubleFilter iso6709Longitude;

    private LocalDateFilter cbkApprovalDate;

    private LocalDateFilter outletOpeningDate;

    private LocalDateFilter outletClosureDate;

    private BigDecimalFilter licenseFeePayable;

    private LongFilter subCountyCodeId;

    private LongFilter bankCodeId;

    private LongFilter outletIdId;

    private LongFilter typeOfOutletId;

    private LongFilter outletStatusId;

    private Boolean distinct;

    public ParticularsOfOutletCriteria() {}

    public ParticularsOfOutletCriteria(ParticularsOfOutletCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.businessReportingDate = other.businessReportingDate == null ? null : other.businessReportingDate.copy();
        this.outletName = other.outletName == null ? null : other.outletName.copy();
        this.town = other.town == null ? null : other.town.copy();
        this.iso6709Latitute = other.iso6709Latitute == null ? null : other.iso6709Latitute.copy();
        this.iso6709Longitude = other.iso6709Longitude == null ? null : other.iso6709Longitude.copy();
        this.cbkApprovalDate = other.cbkApprovalDate == null ? null : other.cbkApprovalDate.copy();
        this.outletOpeningDate = other.outletOpeningDate == null ? null : other.outletOpeningDate.copy();
        this.outletClosureDate = other.outletClosureDate == null ? null : other.outletClosureDate.copy();
        this.licenseFeePayable = other.licenseFeePayable == null ? null : other.licenseFeePayable.copy();
        this.subCountyCodeId = other.subCountyCodeId == null ? null : other.subCountyCodeId.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.outletIdId = other.outletIdId == null ? null : other.outletIdId.copy();
        this.typeOfOutletId = other.typeOfOutletId == null ? null : other.typeOfOutletId.copy();
        this.outletStatusId = other.outletStatusId == null ? null : other.outletStatusId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ParticularsOfOutletCriteria copy() {
        return new ParticularsOfOutletCriteria(this);
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

    public LocalDateFilter getBusinessReportingDate() {
        return businessReportingDate;
    }

    public LocalDateFilter businessReportingDate() {
        if (businessReportingDate == null) {
            businessReportingDate = new LocalDateFilter();
        }
        return businessReportingDate;
    }

    public void setBusinessReportingDate(LocalDateFilter businessReportingDate) {
        this.businessReportingDate = businessReportingDate;
    }

    public StringFilter getOutletName() {
        return outletName;
    }

    public StringFilter outletName() {
        if (outletName == null) {
            outletName = new StringFilter();
        }
        return outletName;
    }

    public void setOutletName(StringFilter outletName) {
        this.outletName = outletName;
    }

    public StringFilter getTown() {
        return town;
    }

    public StringFilter town() {
        if (town == null) {
            town = new StringFilter();
        }
        return town;
    }

    public void setTown(StringFilter town) {
        this.town = town;
    }

    public DoubleFilter getIso6709Latitute() {
        return iso6709Latitute;
    }

    public DoubleFilter iso6709Latitute() {
        if (iso6709Latitute == null) {
            iso6709Latitute = new DoubleFilter();
        }
        return iso6709Latitute;
    }

    public void setIso6709Latitute(DoubleFilter iso6709Latitute) {
        this.iso6709Latitute = iso6709Latitute;
    }

    public DoubleFilter getIso6709Longitude() {
        return iso6709Longitude;
    }

    public DoubleFilter iso6709Longitude() {
        if (iso6709Longitude == null) {
            iso6709Longitude = new DoubleFilter();
        }
        return iso6709Longitude;
    }

    public void setIso6709Longitude(DoubleFilter iso6709Longitude) {
        this.iso6709Longitude = iso6709Longitude;
    }

    public LocalDateFilter getCbkApprovalDate() {
        return cbkApprovalDate;
    }

    public LocalDateFilter cbkApprovalDate() {
        if (cbkApprovalDate == null) {
            cbkApprovalDate = new LocalDateFilter();
        }
        return cbkApprovalDate;
    }

    public void setCbkApprovalDate(LocalDateFilter cbkApprovalDate) {
        this.cbkApprovalDate = cbkApprovalDate;
    }

    public LocalDateFilter getOutletOpeningDate() {
        return outletOpeningDate;
    }

    public LocalDateFilter outletOpeningDate() {
        if (outletOpeningDate == null) {
            outletOpeningDate = new LocalDateFilter();
        }
        return outletOpeningDate;
    }

    public void setOutletOpeningDate(LocalDateFilter outletOpeningDate) {
        this.outletOpeningDate = outletOpeningDate;
    }

    public LocalDateFilter getOutletClosureDate() {
        return outletClosureDate;
    }

    public LocalDateFilter outletClosureDate() {
        if (outletClosureDate == null) {
            outletClosureDate = new LocalDateFilter();
        }
        return outletClosureDate;
    }

    public void setOutletClosureDate(LocalDateFilter outletClosureDate) {
        this.outletClosureDate = outletClosureDate;
    }

    public BigDecimalFilter getLicenseFeePayable() {
        return licenseFeePayable;
    }

    public BigDecimalFilter licenseFeePayable() {
        if (licenseFeePayable == null) {
            licenseFeePayable = new BigDecimalFilter();
        }
        return licenseFeePayable;
    }

    public void setLicenseFeePayable(BigDecimalFilter licenseFeePayable) {
        this.licenseFeePayable = licenseFeePayable;
    }

    public LongFilter getSubCountyCodeId() {
        return subCountyCodeId;
    }

    public LongFilter subCountyCodeId() {
        if (subCountyCodeId == null) {
            subCountyCodeId = new LongFilter();
        }
        return subCountyCodeId;
    }

    public void setSubCountyCodeId(LongFilter subCountyCodeId) {
        this.subCountyCodeId = subCountyCodeId;
    }

    public LongFilter getBankCodeId() {
        return bankCodeId;
    }

    public LongFilter bankCodeId() {
        if (bankCodeId == null) {
            bankCodeId = new LongFilter();
        }
        return bankCodeId;
    }

    public void setBankCodeId(LongFilter bankCodeId) {
        this.bankCodeId = bankCodeId;
    }

    public LongFilter getOutletIdId() {
        return outletIdId;
    }

    public LongFilter outletIdId() {
        if (outletIdId == null) {
            outletIdId = new LongFilter();
        }
        return outletIdId;
    }

    public void setOutletIdId(LongFilter outletIdId) {
        this.outletIdId = outletIdId;
    }

    public LongFilter getTypeOfOutletId() {
        return typeOfOutletId;
    }

    public LongFilter typeOfOutletId() {
        if (typeOfOutletId == null) {
            typeOfOutletId = new LongFilter();
        }
        return typeOfOutletId;
    }

    public void setTypeOfOutletId(LongFilter typeOfOutletId) {
        this.typeOfOutletId = typeOfOutletId;
    }

    public LongFilter getOutletStatusId() {
        return outletStatusId;
    }

    public LongFilter outletStatusId() {
        if (outletStatusId == null) {
            outletStatusId = new LongFilter();
        }
        return outletStatusId;
    }

    public void setOutletStatusId(LongFilter outletStatusId) {
        this.outletStatusId = outletStatusId;
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
        final ParticularsOfOutletCriteria that = (ParticularsOfOutletCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(businessReportingDate, that.businessReportingDate) &&
            Objects.equals(outletName, that.outletName) &&
            Objects.equals(town, that.town) &&
            Objects.equals(iso6709Latitute, that.iso6709Latitute) &&
            Objects.equals(iso6709Longitude, that.iso6709Longitude) &&
            Objects.equals(cbkApprovalDate, that.cbkApprovalDate) &&
            Objects.equals(outletOpeningDate, that.outletOpeningDate) &&
            Objects.equals(outletClosureDate, that.outletClosureDate) &&
            Objects.equals(licenseFeePayable, that.licenseFeePayable) &&
            Objects.equals(subCountyCodeId, that.subCountyCodeId) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(outletIdId, that.outletIdId) &&
            Objects.equals(typeOfOutletId, that.typeOfOutletId) &&
            Objects.equals(outletStatusId, that.outletStatusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            businessReportingDate,
            outletName,
            town,
            iso6709Latitute,
            iso6709Longitude,
            cbkApprovalDate,
            outletOpeningDate,
            outletClosureDate,
            licenseFeePayable,
            subCountyCodeId,
            bankCodeId,
            outletIdId,
            typeOfOutletId,
            outletStatusId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticularsOfOutletCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (businessReportingDate != null ? "businessReportingDate=" + businessReportingDate + ", " : "") +
            (outletName != null ? "outletName=" + outletName + ", " : "") +
            (town != null ? "town=" + town + ", " : "") +
            (iso6709Latitute != null ? "iso6709Latitute=" + iso6709Latitute + ", " : "") +
            (iso6709Longitude != null ? "iso6709Longitude=" + iso6709Longitude + ", " : "") +
            (cbkApprovalDate != null ? "cbkApprovalDate=" + cbkApprovalDate + ", " : "") +
            (outletOpeningDate != null ? "outletOpeningDate=" + outletOpeningDate + ", " : "") +
            (outletClosureDate != null ? "outletClosureDate=" + outletClosureDate + ", " : "") +
            (licenseFeePayable != null ? "licenseFeePayable=" + licenseFeePayable + ", " : "") +
            (subCountyCodeId != null ? "subCountyCodeId=" + subCountyCodeId + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (outletIdId != null ? "outletIdId=" + outletIdId + ", " : "") +
            (typeOfOutletId != null ? "typeOfOutletId=" + typeOfOutletId + ", " : "") +
            (outletStatusId != null ? "outletStatusId=" + outletStatusId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
