package io.github.erp.domain;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CreditCardFacility.
 */
@Entity
@Table(name = "credit_card_facility")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "creditcardfacility")
public class CreditCardFacility implements Serializable {

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
    @Column(name = "total_number_of_active_credit_cards", nullable = false)
    private Integer totalNumberOfActiveCreditCards;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_credit_card_limits_in_ccy", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalCreditCardLimitsInCCY;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_credit_card_limits_in_lcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalCreditCardLimitsInLCY;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_credit_card_amount_utilised_in_ccy", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalCreditCardAmountUtilisedInCCY;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_credit_card_amount_utilised_in_lcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalCreditCardAmountUtilisedInLcy;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_npa_credit_card_amount_in_fcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalNPACreditCardAmountInFCY;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_npa_credit_card_amount_in_lcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalNPACreditCardAmountInLCY;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankCode;

    @ManyToOne(optional = false)
    @NotNull
    private CreditCardOwnership customerCategory;

    @ManyToOne(optional = false)
    @NotNull
    private IsoCurrencyCode currencyCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CreditCardFacility id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public CreditCardFacility reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public Integer getTotalNumberOfActiveCreditCards() {
        return this.totalNumberOfActiveCreditCards;
    }

    public CreditCardFacility totalNumberOfActiveCreditCards(Integer totalNumberOfActiveCreditCards) {
        this.setTotalNumberOfActiveCreditCards(totalNumberOfActiveCreditCards);
        return this;
    }

    public void setTotalNumberOfActiveCreditCards(Integer totalNumberOfActiveCreditCards) {
        this.totalNumberOfActiveCreditCards = totalNumberOfActiveCreditCards;
    }

    public BigDecimal getTotalCreditCardLimitsInCCY() {
        return this.totalCreditCardLimitsInCCY;
    }

    public CreditCardFacility totalCreditCardLimitsInCCY(BigDecimal totalCreditCardLimitsInCCY) {
        this.setTotalCreditCardLimitsInCCY(totalCreditCardLimitsInCCY);
        return this;
    }

    public void setTotalCreditCardLimitsInCCY(BigDecimal totalCreditCardLimitsInCCY) {
        this.totalCreditCardLimitsInCCY = totalCreditCardLimitsInCCY;
    }

    public BigDecimal getTotalCreditCardLimitsInLCY() {
        return this.totalCreditCardLimitsInLCY;
    }

    public CreditCardFacility totalCreditCardLimitsInLCY(BigDecimal totalCreditCardLimitsInLCY) {
        this.setTotalCreditCardLimitsInLCY(totalCreditCardLimitsInLCY);
        return this;
    }

    public void setTotalCreditCardLimitsInLCY(BigDecimal totalCreditCardLimitsInLCY) {
        this.totalCreditCardLimitsInLCY = totalCreditCardLimitsInLCY;
    }

    public BigDecimal getTotalCreditCardAmountUtilisedInCCY() {
        return this.totalCreditCardAmountUtilisedInCCY;
    }

    public CreditCardFacility totalCreditCardAmountUtilisedInCCY(BigDecimal totalCreditCardAmountUtilisedInCCY) {
        this.setTotalCreditCardAmountUtilisedInCCY(totalCreditCardAmountUtilisedInCCY);
        return this;
    }

    public void setTotalCreditCardAmountUtilisedInCCY(BigDecimal totalCreditCardAmountUtilisedInCCY) {
        this.totalCreditCardAmountUtilisedInCCY = totalCreditCardAmountUtilisedInCCY;
    }

    public BigDecimal getTotalCreditCardAmountUtilisedInLcy() {
        return this.totalCreditCardAmountUtilisedInLcy;
    }

    public CreditCardFacility totalCreditCardAmountUtilisedInLcy(BigDecimal totalCreditCardAmountUtilisedInLcy) {
        this.setTotalCreditCardAmountUtilisedInLcy(totalCreditCardAmountUtilisedInLcy);
        return this;
    }

    public void setTotalCreditCardAmountUtilisedInLcy(BigDecimal totalCreditCardAmountUtilisedInLcy) {
        this.totalCreditCardAmountUtilisedInLcy = totalCreditCardAmountUtilisedInLcy;
    }

    public BigDecimal getTotalNPACreditCardAmountInFCY() {
        return this.totalNPACreditCardAmountInFCY;
    }

    public CreditCardFacility totalNPACreditCardAmountInFCY(BigDecimal totalNPACreditCardAmountInFCY) {
        this.setTotalNPACreditCardAmountInFCY(totalNPACreditCardAmountInFCY);
        return this;
    }

    public void setTotalNPACreditCardAmountInFCY(BigDecimal totalNPACreditCardAmountInFCY) {
        this.totalNPACreditCardAmountInFCY = totalNPACreditCardAmountInFCY;
    }

    public BigDecimal getTotalNPACreditCardAmountInLCY() {
        return this.totalNPACreditCardAmountInLCY;
    }

    public CreditCardFacility totalNPACreditCardAmountInLCY(BigDecimal totalNPACreditCardAmountInLCY) {
        this.setTotalNPACreditCardAmountInLCY(totalNPACreditCardAmountInLCY);
        return this;
    }

    public void setTotalNPACreditCardAmountInLCY(BigDecimal totalNPACreditCardAmountInLCY) {
        this.totalNPACreditCardAmountInLCY = totalNPACreditCardAmountInLCY;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public CreditCardFacility bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public CreditCardOwnership getCustomerCategory() {
        return this.customerCategory;
    }

    public void setCustomerCategory(CreditCardOwnership creditCardOwnership) {
        this.customerCategory = creditCardOwnership;
    }

    public CreditCardFacility customerCategory(CreditCardOwnership creditCardOwnership) {
        this.setCustomerCategory(creditCardOwnership);
        return this;
    }

    public IsoCurrencyCode getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(IsoCurrencyCode isoCurrencyCode) {
        this.currencyCode = isoCurrencyCode;
    }

    public CreditCardFacility currencyCode(IsoCurrencyCode isoCurrencyCode) {
        this.setCurrencyCode(isoCurrencyCode);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreditCardFacility)) {
            return false;
        }
        return id != null && id.equals(((CreditCardFacility) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreditCardFacility{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", totalNumberOfActiveCreditCards=" + getTotalNumberOfActiveCreditCards() +
            ", totalCreditCardLimitsInCCY=" + getTotalCreditCardLimitsInCCY() +
            ", totalCreditCardLimitsInLCY=" + getTotalCreditCardLimitsInLCY() +
            ", totalCreditCardAmountUtilisedInCCY=" + getTotalCreditCardAmountUtilisedInCCY() +
            ", totalCreditCardAmountUtilisedInLcy=" + getTotalCreditCardAmountUtilisedInLcy() +
            ", totalNPACreditCardAmountInFCY=" + getTotalNPACreditCardAmountInFCY() +
            ", totalNPACreditCardAmountInLCY=" + getTotalNPACreditCardAmountInLCY() +
            "}";
    }
}
