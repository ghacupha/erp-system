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
 * A DTO for the {@link io.github.erp.domain.PerformanceOfForeignSubsidiaries} entity.
 */
public class PerformanceOfForeignSubsidiariesDTO implements Serializable {

    private Long id;

    @NotNull
    private String subsidiaryName;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    private String subsidiaryId;

    @NotNull
    private BigDecimal grossLoansAmount;

    @NotNull
    private BigDecimal grossNPALoanAmount;

    @NotNull
    private BigDecimal grossAssetsAmount;

    @NotNull
    private BigDecimal grossDepositsAmount;

    @NotNull
    private BigDecimal profitBeforeTax;

    @NotNull
    private Double totalCapitalAdequacyRatio;

    @NotNull
    private Double liquidityRatio;

    @NotNull
    private BigDecimal generalProvisions;

    @NotNull
    private BigDecimal specificProvisions;

    @NotNull
    private BigDecimal interestInSuspenseAmount;

    @NotNull
    @Min(value = 1)
    private Integer totalNumberOfStaff;

    @NotNull
    @Min(value = 1)
    private Integer numberOfBranches;

    private InstitutionCodeDTO bankCode;

    private IsoCountryCodeDTO subsidiaryCountryCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubsidiaryName() {
        return subsidiaryName;
    }

    public void setSubsidiaryName(String subsidiaryName) {
        this.subsidiaryName = subsidiaryName;
    }

    public LocalDate getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getSubsidiaryId() {
        return subsidiaryId;
    }

    public void setSubsidiaryId(String subsidiaryId) {
        this.subsidiaryId = subsidiaryId;
    }

    public BigDecimal getGrossLoansAmount() {
        return grossLoansAmount;
    }

    public void setGrossLoansAmount(BigDecimal grossLoansAmount) {
        this.grossLoansAmount = grossLoansAmount;
    }

    public BigDecimal getGrossNPALoanAmount() {
        return grossNPALoanAmount;
    }

    public void setGrossNPALoanAmount(BigDecimal grossNPALoanAmount) {
        this.grossNPALoanAmount = grossNPALoanAmount;
    }

    public BigDecimal getGrossAssetsAmount() {
        return grossAssetsAmount;
    }

    public void setGrossAssetsAmount(BigDecimal grossAssetsAmount) {
        this.grossAssetsAmount = grossAssetsAmount;
    }

    public BigDecimal getGrossDepositsAmount() {
        return grossDepositsAmount;
    }

    public void setGrossDepositsAmount(BigDecimal grossDepositsAmount) {
        this.grossDepositsAmount = grossDepositsAmount;
    }

    public BigDecimal getProfitBeforeTax() {
        return profitBeforeTax;
    }

    public void setProfitBeforeTax(BigDecimal profitBeforeTax) {
        this.profitBeforeTax = profitBeforeTax;
    }

    public Double getTotalCapitalAdequacyRatio() {
        return totalCapitalAdequacyRatio;
    }

    public void setTotalCapitalAdequacyRatio(Double totalCapitalAdequacyRatio) {
        this.totalCapitalAdequacyRatio = totalCapitalAdequacyRatio;
    }

    public Double getLiquidityRatio() {
        return liquidityRatio;
    }

    public void setLiquidityRatio(Double liquidityRatio) {
        this.liquidityRatio = liquidityRatio;
    }

    public BigDecimal getGeneralProvisions() {
        return generalProvisions;
    }

    public void setGeneralProvisions(BigDecimal generalProvisions) {
        this.generalProvisions = generalProvisions;
    }

    public BigDecimal getSpecificProvisions() {
        return specificProvisions;
    }

    public void setSpecificProvisions(BigDecimal specificProvisions) {
        this.specificProvisions = specificProvisions;
    }

    public BigDecimal getInterestInSuspenseAmount() {
        return interestInSuspenseAmount;
    }

    public void setInterestInSuspenseAmount(BigDecimal interestInSuspenseAmount) {
        this.interestInSuspenseAmount = interestInSuspenseAmount;
    }

    public Integer getTotalNumberOfStaff() {
        return totalNumberOfStaff;
    }

    public void setTotalNumberOfStaff(Integer totalNumberOfStaff) {
        this.totalNumberOfStaff = totalNumberOfStaff;
    }

    public Integer getNumberOfBranches() {
        return numberOfBranches;
    }

    public void setNumberOfBranches(Integer numberOfBranches) {
        this.numberOfBranches = numberOfBranches;
    }

    public InstitutionCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(InstitutionCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public IsoCountryCodeDTO getSubsidiaryCountryCode() {
        return subsidiaryCountryCode;
    }

    public void setSubsidiaryCountryCode(IsoCountryCodeDTO subsidiaryCountryCode) {
        this.subsidiaryCountryCode = subsidiaryCountryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PerformanceOfForeignSubsidiariesDTO)) {
            return false;
        }

        PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO = (PerformanceOfForeignSubsidiariesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, performanceOfForeignSubsidiariesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerformanceOfForeignSubsidiariesDTO{" +
            "id=" + getId() +
            ", subsidiaryName='" + getSubsidiaryName() + "'" +
            ", reportingDate='" + getReportingDate() + "'" +
            ", subsidiaryId='" + getSubsidiaryId() + "'" +
            ", grossLoansAmount=" + getGrossLoansAmount() +
            ", grossNPALoanAmount=" + getGrossNPALoanAmount() +
            ", grossAssetsAmount=" + getGrossAssetsAmount() +
            ", grossDepositsAmount=" + getGrossDepositsAmount() +
            ", profitBeforeTax=" + getProfitBeforeTax() +
            ", totalCapitalAdequacyRatio=" + getTotalCapitalAdequacyRatio() +
            ", liquidityRatio=" + getLiquidityRatio() +
            ", generalProvisions=" + getGeneralProvisions() +
            ", specificProvisions=" + getSpecificProvisions() +
            ", interestInSuspenseAmount=" + getInterestInSuspenseAmount() +
            ", totalNumberOfStaff=" + getTotalNumberOfStaff() +
            ", numberOfBranches=" + getNumberOfBranches() +
            ", bankCode=" + getBankCode() +
            ", subsidiaryCountryCode=" + getSubsidiaryCountryCode() +
            "}";
    }
}
