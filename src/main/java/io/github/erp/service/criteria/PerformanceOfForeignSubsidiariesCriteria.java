package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.PerformanceOfForeignSubsidiaries} entity. This class is used
 * in {@link io.github.erp.web.rest.PerformanceOfForeignSubsidiariesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /performance-of-foreign-subsidiaries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PerformanceOfForeignSubsidiariesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter subsidiaryName;

    private LocalDateFilter reportingDate;

    private StringFilter subsidiaryId;

    private BigDecimalFilter grossLoansAmount;

    private BigDecimalFilter grossNPALoanAmount;

    private BigDecimalFilter grossAssetsAmount;

    private BigDecimalFilter grossDepositsAmount;

    private BigDecimalFilter profitBeforeTax;

    private DoubleFilter totalCapitalAdequacyRatio;

    private DoubleFilter liquidityRatio;

    private BigDecimalFilter generalProvisions;

    private BigDecimalFilter specificProvisions;

    private BigDecimalFilter interestInSuspenseAmount;

    private IntegerFilter totalNumberOfStaff;

    private IntegerFilter numberOfBranches;

    private LongFilter bankCodeId;

    private LongFilter subsidiaryCountryCodeId;

    private Boolean distinct;

    public PerformanceOfForeignSubsidiariesCriteria() {}

    public PerformanceOfForeignSubsidiariesCriteria(PerformanceOfForeignSubsidiariesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.subsidiaryName = other.subsidiaryName == null ? null : other.subsidiaryName.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.subsidiaryId = other.subsidiaryId == null ? null : other.subsidiaryId.copy();
        this.grossLoansAmount = other.grossLoansAmount == null ? null : other.grossLoansAmount.copy();
        this.grossNPALoanAmount = other.grossNPALoanAmount == null ? null : other.grossNPALoanAmount.copy();
        this.grossAssetsAmount = other.grossAssetsAmount == null ? null : other.grossAssetsAmount.copy();
        this.grossDepositsAmount = other.grossDepositsAmount == null ? null : other.grossDepositsAmount.copy();
        this.profitBeforeTax = other.profitBeforeTax == null ? null : other.profitBeforeTax.copy();
        this.totalCapitalAdequacyRatio = other.totalCapitalAdequacyRatio == null ? null : other.totalCapitalAdequacyRatio.copy();
        this.liquidityRatio = other.liquidityRatio == null ? null : other.liquidityRatio.copy();
        this.generalProvisions = other.generalProvisions == null ? null : other.generalProvisions.copy();
        this.specificProvisions = other.specificProvisions == null ? null : other.specificProvisions.copy();
        this.interestInSuspenseAmount = other.interestInSuspenseAmount == null ? null : other.interestInSuspenseAmount.copy();
        this.totalNumberOfStaff = other.totalNumberOfStaff == null ? null : other.totalNumberOfStaff.copy();
        this.numberOfBranches = other.numberOfBranches == null ? null : other.numberOfBranches.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.subsidiaryCountryCodeId = other.subsidiaryCountryCodeId == null ? null : other.subsidiaryCountryCodeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PerformanceOfForeignSubsidiariesCriteria copy() {
        return new PerformanceOfForeignSubsidiariesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSubsidiaryName() {
        return subsidiaryName;
    }

    public StringFilter subsidiaryName() {
        if (subsidiaryName == null) {
            subsidiaryName = new StringFilter();
        }
        return subsidiaryName;
    }

    public void setSubsidiaryName(StringFilter subsidiaryName) {
        this.subsidiaryName = subsidiaryName;
    }

    public LocalDateFilter getReportingDate() {
        return reportingDate;
    }

    public LocalDateFilter reportingDate() {
        if (reportingDate == null) {
            reportingDate = new LocalDateFilter();
        }
        return reportingDate;
    }

    public void setReportingDate(LocalDateFilter reportingDate) {
        this.reportingDate = reportingDate;
    }

    public StringFilter getSubsidiaryId() {
        return subsidiaryId;
    }

    public StringFilter subsidiaryId() {
        if (subsidiaryId == null) {
            subsidiaryId = new StringFilter();
        }
        return subsidiaryId;
    }

    public void setSubsidiaryId(StringFilter subsidiaryId) {
        this.subsidiaryId = subsidiaryId;
    }

    public BigDecimalFilter getGrossLoansAmount() {
        return grossLoansAmount;
    }

    public BigDecimalFilter grossLoansAmount() {
        if (grossLoansAmount == null) {
            grossLoansAmount = new BigDecimalFilter();
        }
        return grossLoansAmount;
    }

    public void setGrossLoansAmount(BigDecimalFilter grossLoansAmount) {
        this.grossLoansAmount = grossLoansAmount;
    }

    public BigDecimalFilter getGrossNPALoanAmount() {
        return grossNPALoanAmount;
    }

    public BigDecimalFilter grossNPALoanAmount() {
        if (grossNPALoanAmount == null) {
            grossNPALoanAmount = new BigDecimalFilter();
        }
        return grossNPALoanAmount;
    }

    public void setGrossNPALoanAmount(BigDecimalFilter grossNPALoanAmount) {
        this.grossNPALoanAmount = grossNPALoanAmount;
    }

    public BigDecimalFilter getGrossAssetsAmount() {
        return grossAssetsAmount;
    }

    public BigDecimalFilter grossAssetsAmount() {
        if (grossAssetsAmount == null) {
            grossAssetsAmount = new BigDecimalFilter();
        }
        return grossAssetsAmount;
    }

    public void setGrossAssetsAmount(BigDecimalFilter grossAssetsAmount) {
        this.grossAssetsAmount = grossAssetsAmount;
    }

    public BigDecimalFilter getGrossDepositsAmount() {
        return grossDepositsAmount;
    }

    public BigDecimalFilter grossDepositsAmount() {
        if (grossDepositsAmount == null) {
            grossDepositsAmount = new BigDecimalFilter();
        }
        return grossDepositsAmount;
    }

    public void setGrossDepositsAmount(BigDecimalFilter grossDepositsAmount) {
        this.grossDepositsAmount = grossDepositsAmount;
    }

    public BigDecimalFilter getProfitBeforeTax() {
        return profitBeforeTax;
    }

    public BigDecimalFilter profitBeforeTax() {
        if (profitBeforeTax == null) {
            profitBeforeTax = new BigDecimalFilter();
        }
        return profitBeforeTax;
    }

    public void setProfitBeforeTax(BigDecimalFilter profitBeforeTax) {
        this.profitBeforeTax = profitBeforeTax;
    }

    public DoubleFilter getTotalCapitalAdequacyRatio() {
        return totalCapitalAdequacyRatio;
    }

    public DoubleFilter totalCapitalAdequacyRatio() {
        if (totalCapitalAdequacyRatio == null) {
            totalCapitalAdequacyRatio = new DoubleFilter();
        }
        return totalCapitalAdequacyRatio;
    }

    public void setTotalCapitalAdequacyRatio(DoubleFilter totalCapitalAdequacyRatio) {
        this.totalCapitalAdequacyRatio = totalCapitalAdequacyRatio;
    }

    public DoubleFilter getLiquidityRatio() {
        return liquidityRatio;
    }

    public DoubleFilter liquidityRatio() {
        if (liquidityRatio == null) {
            liquidityRatio = new DoubleFilter();
        }
        return liquidityRatio;
    }

    public void setLiquidityRatio(DoubleFilter liquidityRatio) {
        this.liquidityRatio = liquidityRatio;
    }

    public BigDecimalFilter getGeneralProvisions() {
        return generalProvisions;
    }

    public BigDecimalFilter generalProvisions() {
        if (generalProvisions == null) {
            generalProvisions = new BigDecimalFilter();
        }
        return generalProvisions;
    }

    public void setGeneralProvisions(BigDecimalFilter generalProvisions) {
        this.generalProvisions = generalProvisions;
    }

    public BigDecimalFilter getSpecificProvisions() {
        return specificProvisions;
    }

    public BigDecimalFilter specificProvisions() {
        if (specificProvisions == null) {
            specificProvisions = new BigDecimalFilter();
        }
        return specificProvisions;
    }

    public void setSpecificProvisions(BigDecimalFilter specificProvisions) {
        this.specificProvisions = specificProvisions;
    }

    public BigDecimalFilter getInterestInSuspenseAmount() {
        return interestInSuspenseAmount;
    }

    public BigDecimalFilter interestInSuspenseAmount() {
        if (interestInSuspenseAmount == null) {
            interestInSuspenseAmount = new BigDecimalFilter();
        }
        return interestInSuspenseAmount;
    }

    public void setInterestInSuspenseAmount(BigDecimalFilter interestInSuspenseAmount) {
        this.interestInSuspenseAmount = interestInSuspenseAmount;
    }

    public IntegerFilter getTotalNumberOfStaff() {
        return totalNumberOfStaff;
    }

    public IntegerFilter totalNumberOfStaff() {
        if (totalNumberOfStaff == null) {
            totalNumberOfStaff = new IntegerFilter();
        }
        return totalNumberOfStaff;
    }

    public void setTotalNumberOfStaff(IntegerFilter totalNumberOfStaff) {
        this.totalNumberOfStaff = totalNumberOfStaff;
    }

    public IntegerFilter getNumberOfBranches() {
        return numberOfBranches;
    }

    public IntegerFilter numberOfBranches() {
        if (numberOfBranches == null) {
            numberOfBranches = new IntegerFilter();
        }
        return numberOfBranches;
    }

    public void setNumberOfBranches(IntegerFilter numberOfBranches) {
        this.numberOfBranches = numberOfBranches;
    }

    public LongFilter getBankCodeId() {
        return bankCodeId;
    }

    public LongFilter bankCodeId() {
        if (bankCodeId == null) {
            bankCodeId = new LongFilter();
        }
        return bankCodeId;
    }

    public void setBankCodeId(LongFilter bankCodeId) {
        this.bankCodeId = bankCodeId;
    }

    public LongFilter getSubsidiaryCountryCodeId() {
        return subsidiaryCountryCodeId;
    }

    public LongFilter subsidiaryCountryCodeId() {
        if (subsidiaryCountryCodeId == null) {
            subsidiaryCountryCodeId = new LongFilter();
        }
        return subsidiaryCountryCodeId;
    }

    public void setSubsidiaryCountryCodeId(LongFilter subsidiaryCountryCodeId) {
        this.subsidiaryCountryCodeId = subsidiaryCountryCodeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PerformanceOfForeignSubsidiariesCriteria that = (PerformanceOfForeignSubsidiariesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(subsidiaryName, that.subsidiaryName) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(subsidiaryId, that.subsidiaryId) &&
            Objects.equals(grossLoansAmount, that.grossLoansAmount) &&
            Objects.equals(grossNPALoanAmount, that.grossNPALoanAmount) &&
            Objects.equals(grossAssetsAmount, that.grossAssetsAmount) &&
            Objects.equals(grossDepositsAmount, that.grossDepositsAmount) &&
            Objects.equals(profitBeforeTax, that.profitBeforeTax) &&
            Objects.equals(totalCapitalAdequacyRatio, that.totalCapitalAdequacyRatio) &&
            Objects.equals(liquidityRatio, that.liquidityRatio) &&
            Objects.equals(generalProvisions, that.generalProvisions) &&
            Objects.equals(specificProvisions, that.specificProvisions) &&
            Objects.equals(interestInSuspenseAmount, that.interestInSuspenseAmount) &&
            Objects.equals(totalNumberOfStaff, that.totalNumberOfStaff) &&
            Objects.equals(numberOfBranches, that.numberOfBranches) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(subsidiaryCountryCodeId, that.subsidiaryCountryCodeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            subsidiaryName,
            reportingDate,
            subsidiaryId,
            grossLoansAmount,
            grossNPALoanAmount,
            grossAssetsAmount,
            grossDepositsAmount,
            profitBeforeTax,
            totalCapitalAdequacyRatio,
            liquidityRatio,
            generalProvisions,
            specificProvisions,
            interestInSuspenseAmount,
            totalNumberOfStaff,
            numberOfBranches,
            bankCodeId,
            subsidiaryCountryCodeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerformanceOfForeignSubsidiariesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (subsidiaryName != null ? "subsidiaryName=" + subsidiaryName + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (subsidiaryId != null ? "subsidiaryId=" + subsidiaryId + ", " : "") +
            (grossLoansAmount != null ? "grossLoansAmount=" + grossLoansAmount + ", " : "") +
            (grossNPALoanAmount != null ? "grossNPALoanAmount=" + grossNPALoanAmount + ", " : "") +
            (grossAssetsAmount != null ? "grossAssetsAmount=" + grossAssetsAmount + ", " : "") +
            (grossDepositsAmount != null ? "grossDepositsAmount=" + grossDepositsAmount + ", " : "") +
            (profitBeforeTax != null ? "profitBeforeTax=" + profitBeforeTax + ", " : "") +
            (totalCapitalAdequacyRatio != null ? "totalCapitalAdequacyRatio=" + totalCapitalAdequacyRatio + ", " : "") +
            (liquidityRatio != null ? "liquidityRatio=" + liquidityRatio + ", " : "") +
            (generalProvisions != null ? "generalProvisions=" + generalProvisions + ", " : "") +
            (specificProvisions != null ? "specificProvisions=" + specificProvisions + ", " : "") +
            (interestInSuspenseAmount != null ? "interestInSuspenseAmount=" + interestInSuspenseAmount + ", " : "") +
            (totalNumberOfStaff != null ? "totalNumberOfStaff=" + totalNumberOfStaff + ", " : "") +
            (numberOfBranches != null ? "numberOfBranches=" + numberOfBranches + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (subsidiaryCountryCodeId != null ? "subsidiaryCountryCodeId=" + subsidiaryCountryCodeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
