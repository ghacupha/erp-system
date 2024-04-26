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
