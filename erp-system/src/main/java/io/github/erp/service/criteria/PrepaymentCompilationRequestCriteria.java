package io.github.erp.service.criteria;

import io.github.erp.domain.enumeration.CompilationStatusTypes;
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
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.PrepaymentCompilationRequest} entity. This class is used
 * in {@link io.github.erp.web.rest.PrepaymentCompilationRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prepayment-compilation-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PrepaymentCompilationRequestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CompilationStatusTypes
     */
    public static class CompilationStatusTypesFilter extends Filter<CompilationStatusTypes> {

        public CompilationStatusTypesFilter() {}

        public CompilationStatusTypesFilter(CompilationStatusTypesFilter filter) {
            super(filter);
        }

        @Override
        public CompilationStatusTypesFilter copy() {
            return new CompilationStatusTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter timeOfRequest;

    private CompilationStatusTypesFilter compilationStatus;

    private IntegerFilter itemsProcessed;

    private UUIDFilter compilationToken;

    private StringFilter narration;

    private LongFilter placeholderId;

    private LongFilter createdById;

    private Boolean distinct;

    public PrepaymentCompilationRequestCriteria() {}

    public PrepaymentCompilationRequestCriteria(PrepaymentCompilationRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.timeOfRequest = other.timeOfRequest == null ? null : other.timeOfRequest.copy();
        this.compilationStatus = other.compilationStatus == null ? null : other.compilationStatus.copy();
        this.itemsProcessed = other.itemsProcessed == null ? null : other.itemsProcessed.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.narration = other.narration == null ? null : other.narration.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PrepaymentCompilationRequestCriteria copy() {
        return new PrepaymentCompilationRequestCriteria(this);
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

    public ZonedDateTimeFilter getTimeOfRequest() {
        return timeOfRequest;
    }

    public ZonedDateTimeFilter timeOfRequest() {
        if (timeOfRequest == null) {
            timeOfRequest = new ZonedDateTimeFilter();
        }
        return timeOfRequest;
    }

    public void setTimeOfRequest(ZonedDateTimeFilter timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public CompilationStatusTypesFilter getCompilationStatus() {
        return compilationStatus;
    }

    public CompilationStatusTypesFilter compilationStatus() {
        if (compilationStatus == null) {
            compilationStatus = new CompilationStatusTypesFilter();
        }
        return compilationStatus;
    }

    public void setCompilationStatus(CompilationStatusTypesFilter compilationStatus) {
        this.compilationStatus = compilationStatus;
    }

    public IntegerFilter getItemsProcessed() {
        return itemsProcessed;
    }

    public IntegerFilter itemsProcessed() {
        if (itemsProcessed == null) {
            itemsProcessed = new IntegerFilter();
        }
        return itemsProcessed;
    }

    public void setItemsProcessed(IntegerFilter itemsProcessed) {
        this.itemsProcessed = itemsProcessed;
    }

    public UUIDFilter getCompilationToken() {
        return compilationToken;
    }

    public UUIDFilter compilationToken() {
        if (compilationToken == null) {
            compilationToken = new UUIDFilter();
        }
        return compilationToken;
    }

    public void setCompilationToken(UUIDFilter compilationToken) {
        this.compilationToken = compilationToken;
    }

    public StringFilter getNarration() {
        return narration;
    }

    public StringFilter narration() {
        if (narration == null) {
            narration = new StringFilter();
        }
        return narration;
    }

    public void setNarration(StringFilter narration) {
        this.narration = narration;
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

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
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
        final PrepaymentCompilationRequestCriteria that = (PrepaymentCompilationRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(timeOfRequest, that.timeOfRequest) &&
            Objects.equals(compilationStatus, that.compilationStatus) &&
            Objects.equals(itemsProcessed, that.itemsProcessed) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(narration, that.narration) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            timeOfRequest,
            compilationStatus,
            itemsProcessed,
            compilationToken,
            narration,
            placeholderId,
            createdById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentCompilationRequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (timeOfRequest != null ? "timeOfRequest=" + timeOfRequest + ", " : "") +
            (compilationStatus != null ? "compilationStatus=" + compilationStatus + ", " : "") +
            (itemsProcessed != null ? "itemsProcessed=" + itemsProcessed + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (narration != null ? "narration=" + narration + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
