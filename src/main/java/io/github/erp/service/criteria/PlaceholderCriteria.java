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
 * Criteria class for the {@link io.github.erp.domain.Placeholder} entity. This class is used
 * in {@link io.github.erp.web.rest.PlaceholderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /placeholders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlaceholderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter token;

    private LongFilter containingPlaceholderId;

    private LongFilter dealerId;

    private LongFilter fileTypeId;

    private LongFilter fileUploadId;

    private LongFilter fixedAssetAcquisitionId;

    private LongFilter fixedAssetDepreciationId;

    private LongFilter fixedAssetNetBookValueId;

    private LongFilter invoiceId;

    private LongFilter messageTokenId;

    private LongFilter paymentId;

    private LongFilter paymentCalculationId;

    private LongFilter paymentRequisitionId;

    private LongFilter paymentCategoryId;

    private LongFilter taxReferenceId;

    private LongFilter taxRuleId;

    private Boolean distinct;

    public PlaceholderCriteria() {}

    public PlaceholderCriteria(PlaceholderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.token = other.token == null ? null : other.token.copy();
        this.containingPlaceholderId = other.containingPlaceholderId == null ? null : other.containingPlaceholderId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.fileTypeId = other.fileTypeId == null ? null : other.fileTypeId.copy();
        this.fileUploadId = other.fileUploadId == null ? null : other.fileUploadId.copy();
        this.fixedAssetAcquisitionId = other.fixedAssetAcquisitionId == null ? null : other.fixedAssetAcquisitionId.copy();
        this.fixedAssetDepreciationId = other.fixedAssetDepreciationId == null ? null : other.fixedAssetDepreciationId.copy();
        this.fixedAssetNetBookValueId = other.fixedAssetNetBookValueId == null ? null : other.fixedAssetNetBookValueId.copy();
        this.invoiceId = other.invoiceId == null ? null : other.invoiceId.copy();
        this.messageTokenId = other.messageTokenId == null ? null : other.messageTokenId.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.paymentCalculationId = other.paymentCalculationId == null ? null : other.paymentCalculationId.copy();
        this.paymentRequisitionId = other.paymentRequisitionId == null ? null : other.paymentRequisitionId.copy();
        this.paymentCategoryId = other.paymentCategoryId == null ? null : other.paymentCategoryId.copy();
        this.taxReferenceId = other.taxReferenceId == null ? null : other.taxReferenceId.copy();
        this.taxRuleId = other.taxRuleId == null ? null : other.taxRuleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PlaceholderCriteria copy() {
        return new PlaceholderCriteria(this);
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

    public StringFilter getToken() {
        return token;
    }

    public StringFilter token() {
        if (token == null) {
            token = new StringFilter();
        }
        return token;
    }

    public void setToken(StringFilter token) {
        this.token = token;
    }

    public LongFilter getContainingPlaceholderId() {
        return containingPlaceholderId;
    }

    public LongFilter containingPlaceholderId() {
        if (containingPlaceholderId == null) {
            containingPlaceholderId = new LongFilter();
        }
        return containingPlaceholderId;
    }

    public void setContainingPlaceholderId(LongFilter containingPlaceholderId) {
        this.containingPlaceholderId = containingPlaceholderId;
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

    public LongFilter getFileTypeId() {
        return fileTypeId;
    }

    public LongFilter fileTypeId() {
        if (fileTypeId == null) {
            fileTypeId = new LongFilter();
        }
        return fileTypeId;
    }

    public void setFileTypeId(LongFilter fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public LongFilter getFileUploadId() {
        return fileUploadId;
    }

    public LongFilter fileUploadId() {
        if (fileUploadId == null) {
            fileUploadId = new LongFilter();
        }
        return fileUploadId;
    }

    public void setFileUploadId(LongFilter fileUploadId) {
        this.fileUploadId = fileUploadId;
    }

    public LongFilter getFixedAssetAcquisitionId() {
        return fixedAssetAcquisitionId;
    }

    public LongFilter fixedAssetAcquisitionId() {
        if (fixedAssetAcquisitionId == null) {
            fixedAssetAcquisitionId = new LongFilter();
        }
        return fixedAssetAcquisitionId;
    }

    public void setFixedAssetAcquisitionId(LongFilter fixedAssetAcquisitionId) {
        this.fixedAssetAcquisitionId = fixedAssetAcquisitionId;
    }

    public LongFilter getFixedAssetDepreciationId() {
        return fixedAssetDepreciationId;
    }

    public LongFilter fixedAssetDepreciationId() {
        if (fixedAssetDepreciationId == null) {
            fixedAssetDepreciationId = new LongFilter();
        }
        return fixedAssetDepreciationId;
    }

    public void setFixedAssetDepreciationId(LongFilter fixedAssetDepreciationId) {
        this.fixedAssetDepreciationId = fixedAssetDepreciationId;
    }

    public LongFilter getFixedAssetNetBookValueId() {
        return fixedAssetNetBookValueId;
    }

    public LongFilter fixedAssetNetBookValueId() {
        if (fixedAssetNetBookValueId == null) {
            fixedAssetNetBookValueId = new LongFilter();
        }
        return fixedAssetNetBookValueId;
    }

    public void setFixedAssetNetBookValueId(LongFilter fixedAssetNetBookValueId) {
        this.fixedAssetNetBookValueId = fixedAssetNetBookValueId;
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

    public LongFilter getMessageTokenId() {
        return messageTokenId;
    }

    public LongFilter messageTokenId() {
        if (messageTokenId == null) {
            messageTokenId = new LongFilter();
        }
        return messageTokenId;
    }

    public void setMessageTokenId(LongFilter messageTokenId) {
        this.messageTokenId = messageTokenId;
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

    public LongFilter getTaxReferenceId() {
        return taxReferenceId;
    }

    public LongFilter taxReferenceId() {
        if (taxReferenceId == null) {
            taxReferenceId = new LongFilter();
        }
        return taxReferenceId;
    }

    public void setTaxReferenceId(LongFilter taxReferenceId) {
        this.taxReferenceId = taxReferenceId;
    }

    public LongFilter getTaxRuleId() {
        return taxRuleId;
    }

    public LongFilter taxRuleId() {
        if (taxRuleId == null) {
            taxRuleId = new LongFilter();
        }
        return taxRuleId;
    }

    public void setTaxRuleId(LongFilter taxRuleId) {
        this.taxRuleId = taxRuleId;
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
        final PlaceholderCriteria that = (PlaceholderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(token, that.token) &&
            Objects.equals(containingPlaceholderId, that.containingPlaceholderId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(fileTypeId, that.fileTypeId) &&
            Objects.equals(fileUploadId, that.fileUploadId) &&
            Objects.equals(fixedAssetAcquisitionId, that.fixedAssetAcquisitionId) &&
            Objects.equals(fixedAssetDepreciationId, that.fixedAssetDepreciationId) &&
            Objects.equals(fixedAssetNetBookValueId, that.fixedAssetNetBookValueId) &&
            Objects.equals(invoiceId, that.invoiceId) &&
            Objects.equals(messageTokenId, that.messageTokenId) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(paymentCalculationId, that.paymentCalculationId) &&
            Objects.equals(paymentRequisitionId, that.paymentRequisitionId) &&
            Objects.equals(paymentCategoryId, that.paymentCategoryId) &&
            Objects.equals(taxReferenceId, that.taxReferenceId) &&
            Objects.equals(taxRuleId, that.taxRuleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            token,
            containingPlaceholderId,
            dealerId,
            fileTypeId,
            fileUploadId,
            fixedAssetAcquisitionId,
            fixedAssetDepreciationId,
            fixedAssetNetBookValueId,
            invoiceId,
            messageTokenId,
            paymentId,
            paymentCalculationId,
            paymentRequisitionId,
            paymentCategoryId,
            taxReferenceId,
            taxRuleId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaceholderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (token != null ? "token=" + token + ", " : "") +
            (containingPlaceholderId != null ? "containingPlaceholderId=" + containingPlaceholderId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (fileTypeId != null ? "fileTypeId=" + fileTypeId + ", " : "") +
            (fileUploadId != null ? "fileUploadId=" + fileUploadId + ", " : "") +
            (fixedAssetAcquisitionId != null ? "fixedAssetAcquisitionId=" + fixedAssetAcquisitionId + ", " : "") +
            (fixedAssetDepreciationId != null ? "fixedAssetDepreciationId=" + fixedAssetDepreciationId + ", " : "") +
            (fixedAssetNetBookValueId != null ? "fixedAssetNetBookValueId=" + fixedAssetNetBookValueId + ", " : "") +
            (invoiceId != null ? "invoiceId=" + invoiceId + ", " : "") +
            (messageTokenId != null ? "messageTokenId=" + messageTokenId + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (paymentCalculationId != null ? "paymentCalculationId=" + paymentCalculationId + ", " : "") +
            (paymentRequisitionId != null ? "paymentRequisitionId=" + paymentRequisitionId + ", " : "") +
            (paymentCategoryId != null ? "paymentCategoryId=" + paymentCategoryId + ", " : "") +
            (taxReferenceId != null ? "taxReferenceId=" + taxReferenceId + ", " : "") +
            (taxRuleId != null ? "taxRuleId=" + taxRuleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
