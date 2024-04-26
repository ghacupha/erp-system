package io.github.erp.service.criteria;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.enumeration.SubmittedFileStatusTypes;
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

/**
 * Criteria class for the {@link io.github.erp.domain.CrbFileTransmissionStatus} entity. This class is used
 * in {@link io.github.erp.web.rest.CrbFileTransmissionStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crb-file-transmission-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrbFileTransmissionStatusCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SubmittedFileStatusTypes
     */
    public static class SubmittedFileStatusTypesFilter extends Filter<SubmittedFileStatusTypes> {

        public SubmittedFileStatusTypesFilter() {}

        public SubmittedFileStatusTypesFilter(SubmittedFileStatusTypesFilter filter) {
            super(filter);
        }

        @Override
        public SubmittedFileStatusTypesFilter copy() {
            return new SubmittedFileStatusTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter submittedFileStatusTypeCode;

    private SubmittedFileStatusTypesFilter submittedFileStatusType;

    private Boolean distinct;

    public CrbFileTransmissionStatusCriteria() {}

    public CrbFileTransmissionStatusCriteria(CrbFileTransmissionStatusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.submittedFileStatusTypeCode = other.submittedFileStatusTypeCode == null ? null : other.submittedFileStatusTypeCode.copy();
        this.submittedFileStatusType = other.submittedFileStatusType == null ? null : other.submittedFileStatusType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CrbFileTransmissionStatusCriteria copy() {
        return new CrbFileTransmissionStatusCriteria(this);
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

    public StringFilter getSubmittedFileStatusTypeCode() {
        return submittedFileStatusTypeCode;
    }

    public StringFilter submittedFileStatusTypeCode() {
        if (submittedFileStatusTypeCode == null) {
            submittedFileStatusTypeCode = new StringFilter();
        }
        return submittedFileStatusTypeCode;
    }

    public void setSubmittedFileStatusTypeCode(StringFilter submittedFileStatusTypeCode) {
        this.submittedFileStatusTypeCode = submittedFileStatusTypeCode;
    }

    public SubmittedFileStatusTypesFilter getSubmittedFileStatusType() {
        return submittedFileStatusType;
    }

    public SubmittedFileStatusTypesFilter submittedFileStatusType() {
        if (submittedFileStatusType == null) {
            submittedFileStatusType = new SubmittedFileStatusTypesFilter();
        }
        return submittedFileStatusType;
    }

    public void setSubmittedFileStatusType(SubmittedFileStatusTypesFilter submittedFileStatusType) {
        this.submittedFileStatusType = submittedFileStatusType;
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
        final CrbFileTransmissionStatusCriteria that = (CrbFileTransmissionStatusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(submittedFileStatusTypeCode, that.submittedFileStatusTypeCode) &&
            Objects.equals(submittedFileStatusType, that.submittedFileStatusType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, submittedFileStatusTypeCode, submittedFileStatusType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbFileTransmissionStatusCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (submittedFileStatusTypeCode != null ? "submittedFileStatusTypeCode=" + submittedFileStatusTypeCode + ", " : "") +
            (submittedFileStatusType != null ? "submittedFileStatusType=" + submittedFileStatusType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
