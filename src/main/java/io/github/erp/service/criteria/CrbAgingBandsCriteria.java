package io.github.erp.service.criteria;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
