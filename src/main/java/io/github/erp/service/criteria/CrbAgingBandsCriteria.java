package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
 * Criteria class for the {@link io.github.erp.domain.CrbAgingBands} entity. This class is used
 * in {@link io.github.erp.web.rest.CrbAgingBandsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crb-aging-bands?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrbAgingBandsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter agingBandCategoryCode;

    private StringFilter agingBandCategory;

    private StringFilter agingBandCategoryDetails;

    private Boolean distinct;

    public CrbAgingBandsCriteria() {}

    public CrbAgingBandsCriteria(CrbAgingBandsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.agingBandCategoryCode = other.agingBandCategoryCode == null ? null : other.agingBandCategoryCode.copy();
        this.agingBandCategory = other.agingBandCategory == null ? null : other.agingBandCategory.copy();
        this.agingBandCategoryDetails = other.agingBandCategoryDetails == null ? null : other.agingBandCategoryDetails.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CrbAgingBandsCriteria copy() {
        return new CrbAgingBandsCriteria(this);
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

    public StringFilter getAgingBandCategoryCode() {
        return agingBandCategoryCode;
    }

    public StringFilter agingBandCategoryCode() {
        if (agingBandCategoryCode == null) {
            agingBandCategoryCode = new StringFilter();
        }
        return agingBandCategoryCode;
    }

    public void setAgingBandCategoryCode(StringFilter agingBandCategoryCode) {
        this.agingBandCategoryCode = agingBandCategoryCode;
    }

    public StringFilter getAgingBandCategory() {
        return agingBandCategory;
    }

    public StringFilter agingBandCategory() {
        if (agingBandCategory == null) {
            agingBandCategory = new StringFilter();
        }
        return agingBandCategory;
    }

    public void setAgingBandCategory(StringFilter agingBandCategory) {
        this.agingBandCategory = agingBandCategory;
    }

    public StringFilter getAgingBandCategoryDetails() {
        return agingBandCategoryDetails;
    }

    public StringFilter agingBandCategoryDetails() {
        if (agingBandCategoryDetails == null) {
            agingBandCategoryDetails = new StringFilter();
        }
        return agingBandCategoryDetails;
    }

    public void setAgingBandCategoryDetails(StringFilter agingBandCategoryDetails) {
        this.agingBandCategoryDetails = agingBandCategoryDetails;
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
        final CrbAgingBandsCriteria that = (CrbAgingBandsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(agingBandCategoryCode, that.agingBandCategoryCode) &&
            Objects.equals(agingBandCategory, that.agingBandCategory) &&
            Objects.equals(agingBandCategoryDetails, that.agingBandCategoryDetails) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agingBandCategoryCode, agingBandCategory, agingBandCategoryDetails, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbAgingBandsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (agingBandCategoryCode != null ? "agingBandCategoryCode=" + agingBandCategoryCode + ", " : "") +
            (agingBandCategory != null ? "agingBandCategory=" + agingBandCategory + ", " : "") +
            (agingBandCategoryDetails != null ? "agingBandCategoryDetails=" + agingBandCategoryDetails + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
