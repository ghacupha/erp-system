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
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.MoratoriumItem} entity. This class is used
 * in {@link io.github.erp.web.rest.MoratoriumItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /moratorium-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MoratoriumItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter moratoriumItemTypeCode;

    private StringFilter moratoriumItemType;

    private Boolean distinct;

    public MoratoriumItemCriteria() {}

    public MoratoriumItemCriteria(MoratoriumItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.moratoriumItemTypeCode = other.moratoriumItemTypeCode == null ? null : other.moratoriumItemTypeCode.copy();
        this.moratoriumItemType = other.moratoriumItemType == null ? null : other.moratoriumItemType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MoratoriumItemCriteria copy() {
        return new MoratoriumItemCriteria(this);
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

    public StringFilter getMoratoriumItemTypeCode() {
        return moratoriumItemTypeCode;
    }

    public StringFilter moratoriumItemTypeCode() {
        if (moratoriumItemTypeCode == null) {
            moratoriumItemTypeCode = new StringFilter();
        }
        return moratoriumItemTypeCode;
    }

    public void setMoratoriumItemTypeCode(StringFilter moratoriumItemTypeCode) {
        this.moratoriumItemTypeCode = moratoriumItemTypeCode;
    }

    public StringFilter getMoratoriumItemType() {
        return moratoriumItemType;
    }

    public StringFilter moratoriumItemType() {
        if (moratoriumItemType == null) {
            moratoriumItemType = new StringFilter();
        }
        return moratoriumItemType;
    }

    public void setMoratoriumItemType(StringFilter moratoriumItemType) {
        this.moratoriumItemType = moratoriumItemType;
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
        final MoratoriumItemCriteria that = (MoratoriumItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(moratoriumItemTypeCode, that.moratoriumItemTypeCode) &&
            Objects.equals(moratoriumItemType, that.moratoriumItemType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, moratoriumItemTypeCode, moratoriumItemType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoratoriumItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (moratoriumItemTypeCode != null ? "moratoriumItemTypeCode=" + moratoriumItemTypeCode + ", " : "") +
            (moratoriumItemType != null ? "moratoriumItemType=" + moratoriumItemType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
