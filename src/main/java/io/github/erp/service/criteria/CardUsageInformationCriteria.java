package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
 * Criteria class for the {@link io.github.erp.domain.CardUsageInformation} entity. This class is used
 * in {@link io.github.erp.web.rest.CardUsageInformationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /card-usage-informations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CardUsageInformationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private IntegerFilter totalNumberOfLiveCards;

    private IntegerFilter totalActiveCards;

    private IntegerFilter totalNumberOfTransactionsDone;

    private BigDecimalFilter totalValueOfTransactionsDoneInLCY;

    private LongFilter bankCodeId;

    private LongFilter cardTypeId;

    private LongFilter cardBrandId;

    private LongFilter cardCategoryTypeId;

    private LongFilter transactionTypeId;

    private LongFilter channelTypeId;

    private LongFilter cardStateId;

    private Boolean distinct;

    public CardUsageInformationCriteria() {}

    public CardUsageInformationCriteria(CardUsageInformationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.totalNumberOfLiveCards = other.totalNumberOfLiveCards == null ? null : other.totalNumberOfLiveCards.copy();
        this.totalActiveCards = other.totalActiveCards == null ? null : other.totalActiveCards.copy();
        this.totalNumberOfTransactionsDone =
            other.totalNumberOfTransactionsDone == null ? null : other.totalNumberOfTransactionsDone.copy();
        this.totalValueOfTransactionsDoneInLCY =
            other.totalValueOfTransactionsDoneInLCY == null ? null : other.totalValueOfTransactionsDoneInLCY.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.cardTypeId = other.cardTypeId == null ? null : other.cardTypeId.copy();
        this.cardBrandId = other.cardBrandId == null ? null : other.cardBrandId.copy();
        this.cardCategoryTypeId = other.cardCategoryTypeId == null ? null : other.cardCategoryTypeId.copy();
        this.transactionTypeId = other.transactionTypeId == null ? null : other.transactionTypeId.copy();
        this.channelTypeId = other.channelTypeId == null ? null : other.channelTypeId.copy();
        this.cardStateId = other.cardStateId == null ? null : other.cardStateId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CardUsageInformationCriteria copy() {
        return new CardUsageInformationCriteria(this);
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

    public IntegerFilter getTotalNumberOfLiveCards() {
        return totalNumberOfLiveCards;
    }

    public IntegerFilter totalNumberOfLiveCards() {
        if (totalNumberOfLiveCards == null) {
            totalNumberOfLiveCards = new IntegerFilter();
        }
        return totalNumberOfLiveCards;
    }

    public void setTotalNumberOfLiveCards(IntegerFilter totalNumberOfLiveCards) {
        this.totalNumberOfLiveCards = totalNumberOfLiveCards;
    }

    public IntegerFilter getTotalActiveCards() {
        return totalActiveCards;
    }

    public IntegerFilter totalActiveCards() {
        if (totalActiveCards == null) {
            totalActiveCards = new IntegerFilter();
        }
        return totalActiveCards;
    }

    public void setTotalActiveCards(IntegerFilter totalActiveCards) {
        this.totalActiveCards = totalActiveCards;
    }

    public IntegerFilter getTotalNumberOfTransactionsDone() {
        return totalNumberOfTransactionsDone;
    }

    public IntegerFilter totalNumberOfTransactionsDone() {
        if (totalNumberOfTransactionsDone == null) {
            totalNumberOfTransactionsDone = new IntegerFilter();
        }
        return totalNumberOfTransactionsDone;
    }

    public void setTotalNumberOfTransactionsDone(IntegerFilter totalNumberOfTransactionsDone) {
        this.totalNumberOfTransactionsDone = totalNumberOfTransactionsDone;
    }

    public BigDecimalFilter getTotalValueOfTransactionsDoneInLCY() {
        return totalValueOfTransactionsDoneInLCY;
    }

    public BigDecimalFilter totalValueOfTransactionsDoneInLCY() {
        if (totalValueOfTransactionsDoneInLCY == null) {
            totalValueOfTransactionsDoneInLCY = new BigDecimalFilter();
        }
        return totalValueOfTransactionsDoneInLCY;
    }

    public void setTotalValueOfTransactionsDoneInLCY(BigDecimalFilter totalValueOfTransactionsDoneInLCY) {
        this.totalValueOfTransactionsDoneInLCY = totalValueOfTransactionsDoneInLCY;
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

    public LongFilter getCardCategoryTypeId() {
        return cardCategoryTypeId;
    }

    public LongFilter cardCategoryTypeId() {
        if (cardCategoryTypeId == null) {
            cardCategoryTypeId = new LongFilter();
        }
        return cardCategoryTypeId;
    }

    public void setCardCategoryTypeId(LongFilter cardCategoryTypeId) {
        this.cardCategoryTypeId = cardCategoryTypeId;
    }

    public LongFilter getTransactionTypeId() {
        return transactionTypeId;
    }

    public LongFilter transactionTypeId() {
        if (transactionTypeId == null) {
            transactionTypeId = new LongFilter();
        }
        return transactionTypeId;
    }

    public void setTransactionTypeId(LongFilter transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public LongFilter getChannelTypeId() {
        return channelTypeId;
    }

    public LongFilter channelTypeId() {
        if (channelTypeId == null) {
            channelTypeId = new LongFilter();
        }
        return channelTypeId;
    }

    public void setChannelTypeId(LongFilter channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public LongFilter getCardStateId() {
        return cardStateId;
    }

    public LongFilter cardStateId() {
        if (cardStateId == null) {
            cardStateId = new LongFilter();
        }
        return cardStateId;
    }

    public void setCardStateId(LongFilter cardStateId) {
        this.cardStateId = cardStateId;
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
        final CardUsageInformationCriteria that = (CardUsageInformationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(totalNumberOfLiveCards, that.totalNumberOfLiveCards) &&
            Objects.equals(totalActiveCards, that.totalActiveCards) &&
            Objects.equals(totalNumberOfTransactionsDone, that.totalNumberOfTransactionsDone) &&
            Objects.equals(totalValueOfTransactionsDoneInLCY, that.totalValueOfTransactionsDoneInLCY) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(cardTypeId, that.cardTypeId) &&
            Objects.equals(cardBrandId, that.cardBrandId) &&
            Objects.equals(cardCategoryTypeId, that.cardCategoryTypeId) &&
            Objects.equals(transactionTypeId, that.transactionTypeId) &&
            Objects.equals(channelTypeId, that.channelTypeId) &&
            Objects.equals(cardStateId, that.cardStateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportingDate,
            totalNumberOfLiveCards,
            totalActiveCards,
            totalNumberOfTransactionsDone,
            totalValueOfTransactionsDoneInLCY,
            bankCodeId,
            cardTypeId,
            cardBrandId,
            cardCategoryTypeId,
            transactionTypeId,
            channelTypeId,
            cardStateId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardUsageInformationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (totalNumberOfLiveCards != null ? "totalNumberOfLiveCards=" + totalNumberOfLiveCards + ", " : "") +
            (totalActiveCards != null ? "totalActiveCards=" + totalActiveCards + ", " : "") +
            (totalNumberOfTransactionsDone != null ? "totalNumberOfTransactionsDone=" + totalNumberOfTransactionsDone + ", " : "") +
            (totalValueOfTransactionsDoneInLCY != null ? "totalValueOfTransactionsDoneInLCY=" + totalValueOfTransactionsDoneInLCY + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (cardTypeId != null ? "cardTypeId=" + cardTypeId + ", " : "") +
            (cardBrandId != null ? "cardBrandId=" + cardBrandId + ", " : "") +
            (cardCategoryTypeId != null ? "cardCategoryTypeId=" + cardCategoryTypeId + ", " : "") +
            (transactionTypeId != null ? "transactionTypeId=" + transactionTypeId + ", " : "") +
            (channelTypeId != null ? "channelTypeId=" + channelTypeId + ", " : "") +
            (cardStateId != null ? "cardStateId=" + cardStateId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
