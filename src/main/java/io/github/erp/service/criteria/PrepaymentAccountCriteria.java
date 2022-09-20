package io.github.erp.service.criteria;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.1.1-SNAPSHOT
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
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.PrepaymentAccount} entity. This class is used
 * in {@link io.github.erp.web.rest.PrepaymentAccountResource} to receive all the possible filtering options from
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

    private BigDecimalFilter prepaymentAmount;

    private UUIDFilter prepaymentGuid;

    private LongFilter settlementCurrencyId;

    private LongFilter prepaymentTransactionId;

    private LongFilter serviceOutletId;

    private LongFilter dealerId;

    private LongFilter debitAccountId;

    private LongFilter transferAccountId;

    private LongFilter placeholderId;

    private LongFilter generalParametersId;

    private LongFilter prepaymentParametersId;

    private Boolean distinct;

    public PrepaymentAccountCriteria() {}

    public PrepaymentAccountCriteria(PrepaymentAccountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.catalogueNumber = other.catalogueNumber == null ? null : other.catalogueNumber.copy();
        this.particulars = other.particulars == null ? null : other.particulars.copy();
        this.prepaymentAmount = other.prepaymentAmount == null ? null : other.prepaymentAmount.copy();
        this.prepaymentGuid = other.prepaymentGuid == null ? null : other.prepaymentGuid.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.prepaymentTransactionId = other.prepaymentTransactionId == null ? null : other.prepaymentTransactionId.copy();
        this.serviceOutletId = other.serviceOutletId == null ? null : other.serviceOutletId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.debitAccountId = other.debitAccountId == null ? null : other.debitAccountId.copy();
        this.transferAccountId = other.transferAccountId == null ? null : other.transferAccountId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.generalParametersId = other.generalParametersId == null ? null : other.generalParametersId.copy();
        this.prepaymentParametersId = other.prepaymentParametersId == null ? null : other.prepaymentParametersId.copy();
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

    public BigDecimalFilter getPrepaymentAmount() {
        return prepaymentAmount;
    }

    public BigDecimalFilter prepaymentAmount() {
        if (prepaymentAmount == null) {
            prepaymentAmount = new BigDecimalFilter();
        }
        return prepaymentAmount;
    }

    public void setPrepaymentAmount(BigDecimalFilter prepaymentAmount) {
        this.prepaymentAmount = prepaymentAmount;
    }

    public UUIDFilter getPrepaymentGuid() {
        return prepaymentGuid;
    }

    public UUIDFilter prepaymentGuid() {
        if (prepaymentGuid == null) {
            prepaymentGuid = new UUIDFilter();
        }
        return prepaymentGuid;
    }

    public void setPrepaymentGuid(UUIDFilter prepaymentGuid) {
        this.prepaymentGuid = prepaymentGuid;
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

    public LongFilter getGeneralParametersId() {
        return generalParametersId;
    }

    public LongFilter generalParametersId() {
        if (generalParametersId == null) {
            generalParametersId = new LongFilter();
        }
        return generalParametersId;
    }

    public void setGeneralParametersId(LongFilter generalParametersId) {
        this.generalParametersId = generalParametersId;
    }

    public LongFilter getPrepaymentParametersId() {
        return prepaymentParametersId;
    }

    public LongFilter prepaymentParametersId() {
        if (prepaymentParametersId == null) {
            prepaymentParametersId = new LongFilter();
        }
        return prepaymentParametersId;
    }

    public void setPrepaymentParametersId(LongFilter prepaymentParametersId) {
        this.prepaymentParametersId = prepaymentParametersId;
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
            Objects.equals(prepaymentAmount, that.prepaymentAmount) &&
            Objects.equals(prepaymentGuid, that.prepaymentGuid) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(prepaymentTransactionId, that.prepaymentTransactionId) &&
            Objects.equals(serviceOutletId, that.serviceOutletId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(debitAccountId, that.debitAccountId) &&
            Objects.equals(transferAccountId, that.transferAccountId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(generalParametersId, that.generalParametersId) &&
            Objects.equals(prepaymentParametersId, that.prepaymentParametersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            catalogueNumber,
            particulars,
            prepaymentAmount,
            prepaymentGuid,
            settlementCurrencyId,
            prepaymentTransactionId,
            serviceOutletId,
            dealerId,
            debitAccountId,
            transferAccountId,
            placeholderId,
            generalParametersId,
            prepaymentParametersId,
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
            (prepaymentAmount != null ? "prepaymentAmount=" + prepaymentAmount + ", " : "") +
            (prepaymentGuid != null ? "prepaymentGuid=" + prepaymentGuid + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (prepaymentTransactionId != null ? "prepaymentTransactionId=" + prepaymentTransactionId + ", " : "") +
            (serviceOutletId != null ? "serviceOutletId=" + serviceOutletId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (debitAccountId != null ? "debitAccountId=" + debitAccountId + ", " : "") +
            (transferAccountId != null ? "transferAccountId=" + transferAccountId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (generalParametersId != null ? "generalParametersId=" + generalParametersId + ", " : "") +
            (prepaymentParametersId != null ? "prepaymentParametersId=" + prepaymentParametersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
