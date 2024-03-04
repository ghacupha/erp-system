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
 * Criteria class for the {@link io.github.erp.domain.AccountAttribute} entity. This class is used
 * in {@link io.github.erp.web.rest.AccountAttributeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /account-attributes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AccountAttributeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private StringFilter customerNumber;

    private StringFilter accountContractNumber;

    private StringFilter accountName;

    private LocalDateFilter accountOpeningDate;

    private LocalDateFilter accountClosingDate;

    private BigDecimalFilter debitInterestRate;

    private BigDecimalFilter creditInterestRate;

    private BigDecimalFilter sanctionedAccountLimitFcy;

    private BigDecimalFilter sanctionedAccountLimitLcy;

    private LocalDateFilter accountStatusChangeDate;

    private LocalDateFilter expiryDate;

    private LongFilter bankCodeId;

    private LongFilter branchCodeId;

    private LongFilter accountOwnershipTypeId;

    private Boolean distinct;

    public AccountAttributeCriteria() {}

    public AccountAttributeCriteria(AccountAttributeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.customerNumber = other.customerNumber == null ? null : other.customerNumber.copy();
        this.accountContractNumber = other.accountContractNumber == null ? null : other.accountContractNumber.copy();
        this.accountName = other.accountName == null ? null : other.accountName.copy();
        this.accountOpeningDate = other.accountOpeningDate == null ? null : other.accountOpeningDate.copy();
        this.accountClosingDate = other.accountClosingDate == null ? null : other.accountClosingDate.copy();
        this.debitInterestRate = other.debitInterestRate == null ? null : other.debitInterestRate.copy();
        this.creditInterestRate = other.creditInterestRate == null ? null : other.creditInterestRate.copy();
        this.sanctionedAccountLimitFcy = other.sanctionedAccountLimitFcy == null ? null : other.sanctionedAccountLimitFcy.copy();
        this.sanctionedAccountLimitLcy = other.sanctionedAccountLimitLcy == null ? null : other.sanctionedAccountLimitLcy.copy();
        this.accountStatusChangeDate = other.accountStatusChangeDate == null ? null : other.accountStatusChangeDate.copy();
        this.expiryDate = other.expiryDate == null ? null : other.expiryDate.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.branchCodeId = other.branchCodeId == null ? null : other.branchCodeId.copy();
        this.accountOwnershipTypeId = other.accountOwnershipTypeId == null ? null : other.accountOwnershipTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AccountAttributeCriteria copy() {
        return new AccountAttributeCriteria(this);
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

    public StringFilter getCustomerNumber() {
        return customerNumber;
    }

    public StringFilter customerNumber() {
        if (customerNumber == null) {
            customerNumber = new StringFilter();
        }
        return customerNumber;
    }

    public void setCustomerNumber(StringFilter customerNumber) {
        this.customerNumber = customerNumber;
    }

    public StringFilter getAccountContractNumber() {
        return accountContractNumber;
    }

    public StringFilter accountContractNumber() {
        if (accountContractNumber == null) {
            accountContractNumber = new StringFilter();
        }
        return accountContractNumber;
    }

    public void setAccountContractNumber(StringFilter accountContractNumber) {
        this.accountContractNumber = accountContractNumber;
    }

    public StringFilter getAccountName() {
        return accountName;
    }

    public StringFilter accountName() {
        if (accountName == null) {
            accountName = new StringFilter();
        }
        return accountName;
    }

    public void setAccountName(StringFilter accountName) {
        this.accountName = accountName;
    }

    public LocalDateFilter getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public LocalDateFilter accountOpeningDate() {
        if (accountOpeningDate == null) {
            accountOpeningDate = new LocalDateFilter();
        }
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(LocalDateFilter accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    public LocalDateFilter getAccountClosingDate() {
        return accountClosingDate;
    }

    public LocalDateFilter accountClosingDate() {
        if (accountClosingDate == null) {
            accountClosingDate = new LocalDateFilter();
        }
        return accountClosingDate;
    }

    public void setAccountClosingDate(LocalDateFilter accountClosingDate) {
        this.accountClosingDate = accountClosingDate;
    }

    public BigDecimalFilter getDebitInterestRate() {
        return debitInterestRate;
    }

    public BigDecimalFilter debitInterestRate() {
        if (debitInterestRate == null) {
            debitInterestRate = new BigDecimalFilter();
        }
        return debitInterestRate;
    }

    public void setDebitInterestRate(BigDecimalFilter debitInterestRate) {
        this.debitInterestRate = debitInterestRate;
    }

    public BigDecimalFilter getCreditInterestRate() {
        return creditInterestRate;
    }

    public BigDecimalFilter creditInterestRate() {
        if (creditInterestRate == null) {
            creditInterestRate = new BigDecimalFilter();
        }
        return creditInterestRate;
    }

    public void setCreditInterestRate(BigDecimalFilter creditInterestRate) {
        this.creditInterestRate = creditInterestRate;
    }

    public BigDecimalFilter getSanctionedAccountLimitFcy() {
        return sanctionedAccountLimitFcy;
    }

    public BigDecimalFilter sanctionedAccountLimitFcy() {
        if (sanctionedAccountLimitFcy == null) {
            sanctionedAccountLimitFcy = new BigDecimalFilter();
        }
        return sanctionedAccountLimitFcy;
    }

    public void setSanctionedAccountLimitFcy(BigDecimalFilter sanctionedAccountLimitFcy) {
        this.sanctionedAccountLimitFcy = sanctionedAccountLimitFcy;
    }

    public BigDecimalFilter getSanctionedAccountLimitLcy() {
        return sanctionedAccountLimitLcy;
    }

    public BigDecimalFilter sanctionedAccountLimitLcy() {
        if (sanctionedAccountLimitLcy == null) {
            sanctionedAccountLimitLcy = new BigDecimalFilter();
        }
        return sanctionedAccountLimitLcy;
    }

    public void setSanctionedAccountLimitLcy(BigDecimalFilter sanctionedAccountLimitLcy) {
        this.sanctionedAccountLimitLcy = sanctionedAccountLimitLcy;
    }

    public LocalDateFilter getAccountStatusChangeDate() {
        return accountStatusChangeDate;
    }

    public LocalDateFilter accountStatusChangeDate() {
        if (accountStatusChangeDate == null) {
            accountStatusChangeDate = new LocalDateFilter();
        }
        return accountStatusChangeDate;
    }

    public void setAccountStatusChangeDate(LocalDateFilter accountStatusChangeDate) {
        this.accountStatusChangeDate = accountStatusChangeDate;
    }

    public LocalDateFilter getExpiryDate() {
        return expiryDate;
    }

    public LocalDateFilter expiryDate() {
        if (expiryDate == null) {
            expiryDate = new LocalDateFilter();
        }
        return expiryDate;
    }

    public void setExpiryDate(LocalDateFilter expiryDate) {
        this.expiryDate = expiryDate;
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

    public LongFilter getAccountOwnershipTypeId() {
        return accountOwnershipTypeId;
    }

    public LongFilter accountOwnershipTypeId() {
        if (accountOwnershipTypeId == null) {
            accountOwnershipTypeId = new LongFilter();
        }
        return accountOwnershipTypeId;
    }

    public void setAccountOwnershipTypeId(LongFilter accountOwnershipTypeId) {
        this.accountOwnershipTypeId = accountOwnershipTypeId;
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
        final AccountAttributeCriteria that = (AccountAttributeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(customerNumber, that.customerNumber) &&
            Objects.equals(accountContractNumber, that.accountContractNumber) &&
            Objects.equals(accountName, that.accountName) &&
            Objects.equals(accountOpeningDate, that.accountOpeningDate) &&
            Objects.equals(accountClosingDate, that.accountClosingDate) &&
            Objects.equals(debitInterestRate, that.debitInterestRate) &&
            Objects.equals(creditInterestRate, that.creditInterestRate) &&
            Objects.equals(sanctionedAccountLimitFcy, that.sanctionedAccountLimitFcy) &&
            Objects.equals(sanctionedAccountLimitLcy, that.sanctionedAccountLimitLcy) &&
            Objects.equals(accountStatusChangeDate, that.accountStatusChangeDate) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(branchCodeId, that.branchCodeId) &&
            Objects.equals(accountOwnershipTypeId, that.accountOwnershipTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportingDate,
            customerNumber,
            accountContractNumber,
            accountName,
            accountOpeningDate,
            accountClosingDate,
            debitInterestRate,
            creditInterestRate,
            sanctionedAccountLimitFcy,
            sanctionedAccountLimitLcy,
            accountStatusChangeDate,
            expiryDate,
            bankCodeId,
            branchCodeId,
            accountOwnershipTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountAttributeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (customerNumber != null ? "customerNumber=" + customerNumber + ", " : "") +
            (accountContractNumber != null ? "accountContractNumber=" + accountContractNumber + ", " : "") +
            (accountName != null ? "accountName=" + accountName + ", " : "") +
            (accountOpeningDate != null ? "accountOpeningDate=" + accountOpeningDate + ", " : "") +
            (accountClosingDate != null ? "accountClosingDate=" + accountClosingDate + ", " : "") +
            (debitInterestRate != null ? "debitInterestRate=" + debitInterestRate + ", " : "") +
            (creditInterestRate != null ? "creditInterestRate=" + creditInterestRate + ", " : "") +
            (sanctionedAccountLimitFcy != null ? "sanctionedAccountLimitFcy=" + sanctionedAccountLimitFcy + ", " : "") +
            (sanctionedAccountLimitLcy != null ? "sanctionedAccountLimitLcy=" + sanctionedAccountLimitLcy + ", " : "") +
            (accountStatusChangeDate != null ? "accountStatusChangeDate=" + accountStatusChangeDate + ", " : "") +
            (expiryDate != null ? "expiryDate=" + expiryDate + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (branchCodeId != null ? "branchCodeId=" + branchCodeId + ", " : "") +
            (accountOwnershipTypeId != null ? "accountOwnershipTypeId=" + accountOwnershipTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
