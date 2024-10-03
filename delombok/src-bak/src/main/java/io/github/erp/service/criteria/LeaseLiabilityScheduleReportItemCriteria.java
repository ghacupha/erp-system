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

/**
 * Criteria class for the {@link io.github.erp.domain.LeaseLiabilityScheduleReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.LeaseLiabilityScheduleReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-liability-schedule-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseLiabilityScheduleReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter sequenceNumber;

    private BigDecimalFilter openingBalance;

    private BigDecimalFilter cashPayment;

    private BigDecimalFilter principalPayment;

    private BigDecimalFilter interestPayment;

    private BigDecimalFilter outstandingBalance;

    private BigDecimalFilter interestPayableOpening;

    private BigDecimalFilter interestAccrued;

    private BigDecimalFilter interestPayableClosing;

    private LongFilter amortizationScheduleId;

    private Boolean distinct;

    public LeaseLiabilityScheduleReportItemCriteria() {}

    public LeaseLiabilityScheduleReportItemCriteria(LeaseLiabilityScheduleReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sequenceNumber = other.sequenceNumber == null ? null : other.sequenceNumber.copy();
        this.openingBalance = other.openingBalance == null ? null : other.openingBalance.copy();
        this.cashPayment = other.cashPayment == null ? null : other.cashPayment.copy();
        this.principalPayment = other.principalPayment == null ? null : other.principalPayment.copy();
        this.interestPayment = other.interestPayment == null ? null : other.interestPayment.copy();
        this.outstandingBalance = other.outstandingBalance == null ? null : other.outstandingBalance.copy();
        this.interestPayableOpening = other.interestPayableOpening == null ? null : other.interestPayableOpening.copy();
        this.interestAccrued = other.interestAccrued == null ? null : other.interestAccrued.copy();
        this.interestPayableClosing = other.interestPayableClosing == null ? null : other.interestPayableClosing.copy();
        this.amortizationScheduleId = other.amortizationScheduleId == null ? null : other.amortizationScheduleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaseLiabilityScheduleReportItemCriteria copy() {
        return new LeaseLiabilityScheduleReportItemCriteria(this);
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

    public IntegerFilter getSequenceNumber() {
        return sequenceNumber;
    }

    public IntegerFilter sequenceNumber() {
        if (sequenceNumber == null) {
            sequenceNumber = new IntegerFilter();
        }
        return sequenceNumber;
    }

    public void setSequenceNumber(IntegerFilter sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public BigDecimalFilter getOpeningBalance() {
        return openingBalance;
    }

    public BigDecimalFilter openingBalance() {
        if (openingBalance == null) {
            openingBalance = new BigDecimalFilter();
        }
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimalFilter openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimalFilter getCashPayment() {
        return cashPayment;
    }

    public BigDecimalFilter cashPayment() {
        if (cashPayment == null) {
            cashPayment = new BigDecimalFilter();
        }
        return cashPayment;
    }

    public void setCashPayment(BigDecimalFilter cashPayment) {
        this.cashPayment = cashPayment;
    }

    public BigDecimalFilter getPrincipalPayment() {
        return principalPayment;
    }

    public BigDecimalFilter principalPayment() {
        if (principalPayment == null) {
            principalPayment = new BigDecimalFilter();
        }
        return principalPayment;
    }

    public void setPrincipalPayment(BigDecimalFilter principalPayment) {
        this.principalPayment = principalPayment;
    }

    public BigDecimalFilter getInterestPayment() {
        return interestPayment;
    }

    public BigDecimalFilter interestPayment() {
        if (interestPayment == null) {
            interestPayment = new BigDecimalFilter();
        }
        return interestPayment;
    }

    public void setInterestPayment(BigDecimalFilter interestPayment) {
        this.interestPayment = interestPayment;
    }

    public BigDecimalFilter getOutstandingBalance() {
        return outstandingBalance;
    }

    public BigDecimalFilter outstandingBalance() {
        if (outstandingBalance == null) {
            outstandingBalance = new BigDecimalFilter();
        }
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimalFilter outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public BigDecimalFilter getInterestPayableOpening() {
        return interestPayableOpening;
    }

    public BigDecimalFilter interestPayableOpening() {
        if (interestPayableOpening == null) {
            interestPayableOpening = new BigDecimalFilter();
        }
        return interestPayableOpening;
    }

    public void setInterestPayableOpening(BigDecimalFilter interestPayableOpening) {
        this.interestPayableOpening = interestPayableOpening;
    }

    public BigDecimalFilter getInterestAccrued() {
        return interestAccrued;
    }

    public BigDecimalFilter interestAccrued() {
        if (interestAccrued == null) {
            interestAccrued = new BigDecimalFilter();
        }
        return interestAccrued;
    }

    public void setInterestAccrued(BigDecimalFilter interestAccrued) {
        this.interestAccrued = interestAccrued;
    }

    public BigDecimalFilter getInterestPayableClosing() {
        return interestPayableClosing;
    }

    public BigDecimalFilter interestPayableClosing() {
        if (interestPayableClosing == null) {
            interestPayableClosing = new BigDecimalFilter();
        }
        return interestPayableClosing;
    }

    public void setInterestPayableClosing(BigDecimalFilter interestPayableClosing) {
        this.interestPayableClosing = interestPayableClosing;
    }

    public LongFilter getAmortizationScheduleId() {
        return amortizationScheduleId;
    }

    public LongFilter amortizationScheduleId() {
        if (amortizationScheduleId == null) {
            amortizationScheduleId = new LongFilter();
        }
        return amortizationScheduleId;
    }

    public void setAmortizationScheduleId(LongFilter amortizationScheduleId) {
        this.amortizationScheduleId = amortizationScheduleId;
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
        final LeaseLiabilityScheduleReportItemCriteria that = (LeaseLiabilityScheduleReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNumber, that.sequenceNumber) &&
            Objects.equals(openingBalance, that.openingBalance) &&
            Objects.equals(cashPayment, that.cashPayment) &&
            Objects.equals(principalPayment, that.principalPayment) &&
            Objects.equals(interestPayment, that.interestPayment) &&
            Objects.equals(outstandingBalance, that.outstandingBalance) &&
            Objects.equals(interestPayableOpening, that.interestPayableOpening) &&
            Objects.equals(interestAccrued, that.interestAccrued) &&
            Objects.equals(interestPayableClosing, that.interestPayableClosing) &&
            Objects.equals(amortizationScheduleId, that.amortizationScheduleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sequenceNumber,
            openingBalance,
            cashPayment,
            principalPayment,
            interestPayment,
            outstandingBalance,
            interestPayableOpening,
            interestAccrued,
            interestPayableClosing,
            amortizationScheduleId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityScheduleReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber + ", " : "") +
            (openingBalance != null ? "openingBalance=" + openingBalance + ", " : "") +
            (cashPayment != null ? "cashPayment=" + cashPayment + ", " : "") +
            (principalPayment != null ? "principalPayment=" + principalPayment + ", " : "") +
            (interestPayment != null ? "interestPayment=" + interestPayment + ", " : "") +
            (outstandingBalance != null ? "outstandingBalance=" + outstandingBalance + ", " : "") +
            (interestPayableOpening != null ? "interestPayableOpening=" + interestPayableOpening + ", " : "") +
            (interestAccrued != null ? "interestAccrued=" + interestAccrued + ", " : "") +
            (interestPayableClosing != null ? "interestPayableClosing=" + interestPayableClosing + ", " : "") +
            (amortizationScheduleId != null ? "amortizationScheduleId=" + amortizationScheduleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
