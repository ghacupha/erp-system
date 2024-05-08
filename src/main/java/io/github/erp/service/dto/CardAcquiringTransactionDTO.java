package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CardAcquiringTransaction} entity.
 */
public class CardAcquiringTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    private String terminalId;

    @NotNull
    @Min(value = 0)
    private Integer numberOfTransactions;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal valueOfTransactionsInLCY;

    private InstitutionCodeDTO bankCode;

    private ChannelTypeDTO channelType;

    private CardBrandTypeDTO cardBrandType;

    private IsoCurrencyCodeDTO currencyOfTransaction;

    private CardCategoryTypeDTO cardIssuerCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Integer getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(Integer numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public BigDecimal getValueOfTransactionsInLCY() {
        return valueOfTransactionsInLCY;
    }

    public void setValueOfTransactionsInLCY(BigDecimal valueOfTransactionsInLCY) {
        this.valueOfTransactionsInLCY = valueOfTransactionsInLCY;
    }

    public InstitutionCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(InstitutionCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public ChannelTypeDTO getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelTypeDTO channelType) {
        this.channelType = channelType;
    }

    public CardBrandTypeDTO getCardBrandType() {
        return cardBrandType;
    }

    public void setCardBrandType(CardBrandTypeDTO cardBrandType) {
        this.cardBrandType = cardBrandType;
    }

    public IsoCurrencyCodeDTO getCurrencyOfTransaction() {
        return currencyOfTransaction;
    }

    public void setCurrencyOfTransaction(IsoCurrencyCodeDTO currencyOfTransaction) {
        this.currencyOfTransaction = currencyOfTransaction;
    }

    public CardCategoryTypeDTO getCardIssuerCategory() {
        return cardIssuerCategory;
    }

    public void setCardIssuerCategory(CardCategoryTypeDTO cardIssuerCategory) {
        this.cardIssuerCategory = cardIssuerCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardAcquiringTransactionDTO)) {
            return false;
        }

        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = (CardAcquiringTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardAcquiringTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardAcquiringTransactionDTO{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", terminalId='" + getTerminalId() + "'" +
            ", numberOfTransactions=" + getNumberOfTransactions() +
            ", valueOfTransactionsInLCY=" + getValueOfTransactionsInLCY() +
            ", bankCode=" + getBankCode() +
            ", channelType=" + getChannelType() +
            ", cardBrandType=" + getCardBrandType() +
            ", currencyOfTransaction=" + getCurrencyOfTransaction() +
            ", cardIssuerCategory=" + getCardIssuerCategory() +
            "}";
    }
}
