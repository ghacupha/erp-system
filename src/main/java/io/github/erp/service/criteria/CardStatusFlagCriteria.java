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
import io.github.erp.domain.enumeration.FlagCodes;
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
 * Criteria class for the {@link io.github.erp.domain.CardStatusFlag} entity. This class is used
 * in {@link io.github.erp.web.rest.CardStatusFlagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /card-status-flags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CardStatusFlagCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FlagCodes
     */
    public static class FlagCodesFilter extends Filter<FlagCodes> {

        public FlagCodesFilter() {}

        public FlagCodesFilter(FlagCodesFilter filter) {
            super(filter);
        }

        @Override
        public FlagCodesFilter copy() {
            return new FlagCodesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FlagCodesFilter cardStatusFlag;

    private StringFilter cardStatusFlagDescription;

    private StringFilter cardStatusFlagDetails;

    private Boolean distinct;

    public CardStatusFlagCriteria() {}

    public CardStatusFlagCriteria(CardStatusFlagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cardStatusFlag = other.cardStatusFlag == null ? null : other.cardStatusFlag.copy();
        this.cardStatusFlagDescription = other.cardStatusFlagDescription == null ? null : other.cardStatusFlagDescription.copy();
        this.cardStatusFlagDetails = other.cardStatusFlagDetails == null ? null : other.cardStatusFlagDetails.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CardStatusFlagCriteria copy() {
        return new CardStatusFlagCriteria(this);
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

    public FlagCodesFilter getCardStatusFlag() {
        return cardStatusFlag;
    }

    public FlagCodesFilter cardStatusFlag() {
        if (cardStatusFlag == null) {
            cardStatusFlag = new FlagCodesFilter();
        }
        return cardStatusFlag;
    }

    public void setCardStatusFlag(FlagCodesFilter cardStatusFlag) {
        this.cardStatusFlag = cardStatusFlag;
    }

    public StringFilter getCardStatusFlagDescription() {
        return cardStatusFlagDescription;
    }

    public StringFilter cardStatusFlagDescription() {
        if (cardStatusFlagDescription == null) {
            cardStatusFlagDescription = new StringFilter();
        }
        return cardStatusFlagDescription;
    }

    public void setCardStatusFlagDescription(StringFilter cardStatusFlagDescription) {
        this.cardStatusFlagDescription = cardStatusFlagDescription;
    }

    public StringFilter getCardStatusFlagDetails() {
        return cardStatusFlagDetails;
    }

    public StringFilter cardStatusFlagDetails() {
        if (cardStatusFlagDetails == null) {
            cardStatusFlagDetails = new StringFilter();
        }
        return cardStatusFlagDetails;
    }

    public void setCardStatusFlagDetails(StringFilter cardStatusFlagDetails) {
        this.cardStatusFlagDetails = cardStatusFlagDetails;
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
        final CardStatusFlagCriteria that = (CardStatusFlagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cardStatusFlag, that.cardStatusFlag) &&
            Objects.equals(cardStatusFlagDescription, that.cardStatusFlagDescription) &&
            Objects.equals(cardStatusFlagDetails, that.cardStatusFlagDetails) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardStatusFlag, cardStatusFlagDescription, cardStatusFlagDetails, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardStatusFlagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cardStatusFlag != null ? "cardStatusFlag=" + cardStatusFlag + ", " : "") +
            (cardStatusFlagDescription != null ? "cardStatusFlagDescription=" + cardStatusFlagDescription + ", " : "") +
            (cardStatusFlagDetails != null ? "cardStatusFlagDetails=" + cardStatusFlagDetails + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
