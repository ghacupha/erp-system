package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
 * Criteria class for the {@link io.github.erp.domain.WIPListItem} entity. This class is used
 * in {@link io.github.erp.web.rest.WIPListItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /wip-list-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WIPListItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sequenceNumber;

    private StringFilter particulars;

    private LocalDateFilter instalmentDate;

    private BigDecimalFilter instalmentAmount;

    private StringFilter settlementCurrency;

    private StringFilter outletCode;

    private StringFilter settlementTransaction;

    private LocalDateFilter settlementTransactionDate;

    private StringFilter dealerName;

    private StringFilter workProject;

    private Boolean distinct;

    public WIPListItemCriteria() {}

    public WIPListItemCriteria(WIPListItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sequenceNumber = other.sequenceNumber == null ? null : other.sequenceNumber.copy();
        this.particulars = other.particulars == null ? null : other.particulars.copy();
        this.instalmentDate = other.instalmentDate == null ? null : other.instalmentDate.copy();
        this.instalmentAmount = other.instalmentAmount == null ? null : other.instalmentAmount.copy();
        this.settlementCurrency = other.settlementCurrency == null ? null : other.settlementCurrency.copy();
        this.outletCode = other.outletCode == null ? null : other.outletCode.copy();
        this.settlementTransaction = other.settlementTransaction == null ? null : other.settlementTransaction.copy();
        this.settlementTransactionDate = other.settlementTransactionDate == null ? null : other.settlementTransactionDate.copy();
        this.dealerName = other.dealerName == null ? null : other.dealerName.copy();
        this.workProject = other.workProject == null ? null : other.workProject.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WIPListItemCriteria copy() {
        return new WIPListItemCriteria(this);
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

    public StringFilter getSequenceNumber() {
        return sequenceNumber;
    }

    public StringFilter sequenceNumber() {
        if (sequenceNumber == null) {
            sequenceNumber = new StringFilter();
        }
        return sequenceNumber;
    }

    public void setSequenceNumber(StringFilter sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public StringFilter getParticulars() {
        return particulars;
    }

    public StringFilter particulars() {
        if (particulars == null) {
            particulars = new StringFilter();
        }
        return particulars;
    }

    public void setParticulars(StringFilter particulars) {
        this.particulars = particulars;
    }

    public LocalDateFilter getInstalmentDate() {
        return instalmentDate;
    }

    public LocalDateFilter instalmentDate() {
        if (instalmentDate == null) {
            instalmentDate = new LocalDateFilter();
        }
        return instalmentDate;
    }

    public void setInstalmentDate(LocalDateFilter instalmentDate) {
        this.instalmentDate = instalmentDate;
    }

    public BigDecimalFilter getInstalmentAmount() {
        return instalmentAmount;
    }

    public BigDecimalFilter instalmentAmount() {
        if (instalmentAmount == null) {
            instalmentAmount = new BigDecimalFilter();
        }
        return instalmentAmount;
    }

    public void setInstalmentAmount(BigDecimalFilter instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public StringFilter getSettlementCurrency() {
        return settlementCurrency;
    }

    public StringFilter settlementCurrency() {
        if (settlementCurrency == null) {
            settlementCurrency = new StringFilter();
        }
        return settlementCurrency;
    }

    public void setSettlementCurrency(StringFilter settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public StringFilter getOutletCode() {
        return outletCode;
    }

    public StringFilter outletCode() {
        if (outletCode == null) {
            outletCode = new StringFilter();
        }
        return outletCode;
    }

    public void setOutletCode(StringFilter outletCode) {
        this.outletCode = outletCode;
    }

    public StringFilter getSettlementTransaction() {
        return settlementTransaction;
    }

    public StringFilter settlementTransaction() {
        if (settlementTransaction == null) {
            settlementTransaction = new StringFilter();
        }
        return settlementTransaction;
    }

    public void setSettlementTransaction(StringFilter settlementTransaction) {
        this.settlementTransaction = settlementTransaction;
    }

    public LocalDateFilter getSettlementTransactionDate() {
        return settlementTransactionDate;
    }

    public LocalDateFilter settlementTransactionDate() {
        if (settlementTransactionDate == null) {
            settlementTransactionDate = new LocalDateFilter();
        }
        return settlementTransactionDate;
    }

    public void setSettlementTransactionDate(LocalDateFilter settlementTransactionDate) {
        this.settlementTransactionDate = settlementTransactionDate;
    }

    public StringFilter getDealerName() {
        return dealerName;
    }

    public StringFilter dealerName() {
        if (dealerName == null) {
            dealerName = new StringFilter();
        }
        return dealerName;
    }

    public void setDealerName(StringFilter dealerName) {
        this.dealerName = dealerName;
    }

    public StringFilter getWorkProject() {
        return workProject;
    }

    public StringFilter workProject() {
        if (workProject == null) {
            workProject = new StringFilter();
        }
        return workProject;
    }

    public void setWorkProject(StringFilter workProject) {
        this.workProject = workProject;
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
        final WIPListItemCriteria that = (WIPListItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNumber, that.sequenceNumber) &&
            Objects.equals(particulars, that.particulars) &&
            Objects.equals(instalmentDate, that.instalmentDate) &&
            Objects.equals(instalmentAmount, that.instalmentAmount) &&
            Objects.equals(settlementCurrency, that.settlementCurrency) &&
            Objects.equals(outletCode, that.outletCode) &&
            Objects.equals(settlementTransaction, that.settlementTransaction) &&
            Objects.equals(settlementTransactionDate, that.settlementTransactionDate) &&
            Objects.equals(dealerName, that.dealerName) &&
            Objects.equals(workProject, that.workProject) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sequenceNumber,
            particulars,
            instalmentDate,
            instalmentAmount,
            settlementCurrency,
            outletCode,
            settlementTransaction,
            settlementTransactionDate,
            dealerName,
            workProject,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WIPListItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber + ", " : "") +
            (particulars != null ? "particulars=" + particulars + ", " : "") +
            (instalmentDate != null ? "instalmentDate=" + instalmentDate + ", " : "") +
            (instalmentAmount != null ? "instalmentAmount=" + instalmentAmount + ", " : "") +
            (settlementCurrency != null ? "settlementCurrency=" + settlementCurrency + ", " : "") +
            (outletCode != null ? "outletCode=" + outletCode + ", " : "") +
            (settlementTransaction != null ? "settlementTransaction=" + settlementTransaction + ", " : "") +
            (settlementTransactionDate != null ? "settlementTransactionDate=" + settlementTransactionDate + ", " : "") +
            (dealerName != null ? "dealerName=" + dealerName + ", " : "") +
            (workProject != null ? "workProject=" + workProject + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
