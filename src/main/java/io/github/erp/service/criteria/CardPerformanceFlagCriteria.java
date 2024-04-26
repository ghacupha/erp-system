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
import io.github.erp.domain.enumeration.CardPerformanceFlags;
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
 * Criteria class for the {@link io.github.erp.domain.CardPerformanceFlag} entity. This class is used
 * in {@link io.github.erp.web.rest.CardPerformanceFlagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /card-performance-flags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CardPerformanceFlagCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CardPerformanceFlags
     */
    public static class CardPerformanceFlagsFilter extends Filter<CardPerformanceFlags> {

        public CardPerformanceFlagsFilter() {}

        public CardPerformanceFlagsFilter(CardPerformanceFlagsFilter filter) {
            super(filter);
        }

        @Override
        public CardPerformanceFlagsFilter copy() {
            return new CardPerformanceFlagsFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private CardPerformanceFlagsFilter cardPerformanceFlag;

    private StringFilter cardPerformanceFlagDescription;

    private Boolean distinct;

    public CardPerformanceFlagCriteria() {}

    public CardPerformanceFlagCriteria(CardPerformanceFlagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cardPerformanceFlag = other.cardPerformanceFlag == null ? null : other.cardPerformanceFlag.copy();
        this.cardPerformanceFlagDescription =
            other.cardPerformanceFlagDescription == null ? null : other.cardPerformanceFlagDescription.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CardPerformanceFlagCriteria copy() {
        return new CardPerformanceFlagCriteria(this);
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

    public CardPerformanceFlagsFilter getCardPerformanceFlag() {
        return cardPerformanceFlag;
    }

    public CardPerformanceFlagsFilter cardPerformanceFlag() {
        if (cardPerformanceFlag == null) {
            cardPerformanceFlag = new CardPerformanceFlagsFilter();
        }
        return cardPerformanceFlag;
    }

    public void setCardPerformanceFlag(CardPerformanceFlagsFilter cardPerformanceFlag) {
        this.cardPerformanceFlag = cardPerformanceFlag;
    }

    public StringFilter getCardPerformanceFlagDescription() {
        return cardPerformanceFlagDescription;
    }

    public StringFilter cardPerformanceFlagDescription() {
        if (cardPerformanceFlagDescription == null) {
            cardPerformanceFlagDescription = new StringFilter();
        }
        return cardPerformanceFlagDescription;
    }

    public void setCardPerformanceFlagDescription(StringFilter cardPerformanceFlagDescription) {
        this.cardPerformanceFlagDescription = cardPerformanceFlagDescription;
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
        final CardPerformanceFlagCriteria that = (CardPerformanceFlagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cardPerformanceFlag, that.cardPerformanceFlag) &&
            Objects.equals(cardPerformanceFlagDescription, that.cardPerformanceFlagDescription) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardPerformanceFlag, cardPerformanceFlagDescription, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardPerformanceFlagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cardPerformanceFlag != null ? "cardPerformanceFlag=" + cardPerformanceFlag + ", " : "") +
            (cardPerformanceFlagDescription != null ? "cardPerformanceFlagDescription=" + cardPerformanceFlagDescription + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
