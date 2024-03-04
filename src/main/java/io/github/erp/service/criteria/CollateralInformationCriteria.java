package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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

import io.github.erp.domain.enumeration.CollateralInsuredFlagTypes;
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
 * Criteria class for the {@link io.github.erp.domain.CollateralInformation} entity. This class is used
 * in {@link io.github.erp.web.rest.CollateralInformationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /collateral-informations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CollateralInformationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CollateralInsuredFlagTypes
     */
    public static class CollateralInsuredFlagTypesFilter extends Filter<CollateralInsuredFlagTypes> {

        public CollateralInsuredFlagTypesFilter() {}

        public CollateralInsuredFlagTypesFilter(CollateralInsuredFlagTypesFilter filter) {
            super(filter);
        }

        @Override
        public CollateralInsuredFlagTypesFilter copy() {
            return new CollateralInsuredFlagTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private StringFilter collateralId;

    private StringFilter loanContractId;

    private StringFilter customerId;

    private StringFilter registrationPropertyNumber;

    private BigDecimalFilter collateralOMVInCCY;

    private BigDecimalFilter collateralFSVInLCY;

    private BigDecimalFilter collateralDiscountedValue;

    private BigDecimalFilter amountCharged;

    private DoubleFilter collateralDiscountRate;

    private DoubleFilter loanToValueRatio;

    private StringFilter nameOfPropertyValuer;

    private LocalDateFilter collateralLastValuationDate;

    private CollateralInsuredFlagTypesFilter insuredFlag;

    private StringFilter nameOfInsurer;

    private BigDecimalFilter amountInsured;

    private LocalDateFilter insuranceExpiryDate;

    private StringFilter guaranteeInsurers;

    private LongFilter bankCodeId;

    private LongFilter branchCodeId;

    private LongFilter collateralTypeId;

    private LongFilter countyCodeId;

    private Boolean distinct;

    public CollateralInformationCriteria() {}

    public CollateralInformationCriteria(CollateralInformationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.collateralId = other.collateralId == null ? null : other.collateralId.copy();
        this.loanContractId = other.loanContractId == null ? null : other.loanContractId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.registrationPropertyNumber = other.registrationPropertyNumber == null ? null : other.registrationPropertyNumber.copy();
        this.collateralOMVInCCY = other.collateralOMVInCCY == null ? null : other.collateralOMVInCCY.copy();
        this.collateralFSVInLCY = other.collateralFSVInLCY == null ? null : other.collateralFSVInLCY.copy();
        this.collateralDiscountedValue = other.collateralDiscountedValue == null ? null : other.collateralDiscountedValue.copy();
        this.amountCharged = other.amountCharged == null ? null : other.amountCharged.copy();
        this.collateralDiscountRate = other.collateralDiscountRate == null ? null : other.collateralDiscountRate.copy();
        this.loanToValueRatio = other.loanToValueRatio == null ? null : other.loanToValueRatio.copy();
        this.nameOfPropertyValuer = other.nameOfPropertyValuer == null ? null : other.nameOfPropertyValuer.copy();
        this.collateralLastValuationDate = other.collateralLastValuationDate == null ? null : other.collateralLastValuationDate.copy();
        this.insuredFlag = other.insuredFlag == null ? null : other.insuredFlag.copy();
        this.nameOfInsurer = other.nameOfInsurer == null ? null : other.nameOfInsurer.copy();
        this.amountInsured = other.amountInsured == null ? null : other.amountInsured.copy();
        this.insuranceExpiryDate = other.insuranceExpiryDate == null ? null : other.insuranceExpiryDate.copy();
        this.guaranteeInsurers = other.guaranteeInsurers == null ? null : other.guaranteeInsurers.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.branchCodeId = other.branchCodeId == null ? null : other.branchCodeId.copy();
        this.collateralTypeId = other.collateralTypeId == null ? null : other.collateralTypeId.copy();
        this.countyCodeId = other.countyCodeId == null ? null : other.countyCodeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CollateralInformationCriteria copy() {
        return new CollateralInformationCriteria(this);
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

    public StringFilter getCollateralId() {
        return collateralId;
    }

    public StringFilter collateralId() {
        if (collateralId == null) {
            collateralId = new StringFilter();
        }
        return collateralId;
    }

    public void setCollateralId(StringFilter collateralId) {
        this.collateralId = collateralId;
    }

    public StringFilter getLoanContractId() {
        return loanContractId;
    }

    public StringFilter loanContractId() {
        if (loanContractId == null) {
            loanContractId = new StringFilter();
        }
        return loanContractId;
    }

    public void setLoanContractId(StringFilter loanContractId) {
        this.loanContractId = loanContractId;
    }

    public StringFilter getCustomerId() {
        return customerId;
    }

    public StringFilter customerId() {
        if (customerId == null) {
            customerId = new StringFilter();
        }
        return customerId;
    }

    public void setCustomerId(StringFilter customerId) {
        this.customerId = customerId;
    }

    public StringFilter getRegistrationPropertyNumber() {
        return registrationPropertyNumber;
    }

    public StringFilter registrationPropertyNumber() {
        if (registrationPropertyNumber == null) {
            registrationPropertyNumber = new StringFilter();
        }
        return registrationPropertyNumber;
    }

    public void setRegistrationPropertyNumber(StringFilter registrationPropertyNumber) {
        this.registrationPropertyNumber = registrationPropertyNumber;
    }

    public BigDecimalFilter getCollateralOMVInCCY() {
        return collateralOMVInCCY;
    }

    public BigDecimalFilter collateralOMVInCCY() {
        if (collateralOMVInCCY == null) {
            collateralOMVInCCY = new BigDecimalFilter();
        }
        return collateralOMVInCCY;
    }

    public void setCollateralOMVInCCY(BigDecimalFilter collateralOMVInCCY) {
        this.collateralOMVInCCY = collateralOMVInCCY;
    }

    public BigDecimalFilter getCollateralFSVInLCY() {
        return collateralFSVInLCY;
    }

    public BigDecimalFilter collateralFSVInLCY() {
        if (collateralFSVInLCY == null) {
            collateralFSVInLCY = new BigDecimalFilter();
        }
        return collateralFSVInLCY;
    }

    public void setCollateralFSVInLCY(BigDecimalFilter collateralFSVInLCY) {
        this.collateralFSVInLCY = collateralFSVInLCY;
    }

    public BigDecimalFilter getCollateralDiscountedValue() {
        return collateralDiscountedValue;
    }

    public BigDecimalFilter collateralDiscountedValue() {
        if (collateralDiscountedValue == null) {
            collateralDiscountedValue = new BigDecimalFilter();
        }
        return collateralDiscountedValue;
    }

    public void setCollateralDiscountedValue(BigDecimalFilter collateralDiscountedValue) {
        this.collateralDiscountedValue = collateralDiscountedValue;
    }

    public BigDecimalFilter getAmountCharged() {
        return amountCharged;
    }

    public BigDecimalFilter amountCharged() {
        if (amountCharged == null) {
            amountCharged = new BigDecimalFilter();
        }
        return amountCharged;
    }

    public void setAmountCharged(BigDecimalFilter amountCharged) {
        this.amountCharged = amountCharged;
    }

    public DoubleFilter getCollateralDiscountRate() {
        return collateralDiscountRate;
    }

    public DoubleFilter collateralDiscountRate() {
        if (collateralDiscountRate == null) {
            collateralDiscountRate = new DoubleFilter();
        }
        return collateralDiscountRate;
    }

    public void setCollateralDiscountRate(DoubleFilter collateralDiscountRate) {
        this.collateralDiscountRate = collateralDiscountRate;
    }

    public DoubleFilter getLoanToValueRatio() {
        return loanToValueRatio;
    }

    public DoubleFilter loanToValueRatio() {
        if (loanToValueRatio == null) {
            loanToValueRatio = new DoubleFilter();
        }
        return loanToValueRatio;
    }

    public void setLoanToValueRatio(DoubleFilter loanToValueRatio) {
        this.loanToValueRatio = loanToValueRatio;
    }

    public StringFilter getNameOfPropertyValuer() {
        return nameOfPropertyValuer;
    }

    public StringFilter nameOfPropertyValuer() {
        if (nameOfPropertyValuer == null) {
            nameOfPropertyValuer = new StringFilter();
        }
        return nameOfPropertyValuer;
    }

    public void setNameOfPropertyValuer(StringFilter nameOfPropertyValuer) {
        this.nameOfPropertyValuer = nameOfPropertyValuer;
    }

    public LocalDateFilter getCollateralLastValuationDate() {
        return collateralLastValuationDate;
    }

    public LocalDateFilter collateralLastValuationDate() {
        if (collateralLastValuationDate == null) {
            collateralLastValuationDate = new LocalDateFilter();
        }
        return collateralLastValuationDate;
    }

    public void setCollateralLastValuationDate(LocalDateFilter collateralLastValuationDate) {
        this.collateralLastValuationDate = collateralLastValuationDate;
    }

    public CollateralInsuredFlagTypesFilter getInsuredFlag() {
        return insuredFlag;
    }

    public CollateralInsuredFlagTypesFilter insuredFlag() {
        if (insuredFlag == null) {
            insuredFlag = new CollateralInsuredFlagTypesFilter();
        }
        return insuredFlag;
    }

    public void setInsuredFlag(CollateralInsuredFlagTypesFilter insuredFlag) {
        this.insuredFlag = insuredFlag;
    }

    public StringFilter getNameOfInsurer() {
        return nameOfInsurer;
    }

    public StringFilter nameOfInsurer() {
        if (nameOfInsurer == null) {
            nameOfInsurer = new StringFilter();
        }
        return nameOfInsurer;
    }

    public void setNameOfInsurer(StringFilter nameOfInsurer) {
        this.nameOfInsurer = nameOfInsurer;
    }

    public BigDecimalFilter getAmountInsured() {
        return amountInsured;
    }

    public BigDecimalFilter amountInsured() {
        if (amountInsured == null) {
            amountInsured = new BigDecimalFilter();
        }
        return amountInsured;
    }

    public void setAmountInsured(BigDecimalFilter amountInsured) {
        this.amountInsured = amountInsured;
    }

    public LocalDateFilter getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public LocalDateFilter insuranceExpiryDate() {
        if (insuranceExpiryDate == null) {
            insuranceExpiryDate = new LocalDateFilter();
        }
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(LocalDateFilter insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public StringFilter getGuaranteeInsurers() {
        return guaranteeInsurers;
    }

    public StringFilter guaranteeInsurers() {
        if (guaranteeInsurers == null) {
            guaranteeInsurers = new StringFilter();
        }
        return guaranteeInsurers;
    }

    public void setGuaranteeInsurers(StringFilter guaranteeInsurers) {
        this.guaranteeInsurers = guaranteeInsurers;
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

    public LongFilter getBranchCodeId() {
        return branchCodeId;
    }

    public LongFilter branchCodeId() {
        if (branchCodeId == null) {
            branchCodeId = new LongFilter();
        }
        return branchCodeId;
    }

    public void setBranchCodeId(LongFilter branchCodeId) {
        this.branchCodeId = branchCodeId;
    }

    public LongFilter getCollateralTypeId() {
        return collateralTypeId;
    }

    public LongFilter collateralTypeId() {
        if (collateralTypeId == null) {
            collateralTypeId = new LongFilter();
        }
        return collateralTypeId;
    }

    public void setCollateralTypeId(LongFilter collateralTypeId) {
        this.collateralTypeId = collateralTypeId;
    }

    public LongFilter getCountyCodeId() {
        return countyCodeId;
    }

    public LongFilter countyCodeId() {
        if (countyCodeId == null) {
            countyCodeId = new LongFilter();
        }
        return countyCodeId;
    }

    public void setCountyCodeId(LongFilter countyCodeId) {
        this.countyCodeId = countyCodeId;
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
        final CollateralInformationCriteria that = (CollateralInformationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(collateralId, that.collateralId) &&
            Objects.equals(loanContractId, that.loanContractId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(registrationPropertyNumber, that.registrationPropertyNumber) &&
            Objects.equals(collateralOMVInCCY, that.collateralOMVInCCY) &&
            Objects.equals(collateralFSVInLCY, that.collateralFSVInLCY) &&
            Objects.equals(collateralDiscountedValue, that.collateralDiscountedValue) &&
            Objects.equals(amountCharged, that.amountCharged) &&
            Objects.equals(collateralDiscountRate, that.collateralDiscountRate) &&
            Objects.equals(loanToValueRatio, that.loanToValueRatio) &&
            Objects.equals(nameOfPropertyValuer, that.nameOfPropertyValuer) &&
            Objects.equals(collateralLastValuationDate, that.collateralLastValuationDate) &&
            Objects.equals(insuredFlag, that.insuredFlag) &&
            Objects.equals(nameOfInsurer, that.nameOfInsurer) &&
            Objects.equals(amountInsured, that.amountInsured) &&
            Objects.equals(insuranceExpiryDate, that.insuranceExpiryDate) &&
            Objects.equals(guaranteeInsurers, that.guaranteeInsurers) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(branchCodeId, that.branchCodeId) &&
            Objects.equals(collateralTypeId, that.collateralTypeId) &&
            Objects.equals(countyCodeId, that.countyCodeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportingDate,
            collateralId,
            loanContractId,
            customerId,
            registrationPropertyNumber,
            collateralOMVInCCY,
            collateralFSVInLCY,
            collateralDiscountedValue,
            amountCharged,
            collateralDiscountRate,
            loanToValueRatio,
            nameOfPropertyValuer,
            collateralLastValuationDate,
            insuredFlag,
            nameOfInsurer,
            amountInsured,
            insuranceExpiryDate,
            guaranteeInsurers,
            bankCodeId,
            branchCodeId,
            collateralTypeId,
            countyCodeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollateralInformationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (collateralId != null ? "collateralId=" + collateralId + ", " : "") +
            (loanContractId != null ? "loanContractId=" + loanContractId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (registrationPropertyNumber != null ? "registrationPropertyNumber=" + registrationPropertyNumber + ", " : "") +
            (collateralOMVInCCY != null ? "collateralOMVInCCY=" + collateralOMVInCCY + ", " : "") +
            (collateralFSVInLCY != null ? "collateralFSVInLCY=" + collateralFSVInLCY + ", " : "") +
            (collateralDiscountedValue != null ? "collateralDiscountedValue=" + collateralDiscountedValue + ", " : "") +
            (amountCharged != null ? "amountCharged=" + amountCharged + ", " : "") +
            (collateralDiscountRate != null ? "collateralDiscountRate=" + collateralDiscountRate + ", " : "") +
            (loanToValueRatio != null ? "loanToValueRatio=" + loanToValueRatio + ", " : "") +
            (nameOfPropertyValuer != null ? "nameOfPropertyValuer=" + nameOfPropertyValuer + ", " : "") +
            (collateralLastValuationDate != null ? "collateralLastValuationDate=" + collateralLastValuationDate + ", " : "") +
            (insuredFlag != null ? "insuredFlag=" + insuredFlag + ", " : "") +
            (nameOfInsurer != null ? "nameOfInsurer=" + nameOfInsurer + ", " : "") +
            (amountInsured != null ? "amountInsured=" + amountInsured + ", " : "") +
            (insuranceExpiryDate != null ? "insuranceExpiryDate=" + insuranceExpiryDate + ", " : "") +
            (guaranteeInsurers != null ? "guaranteeInsurers=" + guaranteeInsurers + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (branchCodeId != null ? "branchCodeId=" + branchCodeId + ", " : "") +
            (collateralTypeId != null ? "collateralTypeId=" + collateralTypeId + ", " : "") +
            (countyCodeId != null ? "countyCodeId=" + countyCodeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
