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
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.CardIssuerCharges} entity. This class is used
 * in {@link io.github.erp.web.rest.CardIssuerChargesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /card-issuer-charges?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CardIssuerChargesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private BigDecimalFilter cardFeeChargeInLCY;

    private LongFilter bankCodeId;

    private LongFilter cardCategoryId;

    private LongFilter cardTypeId;

    private LongFilter cardBrandId;

    private LongFilter cardClassId;

    private LongFilter cardChargeTypeId;

    private Boolean distinct;

    public CardIssuerChargesCriteria() {}

    public CardIssuerChargesCriteria(CardIssuerChargesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.cardFeeChargeInLCY = other.cardFeeChargeInLCY == null ? null : other.cardFeeChargeInLCY.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.cardCategoryId = other.cardCategoryId == null ? null : other.cardCategoryId.copy();
        this.cardTypeId = other.cardTypeId == null ? null : other.cardTypeId.copy();
        this.cardBrandId = other.cardBrandId == null ? null : other.cardBrandId.copy();
        this.cardClassId = other.cardClassId == null ? null : other.cardClassId.copy();
        this.cardChargeTypeId = other.cardChargeTypeId == null ? null : other.cardChargeTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CardIssuerChargesCriteria copy() {
        return new CardIssuerChargesCriteria(this);
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

    public LocalDateFilter getReportingDate() {
        return reportingDate;
    }

    public LocalDateFilter reportingDate() {
        if (reportingDate == null) {
            reportingDate = new LocalDateFilter();
        }
        return reportingDate;
    }

    public void setReportingDate(LocalDateFilter reportingDate) {
        this.reportingDate = reportingDate;
    }

    public BigDecimalFilter getCardFeeChargeInLCY() {
        return cardFeeChargeInLCY;
    }

    public BigDecimalFilter cardFeeChargeInLCY() {
        if (cardFeeChargeInLCY == null) {
            cardFeeChargeInLCY = new BigDecimalFilter();
        }
        return cardFeeChargeInLCY;
    }

    public void setCardFeeChargeInLCY(BigDecimalFilter cardFeeChargeInLCY) {
        this.cardFeeChargeInLCY = cardFeeChargeInLCY;
    }

    public LongFilter getBankCodeId() {
        return bankCodeId;
    }

    public LongFilter bankCodeId() {
        if (bankCodeId == null) {
            bankCodeId = new LongFilter();
        }
        return bankCodeId;
    }

    public void setBankCodeId(LongFilter bankCodeId) {
        this.bankCodeId = bankCodeId;
    }

    public LongFilter getCardCategoryId() {
        return cardCategoryId;
    }

    public LongFilter cardCategoryId() {
        if (cardCategoryId == null) {
            cardCategoryId = new LongFilter();
        }
        return cardCategoryId;
    }

    public void setCardCategoryId(LongFilter cardCategoryId) {
        this.cardCategoryId = cardCategoryId;
    }

    public LongFilter getCardTypeId() {
        return cardTypeId;
    }

    public LongFilter cardTypeId() {
        if (cardTypeId == null) {
            cardTypeId = new LongFilter();
        }
        return cardTypeId;
    }

    public void setCardTypeId(LongFilter cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public LongFilter getCardBrandId() {
        return cardBrandId;
    }

    public LongFilter cardBrandId() {
        if (cardBrandId == null) {
            cardBrandId = new LongFilter();
        }
        return cardBrandId;
    }

    public void setCardBrandId(LongFilter cardBrandId) {
        this.cardBrandId = cardBrandId;
    }

    public LongFilter getCardClassId() {
        return cardClassId;
    }

    public LongFilter cardClassId() {
        if (cardClassId == null) {
            cardClassId = new LongFilter();
        }
        return cardClassId;
    }

    public void setCardClassId(LongFilter cardClassId) {
        this.cardClassId = cardClassId;
    }

    public LongFilter getCardChargeTypeId() {
        return cardChargeTypeId;
    }

    public LongFilter cardChargeTypeId() {
        if (cardChargeTypeId == null) {
            cardChargeTypeId = new LongFilter();
        }
        return cardChargeTypeId;
    }

    public void setCardChargeTypeId(LongFilter cardChargeTypeId) {
        this.cardChargeTypeId = cardChargeTypeId;
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
        final CardIssuerChargesCriteria that = (CardIssuerChargesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(cardFeeChargeInLCY, that.cardFeeChargeInLCY) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(cardCategoryId, that.cardCategoryId) &&
            Objects.equals(cardTypeId, that.cardTypeId) &&
            Objects.equals(cardBrandId, that.cardBrandId) &&
            Objects.equals(cardClassId, that.cardClassId) &&
            Objects.equals(cardChargeTypeId, that.cardChargeTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportingDate,
            cardFeeChargeInLCY,
            bankCodeId,
            cardCategoryId,
            cardTypeId,
            cardBrandId,
            cardClassId,
            cardChargeTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardIssuerChargesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (cardFeeChargeInLCY != null ? "cardFeeChargeInLCY=" + cardFeeChargeInLCY + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (cardCategoryId != null ? "cardCategoryId=" + cardCategoryId + ", " : "") +
            (cardTypeId != null ? "cardTypeId=" + cardTypeId + ", " : "") +
            (cardBrandId != null ? "cardBrandId=" + cardBrandId + ", " : "") +
            (cardClassId != null ? "cardClassId=" + cardClassId + ", " : "") +
            (cardChargeTypeId != null ? "cardChargeTypeId=" + cardChargeTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
