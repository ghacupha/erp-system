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

import io.github.erp.domain.enumeration.AgencyStatusType;
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

    /**
     * Class for filtering AgencyStatusType
     */
    public static class AgencyStatusTypeFilter extends Filter<AgencyStatusType> {

        public AgencyStatusTypeFilter() {}

        public AgencyStatusTypeFilter(AgencyStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public AgencyStatusTypeFilter copy() {
            return new AgencyStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter referenceNumber;

    private LocalDateFilter referenceDate;

    private BigDecimalFilter assessmentAmount;

    private AgencyStatusTypeFilter agencyStatus;

    private LongFilter correspondentsId;

    private LongFilter settlementCurrencyId;

    private LongFilter assessorId;

    private LongFilter placeholderId;

    private LongFilter businessDocumentId;

    private Boolean distinct;

    public AgencyNoticeCriteria() {}

    public AgencyNoticeCriteria(AgencyNoticeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.referenceNumber = other.referenceNumber == null ? null : other.referenceNumber.copy();
        this.referenceDate = other.referenceDate == null ? null : other.referenceDate.copy();
        this.assessmentAmount = other.assessmentAmount == null ? null : other.assessmentAmount.copy();
        this.agencyStatus = other.agencyStatus == null ? null : other.agencyStatus.copy();
        this.correspondentsId = other.correspondentsId == null ? null : other.correspondentsId.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.assessorId = other.assessorId == null ? null : other.assessorId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.businessDocumentId = other.businessDocumentId == null ? null : other.businessDocumentId.copy();
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

    public AgencyStatusTypeFilter getAgencyStatus() {
        return agencyStatus;
    }

    public AgencyStatusTypeFilter agencyStatus() {
        if (agencyStatus == null) {
            agencyStatus = new AgencyStatusTypeFilter();
        }
        return agencyStatus;
    }

    public void setAgencyStatus(AgencyStatusTypeFilter agencyStatus) {
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

    public LongFilter getBusinessDocumentId() {
        return businessDocumentId;
    }

    public LongFilter businessDocumentId() {
        if (businessDocumentId == null) {
            businessDocumentId = new LongFilter();
        }
        return businessDocumentId;
    }

    public void setBusinessDocumentId(LongFilter businessDocumentId) {
        this.businessDocumentId = businessDocumentId;
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
            Objects.equals(assessmentAmount, that.assessmentAmount) &&
            Objects.equals(agencyStatus, that.agencyStatus) &&
            Objects.equals(correspondentsId, that.correspondentsId) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(assessorId, that.assessorId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(businessDocumentId, that.businessDocumentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            referenceNumber,
            referenceDate,
            assessmentAmount,
            agencyStatus,
            correspondentsId,
            settlementCurrencyId,
            assessorId,
            placeholderId,
            businessDocumentId,
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
            (assessmentAmount != null ? "assessmentAmount=" + assessmentAmount + ", " : "") +
            (agencyStatus != null ? "agencyStatus=" + agencyStatus + ", " : "") +
            (correspondentsId != null ? "correspondentsId=" + correspondentsId + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (assessorId != null ? "assessorId=" + assessorId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (businessDocumentId != null ? "businessDocumentId=" + businessDocumentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
