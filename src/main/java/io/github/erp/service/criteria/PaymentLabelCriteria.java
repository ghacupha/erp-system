package io.github.erp.service.criteria;

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
 * Criteria class for the {@link io.github.erp.domain.PaymentLabel} entity. This class is used
 * in {@link io.github.erp.web.rest.PaymentLabelResource} to receive all the possible filtering options from
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

    private LongFilter containingPaymentLabelId;

    private LongFilter placeholderId;

    private LongFilter paymentCalculationId;

    private LongFilter paymentCategoryId;

    private LongFilter paymentRequisitionId;

    private LongFilter paymentId;

    private LongFilter invoiceId;

    private LongFilter dealerId;

    private LongFilter signedPaymentId;

    private Boolean distinct;

    public PaymentLabelCriteria() {}

    public PaymentLabelCriteria(PaymentLabelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.comments = other.comments == null ? null : other.comments.copy();
        this.containingPaymentLabelId = other.containingPaymentLabelId == null ? null : other.containingPaymentLabelId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.paymentCalculationId = other.paymentCalculationId == null ? null : other.paymentCalculationId.copy();
        this.paymentCategoryId = other.paymentCategoryId == null ? null : other.paymentCategoryId.copy();
        this.paymentRequisitionId = other.paymentRequisitionId == null ? null : other.paymentRequisitionId.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.invoiceId = other.invoiceId == null ? null : other.invoiceId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.signedPaymentId = other.signedPaymentId == null ? null : other.signedPaymentId.copy();
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

    public LongFilter getPaymentCalculationId() {
        return paymentCalculationId;
    }

    public LongFilter paymentCalculationId() {
        if (paymentCalculationId == null) {
            paymentCalculationId = new LongFilter();
        }
        return paymentCalculationId;
    }

    public void setPaymentCalculationId(LongFilter paymentCalculationId) {
        this.paymentCalculationId = paymentCalculationId;
    }

    public LongFilter getPaymentCategoryId() {
        return paymentCategoryId;
    }

    public LongFilter paymentCategoryId() {
        if (paymentCategoryId == null) {
            paymentCategoryId = new LongFilter();
        }
        return paymentCategoryId;
    }

    public void setPaymentCategoryId(LongFilter paymentCategoryId) {
        this.paymentCategoryId = paymentCategoryId;
    }

    public LongFilter getPaymentRequisitionId() {
        return paymentRequisitionId;
    }

    public LongFilter paymentRequisitionId() {
        if (paymentRequisitionId == null) {
            paymentRequisitionId = new LongFilter();
        }
        return paymentRequisitionId;
    }

    public void setPaymentRequisitionId(LongFilter paymentRequisitionId) {
        this.paymentRequisitionId = paymentRequisitionId;
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            paymentId = new LongFilter();
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public LongFilter invoiceId() {
        if (invoiceId == null) {
            invoiceId = new LongFilter();
        }
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LongFilter getDealerId() {
        return dealerId;
    }

    public LongFilter dealerId() {
        if (dealerId == null) {
            dealerId = new LongFilter();
        }
        return dealerId;
    }

    public void setDealerId(LongFilter dealerId) {
        this.dealerId = dealerId;
    }

    public LongFilter getSignedPaymentId() {
        return signedPaymentId;
    }

    public LongFilter signedPaymentId() {
        if (signedPaymentId == null) {
            signedPaymentId = new LongFilter();
        }
        return signedPaymentId;
    }

    public void setSignedPaymentId(LongFilter signedPaymentId) {
        this.signedPaymentId = signedPaymentId;
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
            Objects.equals(containingPaymentLabelId, that.containingPaymentLabelId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(paymentCalculationId, that.paymentCalculationId) &&
            Objects.equals(paymentCategoryId, that.paymentCategoryId) &&
            Objects.equals(paymentRequisitionId, that.paymentRequisitionId) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(invoiceId, that.invoiceId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(signedPaymentId, that.signedPaymentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            comments,
            containingPaymentLabelId,
            placeholderId,
            paymentCalculationId,
            paymentCategoryId,
            paymentRequisitionId,
            paymentId,
            invoiceId,
            dealerId,
            signedPaymentId,
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
            (containingPaymentLabelId != null ? "containingPaymentLabelId=" + containingPaymentLabelId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (paymentCalculationId != null ? "paymentCalculationId=" + paymentCalculationId + ", " : "") +
            (paymentCategoryId != null ? "paymentCategoryId=" + paymentCategoryId + ", " : "") +
            (paymentRequisitionId != null ? "paymentRequisitionId=" + paymentRequisitionId + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (invoiceId != null ? "invoiceId=" + invoiceId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (signedPaymentId != null ? "signedPaymentId=" + signedPaymentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
