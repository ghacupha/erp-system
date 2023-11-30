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
import io.github.erp.domain.enumeration.CollateralInsuredFlagTypes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CollateralInformation.
 */
@Entity
@Table(name = "collateral_information")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "collateralinformation")
public class CollateralInformation implements Serializable {

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
    @Column(name = "collateral_id", nullable = false)
    private String collateralId;

    @NotNull
    @Pattern(regexp = "^\\d{15}$")
    @Column(name = "loan_contract_id", nullable = false)
    private String loanContractId;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "registration_property_number")
    private String registrationPropertyNumber;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "collateral_omv_in_ccy", precision = 21, scale = 2, nullable = false)
    private BigDecimal collateralOMVInCCY;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "collateral_fsv_in_lcy", precision = 21, scale = 2, nullable = false)
    private BigDecimal collateralFSVInLCY;

    @DecimalMin(value = "0")
    @Column(name = "collateral_discounted_value", precision = 21, scale = 2)
    private BigDecimal collateralDiscountedValue;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount_charged", precision = 21, scale = 2, nullable = false)
    private BigDecimal amountCharged;

    @DecimalMin(value = "0")
    @Column(name = "collateral_discount_rate")
    private Double collateralDiscountRate;

    @DecimalMin(value = "0")
    @Column(name = "loan_to_value_ratio")
    private Double loanToValueRatio;

    @Column(name = "name_of_property_valuer")
    private String nameOfPropertyValuer;

    @Column(name = "collateral_last_valuation_date")
    private LocalDate collateralLastValuationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "insured_flag", nullable = false)
    private CollateralInsuredFlagTypes insuredFlag;

    @Column(name = "name_of_insurer")
    private String nameOfInsurer;

    @DecimalMin(value = "0")
    @Column(name = "amount_insured", precision = 21, scale = 2)
    private BigDecimal amountInsured;

    @Column(name = "insurance_expiry_date")
    private LocalDate insuranceExpiryDate;

    @Column(name = "guarantee_insurers")
    private String guaranteeInsurers;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private BankBranchCode branchCode;

    @ManyToOne(optional = false)
    @NotNull
    private CollateralType collateralType;

    @ManyToOne
    private CountySubCountyCode countyCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CollateralInformation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public CollateralInformation reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getCollateralId() {
        return this.collateralId;
    }

    public CollateralInformation collateralId(String collateralId) {
        this.setCollateralId(collateralId);
        return this;
    }

    public void setCollateralId(String collateralId) {
        this.collateralId = collateralId;
    }

    public String getLoanContractId() {
        return this.loanContractId;
    }

    public CollateralInformation loanContractId(String loanContractId) {
        this.setLoanContractId(loanContractId);
        return this;
    }

    public void setLoanContractId(String loanContractId) {
        this.loanContractId = loanContractId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public CollateralInformation customerId(String customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRegistrationPropertyNumber() {
        return this.registrationPropertyNumber;
    }

    public CollateralInformation registrationPropertyNumber(String registrationPropertyNumber) {
        this.setRegistrationPropertyNumber(registrationPropertyNumber);
        return this;
    }

    public void setRegistrationPropertyNumber(String registrationPropertyNumber) {
        this.registrationPropertyNumber = registrationPropertyNumber;
    }

    public BigDecimal getCollateralOMVInCCY() {
        return this.collateralOMVInCCY;
    }

    public CollateralInformation collateralOMVInCCY(BigDecimal collateralOMVInCCY) {
        this.setCollateralOMVInCCY(collateralOMVInCCY);
        return this;
    }

    public void setCollateralOMVInCCY(BigDecimal collateralOMVInCCY) {
        this.collateralOMVInCCY = collateralOMVInCCY;
    }

    public BigDecimal getCollateralFSVInLCY() {
        return this.collateralFSVInLCY;
    }

    public CollateralInformation collateralFSVInLCY(BigDecimal collateralFSVInLCY) {
        this.setCollateralFSVInLCY(collateralFSVInLCY);
        return this;
    }

    public void setCollateralFSVInLCY(BigDecimal collateralFSVInLCY) {
        this.collateralFSVInLCY = collateralFSVInLCY;
    }

    public BigDecimal getCollateralDiscountedValue() {
        return this.collateralDiscountedValue;
    }

    public CollateralInformation collateralDiscountedValue(BigDecimal collateralDiscountedValue) {
        this.setCollateralDiscountedValue(collateralDiscountedValue);
        return this;
    }

    public void setCollateralDiscountedValue(BigDecimal collateralDiscountedValue) {
        this.collateralDiscountedValue = collateralDiscountedValue;
    }

    public BigDecimal getAmountCharged() {
        return this.amountCharged;
    }

    public CollateralInformation amountCharged(BigDecimal amountCharged) {
        this.setAmountCharged(amountCharged);
        return this;
    }

    public void setAmountCharged(BigDecimal amountCharged) {
        this.amountCharged = amountCharged;
    }

    public Double getCollateralDiscountRate() {
        return this.collateralDiscountRate;
    }

    public CollateralInformation collateralDiscountRate(Double collateralDiscountRate) {
        this.setCollateralDiscountRate(collateralDiscountRate);
        return this;
    }

    public void setCollateralDiscountRate(Double collateralDiscountRate) {
        this.collateralDiscountRate = collateralDiscountRate;
    }

    public Double getLoanToValueRatio() {
        return this.loanToValueRatio;
    }

    public CollateralInformation loanToValueRatio(Double loanToValueRatio) {
        this.setLoanToValueRatio(loanToValueRatio);
        return this;
    }

    public void setLoanToValueRatio(Double loanToValueRatio) {
        this.loanToValueRatio = loanToValueRatio;
    }

    public String getNameOfPropertyValuer() {
        return this.nameOfPropertyValuer;
    }

    public CollateralInformation nameOfPropertyValuer(String nameOfPropertyValuer) {
        this.setNameOfPropertyValuer(nameOfPropertyValuer);
        return this;
    }

    public void setNameOfPropertyValuer(String nameOfPropertyValuer) {
        this.nameOfPropertyValuer = nameOfPropertyValuer;
    }

    public LocalDate getCollateralLastValuationDate() {
        return this.collateralLastValuationDate;
    }

    public CollateralInformation collateralLastValuationDate(LocalDate collateralLastValuationDate) {
        this.setCollateralLastValuationDate(collateralLastValuationDate);
        return this;
    }

    public void setCollateralLastValuationDate(LocalDate collateralLastValuationDate) {
        this.collateralLastValuationDate = collateralLastValuationDate;
    }

    public CollateralInsuredFlagTypes getInsuredFlag() {
        return this.insuredFlag;
    }

    public CollateralInformation insuredFlag(CollateralInsuredFlagTypes insuredFlag) {
        this.setInsuredFlag(insuredFlag);
        return this;
    }

    public void setInsuredFlag(CollateralInsuredFlagTypes insuredFlag) {
        this.insuredFlag = insuredFlag;
    }

    public String getNameOfInsurer() {
        return this.nameOfInsurer;
    }

    public CollateralInformation nameOfInsurer(String nameOfInsurer) {
        this.setNameOfInsurer(nameOfInsurer);
        return this;
    }

    public void setNameOfInsurer(String nameOfInsurer) {
        this.nameOfInsurer = nameOfInsurer;
    }

    public BigDecimal getAmountInsured() {
        return this.amountInsured;
    }

    public CollateralInformation amountInsured(BigDecimal amountInsured) {
        this.setAmountInsured(amountInsured);
        return this;
    }

    public void setAmountInsured(BigDecimal amountInsured) {
        this.amountInsured = amountInsured;
    }

    public LocalDate getInsuranceExpiryDate() {
        return this.insuranceExpiryDate;
    }

    public CollateralInformation insuranceExpiryDate(LocalDate insuranceExpiryDate) {
        this.setInsuranceExpiryDate(insuranceExpiryDate);
        return this;
    }

    public void setInsuranceExpiryDate(LocalDate insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public String getGuaranteeInsurers() {
        return this.guaranteeInsurers;
    }

    public CollateralInformation guaranteeInsurers(String guaranteeInsurers) {
        this.setGuaranteeInsurers(guaranteeInsurers);
        return this;
    }

    public void setGuaranteeInsurers(String guaranteeInsurers) {
        this.guaranteeInsurers = guaranteeInsurers;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public CollateralInformation bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public BankBranchCode getBranchCode() {
        return this.branchCode;
    }

    public void setBranchCode(BankBranchCode bankBranchCode) {
        this.branchCode = bankBranchCode;
    }

    public CollateralInformation branchCode(BankBranchCode bankBranchCode) {
        this.setBranchCode(bankBranchCode);
        return this;
    }

    public CollateralType getCollateralType() {
        return this.collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
    }

    public CollateralInformation collateralType(CollateralType collateralType) {
        this.setCollateralType(collateralType);
        return this;
    }

    public CountySubCountyCode getCountyCode() {
        return this.countyCode;
    }

    public void setCountyCode(CountySubCountyCode countySubCountyCode) {
        this.countyCode = countySubCountyCode;
    }

    public CollateralInformation countyCode(CountySubCountyCode countySubCountyCode) {
        this.setCountyCode(countySubCountyCode);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollateralInformation)) {
            return false;
        }
        return id != null && id.equals(((CollateralInformation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollateralInformation{" +
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
            "}";
    }
}
