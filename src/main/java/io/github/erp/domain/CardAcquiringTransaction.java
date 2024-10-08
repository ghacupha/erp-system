package io.github.erp.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CardAcquiringTransaction.
 */
@Entity
@Table(name = "card_acquiring_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardacquiringtransaction")
public class CardAcquiringTransaction implements Serializable {

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
    @Column(name = "terminal_id", nullable = false)
    private String terminalId;

    @NotNull
    @Min(value = 0)
    @Column(name = "number_of_transactions", nullable = false)
    private Integer numberOfTransactions;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "value_of_transactions_in_lcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal valueOfTransactionsInLCY;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankCode;

    @ManyToOne(optional = false)
    @NotNull
    private ChannelType channelType;

    @ManyToOne(optional = false)
    @NotNull
    private CardBrandType cardBrandType;

    @ManyToOne(optional = false)
    @NotNull
    private IsoCurrencyCode currencyOfTransaction;

    @ManyToOne(optional = false)
    @NotNull
    private CardCategoryType cardIssuerCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardAcquiringTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public CardAcquiringTransaction reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getTerminalId() {
        return this.terminalId;
    }

    public CardAcquiringTransaction terminalId(String terminalId) {
        this.setTerminalId(terminalId);
        return this;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Integer getNumberOfTransactions() {
        return this.numberOfTransactions;
    }

    public CardAcquiringTransaction numberOfTransactions(Integer numberOfTransactions) {
        this.setNumberOfTransactions(numberOfTransactions);
        return this;
    }

    public void setNumberOfTransactions(Integer numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public BigDecimal getValueOfTransactionsInLCY() {
        return this.valueOfTransactionsInLCY;
    }

    public CardAcquiringTransaction valueOfTransactionsInLCY(BigDecimal valueOfTransactionsInLCY) {
        this.setValueOfTransactionsInLCY(valueOfTransactionsInLCY);
        return this;
    }

    public void setValueOfTransactionsInLCY(BigDecimal valueOfTransactionsInLCY) {
        this.valueOfTransactionsInLCY = valueOfTransactionsInLCY;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public CardAcquiringTransaction bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public ChannelType getChannelType() {
        return this.channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    public CardAcquiringTransaction channelType(ChannelType channelType) {
        this.setChannelType(channelType);
        return this;
    }

    public CardBrandType getCardBrandType() {
        return this.cardBrandType;
    }

    public void setCardBrandType(CardBrandType cardBrandType) {
        this.cardBrandType = cardBrandType;
    }

    public CardAcquiringTransaction cardBrandType(CardBrandType cardBrandType) {
        this.setCardBrandType(cardBrandType);
        return this;
    }

    public IsoCurrencyCode getCurrencyOfTransaction() {
        return this.currencyOfTransaction;
    }

    public void setCurrencyOfTransaction(IsoCurrencyCode isoCurrencyCode) {
        this.currencyOfTransaction = isoCurrencyCode;
    }

    public CardAcquiringTransaction currencyOfTransaction(IsoCurrencyCode isoCurrencyCode) {
        this.setCurrencyOfTransaction(isoCurrencyCode);
        return this;
    }

    public CardCategoryType getCardIssuerCategory() {
        return this.cardIssuerCategory;
    }

    public void setCardIssuerCategory(CardCategoryType cardCategoryType) {
        this.cardIssuerCategory = cardCategoryType;
    }

    public CardAcquiringTransaction cardIssuerCategory(CardCategoryType cardCategoryType) {
        this.setCardIssuerCategory(cardCategoryType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardAcquiringTransaction)) {
            return false;
        }
        return id != null && id.equals(((CardAcquiringTransaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardAcquiringTransaction{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", terminalId='" + getTerminalId() + "'" +
            ", numberOfTransactions=" + getNumberOfTransactions() +
            ", valueOfTransactionsInLCY=" + getValueOfTransactionsInLCY() +
            "}";
    }
}
