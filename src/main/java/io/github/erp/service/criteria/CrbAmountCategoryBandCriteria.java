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
 * Criteria class for the {@link io.github.erp.domain.CrbAmountCategoryBand} entity. This class is used
 * in {@link io.github.erp.web.rest.CrbAmountCategoryBandResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crb-amount-category-bands?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrbAmountCategoryBandCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter amountCategoryBandCode;

    private StringFilter amountCategoryBand;

    private Boolean distinct;

    public CrbAmountCategoryBandCriteria() {}

    public CrbAmountCategoryBandCriteria(CrbAmountCategoryBandCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amountCategoryBandCode = other.amountCategoryBandCode == null ? null : other.amountCategoryBandCode.copy();
        this.amountCategoryBand = other.amountCategoryBand == null ? null : other.amountCategoryBand.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CrbAmountCategoryBandCriteria copy() {
        return new CrbAmountCategoryBandCriteria(this);
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

    public StringFilter getAmountCategoryBandCode() {
        return amountCategoryBandCode;
    }

    public StringFilter amountCategoryBandCode() {
        if (amountCategoryBandCode == null) {
            amountCategoryBandCode = new StringFilter();
        }
        return amountCategoryBandCode;
    }

    public void setAmountCategoryBandCode(StringFilter amountCategoryBandCode) {
        this.amountCategoryBandCode = amountCategoryBandCode;
    }

    public StringFilter getAmountCategoryBand() {
        return amountCategoryBand;
    }

    public StringFilter amountCategoryBand() {
        if (amountCategoryBand == null) {
            amountCategoryBand = new StringFilter();
        }
        return amountCategoryBand;
    }

    public void setAmountCategoryBand(StringFilter amountCategoryBand) {
        this.amountCategoryBand = amountCategoryBand;
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
        final CrbAmountCategoryBandCriteria that = (CrbAmountCategoryBandCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(amountCategoryBandCode, that.amountCategoryBandCode) &&
            Objects.equals(amountCategoryBand, that.amountCategoryBand) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountCategoryBandCode, amountCategoryBand, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbAmountCategoryBandCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (amountCategoryBandCode != null ? "amountCategoryBandCode=" + amountCategoryBandCode + ", " : "") +
            (amountCategoryBand != null ? "amountCategoryBand=" + amountCategoryBand + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
