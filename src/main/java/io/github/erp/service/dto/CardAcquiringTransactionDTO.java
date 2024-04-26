package io.github.erp.service.dto;

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
