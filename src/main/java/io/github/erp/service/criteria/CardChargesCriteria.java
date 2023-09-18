package io.github.erp.service.criteria;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
 * Criteria class for the {@link io.github.erp.domain.CardCharges} entity. This class is used
 * in {@link io.github.erp.web.rest.CardChargesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /card-charges?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CardChargesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cardChargeType;

    private StringFilter cardChargeTypeName;

    private Boolean distinct;

    public CardChargesCriteria() {}

    public CardChargesCriteria(CardChargesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cardChargeType = other.cardChargeType == null ? null : other.cardChargeType.copy();
        this.cardChargeTypeName = other.cardChargeTypeName == null ? null : other.cardChargeTypeName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CardChargesCriteria copy() {
        return new CardChargesCriteria(this);
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

    public StringFilter getCardChargeType() {
        return cardChargeType;
    }

    public StringFilter cardChargeType() {
        if (cardChargeType == null) {
            cardChargeType = new StringFilter();
        }
        return cardChargeType;
    }

    public void setCardChargeType(StringFilter cardChargeType) {
        this.cardChargeType = cardChargeType;
    }

    public StringFilter getCardChargeTypeName() {
        return cardChargeTypeName;
    }

    public StringFilter cardChargeTypeName() {
        if (cardChargeTypeName == null) {
            cardChargeTypeName = new StringFilter();
        }
        return cardChargeTypeName;
    }

    public void setCardChargeTypeName(StringFilter cardChargeTypeName) {
        this.cardChargeTypeName = cardChargeTypeName;
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
        final CardChargesCriteria that = (CardChargesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cardChargeType, that.cardChargeType) &&
            Objects.equals(cardChargeTypeName, that.cardChargeTypeName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardChargeType, cardChargeTypeName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardChargesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cardChargeType != null ? "cardChargeType=" + cardChargeType + ", " : "") +
            (cardChargeTypeName != null ? "cardChargeTypeName=" + cardChargeTypeName + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
