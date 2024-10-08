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
 * Criteria class for the {@link io.github.erp.domain.LeaseAmortizationCalculation} entity. This class is used
 * in {@link io.github.erp.web.rest.LeaseAmortizationCalculationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-amortization-calculations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseAmortizationCalculationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter periodicity;

    private BigDecimalFilter leaseAmount;

    private IntegerFilter numberOfPeriods;

    private BigDecimalFilter interestRate;

    private LongFilter leaseLiabilityId;

    private LongFilter leaseContractId;

    private Boolean distinct;

    public LeaseAmortizationCalculationCriteria() {}

    public LeaseAmortizationCalculationCriteria(LeaseAmortizationCalculationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.periodicity = other.periodicity == null ? null : other.periodicity.copy();
        this.leaseAmount = other.leaseAmount == null ? null : other.leaseAmount.copy();
        this.numberOfPeriods = other.numberOfPeriods == null ? null : other.numberOfPeriods.copy();
        this.interestRate = other.interestRate == null ? null : other.interestRate.copy();
        this.leaseLiabilityId = other.leaseLiabilityId == null ? null : other.leaseLiabilityId.copy();
        this.leaseContractId = other.leaseContractId == null ? null : other.leaseContractId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaseAmortizationCalculationCriteria copy() {
        return new LeaseAmortizationCalculationCriteria(this);
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

    public StringFilter getPeriodicity() {
        return periodicity;
    }

    public StringFilter periodicity() {
        if (periodicity == null) {
            periodicity = new StringFilter();
        }
        return periodicity;
    }

    public void setPeriodicity(StringFilter periodicity) {
        this.periodicity = periodicity;
    }

    public BigDecimalFilter getLeaseAmount() {
        return leaseAmount;
    }

    public BigDecimalFilter leaseAmount() {
        if (leaseAmount == null) {
            leaseAmount = new BigDecimalFilter();
        }
        return leaseAmount;
    }

    public void setLeaseAmount(BigDecimalFilter leaseAmount) {
        this.leaseAmount = leaseAmount;
    }

    public IntegerFilter getNumberOfPeriods() {
        return numberOfPeriods;
    }

    public IntegerFilter numberOfPeriods() {
        if (numberOfPeriods == null) {
            numberOfPeriods = new IntegerFilter();
        }
        return numberOfPeriods;
    }

    public void setNumberOfPeriods(IntegerFilter numberOfPeriods) {
        this.numberOfPeriods = numberOfPeriods;
    }

    public BigDecimalFilter getInterestRate() {
        return interestRate;
    }

    public BigDecimalFilter interestRate() {
        if (interestRate == null) {
            interestRate = new BigDecimalFilter();
        }
        return interestRate;
    }

    public void setInterestRate(BigDecimalFilter interestRate) {
        this.interestRate = interestRate;
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

    public LongFilter getLeaseContractId() {
        return leaseContractId;
    }

    public LongFilter leaseContractId() {
        if (leaseContractId == null) {
            leaseContractId = new LongFilter();
        }
        return leaseContractId;
    }

    public void setLeaseContractId(LongFilter leaseContractId) {
        this.leaseContractId = leaseContractId;
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
        final LeaseAmortizationCalculationCriteria that = (LeaseAmortizationCalculationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(periodicity, that.periodicity) &&
            Objects.equals(leaseAmount, that.leaseAmount) &&
            Objects.equals(numberOfPeriods, that.numberOfPeriods) &&
            Objects.equals(interestRate, that.interestRate) &&
            Objects.equals(leaseLiabilityId, that.leaseLiabilityId) &&
            Objects.equals(leaseContractId, that.leaseContractId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, periodicity, leaseAmount, numberOfPeriods, interestRate, leaseLiabilityId, leaseContractId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseAmortizationCalculationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (periodicity != null ? "periodicity=" + periodicity + ", " : "") +
            (leaseAmount != null ? "leaseAmount=" + leaseAmount + ", " : "") +
            (numberOfPeriods != null ? "numberOfPeriods=" + numberOfPeriods + ", " : "") +
            (interestRate != null ? "interestRate=" + interestRate + ", " : "") +
            (leaseLiabilityId != null ? "leaseLiabilityId=" + leaseLiabilityId + ", " : "") +
            (leaseContractId != null ? "leaseContractId=" + leaseContractId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
