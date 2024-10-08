package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.enumeration.DepreciationNoticeStatusType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.DepreciationJobNotice} entity. This class is used
 * in {@link io.github.erp.web.rest.DepreciationJobNoticeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /depreciation-job-notices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepreciationJobNoticeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DepreciationNoticeStatusType
     */
    public static class DepreciationNoticeStatusTypeFilter extends Filter<DepreciationNoticeStatusType> {

        public DepreciationNoticeStatusTypeFilter() {}

        public DepreciationNoticeStatusTypeFilter(DepreciationNoticeStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public DepreciationNoticeStatusTypeFilter copy() {
            return new DepreciationNoticeStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventNarrative;

    private ZonedDateTimeFilter eventTimeStamp;

    private DepreciationNoticeStatusTypeFilter depreciationNoticeStatus;

    private StringFilter sourceModule;

    private StringFilter sourceEntity;

    private StringFilter errorCode;

    private StringFilter userAction;

    private LongFilter depreciationJobId;

    private LongFilter depreciationBatchSequenceId;

    private LongFilter depreciationPeriodId;

    private LongFilter placeholderId;

    private LongFilter universallyUniqueMappingId;

    private Boolean distinct;

    public DepreciationJobNoticeCriteria() {}

    public DepreciationJobNoticeCriteria(DepreciationJobNoticeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.eventNarrative = other.eventNarrative == null ? null : other.eventNarrative.copy();
        this.eventTimeStamp = other.eventTimeStamp == null ? null : other.eventTimeStamp.copy();
        this.depreciationNoticeStatus = other.depreciationNoticeStatus == null ? null : other.depreciationNoticeStatus.copy();
        this.sourceModule = other.sourceModule == null ? null : other.sourceModule.copy();
        this.sourceEntity = other.sourceEntity == null ? null : other.sourceEntity.copy();
        this.errorCode = other.errorCode == null ? null : other.errorCode.copy();
        this.userAction = other.userAction == null ? null : other.userAction.copy();
        this.depreciationJobId = other.depreciationJobId == null ? null : other.depreciationJobId.copy();
        this.depreciationBatchSequenceId = other.depreciationBatchSequenceId == null ? null : other.depreciationBatchSequenceId.copy();
        this.depreciationPeriodId = other.depreciationPeriodId == null ? null : other.depreciationPeriodId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.universallyUniqueMappingId = other.universallyUniqueMappingId == null ? null : other.universallyUniqueMappingId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepreciationJobNoticeCriteria copy() {
        return new DepreciationJobNoticeCriteria(this);
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

    public StringFilter getEventNarrative() {
        return eventNarrative;
    }

    public StringFilter eventNarrative() {
        if (eventNarrative == null) {
            eventNarrative = new StringFilter();
        }
        return eventNarrative;
    }

    public void setEventNarrative(StringFilter eventNarrative) {
        this.eventNarrative = eventNarrative;
    }

    public ZonedDateTimeFilter getEventTimeStamp() {
        return eventTimeStamp;
    }

    public ZonedDateTimeFilter eventTimeStamp() {
        if (eventTimeStamp == null) {
            eventTimeStamp = new ZonedDateTimeFilter();
        }
        return eventTimeStamp;
    }

    public void setEventTimeStamp(ZonedDateTimeFilter eventTimeStamp) {
        this.eventTimeStamp = eventTimeStamp;
    }

    public DepreciationNoticeStatusTypeFilter getDepreciationNoticeStatus() {
        return depreciationNoticeStatus;
    }

    public DepreciationNoticeStatusTypeFilter depreciationNoticeStatus() {
        if (depreciationNoticeStatus == null) {
            depreciationNoticeStatus = new DepreciationNoticeStatusTypeFilter();
        }
        return depreciationNoticeStatus;
    }

    public void setDepreciationNoticeStatus(DepreciationNoticeStatusTypeFilter depreciationNoticeStatus) {
        this.depreciationNoticeStatus = depreciationNoticeStatus;
    }

    public StringFilter getSourceModule() {
        return sourceModule;
    }

    public StringFilter sourceModule() {
        if (sourceModule == null) {
            sourceModule = new StringFilter();
        }
        return sourceModule;
    }

    public void setSourceModule(StringFilter sourceModule) {
        this.sourceModule = sourceModule;
    }

    public StringFilter getSourceEntity() {
        return sourceEntity;
    }

    public StringFilter sourceEntity() {
        if (sourceEntity == null) {
            sourceEntity = new StringFilter();
        }
        return sourceEntity;
    }

    public void setSourceEntity(StringFilter sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    public StringFilter getErrorCode() {
        return errorCode;
    }

    public StringFilter errorCode() {
        if (errorCode == null) {
            errorCode = new StringFilter();
        }
        return errorCode;
    }

    public void setErrorCode(StringFilter errorCode) {
        this.errorCode = errorCode;
    }

    public StringFilter getUserAction() {
        return userAction;
    }

    public StringFilter userAction() {
        if (userAction == null) {
            userAction = new StringFilter();
        }
        return userAction;
    }

    public void setUserAction(StringFilter userAction) {
        this.userAction = userAction;
    }

    public LongFilter getDepreciationJobId() {
        return depreciationJobId;
    }

    public LongFilter depreciationJobId() {
        if (depreciationJobId == null) {
            depreciationJobId = new LongFilter();
        }
        return depreciationJobId;
    }

    public void setDepreciationJobId(LongFilter depreciationJobId) {
        this.depreciationJobId = depreciationJobId;
    }

    public LongFilter getDepreciationBatchSequenceId() {
        return depreciationBatchSequenceId;
    }

    public LongFilter depreciationBatchSequenceId() {
        if (depreciationBatchSequenceId == null) {
            depreciationBatchSequenceId = new LongFilter();
        }
        return depreciationBatchSequenceId;
    }

    public void setDepreciationBatchSequenceId(LongFilter depreciationBatchSequenceId) {
        this.depreciationBatchSequenceId = depreciationBatchSequenceId;
    }

    public LongFilter getDepreciationPeriodId() {
        return depreciationPeriodId;
    }

    public LongFilter depreciationPeriodId() {
        if (depreciationPeriodId == null) {
            depreciationPeriodId = new LongFilter();
        }
        return depreciationPeriodId;
    }

    public void setDepreciationPeriodId(LongFilter depreciationPeriodId) {
        this.depreciationPeriodId = depreciationPeriodId;
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

    public LongFilter getUniversallyUniqueMappingId() {
        return universallyUniqueMappingId;
    }

    public LongFilter universallyUniqueMappingId() {
        if (universallyUniqueMappingId == null) {
            universallyUniqueMappingId = new LongFilter();
        }
        return universallyUniqueMappingId;
    }

    public void setUniversallyUniqueMappingId(LongFilter universallyUniqueMappingId) {
        this.universallyUniqueMappingId = universallyUniqueMappingId;
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
        final DepreciationJobNoticeCriteria that = (DepreciationJobNoticeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventNarrative, that.eventNarrative) &&
            Objects.equals(eventTimeStamp, that.eventTimeStamp) &&
            Objects.equals(depreciationNoticeStatus, that.depreciationNoticeStatus) &&
            Objects.equals(sourceModule, that.sourceModule) &&
            Objects.equals(sourceEntity, that.sourceEntity) &&
            Objects.equals(errorCode, that.errorCode) &&
            Objects.equals(userAction, that.userAction) &&
            Objects.equals(depreciationJobId, that.depreciationJobId) &&
            Objects.equals(depreciationBatchSequenceId, that.depreciationBatchSequenceId) &&
            Objects.equals(depreciationPeriodId, that.depreciationPeriodId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(universallyUniqueMappingId, that.universallyUniqueMappingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            eventNarrative,
            eventTimeStamp,
            depreciationNoticeStatus,
            sourceModule,
            sourceEntity,
            errorCode,
            userAction,
            depreciationJobId,
            depreciationBatchSequenceId,
            depreciationPeriodId,
            placeholderId,
            universallyUniqueMappingId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationJobNoticeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (eventNarrative != null ? "eventNarrative=" + eventNarrative + ", " : "") +
            (eventTimeStamp != null ? "eventTimeStamp=" + eventTimeStamp + ", " : "") +
            (depreciationNoticeStatus != null ? "depreciationNoticeStatus=" + depreciationNoticeStatus + ", " : "") +
            (sourceModule != null ? "sourceModule=" + sourceModule + ", " : "") +
            (sourceEntity != null ? "sourceEntity=" + sourceEntity + ", " : "") +
            (errorCode != null ? "errorCode=" + errorCode + ", " : "") +
            (userAction != null ? "userAction=" + userAction + ", " : "") +
            (depreciationJobId != null ? "depreciationJobId=" + depreciationJobId + ", " : "") +
            (depreciationBatchSequenceId != null ? "depreciationBatchSequenceId=" + depreciationBatchSequenceId + ", " : "") +
            (depreciationPeriodId != null ? "depreciationPeriodId=" + depreciationPeriodId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (universallyUniqueMappingId != null ? "universallyUniqueMappingId=" + universallyUniqueMappingId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
