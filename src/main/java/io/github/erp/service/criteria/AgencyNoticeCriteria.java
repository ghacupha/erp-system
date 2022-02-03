package io.github.erp.service.criteria;

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
 * Criteria class for the {@link io.github.erp.domain.AgencyNotice} entity. This class is used
 * in {@link io.github.erp.web.rest.AgencyNoticeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agency-notices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AgencyNoticeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter referenceNumber;

    private LocalDateFilter referenceDate;

    private StringFilter taxCode;

    private BigDecimalFilter assessmentAmount;

    private BooleanFilter agencyStatus;

    private LongFilter correspondentsId;

    private LongFilter settlementCurrencyId;

    private LongFilter assessorId;

    private Boolean distinct;

    public AgencyNoticeCriteria() {}

    public AgencyNoticeCriteria(AgencyNoticeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.referenceNumber = other.referenceNumber == null ? null : other.referenceNumber.copy();
        this.referenceDate = other.referenceDate == null ? null : other.referenceDate.copy();
        this.taxCode = other.taxCode == null ? null : other.taxCode.copy();
        this.assessmentAmount = other.assessmentAmount == null ? null : other.assessmentAmount.copy();
        this.agencyStatus = other.agencyStatus == null ? null : other.agencyStatus.copy();
        this.correspondentsId = other.correspondentsId == null ? null : other.correspondentsId.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.assessorId = other.assessorId == null ? null : other.assessorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AgencyNoticeCriteria copy() {
        return new AgencyNoticeCriteria(this);
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

    public StringFilter getReferenceNumber() {
        return referenceNumber;
    }

    public StringFilter referenceNumber() {
        if (referenceNumber == null) {
            referenceNumber = new StringFilter();
        }
        return referenceNumber;
    }

    public void setReferenceNumber(StringFilter referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDateFilter getReferenceDate() {
        return referenceDate;
    }

    public LocalDateFilter referenceDate() {
        if (referenceDate == null) {
            referenceDate = new LocalDateFilter();
        }
        return referenceDate;
    }

    public void setReferenceDate(LocalDateFilter referenceDate) {
        this.referenceDate = referenceDate;
    }

    public StringFilter getTaxCode() {
        return taxCode;
    }

    public StringFilter taxCode() {
        if (taxCode == null) {
            taxCode = new StringFilter();
        }
        return taxCode;
    }

    public void setTaxCode(StringFilter taxCode) {
        this.taxCode = taxCode;
    }

    public BigDecimalFilter getAssessmentAmount() {
        return assessmentAmount;
    }

    public BigDecimalFilter assessmentAmount() {
        if (assessmentAmount == null) {
            assessmentAmount = new BigDecimalFilter();
        }
        return assessmentAmount;
    }

    public void setAssessmentAmount(BigDecimalFilter assessmentAmount) {
        this.assessmentAmount = assessmentAmount;
    }

    public BooleanFilter getAgencyStatus() {
        return agencyStatus;
    }

    public BooleanFilter agencyStatus() {
        if (agencyStatus == null) {
            agencyStatus = new BooleanFilter();
        }
        return agencyStatus;
    }

    public void setAgencyStatus(BooleanFilter agencyStatus) {
        this.agencyStatus = agencyStatus;
    }

    public LongFilter getCorrespondentsId() {
        return correspondentsId;
    }

    public LongFilter correspondentsId() {
        if (correspondentsId == null) {
            correspondentsId = new LongFilter();
        }
        return correspondentsId;
    }

    public void setCorrespondentsId(LongFilter correspondentsId) {
        this.correspondentsId = correspondentsId;
    }

    public LongFilter getSettlementCurrencyId() {
        return settlementCurrencyId;
    }

    public LongFilter settlementCurrencyId() {
        if (settlementCurrencyId == null) {
            settlementCurrencyId = new LongFilter();
        }
        return settlementCurrencyId;
    }

    public void setSettlementCurrencyId(LongFilter settlementCurrencyId) {
        this.settlementCurrencyId = settlementCurrencyId;
    }

    public LongFilter getAssessorId() {
        return assessorId;
    }

    public LongFilter assessorId() {
        if (assessorId == null) {
            assessorId = new LongFilter();
        }
        return assessorId;
    }

    public void setAssessorId(LongFilter assessorId) {
        this.assessorId = assessorId;
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
        final AgencyNoticeCriteria that = (AgencyNoticeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(referenceNumber, that.referenceNumber) &&
            Objects.equals(referenceDate, that.referenceDate) &&
            Objects.equals(taxCode, that.taxCode) &&
            Objects.equals(assessmentAmount, that.assessmentAmount) &&
            Objects.equals(agencyStatus, that.agencyStatus) &&
            Objects.equals(correspondentsId, that.correspondentsId) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(assessorId, that.assessorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            referenceNumber,
            referenceDate,
            taxCode,
            assessmentAmount,
            agencyStatus,
            correspondentsId,
            settlementCurrencyId,
            assessorId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgencyNoticeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (referenceNumber != null ? "referenceNumber=" + referenceNumber + ", " : "") +
            (referenceDate != null ? "referenceDate=" + referenceDate + ", " : "") +
            (taxCode != null ? "taxCode=" + taxCode + ", " : "") +
            (assessmentAmount != null ? "assessmentAmount=" + assessmentAmount + ", " : "") +
            (agencyStatus != null ? "agencyStatus=" + agencyStatus + ", " : "") +
            (correspondentsId != null ? "correspondentsId=" + correspondentsId + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (assessorId != null ? "assessorId=" + assessorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
