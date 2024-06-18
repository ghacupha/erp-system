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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.LeaseLiability} entity. This class is used
 * in {@link io.github.erp.web.rest.LeaseLiabilityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-liabilities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseLiabilityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter leaseId;

    private BigDecimalFilter liabilityAmount;

    private FloatFilter interestRate;

    private ZonedDateTimeFilter startDate;

    private FloatFilter endDate;

    private LongFilter leaseAmortizationCalculationId;

    private LongFilter leasePaymentId;

    private Boolean distinct;

    public LeaseLiabilityCriteria() {}

    public LeaseLiabilityCriteria(LeaseLiabilityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.leaseId = other.leaseId == null ? null : other.leaseId.copy();
        this.liabilityAmount = other.liabilityAmount == null ? null : other.liabilityAmount.copy();
        this.interestRate = other.interestRate == null ? null : other.interestRate.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.leaseAmortizationCalculationId =
            other.leaseAmortizationCalculationId == null ? null : other.leaseAmortizationCalculationId.copy();
        this.leasePaymentId = other.leasePaymentId == null ? null : other.leasePaymentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaseLiabilityCriteria copy() {
        return new LeaseLiabilityCriteria(this);
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

    public StringFilter getLeaseId() {
        return leaseId;
    }

    public StringFilter leaseId() {
        if (leaseId == null) {
            leaseId = new StringFilter();
        }
        return leaseId;
    }

    public void setLeaseId(StringFilter leaseId) {
        this.leaseId = leaseId;
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

    public FloatFilter getInterestRate() {
        return interestRate;
    }

    public FloatFilter interestRate() {
        if (interestRate == null) {
            interestRate = new FloatFilter();
        }
        return interestRate;
    }

    public void setInterestRate(FloatFilter interestRate) {
        this.interestRate = interestRate;
    }

    public ZonedDateTimeFilter getStartDate() {
        return startDate;
    }

    public ZonedDateTimeFilter startDate() {
        if (startDate == null) {
            startDate = new ZonedDateTimeFilter();
        }
        return startDate;
    }

    public void setStartDate(ZonedDateTimeFilter startDate) {
        this.startDate = startDate;
    }

    public FloatFilter getEndDate() {
        return endDate;
    }

    public FloatFilter endDate() {
        if (endDate == null) {
            endDate = new FloatFilter();
        }
        return endDate;
    }

    public void setEndDate(FloatFilter endDate) {
        this.endDate = endDate;
    }

    public LongFilter getLeaseAmortizationCalculationId() {
        return leaseAmortizationCalculationId;
    }

    public LongFilter leaseAmortizationCalculationId() {
        if (leaseAmortizationCalculationId == null) {
            leaseAmortizationCalculationId = new LongFilter();
        }
        return leaseAmortizationCalculationId;
    }

    public void setLeaseAmortizationCalculationId(LongFilter leaseAmortizationCalculationId) {
        this.leaseAmortizationCalculationId = leaseAmortizationCalculationId;
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
        final LeaseLiabilityCriteria that = (LeaseLiabilityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(leaseId, that.leaseId) &&
            Objects.equals(liabilityAmount, that.liabilityAmount) &&
            Objects.equals(interestRate, that.interestRate) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(leaseAmortizationCalculationId, that.leaseAmortizationCalculationId) &&
            Objects.equals(leasePaymentId, that.leasePaymentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            leaseId,
            liabilityAmount,
            interestRate,
            startDate,
            endDate,
            leaseAmortizationCalculationId,
            leasePaymentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (leaseId != null ? "leaseId=" + leaseId + ", " : "") +
            (liabilityAmount != null ? "liabilityAmount=" + liabilityAmount + ", " : "") +
            (interestRate != null ? "interestRate=" + interestRate + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (leaseAmortizationCalculationId != null ? "leaseAmortizationCalculationId=" + leaseAmortizationCalculationId + ", " : "") +
            (leasePaymentId != null ? "leasePaymentId=" + leasePaymentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
