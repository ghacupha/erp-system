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
 * A DTO for the {@link io.github.erp.domain.CreditCardFacility} entity.
 */
public class CreditCardFacilityDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    @Min(value = 0)
    private Integer totalNumberOfActiveCreditCards;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalCreditCardLimitsInCCY;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalCreditCardLimitsInLCY;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalCreditCardAmountUtilisedInCCY;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalCreditCardAmountUtilisedInLcy;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalNPACreditCardAmountInFCY;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalNPACreditCardAmountInLCY;

    private InstitutionCodeDTO bankCode;

    private CreditCardOwnershipDTO customerCategory;

    private IsoCurrencyCodeDTO currencyCode;

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

    public Integer getTotalNumberOfActiveCreditCards() {
        return totalNumberOfActiveCreditCards;
    }

    public void setTotalNumberOfActiveCreditCards(Integer totalNumberOfActiveCreditCards) {
        this.totalNumberOfActiveCreditCards = totalNumberOfActiveCreditCards;
    }

    public BigDecimal getTotalCreditCardLimitsInCCY() {
        return totalCreditCardLimitsInCCY;
    }

    public void setTotalCreditCardLimitsInCCY(BigDecimal totalCreditCardLimitsInCCY) {
        this.totalCreditCardLimitsInCCY = totalCreditCardLimitsInCCY;
    }

    public BigDecimal getTotalCreditCardLimitsInLCY() {
        return totalCreditCardLimitsInLCY;
    }

    public void setTotalCreditCardLimitsInLCY(BigDecimal totalCreditCardLimitsInLCY) {
        this.totalCreditCardLimitsInLCY = totalCreditCardLimitsInLCY;
    }

    public BigDecimal getTotalCreditCardAmountUtilisedInCCY() {
        return totalCreditCardAmountUtilisedInCCY;
    }

    public void setTotalCreditCardAmountUtilisedInCCY(BigDecimal totalCreditCardAmountUtilisedInCCY) {
        this.totalCreditCardAmountUtilisedInCCY = totalCreditCardAmountUtilisedInCCY;
    }

    public BigDecimal getTotalCreditCardAmountUtilisedInLcy() {
        return totalCreditCardAmountUtilisedInLcy;
    }

    public void setTotalCreditCardAmountUtilisedInLcy(BigDecimal totalCreditCardAmountUtilisedInLcy) {
        this.totalCreditCardAmountUtilisedInLcy = totalCreditCardAmountUtilisedInLcy;
    }

    public BigDecimal getTotalNPACreditCardAmountInFCY() {
        return totalNPACreditCardAmountInFCY;
    }

    public void setTotalNPACreditCardAmountInFCY(BigDecimal totalNPACreditCardAmountInFCY) {
        this.totalNPACreditCardAmountInFCY = totalNPACreditCardAmountInFCY;
    }

    public BigDecimal getTotalNPACreditCardAmountInLCY() {
        return totalNPACreditCardAmountInLCY;
    }

    public void setTotalNPACreditCardAmountInLCY(BigDecimal totalNPACreditCardAmountInLCY) {
        this.totalNPACreditCardAmountInLCY = totalNPACreditCardAmountInLCY;
    }

    public InstitutionCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(InstitutionCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public CreditCardOwnershipDTO getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(CreditCardOwnershipDTO customerCategory) {
        this.customerCategory = customerCategory;
    }

    public IsoCurrencyCodeDTO getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(IsoCurrencyCodeDTO currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreditCardFacilityDTO)) {
            return false;
        }

        CreditCardFacilityDTO creditCardFacilityDTO = (CreditCardFacilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, creditCardFacilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreditCardFacilityDTO{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", totalNumberOfActiveCreditCards=" + getTotalNumberOfActiveCreditCards() +
            ", totalCreditCardLimitsInCCY=" + getTotalCreditCardLimitsInCCY() +
            ", totalCreditCardLimitsInLCY=" + getTotalCreditCardLimitsInLCY() +
            ", totalCreditCardAmountUtilisedInCCY=" + getTotalCreditCardAmountUtilisedInCCY() +
            ", totalCreditCardAmountUtilisedInLcy=" + getTotalCreditCardAmountUtilisedInLcy() +
            ", totalNPACreditCardAmountInFCY=" + getTotalNPACreditCardAmountInFCY() +
            ", totalNPACreditCardAmountInLCY=" + getTotalNPACreditCardAmountInLCY() +
            ", bankCode=" + getBankCode() +
            ", customerCategory=" + getCustomerCategory() +
            ", currencyCode=" + getCurrencyCode() +
            "}";
    }
}
