package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
 * A DTO for the {@link io.github.erp.domain.CardUsageInformation} entity.
 */
public class CardUsageInformationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    @Min(value = 0)
    private Integer totalNumberOfLiveCards;

    @NotNull
    @Min(value = 0)
    private Integer totalActiveCards;

    @NotNull
    @Min(value = 0)
    private Integer totalNumberOfTransactionsDone;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalValueOfTransactionsDoneInLCY;

    private InstitutionCodeDTO bankCode;

    private CardTypesDTO cardType;

    private CardBrandTypeDTO cardBrand;

    private CardCategoryTypeDTO cardCategoryType;

    private BankTransactionTypeDTO transactionType;

    private ChannelTypeDTO channelType;

    private CardStateDTO cardState;

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

    public Integer getTotalNumberOfLiveCards() {
        return totalNumberOfLiveCards;
    }

    public void setTotalNumberOfLiveCards(Integer totalNumberOfLiveCards) {
        this.totalNumberOfLiveCards = totalNumberOfLiveCards;
    }

    public Integer getTotalActiveCards() {
        return totalActiveCards;
    }

    public void setTotalActiveCards(Integer totalActiveCards) {
        this.totalActiveCards = totalActiveCards;
    }

    public Integer getTotalNumberOfTransactionsDone() {
        return totalNumberOfTransactionsDone;
    }

    public void setTotalNumberOfTransactionsDone(Integer totalNumberOfTransactionsDone) {
        this.totalNumberOfTransactionsDone = totalNumberOfTransactionsDone;
    }

    public BigDecimal getTotalValueOfTransactionsDoneInLCY() {
        return totalValueOfTransactionsDoneInLCY;
    }

    public void setTotalValueOfTransactionsDoneInLCY(BigDecimal totalValueOfTransactionsDoneInLCY) {
        this.totalValueOfTransactionsDoneInLCY = totalValueOfTransactionsDoneInLCY;
    }

    public InstitutionCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(InstitutionCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public CardTypesDTO getCardType() {
        return cardType;
    }

    public void setCardType(CardTypesDTO cardType) {
        this.cardType = cardType;
    }

    public CardBrandTypeDTO getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(CardBrandTypeDTO cardBrand) {
        this.cardBrand = cardBrand;
    }

    public CardCategoryTypeDTO getCardCategoryType() {
        return cardCategoryType;
    }

    public void setCardCategoryType(CardCategoryTypeDTO cardCategoryType) {
        this.cardCategoryType = cardCategoryType;
    }

    public BankTransactionTypeDTO getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(BankTransactionTypeDTO transactionType) {
        this.transactionType = transactionType;
    }

    public ChannelTypeDTO getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelTypeDTO channelType) {
        this.channelType = channelType;
    }

    public CardStateDTO getCardState() {
        return cardState;
    }

    public void setCardState(CardStateDTO cardState) {
        this.cardState = cardState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardUsageInformationDTO)) {
            return false;
        }

        CardUsageInformationDTO cardUsageInformationDTO = (CardUsageInformationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardUsageInformationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardUsageInformationDTO{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", totalNumberOfLiveCards=" + getTotalNumberOfLiveCards() +
            ", totalActiveCards=" + getTotalActiveCards() +
            ", totalNumberOfTransactionsDone=" + getTotalNumberOfTransactionsDone() +
            ", totalValueOfTransactionsDoneInLCY=" + getTotalValueOfTransactionsDoneInLCY() +
            ", bankCode=" + getBankCode() +
            ", cardType=" + getCardType() +
            ", cardBrand=" + getCardBrand() +
            ", cardCategoryType=" + getCardCategoryType() +
            ", transactionType=" + getTransactionType() +
            ", channelType=" + getChannelType() +
            ", cardState=" + getCardState() +
            "}";
    }
}
