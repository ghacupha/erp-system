package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
 * Criteria class for the {@link io.github.erp.domain.UltimateBeneficiaryCategory} entity. This class is used
 * in {@link io.github.erp.web.rest.UltimateBeneficiaryCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ultimate-beneficiary-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UltimateBeneficiaryCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ultimateBeneficiaryCategoryTypeCode;

    private StringFilter ultimateBeneficiaryType;

    private Boolean distinct;

    public UltimateBeneficiaryCategoryCriteria() {}

    public UltimateBeneficiaryCategoryCriteria(UltimateBeneficiaryCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ultimateBeneficiaryCategoryTypeCode =
            other.ultimateBeneficiaryCategoryTypeCode == null ? null : other.ultimateBeneficiaryCategoryTypeCode.copy();
        this.ultimateBeneficiaryType = other.ultimateBeneficiaryType == null ? null : other.ultimateBeneficiaryType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UltimateBeneficiaryCategoryCriteria copy() {
        return new UltimateBeneficiaryCategoryCriteria(this);
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

    public StringFilter getUltimateBeneficiaryCategoryTypeCode() {
        return ultimateBeneficiaryCategoryTypeCode;
    }

    public StringFilter ultimateBeneficiaryCategoryTypeCode() {
        if (ultimateBeneficiaryCategoryTypeCode == null) {
            ultimateBeneficiaryCategoryTypeCode = new StringFilter();
        }
        return ultimateBeneficiaryCategoryTypeCode;
    }

    public void setUltimateBeneficiaryCategoryTypeCode(StringFilter ultimateBeneficiaryCategoryTypeCode) {
        this.ultimateBeneficiaryCategoryTypeCode = ultimateBeneficiaryCategoryTypeCode;
    }

    public StringFilter getUltimateBeneficiaryType() {
        return ultimateBeneficiaryType;
    }

    public StringFilter ultimateBeneficiaryType() {
        if (ultimateBeneficiaryType == null) {
            ultimateBeneficiaryType = new StringFilter();
        }
        return ultimateBeneficiaryType;
    }

    public void setUltimateBeneficiaryType(StringFilter ultimateBeneficiaryType) {
        this.ultimateBeneficiaryType = ultimateBeneficiaryType;
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
        final UltimateBeneficiaryCategoryCriteria that = (UltimateBeneficiaryCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ultimateBeneficiaryCategoryTypeCode, that.ultimateBeneficiaryCategoryTypeCode) &&
            Objects.equals(ultimateBeneficiaryType, that.ultimateBeneficiaryType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ultimateBeneficiaryCategoryTypeCode, ultimateBeneficiaryType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UltimateBeneficiaryCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ultimateBeneficiaryCategoryTypeCode != null ? "ultimateBeneficiaryCategoryTypeCode=" + ultimateBeneficiaryCategoryTypeCode + ", " : "") +
            (ultimateBeneficiaryType != null ? "ultimateBeneficiaryType=" + ultimateBeneficiaryType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
