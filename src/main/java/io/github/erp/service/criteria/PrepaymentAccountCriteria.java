package io.github.erp.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.github.erp.erp.resources.PrepaymentAccountResource;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.PrepaymentAccount} entity. This class is used
 * in {@link PrepaymentAccountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prepayment-accounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PrepaymentAccountCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter catalogueNumber;

    private StringFilter particulars;

    private LongFilter settlementCurrencyId;

    private LongFilter prepaymentTransactionId;

    private LongFilter serviceOutletId;

    private LongFilter dealerId;

    private LongFilter placeholderId;

    private LongFilter debitAccountId;

    private LongFilter transferAccountId;

    private Boolean distinct;

    public PrepaymentAccountCriteria() {}

    public PrepaymentAccountCriteria(PrepaymentAccountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.catalogueNumber = other.catalogueNumber == null ? null : other.catalogueNumber.copy();
        this.particulars = other.particulars == null ? null : other.particulars.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.prepaymentTransactionId = other.prepaymentTransactionId == null ? null : other.prepaymentTransactionId.copy();
        this.serviceOutletId = other.serviceOutletId == null ? null : other.serviceOutletId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.debitAccountId = other.debitAccountId == null ? null : other.debitAccountId.copy();
        this.transferAccountId = other.transferAccountId == null ? null : other.transferAccountId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PrepaymentAccountCriteria copy() {
        return new PrepaymentAccountCriteria(this);
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

    public StringFilter getCatalogueNumber() {
        return catalogueNumber;
    }

    public StringFilter catalogueNumber() {
        if (catalogueNumber == null) {
            catalogueNumber = new StringFilter();
        }
        return catalogueNumber;
    }

    public void setCatalogueNumber(StringFilter catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public StringFilter getParticulars() {
        return particulars;
    }

    public StringFilter particulars() {
        if (particulars == null) {
            particulars = new StringFilter();
        }
        return particulars;
    }

    public void setParticulars(StringFilter particulars) {
        this.particulars = particulars;
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

    public LongFilter getPrepaymentTransactionId() {
        return prepaymentTransactionId;
    }

    public LongFilter prepaymentTransactionId() {
        if (prepaymentTransactionId == null) {
            prepaymentTransactionId = new LongFilter();
        }
        return prepaymentTransactionId;
    }

    public void setPrepaymentTransactionId(LongFilter prepaymentTransactionId) {
        this.prepaymentTransactionId = prepaymentTransactionId;
    }

    public LongFilter getServiceOutletId() {
        return serviceOutletId;
    }

    public LongFilter serviceOutletId() {
        if (serviceOutletId == null) {
            serviceOutletId = new LongFilter();
        }
        return serviceOutletId;
    }

    public void setServiceOutletId(LongFilter serviceOutletId) {
        this.serviceOutletId = serviceOutletId;
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

    public LongFilter getDebitAccountId() {
        return debitAccountId;
    }

    public LongFilter debitAccountId() {
        if (debitAccountId == null) {
            debitAccountId = new LongFilter();
        }
        return debitAccountId;
    }

    public void setDebitAccountId(LongFilter debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public LongFilter getTransferAccountId() {
        return transferAccountId;
    }

    public LongFilter transferAccountId() {
        if (transferAccountId == null) {
            transferAccountId = new LongFilter();
        }
        return transferAccountId;
    }

    public void setTransferAccountId(LongFilter transferAccountId) {
        this.transferAccountId = transferAccountId;
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
        final PrepaymentAccountCriteria that = (PrepaymentAccountCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(catalogueNumber, that.catalogueNumber) &&
            Objects.equals(particulars, that.particulars) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(prepaymentTransactionId, that.prepaymentTransactionId) &&
            Objects.equals(serviceOutletId, that.serviceOutletId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(debitAccountId, that.debitAccountId) &&
            Objects.equals(transferAccountId, that.transferAccountId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            catalogueNumber,
            particulars,
            settlementCurrencyId,
            prepaymentTransactionId,
            serviceOutletId,
            dealerId,
            placeholderId,
            debitAccountId,
            transferAccountId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentAccountCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (catalogueNumber != null ? "catalogueNumber=" + catalogueNumber + ", " : "") +
            (particulars != null ? "particulars=" + particulars + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (prepaymentTransactionId != null ? "prepaymentTransactionId=" + prepaymentTransactionId + ", " : "") +
            (serviceOutletId != null ? "serviceOutletId=" + serviceOutletId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (debitAccountId != null ? "debitAccountId=" + debitAccountId + ", " : "") +
            (transferAccountId != null ? "transferAccountId=" + transferAccountId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
