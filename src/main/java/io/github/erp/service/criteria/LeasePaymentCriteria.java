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
 * Criteria class for the {@link io.github.erp.domain.LeasePayment} entity. This class is used
 * in {@link io.github.erp.web.rest.LeasePaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeasePaymentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter paymentAmount;

    private LocalDateFilter paymentDate;

    private LongFilter leaseLiabilityId;

    private Boolean distinct;

    public LeasePaymentCriteria() {}

    public LeasePaymentCriteria(LeasePaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentAmount = other.paymentAmount == null ? null : other.paymentAmount.copy();
        this.paymentDate = other.paymentDate == null ? null : other.paymentDate.copy();
        this.leaseLiabilityId = other.leaseLiabilityId == null ? null : other.leaseLiabilityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeasePaymentCriteria copy() {
        return new LeasePaymentCriteria(this);
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

    public BigDecimalFilter getPaymentAmount() {
        return paymentAmount;
    }

    public BigDecimalFilter paymentAmount() {
        if (paymentAmount == null) {
            paymentAmount = new BigDecimalFilter();
        }
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimalFilter paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public LocalDateFilter getPaymentDate() {
        return paymentDate;
    }

    public LocalDateFilter paymentDate() {
        if (paymentDate == null) {
            paymentDate = new LocalDateFilter();
        }
        return paymentDate;
    }

    public void setPaymentDate(LocalDateFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LongFilter getLeaseLiabilityId() {
        return leaseLiabilityId;
    }

    public LongFilter leaseLiabilityId() {
        if (leaseLiabilityId == null) {
            leaseLiabilityId = new LongFilter();
        }
        return leaseLiabilityId;
    }

    public void setLeaseLiabilityId(LongFilter leaseLiabilityId) {
        this.leaseLiabilityId = leaseLiabilityId;
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
        final LeasePaymentCriteria that = (LeasePaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentAmount, that.paymentAmount) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(leaseLiabilityId, that.leaseLiabilityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentAmount, paymentDate, leaseLiabilityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeasePaymentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (paymentAmount != null ? "paymentAmount=" + paymentAmount + ", " : "") +
            (paymentDate != null ? "paymentDate=" + paymentDate + ", " : "") +
            (leaseLiabilityId != null ? "leaseLiabilityId=" + leaseLiabilityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
