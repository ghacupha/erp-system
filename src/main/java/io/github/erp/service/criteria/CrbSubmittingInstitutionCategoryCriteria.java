package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
 * Criteria class for the {@link io.github.erp.domain.CrbSubmittingInstitutionCategory} entity. This class is used
 * in {@link io.github.erp.web.rest.CrbSubmittingInstitutionCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crb-submitting-institution-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrbSubmittingInstitutionCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter submittingInstitutionCategoryTypeCode;

    private StringFilter submittingInstitutionCategoryType;

    private Boolean distinct;

    public CrbSubmittingInstitutionCategoryCriteria() {}

    public CrbSubmittingInstitutionCategoryCriteria(CrbSubmittingInstitutionCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.submittingInstitutionCategoryTypeCode =
            other.submittingInstitutionCategoryTypeCode == null ? null : other.submittingInstitutionCategoryTypeCode.copy();
        this.submittingInstitutionCategoryType =
            other.submittingInstitutionCategoryType == null ? null : other.submittingInstitutionCategoryType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CrbSubmittingInstitutionCategoryCriteria copy() {
        return new CrbSubmittingInstitutionCategoryCriteria(this);
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

    public StringFilter getSubmittingInstitutionCategoryTypeCode() {
        return submittingInstitutionCategoryTypeCode;
    }

    public StringFilter submittingInstitutionCategoryTypeCode() {
        if (submittingInstitutionCategoryTypeCode == null) {
            submittingInstitutionCategoryTypeCode = new StringFilter();
        }
        return submittingInstitutionCategoryTypeCode;
    }

    public void setSubmittingInstitutionCategoryTypeCode(StringFilter submittingInstitutionCategoryTypeCode) {
        this.submittingInstitutionCategoryTypeCode = submittingInstitutionCategoryTypeCode;
    }

    public StringFilter getSubmittingInstitutionCategoryType() {
        return submittingInstitutionCategoryType;
    }

    public StringFilter submittingInstitutionCategoryType() {
        if (submittingInstitutionCategoryType == null) {
            submittingInstitutionCategoryType = new StringFilter();
        }
        return submittingInstitutionCategoryType;
    }

    public void setSubmittingInstitutionCategoryType(StringFilter submittingInstitutionCategoryType) {
        this.submittingInstitutionCategoryType = submittingInstitutionCategoryType;
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
        final CrbSubmittingInstitutionCategoryCriteria that = (CrbSubmittingInstitutionCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(submittingInstitutionCategoryTypeCode, that.submittingInstitutionCategoryTypeCode) &&
            Objects.equals(submittingInstitutionCategoryType, that.submittingInstitutionCategoryType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, submittingInstitutionCategoryTypeCode, submittingInstitutionCategoryType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbSubmittingInstitutionCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (submittingInstitutionCategoryTypeCode != null ? "submittingInstitutionCategoryTypeCode=" + submittingInstitutionCategoryTypeCode + ", " : "") +
            (submittingInstitutionCategoryType != null ? "submittingInstitutionCategoryType=" + submittingInstitutionCategoryType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
