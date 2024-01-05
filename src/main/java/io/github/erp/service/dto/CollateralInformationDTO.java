package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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

import io.github.erp.domain.enumeration.CollateralInsuredFlagTypes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CollateralInformation} entity.
 */
public class CollateralInformationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    private String collateralId;

    @NotNull
    @Pattern(regexp = "^\\d{15}$")
    private String loanContractId;

    @NotNull
    private String customerId;

    private String registrationPropertyNumber;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal collateralOMVInCCY;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal collateralFSVInLCY;

    @DecimalMin(value = "0")
    private BigDecimal collateralDiscountedValue;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amountCharged;

    @DecimalMin(value = "0")
    private Double collateralDiscountRate;

    @DecimalMin(value = "0")
    private Double loanToValueRatio;

    private String nameOfPropertyValuer;

    private LocalDate collateralLastValuationDate;

    @NotNull
    private CollateralInsuredFlagTypes insuredFlag;

    private String nameOfInsurer;

    @DecimalMin(value = "0")
    private BigDecimal amountInsured;

    private LocalDate insuranceExpiryDate;

    private String guaranteeInsurers;

    private InstitutionCodeDTO bankCode;

    private BankBranchCodeDTO branchCode;

    private CollateralTypeDTO collateralType;

    private CountySubCountyCodeDTO countyCode;

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

    public String getCollateralId() {
        return collateralId;
    }

    public void setCollateralId(String collateralId) {
        this.collateralId = collateralId;
    }

    public String getLoanContractId() {
        return loanContractId;
    }

    public void setLoanContractId(String loanContractId) {
        this.loanContractId = loanContractId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRegistrationPropertyNumber() {
        return registrationPropertyNumber;
    }

    public void setRegistrationPropertyNumber(String registrationPropertyNumber) {
        this.registrationPropertyNumber = registrationPropertyNumber;
    }

    public BigDecimal getCollateralOMVInCCY() {
        return collateralOMVInCCY;
    }

    public void setCollateralOMVInCCY(BigDecimal collateralOMVInCCY) {
        this.collateralOMVInCCY = collateralOMVInCCY;
    }

    public BigDecimal getCollateralFSVInLCY() {
        return collateralFSVInLCY;
    }

    public void setCollateralFSVInLCY(BigDecimal collateralFSVInLCY) {
        this.collateralFSVInLCY = collateralFSVInLCY;
    }

    public BigDecimal getCollateralDiscountedValue() {
        return collateralDiscountedValue;
    }

    public void setCollateralDiscountedValue(BigDecimal collateralDiscountedValue) {
        this.collateralDiscountedValue = collateralDiscountedValue;
    }

    public BigDecimal getAmountCharged() {
        return amountCharged;
    }

    public void setAmountCharged(BigDecimal amountCharged) {
        this.amountCharged = amountCharged;
    }

    public Double getCollateralDiscountRate() {
        return collateralDiscountRate;
    }

    public void setCollateralDiscountRate(Double collateralDiscountRate) {
        this.collateralDiscountRate = collateralDiscountRate;
    }

    public Double getLoanToValueRatio() {
        return loanToValueRatio;
    }

    public void setLoanToValueRatio(Double loanToValueRatio) {
        this.loanToValueRatio = loanToValueRatio;
    }

    public String getNameOfPropertyValuer() {
        return nameOfPropertyValuer;
    }

    public void setNameOfPropertyValuer(String nameOfPropertyValuer) {
        this.nameOfPropertyValuer = nameOfPropertyValuer;
    }

    public LocalDate getCollateralLastValuationDate() {
        return collateralLastValuationDate;
    }

    public void setCollateralLastValuationDate(LocalDate collateralLastValuationDate) {
        this.collateralLastValuationDate = collateralLastValuationDate;
    }

    public CollateralInsuredFlagTypes getInsuredFlag() {
        return insuredFlag;
    }

    public void setInsuredFlag(CollateralInsuredFlagTypes insuredFlag) {
        this.insuredFlag = insuredFlag;
    }

    public String getNameOfInsurer() {
        return nameOfInsurer;
    }

    public void setNameOfInsurer(String nameOfInsurer) {
        this.nameOfInsurer = nameOfInsurer;
    }

    public BigDecimal getAmountInsured() {
        return amountInsured;
    }

    public void setAmountInsured(BigDecimal amountInsured) {
        this.amountInsured = amountInsured;
    }

    public LocalDate getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(LocalDate insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public String getGuaranteeInsurers() {
        return guaranteeInsurers;
    }

    public void setGuaranteeInsurers(String guaranteeInsurers) {
        this.guaranteeInsurers = guaranteeInsurers;
    }

    public InstitutionCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(InstitutionCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public BankBranchCodeDTO getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(BankBranchCodeDTO branchCode) {
        this.branchCode = branchCode;
    }

    public CollateralTypeDTO getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralTypeDTO collateralType) {
        this.collateralType = collateralType;
    }

    public CountySubCountyCodeDTO getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(CountySubCountyCodeDTO countyCode) {
        this.countyCode = countyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollateralInformationDTO)) {
            return false;
        }

        CollateralInformationDTO collateralInformationDTO = (CollateralInformationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, collateralInformationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollateralInformationDTO{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", collateralId='" + getCollateralId() + "'" +
            ", loanContractId='" + getLoanContractId() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", registrationPropertyNumber='" + getRegistrationPropertyNumber() + "'" +
            ", collateralOMVInCCY=" + getCollateralOMVInCCY() +
            ", collateralFSVInLCY=" + getCollateralFSVInLCY() +
            ", collateralDiscountedValue=" + getCollateralDiscountedValue() +
            ", amountCharged=" + getAmountCharged() +
            ", collateralDiscountRate=" + getCollateralDiscountRate() +
            ", loanToValueRatio=" + getLoanToValueRatio() +
            ", nameOfPropertyValuer='" + getNameOfPropertyValuer() + "'" +
            ", collateralLastValuationDate='" + getCollateralLastValuationDate() + "'" +
            ", insuredFlag='" + getInsuredFlag() + "'" +
            ", nameOfInsurer='" + getNameOfInsurer() + "'" +
            ", amountInsured=" + getAmountInsured() +
            ", insuranceExpiryDate='" + getInsuranceExpiryDate() + "'" +
            ", guaranteeInsurers='" + getGuaranteeInsurers() + "'" +
            ", bankCode=" + getBankCode() +
            ", branchCode=" + getBranchCode() +
            ", collateralType=" + getCollateralType() +
            ", countyCode=" + getCountyCode() +
            "}";
    }
}
