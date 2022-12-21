package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 6 (Caleb Series) Server ver 0.1.9-SNAPSHOT
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
 * Criteria class for the {@link io.github.erp.domain.ServiceOutlet} entity. This class is used
 * in {@link io.github.erp.web.rest.ServiceOutletResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /service-outlets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ServiceOutletCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter outletCode;

    private StringFilter outletName;

    private StringFilter town;

    private StringFilter parliamentaryConstituency;

    private StringFilter gpsCoordinates;

    private LocalDateFilter outletOpeningDate;

    private LocalDateFilter regulatorApprovalDate;

    private LocalDateFilter outletClosureDate;

    private LocalDateFilter dateLastModified;

    private BigDecimalFilter licenseFeePayable;

    private LongFilter placeholderId;

    private LongFilter bankCodeId;

    private LongFilter outletTypeId;

    private LongFilter outletStatusId;

    private LongFilter countyNameId;

    private LongFilter subCountyNameId;

    private Boolean distinct;

    public ServiceOutletCriteria() {}

    public ServiceOutletCriteria(ServiceOutletCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.outletCode = other.outletCode == null ? null : other.outletCode.copy();
        this.outletName = other.outletName == null ? null : other.outletName.copy();
        this.town = other.town == null ? null : other.town.copy();
        this.parliamentaryConstituency = other.parliamentaryConstituency == null ? null : other.parliamentaryConstituency.copy();
        this.gpsCoordinates = other.gpsCoordinates == null ? null : other.gpsCoordinates.copy();
        this.outletOpeningDate = other.outletOpeningDate == null ? null : other.outletOpeningDate.copy();
        this.regulatorApprovalDate = other.regulatorApprovalDate == null ? null : other.regulatorApprovalDate.copy();
        this.outletClosureDate = other.outletClosureDate == null ? null : other.outletClosureDate.copy();
        this.dateLastModified = other.dateLastModified == null ? null : other.dateLastModified.copy();
        this.licenseFeePayable = other.licenseFeePayable == null ? null : other.licenseFeePayable.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.outletTypeId = other.outletTypeId == null ? null : other.outletTypeId.copy();
        this.outletStatusId = other.outletStatusId == null ? null : other.outletStatusId.copy();
        this.countyNameId = other.countyNameId == null ? null : other.countyNameId.copy();
        this.subCountyNameId = other.subCountyNameId == null ? null : other.subCountyNameId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ServiceOutletCriteria copy() {
        return new ServiceOutletCriteria(this);
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

    public StringFilter getOutletCode() {
        return outletCode;
    }

    public StringFilter outletCode() {
        if (outletCode == null) {
            outletCode = new StringFilter();
        }
        return outletCode;
    }

    public void setOutletCode(StringFilter outletCode) {
        this.outletCode = outletCode;
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

    public StringFilter getParliamentaryConstituency() {
        return parliamentaryConstituency;
    }

    public StringFilter parliamentaryConstituency() {
        if (parliamentaryConstituency == null) {
            parliamentaryConstituency = new StringFilter();
        }
        return parliamentaryConstituency;
    }

    public void setParliamentaryConstituency(StringFilter parliamentaryConstituency) {
        this.parliamentaryConstituency = parliamentaryConstituency;
    }

    public StringFilter getGpsCoordinates() {
        return gpsCoordinates;
    }

    public StringFilter gpsCoordinates() {
        if (gpsCoordinates == null) {
            gpsCoordinates = new StringFilter();
        }
        return gpsCoordinates;
    }

    public void setGpsCoordinates(StringFilter gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
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

    public LocalDateFilter getRegulatorApprovalDate() {
        return regulatorApprovalDate;
    }

    public LocalDateFilter regulatorApprovalDate() {
        if (regulatorApprovalDate == null) {
            regulatorApprovalDate = new LocalDateFilter();
        }
        return regulatorApprovalDate;
    }

    public void setRegulatorApprovalDate(LocalDateFilter regulatorApprovalDate) {
        this.regulatorApprovalDate = regulatorApprovalDate;
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

    public LocalDateFilter getDateLastModified() {
        return dateLastModified;
    }

    public LocalDateFilter dateLastModified() {
        if (dateLastModified == null) {
            dateLastModified = new LocalDateFilter();
        }
        return dateLastModified;
    }

    public void setDateLastModified(LocalDateFilter dateLastModified) {
        this.dateLastModified = dateLastModified;
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

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
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

    public LongFilter getOutletTypeId() {
        return outletTypeId;
    }

    public LongFilter outletTypeId() {
        if (outletTypeId == null) {
            outletTypeId = new LongFilter();
        }
        return outletTypeId;
    }

    public void setOutletTypeId(LongFilter outletTypeId) {
        this.outletTypeId = outletTypeId;
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

    public LongFilter getCountyNameId() {
        return countyNameId;
    }

    public LongFilter countyNameId() {
        if (countyNameId == null) {
            countyNameId = new LongFilter();
        }
        return countyNameId;
    }

    public void setCountyNameId(LongFilter countyNameId) {
        this.countyNameId = countyNameId;
    }

    public LongFilter getSubCountyNameId() {
        return subCountyNameId;
    }

    public LongFilter subCountyNameId() {
        if (subCountyNameId == null) {
            subCountyNameId = new LongFilter();
        }
        return subCountyNameId;
    }

    public void setSubCountyNameId(LongFilter subCountyNameId) {
        this.subCountyNameId = subCountyNameId;
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
        final ServiceOutletCriteria that = (ServiceOutletCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(outletCode, that.outletCode) &&
            Objects.equals(outletName, that.outletName) &&
            Objects.equals(town, that.town) &&
            Objects.equals(parliamentaryConstituency, that.parliamentaryConstituency) &&
            Objects.equals(gpsCoordinates, that.gpsCoordinates) &&
            Objects.equals(outletOpeningDate, that.outletOpeningDate) &&
            Objects.equals(regulatorApprovalDate, that.regulatorApprovalDate) &&
            Objects.equals(outletClosureDate, that.outletClosureDate) &&
            Objects.equals(dateLastModified, that.dateLastModified) &&
            Objects.equals(licenseFeePayable, that.licenseFeePayable) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(outletTypeId, that.outletTypeId) &&
            Objects.equals(outletStatusId, that.outletStatusId) &&
            Objects.equals(countyNameId, that.countyNameId) &&
            Objects.equals(subCountyNameId, that.subCountyNameId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            outletCode,
            outletName,
            town,
            parliamentaryConstituency,
            gpsCoordinates,
            outletOpeningDate,
            regulatorApprovalDate,
            outletClosureDate,
            dateLastModified,
            licenseFeePayable,
            placeholderId,
            bankCodeId,
            outletTypeId,
            outletStatusId,
            countyNameId,
            subCountyNameId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceOutletCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (outletCode != null ? "outletCode=" + outletCode + ", " : "") +
            (outletName != null ? "outletName=" + outletName + ", " : "") +
            (town != null ? "town=" + town + ", " : "") +
            (parliamentaryConstituency != null ? "parliamentaryConstituency=" + parliamentaryConstituency + ", " : "") +
            (gpsCoordinates != null ? "gpsCoordinates=" + gpsCoordinates + ", " : "") +
            (outletOpeningDate != null ? "outletOpeningDate=" + outletOpeningDate + ", " : "") +
            (regulatorApprovalDate != null ? "regulatorApprovalDate=" + regulatorApprovalDate + ", " : "") +
            (outletClosureDate != null ? "outletClosureDate=" + outletClosureDate + ", " : "") +
            (dateLastModified != null ? "dateLastModified=" + dateLastModified + ", " : "") +
            (licenseFeePayable != null ? "licenseFeePayable=" + licenseFeePayable + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (outletTypeId != null ? "outletTypeId=" + outletTypeId + ", " : "") +
            (outletStatusId != null ? "outletStatusId=" + outletStatusId + ", " : "") +
            (countyNameId != null ? "countyNameId=" + countyNameId + ", " : "") +
            (subCountyNameId != null ? "subCountyNameId=" + subCountyNameId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
