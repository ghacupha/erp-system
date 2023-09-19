package io.github.erp.service.criteria;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
 * Criteria class for the {@link io.github.erp.domain.LeaseModelMetadata} entity. This class is used
 * in {@link io.github.erp.web.rest.LeaseModelMetadataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-model-metadata?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseModelMetadataCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter modelTitle;

    private DoubleFilter modelVersion;

    private StringFilter description;

    private DoubleFilter annualDiscountingRate;

    private LocalDateFilter commencementDate;

    private LocalDateFilter terminalDate;

    private DoubleFilter totalReportingPeriods;

    private DoubleFilter reportingPeriodsPerYear;

    private DoubleFilter settlementPeriodsPerYear;

    private BigDecimalFilter initialLiabilityAmount;

    private BigDecimalFilter initialROUAmount;

    private DoubleFilter totalDepreciationPeriods;

    private LongFilter placeholderId;

    private LongFilter leaseMappingId;

    private LongFilter leaseContractId;

    private LongFilter predecessorId;

    private LongFilter liabilityCurrencyId;

    private LongFilter rouAssetCurrencyId;

    private LongFilter modelAttachmentsId;

    private LongFilter securityClearanceId;

    private LongFilter leaseLiabilityAccountId;

    private LongFilter interestPayableAccountId;

    private LongFilter interestExpenseAccountId;

    private LongFilter rouAssetAccountId;

    private LongFilter rouDepreciationAccountId;

    private LongFilter accruedDepreciationAccountId;

    private Boolean distinct;

    public LeaseModelMetadataCriteria() {}

    public LeaseModelMetadataCriteria(LeaseModelMetadataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.modelTitle = other.modelTitle == null ? null : other.modelTitle.copy();
        this.modelVersion = other.modelVersion == null ? null : other.modelVersion.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.annualDiscountingRate = other.annualDiscountingRate == null ? null : other.annualDiscountingRate.copy();
        this.commencementDate = other.commencementDate == null ? null : other.commencementDate.copy();
        this.terminalDate = other.terminalDate == null ? null : other.terminalDate.copy();
        this.totalReportingPeriods = other.totalReportingPeriods == null ? null : other.totalReportingPeriods.copy();
        this.reportingPeriodsPerYear = other.reportingPeriodsPerYear == null ? null : other.reportingPeriodsPerYear.copy();
        this.settlementPeriodsPerYear = other.settlementPeriodsPerYear == null ? null : other.settlementPeriodsPerYear.copy();
        this.initialLiabilityAmount = other.initialLiabilityAmount == null ? null : other.initialLiabilityAmount.copy();
        this.initialROUAmount = other.initialROUAmount == null ? null : other.initialROUAmount.copy();
        this.totalDepreciationPeriods = other.totalDepreciationPeriods == null ? null : other.totalDepreciationPeriods.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.leaseMappingId = other.leaseMappingId == null ? null : other.leaseMappingId.copy();
        this.leaseContractId = other.leaseContractId == null ? null : other.leaseContractId.copy();
        this.predecessorId = other.predecessorId == null ? null : other.predecessorId.copy();
        this.liabilityCurrencyId = other.liabilityCurrencyId == null ? null : other.liabilityCurrencyId.copy();
        this.rouAssetCurrencyId = other.rouAssetCurrencyId == null ? null : other.rouAssetCurrencyId.copy();
        this.modelAttachmentsId = other.modelAttachmentsId == null ? null : other.modelAttachmentsId.copy();
        this.securityClearanceId = other.securityClearanceId == null ? null : other.securityClearanceId.copy();
        this.leaseLiabilityAccountId = other.leaseLiabilityAccountId == null ? null : other.leaseLiabilityAccountId.copy();
        this.interestPayableAccountId = other.interestPayableAccountId == null ? null : other.interestPayableAccountId.copy();
        this.interestExpenseAccountId = other.interestExpenseAccountId == null ? null : other.interestExpenseAccountId.copy();
        this.rouAssetAccountId = other.rouAssetAccountId == null ? null : other.rouAssetAccountId.copy();
        this.rouDepreciationAccountId = other.rouDepreciationAccountId == null ? null : other.rouDepreciationAccountId.copy();
        this.accruedDepreciationAccountId = other.accruedDepreciationAccountId == null ? null : other.accruedDepreciationAccountId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaseModelMetadataCriteria copy() {
        return new LeaseModelMetadataCriteria(this);
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

    public StringFilter getModelTitle() {
        return modelTitle;
    }

    public StringFilter modelTitle() {
        if (modelTitle == null) {
            modelTitle = new StringFilter();
        }
        return modelTitle;
    }

    public void setModelTitle(StringFilter modelTitle) {
        this.modelTitle = modelTitle;
    }

    public DoubleFilter getModelVersion() {
        return modelVersion;
    }

    public DoubleFilter modelVersion() {
        if (modelVersion == null) {
            modelVersion = new DoubleFilter();
        }
        return modelVersion;
    }

    public void setModelVersion(DoubleFilter modelVersion) {
        this.modelVersion = modelVersion;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DoubleFilter getAnnualDiscountingRate() {
        return annualDiscountingRate;
    }

    public DoubleFilter annualDiscountingRate() {
        if (annualDiscountingRate == null) {
            annualDiscountingRate = new DoubleFilter();
        }
        return annualDiscountingRate;
    }

    public void setAnnualDiscountingRate(DoubleFilter annualDiscountingRate) {
        this.annualDiscountingRate = annualDiscountingRate;
    }

    public LocalDateFilter getCommencementDate() {
        return commencementDate;
    }

    public LocalDateFilter commencementDate() {
        if (commencementDate == null) {
            commencementDate = new LocalDateFilter();
        }
        return commencementDate;
    }

    public void setCommencementDate(LocalDateFilter commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDateFilter getTerminalDate() {
        return terminalDate;
    }

    public LocalDateFilter terminalDate() {
        if (terminalDate == null) {
            terminalDate = new LocalDateFilter();
        }
        return terminalDate;
    }

    public void setTerminalDate(LocalDateFilter terminalDate) {
        this.terminalDate = terminalDate;
    }

    public DoubleFilter getTotalReportingPeriods() {
        return totalReportingPeriods;
    }

    public DoubleFilter totalReportingPeriods() {
        if (totalReportingPeriods == null) {
            totalReportingPeriods = new DoubleFilter();
        }
        return totalReportingPeriods;
    }

    public void setTotalReportingPeriods(DoubleFilter totalReportingPeriods) {
        this.totalReportingPeriods = totalReportingPeriods;
    }

    public DoubleFilter getReportingPeriodsPerYear() {
        return reportingPeriodsPerYear;
    }

    public DoubleFilter reportingPeriodsPerYear() {
        if (reportingPeriodsPerYear == null) {
            reportingPeriodsPerYear = new DoubleFilter();
        }
        return reportingPeriodsPerYear;
    }

    public void setReportingPeriodsPerYear(DoubleFilter reportingPeriodsPerYear) {
        this.reportingPeriodsPerYear = reportingPeriodsPerYear;
    }

    public DoubleFilter getSettlementPeriodsPerYear() {
        return settlementPeriodsPerYear;
    }

    public DoubleFilter settlementPeriodsPerYear() {
        if (settlementPeriodsPerYear == null) {
            settlementPeriodsPerYear = new DoubleFilter();
        }
        return settlementPeriodsPerYear;
    }

    public void setSettlementPeriodsPerYear(DoubleFilter settlementPeriodsPerYear) {
        this.settlementPeriodsPerYear = settlementPeriodsPerYear;
    }

    public BigDecimalFilter getInitialLiabilityAmount() {
        return initialLiabilityAmount;
    }

    public BigDecimalFilter initialLiabilityAmount() {
        if (initialLiabilityAmount == null) {
            initialLiabilityAmount = new BigDecimalFilter();
        }
        return initialLiabilityAmount;
    }

    public void setInitialLiabilityAmount(BigDecimalFilter initialLiabilityAmount) {
        this.initialLiabilityAmount = initialLiabilityAmount;
    }

    public BigDecimalFilter getInitialROUAmount() {
        return initialROUAmount;
    }

    public BigDecimalFilter initialROUAmount() {
        if (initialROUAmount == null) {
            initialROUAmount = new BigDecimalFilter();
        }
        return initialROUAmount;
    }

    public void setInitialROUAmount(BigDecimalFilter initialROUAmount) {
        this.initialROUAmount = initialROUAmount;
    }

    public DoubleFilter getTotalDepreciationPeriods() {
        return totalDepreciationPeriods;
    }

    public DoubleFilter totalDepreciationPeriods() {
        if (totalDepreciationPeriods == null) {
            totalDepreciationPeriods = new DoubleFilter();
        }
        return totalDepreciationPeriods;
    }

    public void setTotalDepreciationPeriods(DoubleFilter totalDepreciationPeriods) {
        this.totalDepreciationPeriods = totalDepreciationPeriods;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
    }

    public LongFilter getLeaseMappingId() {
        return leaseMappingId;
    }

    public LongFilter leaseMappingId() {
        if (leaseMappingId == null) {
            leaseMappingId = new LongFilter();
        }
        return leaseMappingId;
    }

    public void setLeaseMappingId(LongFilter leaseMappingId) {
        this.leaseMappingId = leaseMappingId;
    }

    public LongFilter getLeaseContractId() {
        return leaseContractId;
    }

    public LongFilter leaseContractId() {
        if (leaseContractId == null) {
            leaseContractId = new LongFilter();
        }
        return leaseContractId;
    }

    public void setLeaseContractId(LongFilter leaseContractId) {
        this.leaseContractId = leaseContractId;
    }

    public LongFilter getPredecessorId() {
        return predecessorId;
    }

    public LongFilter predecessorId() {
        if (predecessorId == null) {
            predecessorId = new LongFilter();
        }
        return predecessorId;
    }

    public void setPredecessorId(LongFilter predecessorId) {
        this.predecessorId = predecessorId;
    }

    public LongFilter getLiabilityCurrencyId() {
        return liabilityCurrencyId;
    }

    public LongFilter liabilityCurrencyId() {
        if (liabilityCurrencyId == null) {
            liabilityCurrencyId = new LongFilter();
        }
        return liabilityCurrencyId;
    }

    public void setLiabilityCurrencyId(LongFilter liabilityCurrencyId) {
        this.liabilityCurrencyId = liabilityCurrencyId;
    }

    public LongFilter getRouAssetCurrencyId() {
        return rouAssetCurrencyId;
    }

    public LongFilter rouAssetCurrencyId() {
        if (rouAssetCurrencyId == null) {
            rouAssetCurrencyId = new LongFilter();
        }
        return rouAssetCurrencyId;
    }

    public void setRouAssetCurrencyId(LongFilter rouAssetCurrencyId) {
        this.rouAssetCurrencyId = rouAssetCurrencyId;
    }

    public LongFilter getModelAttachmentsId() {
        return modelAttachmentsId;
    }

    public LongFilter modelAttachmentsId() {
        if (modelAttachmentsId == null) {
            modelAttachmentsId = new LongFilter();
        }
        return modelAttachmentsId;
    }

    public void setModelAttachmentsId(LongFilter modelAttachmentsId) {
        this.modelAttachmentsId = modelAttachmentsId;
    }

    public LongFilter getSecurityClearanceId() {
        return securityClearanceId;
    }

    public LongFilter securityClearanceId() {
        if (securityClearanceId == null) {
            securityClearanceId = new LongFilter();
        }
        return securityClearanceId;
    }

    public void setSecurityClearanceId(LongFilter securityClearanceId) {
        this.securityClearanceId = securityClearanceId;
    }

    public LongFilter getLeaseLiabilityAccountId() {
        return leaseLiabilityAccountId;
    }

    public LongFilter leaseLiabilityAccountId() {
        if (leaseLiabilityAccountId == null) {
            leaseLiabilityAccountId = new LongFilter();
        }
        return leaseLiabilityAccountId;
    }

    public void setLeaseLiabilityAccountId(LongFilter leaseLiabilityAccountId) {
        this.leaseLiabilityAccountId = leaseLiabilityAccountId;
    }

    public LongFilter getInterestPayableAccountId() {
        return interestPayableAccountId;
    }

    public LongFilter interestPayableAccountId() {
        if (interestPayableAccountId == null) {
            interestPayableAccountId = new LongFilter();
        }
        return interestPayableAccountId;
    }

    public void setInterestPayableAccountId(LongFilter interestPayableAccountId) {
        this.interestPayableAccountId = interestPayableAccountId;
    }

    public LongFilter getInterestExpenseAccountId() {
        return interestExpenseAccountId;
    }

    public LongFilter interestExpenseAccountId() {
        if (interestExpenseAccountId == null) {
            interestExpenseAccountId = new LongFilter();
        }
        return interestExpenseAccountId;
    }

    public void setInterestExpenseAccountId(LongFilter interestExpenseAccountId) {
        this.interestExpenseAccountId = interestExpenseAccountId;
    }

    public LongFilter getRouAssetAccountId() {
        return rouAssetAccountId;
    }

    public LongFilter rouAssetAccountId() {
        if (rouAssetAccountId == null) {
            rouAssetAccountId = new LongFilter();
        }
        return rouAssetAccountId;
    }

    public void setRouAssetAccountId(LongFilter rouAssetAccountId) {
        this.rouAssetAccountId = rouAssetAccountId;
    }

    public LongFilter getRouDepreciationAccountId() {
        return rouDepreciationAccountId;
    }

    public LongFilter rouDepreciationAccountId() {
        if (rouDepreciationAccountId == null) {
            rouDepreciationAccountId = new LongFilter();
        }
        return rouDepreciationAccountId;
    }

    public void setRouDepreciationAccountId(LongFilter rouDepreciationAccountId) {
        this.rouDepreciationAccountId = rouDepreciationAccountId;
    }

    public LongFilter getAccruedDepreciationAccountId() {
        return accruedDepreciationAccountId;
    }

    public LongFilter accruedDepreciationAccountId() {
        if (accruedDepreciationAccountId == null) {
            accruedDepreciationAccountId = new LongFilter();
        }
        return accruedDepreciationAccountId;
    }

    public void setAccruedDepreciationAccountId(LongFilter accruedDepreciationAccountId) {
        this.accruedDepreciationAccountId = accruedDepreciationAccountId;
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
        final LeaseModelMetadataCriteria that = (LeaseModelMetadataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(modelTitle, that.modelTitle) &&
            Objects.equals(modelVersion, that.modelVersion) &&
            Objects.equals(description, that.description) &&
            Objects.equals(annualDiscountingRate, that.annualDiscountingRate) &&
            Objects.equals(commencementDate, that.commencementDate) &&
            Objects.equals(terminalDate, that.terminalDate) &&
            Objects.equals(totalReportingPeriods, that.totalReportingPeriods) &&
            Objects.equals(reportingPeriodsPerYear, that.reportingPeriodsPerYear) &&
            Objects.equals(settlementPeriodsPerYear, that.settlementPeriodsPerYear) &&
            Objects.equals(initialLiabilityAmount, that.initialLiabilityAmount) &&
            Objects.equals(initialROUAmount, that.initialROUAmount) &&
            Objects.equals(totalDepreciationPeriods, that.totalDepreciationPeriods) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(leaseMappingId, that.leaseMappingId) &&
            Objects.equals(leaseContractId, that.leaseContractId) &&
            Objects.equals(predecessorId, that.predecessorId) &&
            Objects.equals(liabilityCurrencyId, that.liabilityCurrencyId) &&
            Objects.equals(rouAssetCurrencyId, that.rouAssetCurrencyId) &&
            Objects.equals(modelAttachmentsId, that.modelAttachmentsId) &&
            Objects.equals(securityClearanceId, that.securityClearanceId) &&
            Objects.equals(leaseLiabilityAccountId, that.leaseLiabilityAccountId) &&
            Objects.equals(interestPayableAccountId, that.interestPayableAccountId) &&
            Objects.equals(interestExpenseAccountId, that.interestExpenseAccountId) &&
            Objects.equals(rouAssetAccountId, that.rouAssetAccountId) &&
            Objects.equals(rouDepreciationAccountId, that.rouDepreciationAccountId) &&
            Objects.equals(accruedDepreciationAccountId, that.accruedDepreciationAccountId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            modelTitle,
            modelVersion,
            description,
            annualDiscountingRate,
            commencementDate,
            terminalDate,
            totalReportingPeriods,
            reportingPeriodsPerYear,
            settlementPeriodsPerYear,
            initialLiabilityAmount,
            initialROUAmount,
            totalDepreciationPeriods,
            placeholderId,
            leaseMappingId,
            leaseContractId,
            predecessorId,
            liabilityCurrencyId,
            rouAssetCurrencyId,
            modelAttachmentsId,
            securityClearanceId,
            leaseLiabilityAccountId,
            interestPayableAccountId,
            interestExpenseAccountId,
            rouAssetAccountId,
            rouDepreciationAccountId,
            accruedDepreciationAccountId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseModelMetadataCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (modelTitle != null ? "modelTitle=" + modelTitle + ", " : "") +
            (modelVersion != null ? "modelVersion=" + modelVersion + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (annualDiscountingRate != null ? "annualDiscountingRate=" + annualDiscountingRate + ", " : "") +
            (commencementDate != null ? "commencementDate=" + commencementDate + ", " : "") +
            (terminalDate != null ? "terminalDate=" + terminalDate + ", " : "") +
            (totalReportingPeriods != null ? "totalReportingPeriods=" + totalReportingPeriods + ", " : "") +
            (reportingPeriodsPerYear != null ? "reportingPeriodsPerYear=" + reportingPeriodsPerYear + ", " : "") +
            (settlementPeriodsPerYear != null ? "settlementPeriodsPerYear=" + settlementPeriodsPerYear + ", " : "") +
            (initialLiabilityAmount != null ? "initialLiabilityAmount=" + initialLiabilityAmount + ", " : "") +
            (initialROUAmount != null ? "initialROUAmount=" + initialROUAmount + ", " : "") +
            (totalDepreciationPeriods != null ? "totalDepreciationPeriods=" + totalDepreciationPeriods + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (leaseMappingId != null ? "leaseMappingId=" + leaseMappingId + ", " : "") +
            (leaseContractId != null ? "leaseContractId=" + leaseContractId + ", " : "") +
            (predecessorId != null ? "predecessorId=" + predecessorId + ", " : "") +
            (liabilityCurrencyId != null ? "liabilityCurrencyId=" + liabilityCurrencyId + ", " : "") +
            (rouAssetCurrencyId != null ? "rouAssetCurrencyId=" + rouAssetCurrencyId + ", " : "") +
            (modelAttachmentsId != null ? "modelAttachmentsId=" + modelAttachmentsId + ", " : "") +
            (securityClearanceId != null ? "securityClearanceId=" + securityClearanceId + ", " : "") +
            (leaseLiabilityAccountId != null ? "leaseLiabilityAccountId=" + leaseLiabilityAccountId + ", " : "") +
            (interestPayableAccountId != null ? "interestPayableAccountId=" + interestPayableAccountId + ", " : "") +
            (interestExpenseAccountId != null ? "interestExpenseAccountId=" + interestExpenseAccountId + ", " : "") +
            (rouAssetAccountId != null ? "rouAssetAccountId=" + rouAssetAccountId + ", " : "") +
            (rouDepreciationAccountId != null ? "rouDepreciationAccountId=" + rouDepreciationAccountId + ", " : "") +
            (accruedDepreciationAccountId != null ? "accruedDepreciationAccountId=" + accruedDepreciationAccountId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
