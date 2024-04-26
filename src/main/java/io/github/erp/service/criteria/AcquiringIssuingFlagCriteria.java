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
