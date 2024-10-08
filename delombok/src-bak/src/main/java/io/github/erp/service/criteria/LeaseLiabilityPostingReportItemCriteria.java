package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
 * Criteria class for the {@link io.github.erp.domain.LeaseLiabilityPostingReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.LeaseLiabilityPostingReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-liability-posting-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseLiabilityPostingReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bookingId;

    private StringFilter leaseTitle;

    private StringFilter leaseDescription;

    private StringFilter accountNumber;

    private StringFilter posting;

    private BigDecimalFilter postingAmount;

    private Boolean distinct;

    public LeaseLiabilityPostingReportItemCriteria() {}

    public LeaseLiabilityPostingReportItemCriteria(LeaseLiabilityPostingReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bookingId = other.bookingId == null ? null : other.bookingId.copy();
        this.leaseTitle = other.leaseTitle == null ? null : other.leaseTitle.copy();
        this.leaseDescription = other.leaseDescription == null ? null : other.leaseDescription.copy();
        this.accountNumber = other.accountNumber == null ? null : other.accountNumber.copy();
        this.posting = other.posting == null ? null : other.posting.copy();
        this.postingAmount = other.postingAmount == null ? null : other.postingAmount.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaseLiabilityPostingReportItemCriteria copy() {
        return new LeaseLiabilityPostingReportItemCriteria(this);
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

    public StringFilter getLeaseDescription() {
        return leaseDescription;
    }

    public StringFilter leaseDescription() {
        if (leaseDescription == null) {
            leaseDescription = new StringFilter();
        }
        return leaseDescription;
    }

    public void setLeaseDescription(StringFilter leaseDescription) {
        this.leaseDescription = leaseDescription;
    }

    public StringFilter getAccountNumber() {
        return accountNumber;
    }

    public StringFilter accountNumber() {
        if (accountNumber == null) {
            accountNumber = new StringFilter();
        }
        return accountNumber;
    }

    public void setAccountNumber(StringFilter accountNumber) {
        this.accountNumber = accountNumber;
    }

    public StringFilter getPosting() {
        return posting;
    }

    public StringFilter posting() {
        if (posting == null) {
            posting = new StringFilter();
        }
        return posting;
    }

    public void setPosting(StringFilter posting) {
        this.posting = posting;
    }

    public BigDecimalFilter getPostingAmount() {
        return postingAmount;
    }

    public BigDecimalFilter postingAmount() {
        if (postingAmount == null) {
            postingAmount = new BigDecimalFilter();
        }
        return postingAmount;
    }

    public void setPostingAmount(BigDecimalFilter postingAmount) {
        this.postingAmount = postingAmount;
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
        final LeaseLiabilityPostingReportItemCriteria that = (LeaseLiabilityPostingReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(leaseTitle, that.leaseTitle) &&
            Objects.equals(leaseDescription, that.leaseDescription) &&
            Objects.equals(accountNumber, that.accountNumber) &&
            Objects.equals(posting, that.posting) &&
            Objects.equals(postingAmount, that.postingAmount) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookingId, leaseTitle, leaseDescription, accountNumber, posting, postingAmount, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityPostingReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (bookingId != null ? "bookingId=" + bookingId + ", " : "") +
            (leaseTitle != null ? "leaseTitle=" + leaseTitle + ", " : "") +
            (leaseDescription != null ? "leaseDescription=" + leaseDescription + ", " : "") +
            (accountNumber != null ? "accountNumber=" + accountNumber + ", " : "") +
            (posting != null ? "posting=" + posting + ", " : "") +
            (postingAmount != null ? "postingAmount=" + postingAmount + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
