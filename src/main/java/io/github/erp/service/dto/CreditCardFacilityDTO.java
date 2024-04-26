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
