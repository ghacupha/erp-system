package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
 * Criteria class for the {@link io.github.erp.domain.CardClassType} entity. This class is used
 * in {@link io.github.erp.web.rest.CardClassTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /card-class-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CardClassTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cardClassTypeCode;

    private StringFilter cardClassType;

    private Boolean distinct;

    public CardClassTypeCriteria() {}

    public CardClassTypeCriteria(CardClassTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cardClassTypeCode = other.cardClassTypeCode == null ? null : other.cardClassTypeCode.copy();
        this.cardClassType = other.cardClassType == null ? null : other.cardClassType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CardClassTypeCriteria copy() {
        return new CardClassTypeCriteria(this);
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

    public StringFilter getCardClassTypeCode() {
        return cardClassTypeCode;
    }

    public StringFilter cardClassTypeCode() {
        if (cardClassTypeCode == null) {
            cardClassTypeCode = new StringFilter();
        }
        return cardClassTypeCode;
    }

    public void setCardClassTypeCode(StringFilter cardClassTypeCode) {
        this.cardClassTypeCode = cardClassTypeCode;
    }

    public StringFilter getCardClassType() {
        return cardClassType;
    }

    public StringFilter cardClassType() {
        if (cardClassType == null) {
            cardClassType = new StringFilter();
        }
        return cardClassType;
    }

    public void setCardClassType(StringFilter cardClassType) {
        this.cardClassType = cardClassType;
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
        final CardClassTypeCriteria that = (CardClassTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cardClassTypeCode, that.cardClassTypeCode) &&
            Objects.equals(cardClassType, that.cardClassType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardClassTypeCode, cardClassType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardClassTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cardClassTypeCode != null ? "cardClassTypeCode=" + cardClassTypeCode + ", " : "") +
            (cardClassType != null ? "cardClassType=" + cardClassType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
