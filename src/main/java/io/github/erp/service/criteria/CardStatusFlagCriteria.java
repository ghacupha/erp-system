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
