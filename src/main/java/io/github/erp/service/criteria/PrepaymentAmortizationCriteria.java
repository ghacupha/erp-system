package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0-SNAPSHOT
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
 * Criteria class for the {@link io.github.erp.domain.PrepaymentAmortization} entity. This class is used
 * in {@link io.github.erp.web.rest.PrepaymentAmortizationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prepayment-amortizations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PrepaymentAmortizationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private LocalDateFilter prepaymentPeriod;

    private BigDecimalFilter prepaymentAmount;

    private BooleanFilter inactive;

    private LongFilter prepaymentAccountId;

    private LongFilter settlementCurrencyId;

    private LongFilter debitAccountId;

    private LongFilter creditAccountId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public PrepaymentAmortizationCriteria() {}

    public PrepaymentAmortizationCriteria(PrepaymentAmortizationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.prepaymentPeriod = other.prepaymentPeriod == null ? null : other.prepaymentPeriod.copy();
        this.prepaymentAmount = other.prepaymentAmount == null ? null : other.prepaymentAmount.copy();
        this.inactive = other.inactive == null ? null : other.inactive.copy();
        this.prepaymentAccountId = other.prepaymentAccountId == null ? null : other.prepaymentAccountId.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.debitAccountId = other.debitAccountId == null ? null : other.debitAccountId.copy();
        this.creditAccountId = other.creditAccountId == null ? null : other.creditAccountId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PrepaymentAmortizationCriteria copy() {
        return new PrepaymentAmortizationCriteria(this);
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

    public LocalDateFilter getPrepaymentPeriod() {
        return prepaymentPeriod;
    }

    public LocalDateFilter prepaymentPeriod() {
        if (prepaymentPeriod == null) {
            prepaymentPeriod = new LocalDateFilter();
        }
        return prepaymentPeriod;
    }

    public void setPrepaymentPeriod(LocalDateFilter prepaymentPeriod) {
        this.prepaymentPeriod = prepaymentPeriod;
    }

    public BigDecimalFilter getPrepaymentAmount() {
        return prepaymentAmount;
    }

    public BigDecimalFilter prepaymentAmount() {
        if (prepaymentAmount == null) {
            prepaymentAmount = new BigDecimalFilter();
        }
        return prepaymentAmount;
    }

    public void setPrepaymentAmount(BigDecimalFilter prepaymentAmount) {
        this.prepaymentAmount = prepaymentAmount;
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

    public LongFilter getSettlementCurrencyId() {
        return settlementCurrencyId;
    }

    public LongFilter settlementCurrencyId() {
        if (settlementCurrencyId == null) {
            settlementCurrencyId = new LongFilter();
        }
        return settlementCurrencyId;
    }

    public void setSettlementCurrencyId(LongFilter settlementCurrencyId) {
        this.settlementCurrencyId = settlementCurrencyId;
    }

    public LongFilter getDebitAccountId() {
        return debitAccountId;
    }

    public LongFilter debitAccountId() {
        if (debitAccountId == null) {
            debitAccountId = new LongFilter();
        }
        return debitAccountId;
    }

    public void setDebitAccountId(LongFilter debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public LongFilter getCreditAccountId() {
        return creditAccountId;
    }

    public LongFilter creditAccountId() {
        if (creditAccountId == null) {
            creditAccountId = new LongFilter();
        }
        return creditAccountId;
    }

    public void setCreditAccountId(LongFilter creditAccountId) {
        this.creditAccountId = creditAccountId;
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
        final PrepaymentAmortizationCriteria that = (PrepaymentAmortizationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(prepaymentPeriod, that.prepaymentPeriod) &&
            Objects.equals(prepaymentAmount, that.prepaymentAmount) &&
            Objects.equals(inactive, that.inactive) &&
            Objects.equals(prepaymentAccountId, that.prepaymentAccountId) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(debitAccountId, that.debitAccountId) &&
            Objects.equals(creditAccountId, that.creditAccountId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            prepaymentPeriod,
            prepaymentAmount,
            inactive,
            prepaymentAccountId,
            settlementCurrencyId,
            debitAccountId,
            creditAccountId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentAmortizationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (prepaymentPeriod != null ? "prepaymentPeriod=" + prepaymentPeriod + ", " : "") +
            (prepaymentAmount != null ? "prepaymentAmount=" + prepaymentAmount + ", " : "") +
            (inactive != null ? "inactive=" + inactive + ", " : "") +
            (prepaymentAccountId != null ? "prepaymentAccountId=" + prepaymentAccountId + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (debitAccountId != null ? "debitAccountId=" + debitAccountId + ", " : "") +
            (creditAccountId != null ? "creditAccountId=" + creditAccountId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
