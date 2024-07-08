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
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.IFRS16LeaseContract} entity. This class is used
 * in {@link io.github.erp.web.rest.IFRS16LeaseContractResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ifrs-16-lease-contracts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IFRS16LeaseContractCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bookingId;

    private StringFilter leaseTitle;

    private StringFilter shortTitle;

    private StringFilter description;

    private LocalDateFilter inceptionDate;

    private LocalDateFilter commencementDate;

    private UUIDFilter serialNumber;

    private LongFilter superintendentServiceOutletId;

    private LongFilter mainDealerId;

    private LongFilter firstReportingPeriodId;

    private LongFilter lastReportingPeriodId;

    private LongFilter leaseContractDocumentId;

    private LongFilter leaseContractCalculationsId;

    private LongFilter leasePaymentId;

    private Boolean distinct;

    public IFRS16LeaseContractCriteria() {}

    public IFRS16LeaseContractCriteria(IFRS16LeaseContractCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bookingId = other.bookingId == null ? null : other.bookingId.copy();
        this.leaseTitle = other.leaseTitle == null ? null : other.leaseTitle.copy();
        this.shortTitle = other.shortTitle == null ? null : other.shortTitle.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.inceptionDate = other.inceptionDate == null ? null : other.inceptionDate.copy();
        this.commencementDate = other.commencementDate == null ? null : other.commencementDate.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.superintendentServiceOutletId =
            other.superintendentServiceOutletId == null ? null : other.superintendentServiceOutletId.copy();
        this.mainDealerId = other.mainDealerId == null ? null : other.mainDealerId.copy();
        this.firstReportingPeriodId = other.firstReportingPeriodId == null ? null : other.firstReportingPeriodId.copy();
        this.lastReportingPeriodId = other.lastReportingPeriodId == null ? null : other.lastReportingPeriodId.copy();
        this.leaseContractDocumentId = other.leaseContractDocumentId == null ? null : other.leaseContractDocumentId.copy();
        this.leaseContractCalculationsId = other.leaseContractCalculationsId == null ? null : other.leaseContractCalculationsId.copy();
        this.leasePaymentId = other.leasePaymentId == null ? null : other.leasePaymentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public IFRS16LeaseContractCriteria copy() {
        return new IFRS16LeaseContractCriteria(this);
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

    public StringFilter getBookingId() {
        return bookingId;
    }

    public StringFilter bookingId() {
        if (bookingId == null) {
            bookingId = new StringFilter();
        }
        return bookingId;
    }

    public void setBookingId(StringFilter bookingId) {
        this.bookingId = bookingId;
    }

    public StringFilter getLeaseTitle() {
        return leaseTitle;
    }

    public StringFilter leaseTitle() {
        if (leaseTitle == null) {
            leaseTitle = new StringFilter();
        }
        return leaseTitle;
    }

    public void setLeaseTitle(StringFilter leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public StringFilter getShortTitle() {
        return shortTitle;
    }

    public StringFilter shortTitle() {
        if (shortTitle == null) {
            shortTitle = new StringFilter();
        }
        return shortTitle;
    }

    public void setShortTitle(StringFilter shortTitle) {
        this.shortTitle = shortTitle;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getInceptionDate() {
        return inceptionDate;
    }

    public LocalDateFilter inceptionDate() {
        if (inceptionDate == null) {
            inceptionDate = new LocalDateFilter();
        }
        return inceptionDate;
    }

    public void setInceptionDate(LocalDateFilter inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public LocalDateFilter getCommencementDate() {
        return commencementDate;
    }

    public LocalDateFilter commencementDate() {
        if (commencementDate == null) {
            commencementDate = new LocalDateFilter();
        }
        return commencementDate;
    }

    public void setCommencementDate(LocalDateFilter commencementDate) {
        this.commencementDate = commencementDate;
    }

    public UUIDFilter getSerialNumber() {
        return serialNumber;
    }

    public UUIDFilter serialNumber() {
        if (serialNumber == null) {
            serialNumber = new UUIDFilter();
        }
        return serialNumber;
    }

    public void setSerialNumber(UUIDFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LongFilter getSuperintendentServiceOutletId() {
        return superintendentServiceOutletId;
    }

    public LongFilter superintendentServiceOutletId() {
        if (superintendentServiceOutletId == null) {
            superintendentServiceOutletId = new LongFilter();
        }
        return superintendentServiceOutletId;
    }

    public void setSuperintendentServiceOutletId(LongFilter superintendentServiceOutletId) {
        this.superintendentServiceOutletId = superintendentServiceOutletId;
    }

    public LongFilter getMainDealerId() {
        return mainDealerId;
    }

    public LongFilter mainDealerId() {
        if (mainDealerId == null) {
            mainDealerId = new LongFilter();
        }
        return mainDealerId;
    }

    public void setMainDealerId(LongFilter mainDealerId) {
        this.mainDealerId = mainDealerId;
    }

    public LongFilter getFirstReportingPeriodId() {
        return firstReportingPeriodId;
    }

    public LongFilter firstReportingPeriodId() {
        if (firstReportingPeriodId == null) {
            firstReportingPeriodId = new LongFilter();
        }
        return firstReportingPeriodId;
    }

    public void setFirstReportingPeriodId(LongFilter firstReportingPeriodId) {
        this.firstReportingPeriodId = firstReportingPeriodId;
    }

    public LongFilter getLastReportingPeriodId() {
        return lastReportingPeriodId;
    }

    public LongFilter lastReportingPeriodId() {
        if (lastReportingPeriodId == null) {
            lastReportingPeriodId = new LongFilter();
        }
        return lastReportingPeriodId;
    }

    public void setLastReportingPeriodId(LongFilter lastReportingPeriodId) {
        this.lastReportingPeriodId = lastReportingPeriodId;
    }

    public LongFilter getLeaseContractDocumentId() {
        return leaseContractDocumentId;
    }

    public LongFilter leaseContractDocumentId() {
        if (leaseContractDocumentId == null) {
            leaseContractDocumentId = new LongFilter();
        }
        return leaseContractDocumentId;
    }

    public void setLeaseContractDocumentId(LongFilter leaseContractDocumentId) {
        this.leaseContractDocumentId = leaseContractDocumentId;
    }

    public LongFilter getLeaseContractCalculationsId() {
        return leaseContractCalculationsId;
    }

    public LongFilter leaseContractCalculationsId() {
        if (leaseContractCalculationsId == null) {
            leaseContractCalculationsId = new LongFilter();
        }
        return leaseContractCalculationsId;
    }

    public void setLeaseContractCalculationsId(LongFilter leaseContractCalculationsId) {
        this.leaseContractCalculationsId = leaseContractCalculationsId;
    }

    public LongFilter getLeasePaymentId() {
        return leasePaymentId;
    }

    public LongFilter leasePaymentId() {
        if (leasePaymentId == null) {
            leasePaymentId = new LongFilter();
        }
        return leasePaymentId;
    }

    public void setLeasePaymentId(LongFilter leasePaymentId) {
        this.leasePaymentId = leasePaymentId;
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
        final IFRS16LeaseContractCriteria that = (IFRS16LeaseContractCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(leaseTitle, that.leaseTitle) &&
            Objects.equals(shortTitle, that.shortTitle) &&
            Objects.equals(description, that.description) &&
            Objects.equals(inceptionDate, that.inceptionDate) &&
            Objects.equals(commencementDate, that.commencementDate) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(superintendentServiceOutletId, that.superintendentServiceOutletId) &&
            Objects.equals(mainDealerId, that.mainDealerId) &&
            Objects.equals(firstReportingPeriodId, that.firstReportingPeriodId) &&
            Objects.equals(lastReportingPeriodId, that.lastReportingPeriodId) &&
            Objects.equals(leaseContractDocumentId, that.leaseContractDocumentId) &&
            Objects.equals(leaseContractCalculationsId, that.leaseContractCalculationsId) &&
            Objects.equals(leasePaymentId, that.leasePaymentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            bookingId,
            leaseTitle,
            shortTitle,
            description,
            inceptionDate,
            commencementDate,
            serialNumber,
            superintendentServiceOutletId,
            mainDealerId,
            firstReportingPeriodId,
            lastReportingPeriodId,
            leaseContractDocumentId,
            leaseContractCalculationsId,
            leasePaymentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IFRS16LeaseContractCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (bookingId != null ? "bookingId=" + bookingId + ", " : "") +
            (leaseTitle != null ? "leaseTitle=" + leaseTitle + ", " : "") +
            (shortTitle != null ? "shortTitle=" + shortTitle + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (inceptionDate != null ? "inceptionDate=" + inceptionDate + ", " : "") +
            (commencementDate != null ? "commencementDate=" + commencementDate + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (superintendentServiceOutletId != null ? "superintendentServiceOutletId=" + superintendentServiceOutletId + ", " : "") +
            (mainDealerId != null ? "mainDealerId=" + mainDealerId + ", " : "") +
            (firstReportingPeriodId != null ? "firstReportingPeriodId=" + firstReportingPeriodId + ", " : "") +
            (lastReportingPeriodId != null ? "lastReportingPeriodId=" + lastReportingPeriodId + ", " : "") +
            (leaseContractDocumentId != null ? "leaseContractDocumentId=" + leaseContractDocumentId + ", " : "") +
            (leaseContractCalculationsId != null ? "leaseContractCalculationsId=" + leaseContractCalculationsId + ", " : "") +
            (leasePaymentId != null ? "leasePaymentId=" + leasePaymentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
