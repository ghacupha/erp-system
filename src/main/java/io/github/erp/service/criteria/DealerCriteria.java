package io.github.erp.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.github.erp.erp.resources.DealerResource;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.Dealer} entity. This class is used
 * in {@link DealerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dealers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DealerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter dealerName;

    private StringFilter taxNumber;

    private StringFilter postalAddress;

    private StringFilter physicalAddress;

    private StringFilter accountName;

    private StringFilter accountNumber;

    private StringFilter bankersName;

    private StringFilter bankersBranch;

    private StringFilter bankersSwiftCode;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter paymentLabelId;

    private LongFilter dealerGroupId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public DealerCriteria() {}

    public DealerCriteria(DealerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dealerName = other.dealerName == null ? null : other.dealerName.copy();
        this.taxNumber = other.taxNumber == null ? null : other.taxNumber.copy();
        this.postalAddress = other.postalAddress == null ? null : other.postalAddress.copy();
        this.physicalAddress = other.physicalAddress == null ? null : other.physicalAddress.copy();
        this.accountName = other.accountName == null ? null : other.accountName.copy();
        this.accountNumber = other.accountNumber == null ? null : other.accountNumber.copy();
        this.bankersName = other.bankersName == null ? null : other.bankersName.copy();
        this.bankersBranch = other.bankersBranch == null ? null : other.bankersBranch.copy();
        this.bankersSwiftCode = other.bankersSwiftCode == null ? null : other.bankersSwiftCode.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.paymentLabelId = other.paymentLabelId == null ? null : other.paymentLabelId.copy();
        this.dealerGroupId = other.dealerGroupId == null ? null : other.dealerGroupId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DealerCriteria copy() {
        return new DealerCriteria(this);
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

    public StringFilter getDealerName() {
        return dealerName;
    }

    public StringFilter dealerName() {
        if (dealerName == null) {
            dealerName = new StringFilter();
        }
        return dealerName;
    }

    public void setDealerName(StringFilter dealerName) {
        this.dealerName = dealerName;
    }

    public StringFilter getTaxNumber() {
        return taxNumber;
    }

    public StringFilter taxNumber() {
        if (taxNumber == null) {
            taxNumber = new StringFilter();
        }
        return taxNumber;
    }

    public void setTaxNumber(StringFilter taxNumber) {
        this.taxNumber = taxNumber;
    }

    public StringFilter getPostalAddress() {
        return postalAddress;
    }

    public StringFilter postalAddress() {
        if (postalAddress == null) {
            postalAddress = new StringFilter();
        }
        return postalAddress;
    }

    public void setPostalAddress(StringFilter postalAddress) {
        this.postalAddress = postalAddress;
    }

    public StringFilter getPhysicalAddress() {
        return physicalAddress;
    }

    public StringFilter physicalAddress() {
        if (physicalAddress == null) {
            physicalAddress = new StringFilter();
        }
        return physicalAddress;
    }

    public void setPhysicalAddress(StringFilter physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public StringFilter getAccountName() {
        return accountName;
    }

    public StringFilter accountName() {
        if (accountName == null) {
            accountName = new StringFilter();
        }
        return accountName;
    }

    public void setAccountName(StringFilter accountName) {
        this.accountName = accountName;
    }

    public StringFilter getAccountNumber() {
        return accountNumber;
    }

    public StringFilter accountNumber() {
        if (accountNumber == null) {
            accountNumber = new StringFilter();
        }
        return accountNumber;
    }

    public void setAccountNumber(StringFilter accountNumber) {
        this.accountNumber = accountNumber;
    }

    public StringFilter getBankersName() {
        return bankersName;
    }

    public StringFilter bankersName() {
        if (bankersName == null) {
            bankersName = new StringFilter();
        }
        return bankersName;
    }

    public void setBankersName(StringFilter bankersName) {
        this.bankersName = bankersName;
    }

    public StringFilter getBankersBranch() {
        return bankersBranch;
    }

    public StringFilter bankersBranch() {
        if (bankersBranch == null) {
            bankersBranch = new StringFilter();
        }
        return bankersBranch;
    }

    public void setBankersBranch(StringFilter bankersBranch) {
        this.bankersBranch = bankersBranch;
    }

    public StringFilter getBankersSwiftCode() {
        return bankersSwiftCode;
    }

    public StringFilter bankersSwiftCode() {
        if (bankersSwiftCode == null) {
            bankersSwiftCode = new StringFilter();
        }
        return bankersSwiftCode;
    }

    public void setBankersSwiftCode(StringFilter bankersSwiftCode) {
        this.bankersSwiftCode = bankersSwiftCode;
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

    public LongFilter getPaymentLabelId() {
        return paymentLabelId;
    }

    public LongFilter paymentLabelId() {
        if (paymentLabelId == null) {
            paymentLabelId = new LongFilter();
        }
        return paymentLabelId;
    }

    public void setPaymentLabelId(LongFilter paymentLabelId) {
        this.paymentLabelId = paymentLabelId;
    }

    public LongFilter getDealerGroupId() {
        return dealerGroupId;
    }

    public LongFilter dealerGroupId() {
        if (dealerGroupId == null) {
            dealerGroupId = new LongFilter();
        }
        return dealerGroupId;
    }

    public void setDealerGroupId(LongFilter dealerGroupId) {
        this.dealerGroupId = dealerGroupId;
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
        final DealerCriteria that = (DealerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dealerName, that.dealerName) &&
            Objects.equals(taxNumber, that.taxNumber) &&
            Objects.equals(postalAddress, that.postalAddress) &&
            Objects.equals(physicalAddress, that.physicalAddress) &&
            Objects.equals(accountName, that.accountName) &&
            Objects.equals(accountNumber, that.accountNumber) &&
            Objects.equals(bankersName, that.bankersName) &&
            Objects.equals(bankersBranch, that.bankersBranch) &&
            Objects.equals(bankersSwiftCode, that.bankersSwiftCode) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(paymentLabelId, that.paymentLabelId) &&
            Objects.equals(dealerGroupId, that.dealerGroupId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dealerName,
            taxNumber,
            postalAddress,
            physicalAddress,
            accountName,
            accountNumber,
            bankersName,
            bankersBranch,
            bankersSwiftCode,
            fileUploadToken,
            compilationToken,
            paymentLabelId,
            dealerGroupId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DealerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dealerName != null ? "dealerName=" + dealerName + ", " : "") +
            (taxNumber != null ? "taxNumber=" + taxNumber + ", " : "") +
            (postalAddress != null ? "postalAddress=" + postalAddress + ", " : "") +
            (physicalAddress != null ? "physicalAddress=" + physicalAddress + ", " : "") +
            (accountName != null ? "accountName=" + accountName + ", " : "") +
            (accountNumber != null ? "accountNumber=" + accountNumber + ", " : "") +
            (bankersName != null ? "bankersName=" + bankersName + ", " : "") +
            (bankersBranch != null ? "bankersBranch=" + bankersBranch + ", " : "") +
            (bankersSwiftCode != null ? "bankersSwiftCode=" + bankersSwiftCode + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (paymentLabelId != null ? "paymentLabelId=" + paymentLabelId + ", " : "") +
            (dealerGroupId != null ? "dealerGroupId=" + dealerGroupId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
