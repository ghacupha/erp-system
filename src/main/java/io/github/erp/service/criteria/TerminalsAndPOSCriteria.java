package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.TerminalsAndPOS} entity. This class is used
 * in {@link io.github.erp.web.rest.TerminalsAndPOSResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /terminals-and-pos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TerminalsAndPOSCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private StringFilter terminalId;

    private StringFilter merchantId;

    private StringFilter terminalName;

    private StringFilter terminalLocation;

    private DoubleFilter iso6709Latitute;

    private DoubleFilter iso6709Longitude;

    private LocalDateFilter terminalOpeningDate;

    private LocalDateFilter terminalClosureDate;

    private LongFilter terminalTypeId;

    private LongFilter terminalFunctionalityId;

    private LongFilter physicalLocationId;

    private LongFilter bankIdId;

    private LongFilter branchIdId;

    private Boolean distinct;

    public TerminalsAndPOSCriteria() {}

    public TerminalsAndPOSCriteria(TerminalsAndPOSCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.terminalId = other.terminalId == null ? null : other.terminalId.copy();
        this.merchantId = other.merchantId == null ? null : other.merchantId.copy();
        this.terminalName = other.terminalName == null ? null : other.terminalName.copy();
        this.terminalLocation = other.terminalLocation == null ? null : other.terminalLocation.copy();
        this.iso6709Latitute = other.iso6709Latitute == null ? null : other.iso6709Latitute.copy();
        this.iso6709Longitude = other.iso6709Longitude == null ? null : other.iso6709Longitude.copy();
        this.terminalOpeningDate = other.terminalOpeningDate == null ? null : other.terminalOpeningDate.copy();
        this.terminalClosureDate = other.terminalClosureDate == null ? null : other.terminalClosureDate.copy();
        this.terminalTypeId = other.terminalTypeId == null ? null : other.terminalTypeId.copy();
        this.terminalFunctionalityId = other.terminalFunctionalityId == null ? null : other.terminalFunctionalityId.copy();
        this.physicalLocationId = other.physicalLocationId == null ? null : other.physicalLocationId.copy();
        this.bankIdId = other.bankIdId == null ? null : other.bankIdId.copy();
        this.branchIdId = other.branchIdId == null ? null : other.branchIdId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TerminalsAndPOSCriteria copy() {
        return new TerminalsAndPOSCriteria(this);
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

    public LocalDateFilter getReportingDate() {
        return reportingDate;
    }

    public LocalDateFilter reportingDate() {
        if (reportingDate == null) {
            reportingDate = new LocalDateFilter();
        }
        return reportingDate;
    }

    public void setReportingDate(LocalDateFilter reportingDate) {
        this.reportingDate = reportingDate;
    }

    public StringFilter getTerminalId() {
        return terminalId;
    }

    public StringFilter terminalId() {
        if (terminalId == null) {
            terminalId = new StringFilter();
        }
        return terminalId;
    }

    public void setTerminalId(StringFilter terminalId) {
        this.terminalId = terminalId;
    }

    public StringFilter getMerchantId() {
        return merchantId;
    }

    public StringFilter merchantId() {
        if (merchantId == null) {
            merchantId = new StringFilter();
        }
        return merchantId;
    }

    public void setMerchantId(StringFilter merchantId) {
        this.merchantId = merchantId;
    }

    public StringFilter getTerminalName() {
        return terminalName;
    }

    public StringFilter terminalName() {
        if (terminalName == null) {
            terminalName = new StringFilter();
        }
        return terminalName;
    }

    public void setTerminalName(StringFilter terminalName) {
        this.terminalName = terminalName;
    }

    public StringFilter getTerminalLocation() {
        return terminalLocation;
    }

    public StringFilter terminalLocation() {
        if (terminalLocation == null) {
            terminalLocation = new StringFilter();
        }
        return terminalLocation;
    }

    public void setTerminalLocation(StringFilter terminalLocation) {
        this.terminalLocation = terminalLocation;
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

    public LocalDateFilter getTerminalOpeningDate() {
        return terminalOpeningDate;
    }

    public LocalDateFilter terminalOpeningDate() {
        if (terminalOpeningDate == null) {
            terminalOpeningDate = new LocalDateFilter();
        }
        return terminalOpeningDate;
    }

    public void setTerminalOpeningDate(LocalDateFilter terminalOpeningDate) {
        this.terminalOpeningDate = terminalOpeningDate;
    }

    public LocalDateFilter getTerminalClosureDate() {
        return terminalClosureDate;
    }

    public LocalDateFilter terminalClosureDate() {
        if (terminalClosureDate == null) {
            terminalClosureDate = new LocalDateFilter();
        }
        return terminalClosureDate;
    }

    public void setTerminalClosureDate(LocalDateFilter terminalClosureDate) {
        this.terminalClosureDate = terminalClosureDate;
    }

    public LongFilter getTerminalTypeId() {
        return terminalTypeId;
    }

    public LongFilter terminalTypeId() {
        if (terminalTypeId == null) {
            terminalTypeId = new LongFilter();
        }
        return terminalTypeId;
    }

    public void setTerminalTypeId(LongFilter terminalTypeId) {
        this.terminalTypeId = terminalTypeId;
    }

    public LongFilter getTerminalFunctionalityId() {
        return terminalFunctionalityId;
    }

    public LongFilter terminalFunctionalityId() {
        if (terminalFunctionalityId == null) {
            terminalFunctionalityId = new LongFilter();
        }
        return terminalFunctionalityId;
    }

    public void setTerminalFunctionalityId(LongFilter terminalFunctionalityId) {
        this.terminalFunctionalityId = terminalFunctionalityId;
    }

    public LongFilter getPhysicalLocationId() {
        return physicalLocationId;
    }

    public LongFilter physicalLocationId() {
        if (physicalLocationId == null) {
            physicalLocationId = new LongFilter();
        }
        return physicalLocationId;
    }

    public void setPhysicalLocationId(LongFilter physicalLocationId) {
        this.physicalLocationId = physicalLocationId;
    }

    public LongFilter getBankIdId() {
        return bankIdId;
    }

    public LongFilter bankIdId() {
        if (bankIdId == null) {
            bankIdId = new LongFilter();
        }
        return bankIdId;
    }

    public void setBankIdId(LongFilter bankIdId) {
        this.bankIdId = bankIdId;
    }

    public LongFilter getBranchIdId() {
        return branchIdId;
    }

    public LongFilter branchIdId() {
        if (branchIdId == null) {
            branchIdId = new LongFilter();
        }
        return branchIdId;
    }

    public void setBranchIdId(LongFilter branchIdId) {
        this.branchIdId = branchIdId;
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
        final TerminalsAndPOSCriteria that = (TerminalsAndPOSCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(terminalId, that.terminalId) &&
            Objects.equals(merchantId, that.merchantId) &&
            Objects.equals(terminalName, that.terminalName) &&
            Objects.equals(terminalLocation, that.terminalLocation) &&
            Objects.equals(iso6709Latitute, that.iso6709Latitute) &&
            Objects.equals(iso6709Longitude, that.iso6709Longitude) &&
            Objects.equals(terminalOpeningDate, that.terminalOpeningDate) &&
            Objects.equals(terminalClosureDate, that.terminalClosureDate) &&
            Objects.equals(terminalTypeId, that.terminalTypeId) &&
            Objects.equals(terminalFunctionalityId, that.terminalFunctionalityId) &&
            Objects.equals(physicalLocationId, that.physicalLocationId) &&
            Objects.equals(bankIdId, that.bankIdId) &&
            Objects.equals(branchIdId, that.branchIdId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportingDate,
            terminalId,
            merchantId,
            terminalName,
            terminalLocation,
            iso6709Latitute,
            iso6709Longitude,
            terminalOpeningDate,
            terminalClosureDate,
            terminalTypeId,
            terminalFunctionalityId,
            physicalLocationId,
            bankIdId,
            branchIdId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TerminalsAndPOSCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (terminalId != null ? "terminalId=" + terminalId + ", " : "") +
            (merchantId != null ? "merchantId=" + merchantId + ", " : "") +
            (terminalName != null ? "terminalName=" + terminalName + ", " : "") +
            (terminalLocation != null ? "terminalLocation=" + terminalLocation + ", " : "") +
            (iso6709Latitute != null ? "iso6709Latitute=" + iso6709Latitute + ", " : "") +
            (iso6709Longitude != null ? "iso6709Longitude=" + iso6709Longitude + ", " : "") +
            (terminalOpeningDate != null ? "terminalOpeningDate=" + terminalOpeningDate + ", " : "") +
            (terminalClosureDate != null ? "terminalClosureDate=" + terminalClosureDate + ", " : "") +
            (terminalTypeId != null ? "terminalTypeId=" + terminalTypeId + ", " : "") +
            (terminalFunctionalityId != null ? "terminalFunctionalityId=" + terminalFunctionalityId + ", " : "") +
            (physicalLocationId != null ? "physicalLocationId=" + physicalLocationId + ", " : "") +
            (bankIdId != null ? "bankIdId=" + bankIdId + ", " : "") +
            (branchIdId != null ? "branchIdId=" + branchIdId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
