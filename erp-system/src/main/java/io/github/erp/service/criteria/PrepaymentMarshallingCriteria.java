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
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.PrepaymentMarshalling} entity. This class is used
 * in {@link io.github.erp.web.rest.PrepaymentMarshallingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prepayment-marshallings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PrepaymentMarshallingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter inactive;

    private IntegerFilter amortizationPeriods;

    private BooleanFilter processed;

    private UUIDFilter compilationToken;

    private LongFilter prepaymentAccountId;

    private LongFilter placeholderId;

    private LongFilter firstFiscalMonthId;

    private LongFilter lastFiscalMonthId;

    private LongFilter firstAmortizationPeriodId;

    private Boolean distinct;

    public PrepaymentMarshallingCriteria() {}

    public PrepaymentMarshallingCriteria(PrepaymentMarshallingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.inactive = other.inactive == null ? null : other.inactive.copy();
        this.amortizationPeriods = other.amortizationPeriods == null ? null : other.amortizationPeriods.copy();
        this.processed = other.processed == null ? null : other.processed.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.prepaymentAccountId = other.prepaymentAccountId == null ? null : other.prepaymentAccountId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.firstFiscalMonthId = other.firstFiscalMonthId == null ? null : other.firstFiscalMonthId.copy();
        this.lastFiscalMonthId = other.lastFiscalMonthId == null ? null : other.lastFiscalMonthId.copy();
        this.firstAmortizationPeriodId = other.firstAmortizationPeriodId == null ? null : other.firstAmortizationPeriodId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PrepaymentMarshallingCriteria copy() {
        return new PrepaymentMarshallingCriteria(this);
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

    public BooleanFilter getInactive() {
        return inactive;
    }

    public BooleanFilter inactive() {
        if (inactive == null) {
            inactive = new BooleanFilter();
        }
        return inactive;
    }

    public void setInactive(BooleanFilter inactive) {
        this.inactive = inactive;
    }

    public IntegerFilter getAmortizationPeriods() {
        return amortizationPeriods;
    }

    public IntegerFilter amortizationPeriods() {
        if (amortizationPeriods == null) {
            amortizationPeriods = new IntegerFilter();
        }
        return amortizationPeriods;
    }

    public void setAmortizationPeriods(IntegerFilter amortizationPeriods) {
        this.amortizationPeriods = amortizationPeriods;
    }

    public BooleanFilter getProcessed() {
        return processed;
    }

    public BooleanFilter processed() {
        if (processed == null) {
            processed = new BooleanFilter();
        }
        return processed;
    }

    public void setProcessed(BooleanFilter processed) {
        this.processed = processed;
    }

    public UUIDFilter getCompilationToken() {
        return compilationToken;
    }

    public UUIDFilter compilationToken() {
        if (compilationToken == null) {
            compilationToken = new UUIDFilter();
        }
        return compilationToken;
    }

    public void setCompilationToken(UUIDFilter compilationToken) {
        this.compilationToken = compilationToken;
    }

    public LongFilter getPrepaymentAccountId() {
        return prepaymentAccountId;
    }

    public LongFilter prepaymentAccountId() {
        if (prepaymentAccountId == null) {
            prepaymentAccountId = new LongFilter();
        }
        return prepaymentAccountId;
    }

    public void setPrepaymentAccountId(LongFilter prepaymentAccountId) {
        this.prepaymentAccountId = prepaymentAccountId;
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

    public LongFilter getFirstFiscalMonthId() {
        return firstFiscalMonthId;
    }

    public LongFilter firstFiscalMonthId() {
        if (firstFiscalMonthId == null) {
            firstFiscalMonthId = new LongFilter();
        }
        return firstFiscalMonthId;
    }

    public void setFirstFiscalMonthId(LongFilter firstFiscalMonthId) {
        this.firstFiscalMonthId = firstFiscalMonthId;
    }

    public LongFilter getLastFiscalMonthId() {
        return lastFiscalMonthId;
    }

    public LongFilter lastFiscalMonthId() {
        if (lastFiscalMonthId == null) {
            lastFiscalMonthId = new LongFilter();
        }
        return lastFiscalMonthId;
    }

    public void setLastFiscalMonthId(LongFilter lastFiscalMonthId) {
        this.lastFiscalMonthId = lastFiscalMonthId;
    }

    public LongFilter getFirstAmortizationPeriodId() {
        return firstAmortizationPeriodId;
    }

    public LongFilter firstAmortizationPeriodId() {
        if (firstAmortizationPeriodId == null) {
            firstAmortizationPeriodId = new LongFilter();
        }
        return firstAmortizationPeriodId;
    }

    public void setFirstAmortizationPeriodId(LongFilter firstAmortizationPeriodId) {
        this.firstAmortizationPeriodId = firstAmortizationPeriodId;
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
        final PrepaymentMarshallingCriteria that = (PrepaymentMarshallingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(inactive, that.inactive) &&
            Objects.equals(amortizationPeriods, that.amortizationPeriods) &&
            Objects.equals(processed, that.processed) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(prepaymentAccountId, that.prepaymentAccountId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(firstFiscalMonthId, that.firstFiscalMonthId) &&
            Objects.equals(lastFiscalMonthId, that.lastFiscalMonthId) &&
            Objects.equals(firstAmortizationPeriodId, that.firstAmortizationPeriodId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            inactive,
            amortizationPeriods,
            processed,
            compilationToken,
            prepaymentAccountId,
            placeholderId,
            firstFiscalMonthId,
            lastFiscalMonthId,
            firstAmortizationPeriodId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentMarshallingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (inactive != null ? "inactive=" + inactive + ", " : "") +
            (amortizationPeriods != null ? "amortizationPeriods=" + amortizationPeriods + ", " : "") +
            (processed != null ? "processed=" + processed + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (prepaymentAccountId != null ? "prepaymentAccountId=" + prepaymentAccountId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (firstFiscalMonthId != null ? "firstFiscalMonthId=" + firstFiscalMonthId + ", " : "") +
            (lastFiscalMonthId != null ? "lastFiscalMonthId=" + lastFiscalMonthId + ", " : "") +
            (firstAmortizationPeriodId != null ? "firstAmortizationPeriodId=" + firstAmortizationPeriodId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
