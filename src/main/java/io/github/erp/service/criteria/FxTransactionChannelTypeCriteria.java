package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
 * Criteria class for the {@link io.github.erp.domain.FxTransactionChannelType} entity. This class is used
 * in {@link io.github.erp.web.rest.FxTransactionChannelTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fx-transaction-channel-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FxTransactionChannelTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fxTransactionChannelCode;

    private StringFilter fxTransactionChannelType;

    private Boolean distinct;

    public FxTransactionChannelTypeCriteria() {}

    public FxTransactionChannelTypeCriteria(FxTransactionChannelTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fxTransactionChannelCode = other.fxTransactionChannelCode == null ? null : other.fxTransactionChannelCode.copy();
        this.fxTransactionChannelType = other.fxTransactionChannelType == null ? null : other.fxTransactionChannelType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FxTransactionChannelTypeCriteria copy() {
        return new FxTransactionChannelTypeCriteria(this);
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

    public StringFilter getFxTransactionChannelCode() {
        return fxTransactionChannelCode;
    }

    public StringFilter fxTransactionChannelCode() {
        if (fxTransactionChannelCode == null) {
            fxTransactionChannelCode = new StringFilter();
        }
        return fxTransactionChannelCode;
    }

    public void setFxTransactionChannelCode(StringFilter fxTransactionChannelCode) {
        this.fxTransactionChannelCode = fxTransactionChannelCode;
    }

    public StringFilter getFxTransactionChannelType() {
        return fxTransactionChannelType;
    }

    public StringFilter fxTransactionChannelType() {
        if (fxTransactionChannelType == null) {
            fxTransactionChannelType = new StringFilter();
        }
        return fxTransactionChannelType;
    }

    public void setFxTransactionChannelType(StringFilter fxTransactionChannelType) {
        this.fxTransactionChannelType = fxTransactionChannelType;
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
        final FxTransactionChannelTypeCriteria that = (FxTransactionChannelTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fxTransactionChannelCode, that.fxTransactionChannelCode) &&
            Objects.equals(fxTransactionChannelType, that.fxTransactionChannelType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fxTransactionChannelCode, fxTransactionChannelType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxTransactionChannelTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fxTransactionChannelCode != null ? "fxTransactionChannelCode=" + fxTransactionChannelCode + ", " : "") +
            (fxTransactionChannelType != null ? "fxTransactionChannelType=" + fxTransactionChannelType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
