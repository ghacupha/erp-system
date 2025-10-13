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
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.BouncedChequeCategories} entity. This class is used
 * in {@link io.github.erp.web.rest.BouncedChequeCategoriesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bounced-cheque-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BouncedChequeCategoriesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bouncedChequeCategoryTypeCode;

    private StringFilter bouncedChequeCategoryType;

    private Boolean distinct;

    public BouncedChequeCategoriesCriteria() {}

    public BouncedChequeCategoriesCriteria(BouncedChequeCategoriesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bouncedChequeCategoryTypeCode =
            other.bouncedChequeCategoryTypeCode == null ? null : other.bouncedChequeCategoryTypeCode.copy();
        this.bouncedChequeCategoryType = other.bouncedChequeCategoryType == null ? null : other.bouncedChequeCategoryType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BouncedChequeCategoriesCriteria copy() {
        return new BouncedChequeCategoriesCriteria(this);
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

    public StringFilter getBouncedChequeCategoryTypeCode() {
        return bouncedChequeCategoryTypeCode;
    }

    public StringFilter bouncedChequeCategoryTypeCode() {
        if (bouncedChequeCategoryTypeCode == null) {
            bouncedChequeCategoryTypeCode = new StringFilter();
        }
        return bouncedChequeCategoryTypeCode;
    }

    public void setBouncedChequeCategoryTypeCode(StringFilter bouncedChequeCategoryTypeCode) {
        this.bouncedChequeCategoryTypeCode = bouncedChequeCategoryTypeCode;
    }

    public StringFilter getBouncedChequeCategoryType() {
        return bouncedChequeCategoryType;
    }

    public StringFilter bouncedChequeCategoryType() {
        if (bouncedChequeCategoryType == null) {
            bouncedChequeCategoryType = new StringFilter();
        }
        return bouncedChequeCategoryType;
    }

    public void setBouncedChequeCategoryType(StringFilter bouncedChequeCategoryType) {
        this.bouncedChequeCategoryType = bouncedChequeCategoryType;
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
        final BouncedChequeCategoriesCriteria that = (BouncedChequeCategoriesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bouncedChequeCategoryTypeCode, that.bouncedChequeCategoryTypeCode) &&
            Objects.equals(bouncedChequeCategoryType, that.bouncedChequeCategoryType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bouncedChequeCategoryTypeCode, bouncedChequeCategoryType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BouncedChequeCategoriesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (bouncedChequeCategoryTypeCode != null ? "bouncedChequeCategoryTypeCode=" + bouncedChequeCategoryTypeCode + ", " : "") +
            (bouncedChequeCategoryType != null ? "bouncedChequeCategoryType=" + bouncedChequeCategoryType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
