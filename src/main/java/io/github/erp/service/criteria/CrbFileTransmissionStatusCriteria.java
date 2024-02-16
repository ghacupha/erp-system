package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
