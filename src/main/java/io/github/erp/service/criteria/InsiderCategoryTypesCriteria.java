package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
 * Criteria class for the {@link io.github.erp.domain.InsiderCategoryTypes} entity. This class is used
 * in {@link io.github.erp.web.rest.InsiderCategoryTypesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /insider-category-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InsiderCategoryTypesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter insiderCategoryTypeCode;

    private StringFilter insiderCategoryTypeDetail;

    private Boolean distinct;

    public InsiderCategoryTypesCriteria() {}

    public InsiderCategoryTypesCriteria(InsiderCategoryTypesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.insiderCategoryTypeCode = other.insiderCategoryTypeCode == null ? null : other.insiderCategoryTypeCode.copy();
        this.insiderCategoryTypeDetail = other.insiderCategoryTypeDetail == null ? null : other.insiderCategoryTypeDetail.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InsiderCategoryTypesCriteria copy() {
        return new InsiderCategoryTypesCriteria(this);
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

    public StringFilter getInsiderCategoryTypeCode() {
        return insiderCategoryTypeCode;
    }

    public StringFilter insiderCategoryTypeCode() {
        if (insiderCategoryTypeCode == null) {
            insiderCategoryTypeCode = new StringFilter();
        }
        return insiderCategoryTypeCode;
    }

    public void setInsiderCategoryTypeCode(StringFilter insiderCategoryTypeCode) {
        this.insiderCategoryTypeCode = insiderCategoryTypeCode;
    }

    public StringFilter getInsiderCategoryTypeDetail() {
        return insiderCategoryTypeDetail;
    }

    public StringFilter insiderCategoryTypeDetail() {
        if (insiderCategoryTypeDetail == null) {
            insiderCategoryTypeDetail = new StringFilter();
        }
        return insiderCategoryTypeDetail;
    }

    public void setInsiderCategoryTypeDetail(StringFilter insiderCategoryTypeDetail) {
        this.insiderCategoryTypeDetail = insiderCategoryTypeDetail;
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
        final InsiderCategoryTypesCriteria that = (InsiderCategoryTypesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(insiderCategoryTypeCode, that.insiderCategoryTypeCode) &&
            Objects.equals(insiderCategoryTypeDetail, that.insiderCategoryTypeDetail) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, insiderCategoryTypeCode, insiderCategoryTypeDetail, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsiderCategoryTypesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (insiderCategoryTypeCode != null ? "insiderCategoryTypeCode=" + insiderCategoryTypeCode + ", " : "") +
            (insiderCategoryTypeDetail != null ? "insiderCategoryTypeDetail=" + insiderCategoryTypeDetail + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
