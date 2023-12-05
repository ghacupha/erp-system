package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CardUsageInformation.
 */
@Entity
@Table(name = "card_usage_information")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardusageinformation")
public class CardUsageInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reporting_date", nullable = false)
    private LocalDate reportingDate;

    @NotNull
    @Min(value = 0)
    @Column(name = "total_number_of_live_cards", nullable = false)
    private Integer totalNumberOfLiveCards;

    @NotNull
    @Min(value = 0)
    @Column(name = "total_active_cards", nullable = false)
    private Integer totalActiveCards;

    @NotNull
    @Min(value = 0)
    @Column(name = "total_number_of_transactions_done", nullable = false)
    private Integer totalNumberOfTransactionsDone;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_value_of_transactions_done_in_lcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalValueOfTransactionsDoneInLCY;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankCode;

    @ManyToOne(optional = false)
    @NotNull
    private CardTypes cardType;

    @ManyToOne(optional = false)
    @NotNull
    private CardBrandType cardBrand;

    @ManyToOne(optional = false)
    @NotNull
    private CardCategoryType cardCategoryType;

    @ManyToOne(optional = false)
    @NotNull
    private BankTransactionType transactionType;

    @ManyToOne(optional = false)
    @NotNull
    private ChannelType channelType;

    @ManyToOne(optional = false)
    @NotNull
    private CardState cardState;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardUsageInformation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public CardUsageInformation reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public Integer getTotalNumberOfLiveCards() {
        return this.totalNumberOfLiveCards;
    }

    public CardUsageInformation totalNumberOfLiveCards(Integer totalNumberOfLiveCards) {
        this.setTotalNumberOfLiveCards(totalNumberOfLiveCards);
        return this;
    }

    public void setTotalNumberOfLiveCards(Integer totalNumberOfLiveCards) {
        this.totalNumberOfLiveCards = totalNumberOfLiveCards;
    }

    public Integer getTotalActiveCards() {
        return this.totalActiveCards;
    }

    public CardUsageInformation totalActiveCards(Integer totalActiveCards) {
        this.setTotalActiveCards(totalActiveCards);
        return this;
    }

    public void setTotalActiveCards(Integer totalActiveCards) {
        this.totalActiveCards = totalActiveCards;
    }

    public Integer getTotalNumberOfTransactionsDone() {
        return this.totalNumberOfTransactionsDone;
    }

    public CardUsageInformation totalNumberOfTransactionsDone(Integer totalNumberOfTransactionsDone) {
        this.setTotalNumberOfTransactionsDone(totalNumberOfTransactionsDone);
        return this;
    }

    public void setTotalNumberOfTransactionsDone(Integer totalNumberOfTransactionsDone) {
        this.totalNumberOfTransactionsDone = totalNumberOfTransactionsDone;
    }

    public BigDecimal getTotalValueOfTransactionsDoneInLCY() {
        return this.totalValueOfTransactionsDoneInLCY;
    }

    public CardUsageInformation totalValueOfTransactionsDoneInLCY(BigDecimal totalValueOfTransactionsDoneInLCY) {
        this.setTotalValueOfTransactionsDoneInLCY(totalValueOfTransactionsDoneInLCY);
        return this;
    }

    public void setTotalValueOfTransactionsDoneInLCY(BigDecimal totalValueOfTransactionsDoneInLCY) {
        this.totalValueOfTransactionsDoneInLCY = totalValueOfTransactionsDoneInLCY;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public CardUsageInformation bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public CardTypes getCardType() {
        return this.cardType;
    }

    public void setCardType(CardTypes cardTypes) {
        this.cardType = cardTypes;
    }

    public CardUsageInformation cardType(CardTypes cardTypes) {
        this.setCardType(cardTypes);
        return this;
    }

    public CardBrandType getCardBrand() {
        return this.cardBrand;
    }

    public void setCardBrand(CardBrandType cardBrandType) {
        this.cardBrand = cardBrandType;
    }

    public CardUsageInformation cardBrand(CardBrandType cardBrandType) {
        this.setCardBrand(cardBrandType);
        return this;
    }

    public CardCategoryType getCardCategoryType() {
        return this.cardCategoryType;
    }

    public void setCardCategoryType(CardCategoryType cardCategoryType) {
        this.cardCategoryType = cardCategoryType;
    }

    public CardUsageInformation cardCategoryType(CardCategoryType cardCategoryType) {
        this.setCardCategoryType(cardCategoryType);
        return this;
    }

    public BankTransactionType getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(BankTransactionType bankTransactionType) {
        this.transactionType = bankTransactionType;
    }

    public CardUsageInformation transactionType(BankTransactionType bankTransactionType) {
        this.setTransactionType(bankTransactionType);
        return this;
    }

    public ChannelType getChannelType() {
        return this.channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    public CardUsageInformation channelType(ChannelType channelType) {
        this.setChannelType(channelType);
        return this;
    }

    public CardState getCardState() {
        return this.cardState;
    }

    public void setCardState(CardState cardState) {
        this.cardState = cardState;
    }

    public CardUsageInformation cardState(CardState cardState) {
        this.setCardState(cardState);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardUsageInformation)) {
            return false;
        }
        return id != null && id.equals(((CardUsageInformation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardUsageInformation{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", totalNumberOfLiveCards=" + getTotalNumberOfLiveCards() +
            ", totalActiveCards=" + getTotalActiveCards() +
            ", totalNumberOfTransactionsDone=" + getTotalNumberOfTransactionsDone() +
            ", totalValueOfTransactionsDoneInLCY=" + getTotalValueOfTransactionsDoneInLCY() +
            "}";
    }
}
