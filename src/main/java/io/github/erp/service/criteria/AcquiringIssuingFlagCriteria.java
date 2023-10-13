package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
 * Criteria class for the {@link io.github.erp.domain.AcquiringIssuingFlag} entity. This class is used
 * in {@link io.github.erp.web.rest.AcquiringIssuingFlagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /acquiring-issuing-flags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AcquiringIssuingFlagCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cardAcquiringIssuingFlagCode;

    private StringFilter cardAcquiringIssuingDescription;

    private Boolean distinct;

    public AcquiringIssuingFlagCriteria() {}

    public AcquiringIssuingFlagCriteria(AcquiringIssuingFlagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cardAcquiringIssuingFlagCode = other.cardAcquiringIssuingFlagCode == null ? null : other.cardAcquiringIssuingFlagCode.copy();
        this.cardAcquiringIssuingDescription =
            other.cardAcquiringIssuingDescription == null ? null : other.cardAcquiringIssuingDescription.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AcquiringIssuingFlagCriteria copy() {
        return new AcquiringIssuingFlagCriteria(this);
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

    public StringFilter getCardAcquiringIssuingFlagCode() {
        return cardAcquiringIssuingFlagCode;
    }

    public StringFilter cardAcquiringIssuingFlagCode() {
        if (cardAcquiringIssuingFlagCode == null) {
            cardAcquiringIssuingFlagCode = new StringFilter();
        }
        return cardAcquiringIssuingFlagCode;
    }

    public void setCardAcquiringIssuingFlagCode(StringFilter cardAcquiringIssuingFlagCode) {
        this.cardAcquiringIssuingFlagCode = cardAcquiringIssuingFlagCode;
    }

    public StringFilter getCardAcquiringIssuingDescription() {
        return cardAcquiringIssuingDescription;
    }

    public StringFilter cardAcquiringIssuingDescription() {
        if (cardAcquiringIssuingDescription == null) {
            cardAcquiringIssuingDescription = new StringFilter();
        }
        return cardAcquiringIssuingDescription;
    }

    public void setCardAcquiringIssuingDescription(StringFilter cardAcquiringIssuingDescription) {
        this.cardAcquiringIssuingDescription = cardAcquiringIssuingDescription;
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
        final AcquiringIssuingFlagCriteria that = (AcquiringIssuingFlagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cardAcquiringIssuingFlagCode, that.cardAcquiringIssuingFlagCode) &&
            Objects.equals(cardAcquiringIssuingDescription, that.cardAcquiringIssuingDescription) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardAcquiringIssuingFlagCode, cardAcquiringIssuingDescription, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcquiringIssuingFlagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cardAcquiringIssuingFlagCode != null ? "cardAcquiringIssuingFlagCode=" + cardAcquiringIssuingFlagCode + ", " : "") +
            (cardAcquiringIssuingDescription != null ? "cardAcquiringIssuingDescription=" + cardAcquiringIssuingDescription + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
