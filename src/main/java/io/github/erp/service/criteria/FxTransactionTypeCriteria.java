package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

/**
 * Criteria class for the {@link io.github.erp.domain.FxTransactionType} entity. This class is used
 * in {@link io.github.erp.web.rest.FxTransactionTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fx-transaction-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FxTransactionTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fxTransactionTypeCode;

    private StringFilter fxTransactionType;

    private Boolean distinct;

    public FxTransactionTypeCriteria() {}

    public FxTransactionTypeCriteria(FxTransactionTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fxTransactionTypeCode = other.fxTransactionTypeCode == null ? null : other.fxTransactionTypeCode.copy();
        this.fxTransactionType = other.fxTransactionType == null ? null : other.fxTransactionType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FxTransactionTypeCriteria copy() {
        return new FxTransactionTypeCriteria(this);
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

    public StringFilter getFxTransactionTypeCode() {
        return fxTransactionTypeCode;
    }

    public StringFilter fxTransactionTypeCode() {
        if (fxTransactionTypeCode == null) {
            fxTransactionTypeCode = new StringFilter();
        }
        return fxTransactionTypeCode;
    }

    public void setFxTransactionTypeCode(StringFilter fxTransactionTypeCode) {
        this.fxTransactionTypeCode = fxTransactionTypeCode;
    }

    public StringFilter getFxTransactionType() {
        return fxTransactionType;
    }

    public StringFilter fxTransactionType() {
        if (fxTransactionType == null) {
            fxTransactionType = new StringFilter();
        }
        return fxTransactionType;
    }

    public void setFxTransactionType(StringFilter fxTransactionType) {
        this.fxTransactionType = fxTransactionType;
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
        final FxTransactionTypeCriteria that = (FxTransactionTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fxTransactionTypeCode, that.fxTransactionTypeCode) &&
            Objects.equals(fxTransactionType, that.fxTransactionType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fxTransactionTypeCode, fxTransactionType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxTransactionTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fxTransactionTypeCode != null ? "fxTransactionTypeCode=" + fxTransactionTypeCode + ", " : "") +
            (fxTransactionType != null ? "fxTransactionType=" + fxTransactionType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
