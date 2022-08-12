package io.github.erp.service.criteria;

/*-
 * Erp System - Mark II No 26 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.BusinessStamp} entity. This class is used
 * in {@link io.github.erp.web.rest.BusinessStampResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /business-stamps?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BusinessStampCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter stampDate;

    private StringFilter purpose;

    private StringFilter details;

    private LongFilter stampHolderId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public BusinessStampCriteria() {}

    public BusinessStampCriteria(BusinessStampCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.stampDate = other.stampDate == null ? null : other.stampDate.copy();
        this.purpose = other.purpose == null ? null : other.purpose.copy();
        this.details = other.details == null ? null : other.details.copy();
        this.stampHolderId = other.stampHolderId == null ? null : other.stampHolderId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BusinessStampCriteria copy() {
        return new BusinessStampCriteria(this);
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

    public LocalDateFilter getStampDate() {
        return stampDate;
    }

    public LocalDateFilter stampDate() {
        if (stampDate == null) {
            stampDate = new LocalDateFilter();
        }
        return stampDate;
    }

    public void setStampDate(LocalDateFilter stampDate) {
        this.stampDate = stampDate;
    }

    public StringFilter getPurpose() {
        return purpose;
    }

    public StringFilter purpose() {
        if (purpose == null) {
            purpose = new StringFilter();
        }
        return purpose;
    }

    public void setPurpose(StringFilter purpose) {
        this.purpose = purpose;
    }

    public StringFilter getDetails() {
        return details;
    }

    public StringFilter details() {
        if (details == null) {
            details = new StringFilter();
        }
        return details;
    }

    public void setDetails(StringFilter details) {
        this.details = details;
    }

    public LongFilter getStampHolderId() {
        return stampHolderId;
    }

    public LongFilter stampHolderId() {
        if (stampHolderId == null) {
            stampHolderId = new LongFilter();
        }
        return stampHolderId;
    }

    public void setStampHolderId(LongFilter stampHolderId) {
        this.stampHolderId = stampHolderId;
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
        final BusinessStampCriteria that = (BusinessStampCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(stampDate, that.stampDate) &&
            Objects.equals(purpose, that.purpose) &&
            Objects.equals(details, that.details) &&
            Objects.equals(stampHolderId, that.stampHolderId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stampDate, purpose, details, stampHolderId, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessStampCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (stampDate != null ? "stampDate=" + stampDate + ", " : "") +
            (purpose != null ? "purpose=" + purpose + ", " : "") +
            (details != null ? "details=" + details + ", " : "") +
            (stampHolderId != null ? "stampHolderId=" + stampHolderId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
