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
 * Criteria class for the {@link io.github.erp.domain.CrbDataSubmittingInstitutions} entity. This class is used
 * in {@link io.github.erp.web.rest.CrbDataSubmittingInstitutionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crb-data-submitting-institutions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrbDataSubmittingInstitutionsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter institutionCode;

    private StringFilter institutionName;

    private StringFilter institutionCategory;

    private Boolean distinct;

    public CrbDataSubmittingInstitutionsCriteria() {}

    public CrbDataSubmittingInstitutionsCriteria(CrbDataSubmittingInstitutionsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.institutionCode = other.institutionCode == null ? null : other.institutionCode.copy();
        this.institutionName = other.institutionName == null ? null : other.institutionName.copy();
        this.institutionCategory = other.institutionCategory == null ? null : other.institutionCategory.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CrbDataSubmittingInstitutionsCriteria copy() {
        return new CrbDataSubmittingInstitutionsCriteria(this);
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

    public StringFilter getInstitutionCode() {
        return institutionCode;
    }

    public StringFilter institutionCode() {
        if (institutionCode == null) {
            institutionCode = new StringFilter();
        }
        return institutionCode;
    }

    public void setInstitutionCode(StringFilter institutionCode) {
        this.institutionCode = institutionCode;
    }

    public StringFilter getInstitutionName() {
        return institutionName;
    }

    public StringFilter institutionName() {
        if (institutionName == null) {
            institutionName = new StringFilter();
        }
        return institutionName;
    }

    public void setInstitutionName(StringFilter institutionName) {
        this.institutionName = institutionName;
    }

    public StringFilter getInstitutionCategory() {
        return institutionCategory;
    }

    public StringFilter institutionCategory() {
        if (institutionCategory == null) {
            institutionCategory = new StringFilter();
        }
        return institutionCategory;
    }

    public void setInstitutionCategory(StringFilter institutionCategory) {
        this.institutionCategory = institutionCategory;
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
        final CrbDataSubmittingInstitutionsCriteria that = (CrbDataSubmittingInstitutionsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(institutionCode, that.institutionCode) &&
            Objects.equals(institutionName, that.institutionName) &&
            Objects.equals(institutionCategory, that.institutionCategory) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, institutionCode, institutionName, institutionCategory, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbDataSubmittingInstitutionsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (institutionCode != null ? "institutionCode=" + institutionCode + ", " : "") +
            (institutionName != null ? "institutionName=" + institutionName + ", " : "") +
            (institutionCategory != null ? "institutionCategory=" + institutionCategory + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
