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
import io.github.erp.domain.enumeration.CardStateFlagTypes;
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
 * Criteria class for the {@link io.github.erp.domain.CardState} entity. This class is used
 * in {@link io.github.erp.web.rest.CardStateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /card-states?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CardStateCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CardStateFlagTypes
     */
    public static class CardStateFlagTypesFilter extends Filter<CardStateFlagTypes> {

        public CardStateFlagTypesFilter() {}

        public CardStateFlagTypesFilter(CardStateFlagTypesFilter filter) {
            super(filter);
        }

        @Override
        public CardStateFlagTypesFilter copy() {
            return new CardStateFlagTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private CardStateFlagTypesFilter cardStateFlag;

    private StringFilter cardStateFlagDetails;

    private StringFilter cardStateFlagDescription;

    private Boolean distinct;

    public CardStateCriteria() {}

    public CardStateCriteria(CardStateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cardStateFlag = other.cardStateFlag == null ? null : other.cardStateFlag.copy();
        this.cardStateFlagDetails = other.cardStateFlagDetails == null ? null : other.cardStateFlagDetails.copy();
        this.cardStateFlagDescription = other.cardStateFlagDescription == null ? null : other.cardStateFlagDescription.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CardStateCriteria copy() {
        return new CardStateCriteria(this);
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

    public CardStateFlagTypesFilter getCardStateFlag() {
        return cardStateFlag;
    }

    public CardStateFlagTypesFilter cardStateFlag() {
        if (cardStateFlag == null) {
            cardStateFlag = new CardStateFlagTypesFilter();
        }
        return cardStateFlag;
    }

    public void setCardStateFlag(CardStateFlagTypesFilter cardStateFlag) {
        this.cardStateFlag = cardStateFlag;
    }

    public StringFilter getCardStateFlagDetails() {
        return cardStateFlagDetails;
    }

    public StringFilter cardStateFlagDetails() {
        if (cardStateFlagDetails == null) {
            cardStateFlagDetails = new StringFilter();
        }
        return cardStateFlagDetails;
    }

    public void setCardStateFlagDetails(StringFilter cardStateFlagDetails) {
        this.cardStateFlagDetails = cardStateFlagDetails;
    }

    public StringFilter getCardStateFlagDescription() {
        return cardStateFlagDescription;
    }

    public StringFilter cardStateFlagDescription() {
        if (cardStateFlagDescription == null) {
            cardStateFlagDescription = new StringFilter();
        }
        return cardStateFlagDescription;
    }

    public void setCardStateFlagDescription(StringFilter cardStateFlagDescription) {
        this.cardStateFlagDescription = cardStateFlagDescription;
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
        final CardStateCriteria that = (CardStateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cardStateFlag, that.cardStateFlag) &&
            Objects.equals(cardStateFlagDetails, that.cardStateFlagDetails) &&
            Objects.equals(cardStateFlagDescription, that.cardStateFlagDescription) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardStateFlag, cardStateFlagDetails, cardStateFlagDescription, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardStateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cardStateFlag != null ? "cardStateFlag=" + cardStateFlag + ", " : "") +
            (cardStateFlagDetails != null ? "cardStateFlagDetails=" + cardStateFlagDetails + ", " : "") +
            (cardStateFlagDescription != null ? "cardStateFlagDescription=" + cardStateFlagDescription + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
