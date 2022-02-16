package io.github.erp.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.github.erp.web.rest.api.PaymentLabelResource;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.PaymentLabel} entity. This class is used
 * in {@link PaymentLabelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-labels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentLabelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter comments;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter containingPaymentLabelId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public PaymentLabelCriteria() {}

    public PaymentLabelCriteria(PaymentLabelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.comments = other.comments == null ? null : other.comments.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.containingPaymentLabelId = other.containingPaymentLabelId == null ? null : other.containingPaymentLabelId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaymentLabelCriteria copy() {
        return new PaymentLabelCriteria(this);
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

    public StringFilter getComments() {
        return comments;
    }

    public StringFilter comments() {
        if (comments == null) {
            comments = new StringFilter();
        }
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public StringFilter getFileUploadToken() {
        return fileUploadToken;
    }

    public StringFilter fileUploadToken() {
        if (fileUploadToken == null) {
            fileUploadToken = new StringFilter();
        }
        return fileUploadToken;
    }

    public void setFileUploadToken(StringFilter fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public StringFilter getCompilationToken() {
        return compilationToken;
    }

    public StringFilter compilationToken() {
        if (compilationToken == null) {
            compilationToken = new StringFilter();
        }
        return compilationToken;
    }

    public void setCompilationToken(StringFilter compilationToken) {
        this.compilationToken = compilationToken;
    }

    public LongFilter getContainingPaymentLabelId() {
        return containingPaymentLabelId;
    }

    public LongFilter containingPaymentLabelId() {
        if (containingPaymentLabelId == null) {
            containingPaymentLabelId = new LongFilter();
        }
        return containingPaymentLabelId;
    }

    public void setContainingPaymentLabelId(LongFilter containingPaymentLabelId) {
        this.containingPaymentLabelId = containingPaymentLabelId;
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
        final PaymentLabelCriteria that = (PaymentLabelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(containingPaymentLabelId, that.containingPaymentLabelId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            comments,
            fileUploadToken,
            compilationToken,
            containingPaymentLabelId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentLabelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (comments != null ? "comments=" + comments + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (containingPaymentLabelId != null ? "containingPaymentLabelId=" + containingPaymentLabelId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
