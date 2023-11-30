package io.github.erp.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CardIssuerCharges.
 */
@Entity
@Table(name = "card_issuer_charges")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardissuercharges")
public class CardIssuerCharges implements Serializable {

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
    @DecimalMin(value = "0")
    @Column(name = "card_fee_charge_in_lcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal cardFeeChargeInLCY;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankCode;

    @ManyToOne(optional = false)
    @NotNull
    private CardCategoryType cardCategory;

    @ManyToOne(optional = false)
    @NotNull
    private CardTypes cardType;

    @ManyToOne(optional = false)
    @NotNull
    private CardBrandType cardBrand;

    @ManyToOne(optional = false)
    @NotNull
    private CardClassType cardClass;

    @ManyToOne(optional = false)
    @NotNull
    private CardCharges cardChargeType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardIssuerCharges id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public CardIssuerCharges reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public BigDecimal getCardFeeChargeInLCY() {
        return this.cardFeeChargeInLCY;
    }

    public CardIssuerCharges cardFeeChargeInLCY(BigDecimal cardFeeChargeInLCY) {
        this.setCardFeeChargeInLCY(cardFeeChargeInLCY);
        return this;
    }

    public void setCardFeeChargeInLCY(BigDecimal cardFeeChargeInLCY) {
        this.cardFeeChargeInLCY = cardFeeChargeInLCY;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public CardIssuerCharges bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public CardCategoryType getCardCategory() {
        return this.cardCategory;
    }

    public void setCardCategory(CardCategoryType cardCategoryType) {
        this.cardCategory = cardCategoryType;
    }

    public CardIssuerCharges cardCategory(CardCategoryType cardCategoryType) {
        this.setCardCategory(cardCategoryType);
        return this;
    }

    public CardTypes getCardType() {
        return this.cardType;
    }

    public void setCardType(CardTypes cardTypes) {
        this.cardType = cardTypes;
    }

    public CardIssuerCharges cardType(CardTypes cardTypes) {
        this.setCardType(cardTypes);
        return this;
    }

    public CardBrandType getCardBrand() {
        return this.cardBrand;
    }

    public void setCardBrand(CardBrandType cardBrandType) {
        this.cardBrand = cardBrandType;
    }

    public CardIssuerCharges cardBrand(CardBrandType cardBrandType) {
        this.setCardBrand(cardBrandType);
        return this;
    }

    public CardClassType getCardClass() {
        return this.cardClass;
    }

    public void setCardClass(CardClassType cardClassType) {
        this.cardClass = cardClassType;
    }

    public CardIssuerCharges cardClass(CardClassType cardClassType) {
        this.setCardClass(cardClassType);
        return this;
    }

    public CardCharges getCardChargeType() {
        return this.cardChargeType;
    }

    public void setCardChargeType(CardCharges cardCharges) {
        this.cardChargeType = cardCharges;
    }

    public CardIssuerCharges cardChargeType(CardCharges cardCharges) {
        this.setCardChargeType(cardCharges);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardIssuerCharges)) {
            return false;
        }
        return id != null && id.equals(((CardIssuerCharges) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardIssuerCharges{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", cardFeeChargeInLCY=" + getCardFeeChargeInLCY() +
            "}";
    }
}
