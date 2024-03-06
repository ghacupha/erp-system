package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
 * Criteria class for the {@link io.github.erp.domain.CardAcquiringTransaction} entity. This class is used
 * in {@link io.github.erp.web.rest.CardAcquiringTransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /card-acquiring-transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CardAcquiringTransactionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private StringFilter terminalId;

    private IntegerFilter numberOfTransactions;

    private BigDecimalFilter valueOfTransactionsInLCY;

    private LongFilter bankCodeId;

    private LongFilter channelTypeId;

    private LongFilter cardBrandTypeId;

    private LongFilter currencyOfTransactionId;

    private LongFilter cardIssuerCategoryId;

    private Boolean distinct;

    public CardAcquiringTransactionCriteria() {}

    public CardAcquiringTransactionCriteria(CardAcquiringTransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.terminalId = other.terminalId == null ? null : other.terminalId.copy();
        this.numberOfTransactions = other.numberOfTransactions == null ? null : other.numberOfTransactions.copy();
        this.valueOfTransactionsInLCY = other.valueOfTransactionsInLCY == null ? null : other.valueOfTransactionsInLCY.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.channelTypeId = other.channelTypeId == null ? null : other.channelTypeId.copy();
        this.cardBrandTypeId = other.cardBrandTypeId == null ? null : other.cardBrandTypeId.copy();
        this.currencyOfTransactionId = other.currencyOfTransactionId == null ? null : other.currencyOfTransactionId.copy();
        this.cardIssuerCategoryId = other.cardIssuerCategoryId == null ? null : other.cardIssuerCategoryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CardAcquiringTransactionCriteria copy() {
        return new CardAcquiringTransactionCriteria(this);
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

    public StringFilter getTerminalId() {
        return terminalId;
    }

    public StringFilter terminalId() {
        if (terminalId == null) {
            terminalId = new StringFilter();
        }
        return terminalId;
    }

    public void setTerminalId(StringFilter terminalId) {
        this.terminalId = terminalId;
    }

    public IntegerFilter getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public IntegerFilter numberOfTransactions() {
        if (numberOfTransactions == null) {
            numberOfTransactions = new IntegerFilter();
        }
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(IntegerFilter numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public BigDecimalFilter getValueOfTransactionsInLCY() {
        return valueOfTransactionsInLCY;
    }

    public BigDecimalFilter valueOfTransactionsInLCY() {
        if (valueOfTransactionsInLCY == null) {
            valueOfTransactionsInLCY = new BigDecimalFilter();
        }
        return valueOfTransactionsInLCY;
    }

    public void setValueOfTransactionsInLCY(BigDecimalFilter valueOfTransactionsInLCY) {
        this.valueOfTransactionsInLCY = valueOfTransactionsInLCY;
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

    public LongFilter getCardBrandTypeId() {
        return cardBrandTypeId;
    }

    public LongFilter cardBrandTypeId() {
        if (cardBrandTypeId == null) {
            cardBrandTypeId = new LongFilter();
        }
        return cardBrandTypeId;
    }

    public void setCardBrandTypeId(LongFilter cardBrandTypeId) {
        this.cardBrandTypeId = cardBrandTypeId;
    }

    public LongFilter getCurrencyOfTransactionId() {
        return currencyOfTransactionId;
    }

    public LongFilter currencyOfTransactionId() {
        if (currencyOfTransactionId == null) {
            currencyOfTransactionId = new LongFilter();
        }
        return currencyOfTransactionId;
    }

    public void setCurrencyOfTransactionId(LongFilter currencyOfTransactionId) {
        this.currencyOfTransactionId = currencyOfTransactionId;
    }

    public LongFilter getCardIssuerCategoryId() {
        return cardIssuerCategoryId;
    }

    public LongFilter cardIssuerCategoryId() {
        if (cardIssuerCategoryId == null) {
            cardIssuerCategoryId = new LongFilter();
        }
        return cardIssuerCategoryId;
    }

    public void setCardIssuerCategoryId(LongFilter cardIssuerCategoryId) {
        this.cardIssuerCategoryId = cardIssuerCategoryId;
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
        final CardAcquiringTransactionCriteria that = (CardAcquiringTransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(terminalId, that.terminalId) &&
            Objects.equals(numberOfTransactions, that.numberOfTransactions) &&
            Objects.equals(valueOfTransactionsInLCY, that.valueOfTransactionsInLCY) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(channelTypeId, that.channelTypeId) &&
            Objects.equals(cardBrandTypeId, that.cardBrandTypeId) &&
            Objects.equals(currencyOfTransactionId, that.currencyOfTransactionId) &&
            Objects.equals(cardIssuerCategoryId, that.cardIssuerCategoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportingDate,
            terminalId,
            numberOfTransactions,
            valueOfTransactionsInLCY,
            bankCodeId,
            channelTypeId,
            cardBrandTypeId,
            currencyOfTransactionId,
            cardIssuerCategoryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardAcquiringTransactionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (terminalId != null ? "terminalId=" + terminalId + ", " : "") +
            (numberOfTransactions != null ? "numberOfTransactions=" + numberOfTransactions + ", " : "") +
            (valueOfTransactionsInLCY != null ? "valueOfTransactionsInLCY=" + valueOfTransactionsInLCY + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (channelTypeId != null ? "channelTypeId=" + channelTypeId + ", " : "") +
            (cardBrandTypeId != null ? "cardBrandTypeId=" + cardBrandTypeId + ", " : "") +
            (currencyOfTransactionId != null ? "currencyOfTransactionId=" + currencyOfTransactionId + ", " : "") +
            (cardIssuerCategoryId != null ? "cardIssuerCategoryId=" + cardIssuerCategoryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
