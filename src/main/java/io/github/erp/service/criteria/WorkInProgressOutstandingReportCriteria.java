package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
 * Criteria class for the {@link io.github.erp.domain.WorkInProgressOutstandingReport} entity. This class is used
 * in {@link io.github.erp.web.rest.WorkInProgressOutstandingReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /work-in-progress-outstanding-reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkInProgressOutstandingReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sequenceNumber;

    private StringFilter particulars;

    private StringFilter dealerName;

    private StringFilter instalmentTransactionNumber;

    private LocalDateFilter instalmentTransactionDate;

    private StringFilter iso4217Code;

    private BigDecimalFilter instalmentAmount;

    private BigDecimalFilter totalTransferAmount;

    private BigDecimalFilter outstandingAmount;

    private Boolean distinct;

    public WorkInProgressOutstandingReportCriteria() {}

    public WorkInProgressOutstandingReportCriteria(WorkInProgressOutstandingReportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sequenceNumber = other.sequenceNumber == null ? null : other.sequenceNumber.copy();
        this.particulars = other.particulars == null ? null : other.particulars.copy();
        this.dealerName = other.dealerName == null ? null : other.dealerName.copy();
        this.instalmentTransactionNumber = other.instalmentTransactionNumber == null ? null : other.instalmentTransactionNumber.copy();
        this.instalmentTransactionDate = other.instalmentTransactionDate == null ? null : other.instalmentTransactionDate.copy();
        this.iso4217Code = other.iso4217Code == null ? null : other.iso4217Code.copy();
        this.instalmentAmount = other.instalmentAmount == null ? null : other.instalmentAmount.copy();
        this.totalTransferAmount = other.totalTransferAmount == null ? null : other.totalTransferAmount.copy();
        this.outstandingAmount = other.outstandingAmount == null ? null : other.outstandingAmount.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WorkInProgressOutstandingReportCriteria copy() {
        return new WorkInProgressOutstandingReportCriteria(this);
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

    public StringFilter getInstalmentTransactionNumber() {
        return instalmentTransactionNumber;
    }

    public StringFilter instalmentTransactionNumber() {
        if (instalmentTransactionNumber == null) {
            instalmentTransactionNumber = new StringFilter();
        }
        return instalmentTransactionNumber;
    }

    public void setInstalmentTransactionNumber(StringFilter instalmentTransactionNumber) {
        this.instalmentTransactionNumber = instalmentTransactionNumber;
    }

    public LocalDateFilter getInstalmentTransactionDate() {
        return instalmentTransactionDate;
    }

    public LocalDateFilter instalmentTransactionDate() {
        if (instalmentTransactionDate == null) {
            instalmentTransactionDate = new LocalDateFilter();
        }
        return instalmentTransactionDate;
    }

    public void setInstalmentTransactionDate(LocalDateFilter instalmentTransactionDate) {
        this.instalmentTransactionDate = instalmentTransactionDate;
    }

    public StringFilter getIso4217Code() {
        return iso4217Code;
    }

    public StringFilter iso4217Code() {
        if (iso4217Code == null) {
            iso4217Code = new StringFilter();
        }
        return iso4217Code;
    }

    public void setIso4217Code(StringFilter iso4217Code) {
        this.iso4217Code = iso4217Code;
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

    public BigDecimalFilter getTotalTransferAmount() {
        return totalTransferAmount;
    }

    public BigDecimalFilter totalTransferAmount() {
        if (totalTransferAmount == null) {
            totalTransferAmount = new BigDecimalFilter();
        }
        return totalTransferAmount;
    }

    public void setTotalTransferAmount(BigDecimalFilter totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }

    public BigDecimalFilter getOutstandingAmount() {
        return outstandingAmount;
    }

    public BigDecimalFilter outstandingAmount() {
        if (outstandingAmount == null) {
            outstandingAmount = new BigDecimalFilter();
        }
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimalFilter outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
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
        final WorkInProgressOutstandingReportCriteria that = (WorkInProgressOutstandingReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sequenceNumber, that.sequenceNumber) &&
            Objects.equals(particulars, that.particulars) &&
            Objects.equals(dealerName, that.dealerName) &&
            Objects.equals(instalmentTransactionNumber, that.instalmentTransactionNumber) &&
            Objects.equals(instalmentTransactionDate, that.instalmentTransactionDate) &&
            Objects.equals(iso4217Code, that.iso4217Code) &&
            Objects.equals(instalmentAmount, that.instalmentAmount) &&
            Objects.equals(totalTransferAmount, that.totalTransferAmount) &&
            Objects.equals(outstandingAmount, that.outstandingAmount) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sequenceNumber,
            particulars,
            dealerName,
            instalmentTransactionNumber,
            instalmentTransactionDate,
            iso4217Code,
            instalmentAmount,
            totalTransferAmount,
            outstandingAmount,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressOutstandingReportCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber + ", " : "") +
            (particulars != null ? "particulars=" + particulars + ", " : "") +
            (dealerName != null ? "dealerName=" + dealerName + ", " : "") +
            (instalmentTransactionNumber != null ? "instalmentTransactionNumber=" + instalmentTransactionNumber + ", " : "") +
            (instalmentTransactionDate != null ? "instalmentTransactionDate=" + instalmentTransactionDate + ", " : "") +
            (iso4217Code != null ? "iso4217Code=" + iso4217Code + ", " : "") +
            (instalmentAmount != null ? "instalmentAmount=" + instalmentAmount + ", " : "") +
            (totalTransferAmount != null ? "totalTransferAmount=" + totalTransferAmount + ", " : "") +
            (outstandingAmount != null ? "outstandingAmount=" + outstandingAmount + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
