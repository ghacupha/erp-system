package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
