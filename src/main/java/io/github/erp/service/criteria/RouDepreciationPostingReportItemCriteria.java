package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.RouDepreciationPostingReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.RouDepreciationPostingReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-depreciation-posting-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouDepreciationPostingReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter leaseContractNumber;

    private StringFilter leaseDescription;

    private StringFilter fiscalMonthCode;

    private StringFilter accountForCredit;

    private StringFilter accountForDebit;

    private BigDecimalFilter depreciationAmount;

    private Boolean distinct;

    public RouDepreciationPostingReportItemCriteria() {}

    public RouDepreciationPostingReportItemCriteria(RouDepreciationPostingReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.leaseContractNumber = other.leaseContractNumber == null ? null : other.leaseContractNumber.copy();
        this.leaseDescription = other.leaseDescription == null ? null : other.leaseDescription.copy();
        this.fiscalMonthCode = other.fiscalMonthCode == null ? null : other.fiscalMonthCode.copy();
        this.accountForCredit = other.accountForCredit == null ? null : other.accountForCredit.copy();
        this.accountForDebit = other.accountForDebit == null ? null : other.accountForDebit.copy();
        this.depreciationAmount = other.depreciationAmount == null ? null : other.depreciationAmount.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouDepreciationPostingReportItemCriteria copy() {
        return new RouDepreciationPostingReportItemCriteria(this);
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

    public StringFilter getLeaseContractNumber() {
        return leaseContractNumber;
    }

    public StringFilter leaseContractNumber() {
        if (leaseContractNumber == null) {
            leaseContractNumber = new StringFilter();
        }
        return leaseContractNumber;
    }

    public void setLeaseContractNumber(StringFilter leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public StringFilter getLeaseDescription() {
        return leaseDescription;
    }

    public StringFilter leaseDescription() {
        if (leaseDescription == null) {
            leaseDescription = new StringFilter();
        }
        return leaseDescription;
    }

    public void setLeaseDescription(StringFilter leaseDescription) {
        this.leaseDescription = leaseDescription;
    }

    public StringFilter getFiscalMonthCode() {
        return fiscalMonthCode;
    }

    public StringFilter fiscalMonthCode() {
        if (fiscalMonthCode == null) {
            fiscalMonthCode = new StringFilter();
        }
        return fiscalMonthCode;
    }

    public void setFiscalMonthCode(StringFilter fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public StringFilter getAccountForCredit() {
        return accountForCredit;
    }

    public StringFilter accountForCredit() {
        if (accountForCredit == null) {
            accountForCredit = new StringFilter();
        }
        return accountForCredit;
    }

    public void setAccountForCredit(StringFilter accountForCredit) {
        this.accountForCredit = accountForCredit;
    }

    public StringFilter getAccountForDebit() {
        return accountForDebit;
    }

    public StringFilter accountForDebit() {
        if (accountForDebit == null) {
            accountForDebit = new StringFilter();
        }
        return accountForDebit;
    }

    public void setAccountForDebit(StringFilter accountForDebit) {
        this.accountForDebit = accountForDebit;
    }

    public BigDecimalFilter getDepreciationAmount() {
        return depreciationAmount;
    }

    public BigDecimalFilter depreciationAmount() {
        if (depreciationAmount == null) {
            depreciationAmount = new BigDecimalFilter();
        }
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimalFilter depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
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
        final RouDepreciationPostingReportItemCriteria that = (RouDepreciationPostingReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(leaseContractNumber, that.leaseContractNumber) &&
            Objects.equals(leaseDescription, that.leaseDescription) &&
            Objects.equals(fiscalMonthCode, that.fiscalMonthCode) &&
            Objects.equals(accountForCredit, that.accountForCredit) &&
            Objects.equals(accountForDebit, that.accountForDebit) &&
            Objects.equals(depreciationAmount, that.depreciationAmount) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            leaseContractNumber,
            leaseDescription,
            fiscalMonthCode,
            accountForCredit,
            accountForDebit,
            depreciationAmount,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationPostingReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (leaseContractNumber != null ? "leaseContractNumber=" + leaseContractNumber + ", " : "") +
            (leaseDescription != null ? "leaseDescription=" + leaseDescription + ", " : "") +
            (fiscalMonthCode != null ? "fiscalMonthCode=" + fiscalMonthCode + ", " : "") +
            (accountForCredit != null ? "accountForCredit=" + accountForCredit + ", " : "") +
            (accountForDebit != null ? "accountForDebit=" + accountForDebit + ", " : "") +
            (depreciationAmount != null ? "depreciationAmount=" + depreciationAmount + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
