package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
 * Criteria class for the {@link io.github.erp.domain.AccountBalance} entity. This class is used
 * in {@link io.github.erp.web.rest.AccountBalanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /account-balances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AccountBalanceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private StringFilter customerId;

    private StringFilter accountContractNumber;

    private BigDecimalFilter accruedInterestBalanceFCY;

    private BigDecimalFilter accruedInterestBalanceLCY;

    private BigDecimalFilter accountBalanceFCY;

    private BigDecimalFilter accountBalanceLCY;

    private LongFilter bankCodeId;

    private LongFilter branchIdId;

    private LongFilter currencyCodeId;

    private Boolean distinct;

    public AccountBalanceCriteria() {}

    public AccountBalanceCriteria(AccountBalanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.accountContractNumber = other.accountContractNumber == null ? null : other.accountContractNumber.copy();
        this.accruedInterestBalanceFCY = other.accruedInterestBalanceFCY == null ? null : other.accruedInterestBalanceFCY.copy();
        this.accruedInterestBalanceLCY = other.accruedInterestBalanceLCY == null ? null : other.accruedInterestBalanceLCY.copy();
        this.accountBalanceFCY = other.accountBalanceFCY == null ? null : other.accountBalanceFCY.copy();
        this.accountBalanceLCY = other.accountBalanceLCY == null ? null : other.accountBalanceLCY.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.branchIdId = other.branchIdId == null ? null : other.branchIdId.copy();
        this.currencyCodeId = other.currencyCodeId == null ? null : other.currencyCodeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AccountBalanceCriteria copy() {
        return new AccountBalanceCriteria(this);
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

    public BigDecimalFilter getAccruedInterestBalanceFCY() {
        return accruedInterestBalanceFCY;
    }

    public BigDecimalFilter accruedInterestBalanceFCY() {
        if (accruedInterestBalanceFCY == null) {
            accruedInterestBalanceFCY = new BigDecimalFilter();
        }
        return accruedInterestBalanceFCY;
    }

    public void setAccruedInterestBalanceFCY(BigDecimalFilter accruedInterestBalanceFCY) {
        this.accruedInterestBalanceFCY = accruedInterestBalanceFCY;
    }

    public BigDecimalFilter getAccruedInterestBalanceLCY() {
        return accruedInterestBalanceLCY;
    }

    public BigDecimalFilter accruedInterestBalanceLCY() {
        if (accruedInterestBalanceLCY == null) {
            accruedInterestBalanceLCY = new BigDecimalFilter();
        }
        return accruedInterestBalanceLCY;
    }

    public void setAccruedInterestBalanceLCY(BigDecimalFilter accruedInterestBalanceLCY) {
        this.accruedInterestBalanceLCY = accruedInterestBalanceLCY;
    }

    public BigDecimalFilter getAccountBalanceFCY() {
        return accountBalanceFCY;
    }

    public BigDecimalFilter accountBalanceFCY() {
        if (accountBalanceFCY == null) {
            accountBalanceFCY = new BigDecimalFilter();
        }
        return accountBalanceFCY;
    }

    public void setAccountBalanceFCY(BigDecimalFilter accountBalanceFCY) {
        this.accountBalanceFCY = accountBalanceFCY;
    }

    public BigDecimalFilter getAccountBalanceLCY() {
        return accountBalanceLCY;
    }

    public BigDecimalFilter accountBalanceLCY() {
        if (accountBalanceLCY == null) {
            accountBalanceLCY = new BigDecimalFilter();
        }
        return accountBalanceLCY;
    }

    public void setAccountBalanceLCY(BigDecimalFilter accountBalanceLCY) {
        this.accountBalanceLCY = accountBalanceLCY;
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

    public LongFilter getBranchIdId() {
        return branchIdId;
    }

    public LongFilter branchIdId() {
        if (branchIdId == null) {
            branchIdId = new LongFilter();
        }
        return branchIdId;
    }

    public void setBranchIdId(LongFilter branchIdId) {
        this.branchIdId = branchIdId;
    }

    public LongFilter getCurrencyCodeId() {
        return currencyCodeId;
    }

    public LongFilter currencyCodeId() {
        if (currencyCodeId == null) {
            currencyCodeId = new LongFilter();
        }
        return currencyCodeId;
    }

    public void setCurrencyCodeId(LongFilter currencyCodeId) {
        this.currencyCodeId = currencyCodeId;
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
        final AccountBalanceCriteria that = (AccountBalanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(accountContractNumber, that.accountContractNumber) &&
            Objects.equals(accruedInterestBalanceFCY, that.accruedInterestBalanceFCY) &&
            Objects.equals(accruedInterestBalanceLCY, that.accruedInterestBalanceLCY) &&
            Objects.equals(accountBalanceFCY, that.accountBalanceFCY) &&
            Objects.equals(accountBalanceLCY, that.accountBalanceLCY) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(branchIdId, that.branchIdId) &&
            Objects.equals(currencyCodeId, that.currencyCodeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportingDate,
            customerId,
            accountContractNumber,
            accruedInterestBalanceFCY,
            accruedInterestBalanceLCY,
            accountBalanceFCY,
            accountBalanceLCY,
            bankCodeId,
            branchIdId,
            currencyCodeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountBalanceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (accountContractNumber != null ? "accountContractNumber=" + accountContractNumber + ", " : "") +
            (accruedInterestBalanceFCY != null ? "accruedInterestBalanceFCY=" + accruedInterestBalanceFCY + ", " : "") +
            (accruedInterestBalanceLCY != null ? "accruedInterestBalanceLCY=" + accruedInterestBalanceLCY + ", " : "") +
            (accountBalanceFCY != null ? "accountBalanceFCY=" + accountBalanceFCY + ", " : "") +
            (accountBalanceLCY != null ? "accountBalanceLCY=" + accountBalanceLCY + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (branchIdId != null ? "branchIdId=" + branchIdId + ", " : "") +
            (currencyCodeId != null ? "currencyCodeId=" + currencyCodeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
