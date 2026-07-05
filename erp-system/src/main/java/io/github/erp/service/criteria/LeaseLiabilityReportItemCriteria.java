package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.LeaseLiabilityReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.LeaseLiabilityReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-liability-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseLiabilityReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bookingId;

    private StringFilter leaseTitle;

    private StringFilter liabilityAccountNumber;

    private BigDecimalFilter liabilityAmount;

    private StringFilter interestPayableAccountNumber;

    private BigDecimalFilter interestPayableAmount;

    private Boolean distinct;

    public LeaseLiabilityReportItemCriteria() {}

    public LeaseLiabilityReportItemCriteria(LeaseLiabilityReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bookingId = other.bookingId == null ? null : other.bookingId.copy();
        this.leaseTitle = other.leaseTitle == null ? null : other.leaseTitle.copy();
        this.liabilityAccountNumber = other.liabilityAccountNumber == null ? null : other.liabilityAccountNumber.copy();
        this.liabilityAmount = other.liabilityAmount == null ? null : other.liabilityAmount.copy();
        this.interestPayableAccountNumber = other.interestPayableAccountNumber == null ? null : other.interestPayableAccountNumber.copy();
        this.interestPayableAmount = other.interestPayableAmount == null ? null : other.interestPayableAmount.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaseLiabilityReportItemCriteria copy() {
        return new LeaseLiabilityReportItemCriteria(this);
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

    public StringFilter getLiabilityAccountNumber() {
        return liabilityAccountNumber;
    }

    public StringFilter liabilityAccountNumber() {
        if (liabilityAccountNumber == null) {
            liabilityAccountNumber = new StringFilter();
        }
        return liabilityAccountNumber;
    }

    public void setLiabilityAccountNumber(StringFilter liabilityAccountNumber) {
        this.liabilityAccountNumber = liabilityAccountNumber;
    }

    public BigDecimalFilter getLiabilityAmount() {
        return liabilityAmount;
    }

    public BigDecimalFilter liabilityAmount() {
        if (liabilityAmount == null) {
            liabilityAmount = new BigDecimalFilter();
        }
        return liabilityAmount;
    }

    public void setLiabilityAmount(BigDecimalFilter liabilityAmount) {
        this.liabilityAmount = liabilityAmount;
    }

    public StringFilter getInterestPayableAccountNumber() {
        return interestPayableAccountNumber;
    }

    public StringFilter interestPayableAccountNumber() {
        if (interestPayableAccountNumber == null) {
            interestPayableAccountNumber = new StringFilter();
        }
        return interestPayableAccountNumber;
    }

    public void setInterestPayableAccountNumber(StringFilter interestPayableAccountNumber) {
        this.interestPayableAccountNumber = interestPayableAccountNumber;
    }

    public BigDecimalFilter getInterestPayableAmount() {
        return interestPayableAmount;
    }

    public BigDecimalFilter interestPayableAmount() {
        if (interestPayableAmount == null) {
            interestPayableAmount = new BigDecimalFilter();
        }
        return interestPayableAmount;
    }

    public void setInterestPayableAmount(BigDecimalFilter interestPayableAmount) {
        this.interestPayableAmount = interestPayableAmount;
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
        final LeaseLiabilityReportItemCriteria that = (LeaseLiabilityReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(leaseTitle, that.leaseTitle) &&
            Objects.equals(liabilityAccountNumber, that.liabilityAccountNumber) &&
            Objects.equals(liabilityAmount, that.liabilityAmount) &&
            Objects.equals(interestPayableAccountNumber, that.interestPayableAccountNumber) &&
            Objects.equals(interestPayableAmount, that.interestPayableAmount) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            bookingId,
            leaseTitle,
            liabilityAccountNumber,
            liabilityAmount,
            interestPayableAccountNumber,
            interestPayableAmount,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (bookingId != null ? "bookingId=" + bookingId + ", " : "") +
            (leaseTitle != null ? "leaseTitle=" + leaseTitle + ", " : "") +
            (liabilityAccountNumber != null ? "liabilityAccountNumber=" + liabilityAccountNumber + ", " : "") +
            (liabilityAmount != null ? "liabilityAmount=" + liabilityAmount + ", " : "") +
            (interestPayableAccountNumber != null ? "interestPayableAccountNumber=" + interestPayableAccountNumber + ", " : "") +
            (interestPayableAmount != null ? "interestPayableAmount=" + interestPayableAmount + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
