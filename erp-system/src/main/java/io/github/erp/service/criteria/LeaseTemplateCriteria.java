package io.github.erp.service.criteria;
/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.LeaseTemplate} entity. This class is used
 * in {@link io.github.erp.web.rest.LeaseTemplateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 */
public class LeaseTemplateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter templateTitle;

    private LongFilter assetAccountId;

    private LongFilter depreciationAccountId;

    private LongFilter accruedDepreciationAccountId;

    private LongFilter interestPaidTransferDebitAccountId;

    private LongFilter interestPaidTransferCreditAccountId;

    private LongFilter interestAccruedDebitAccountId;

    private LongFilter interestAccruedCreditAccountId;

    private LongFilter leaseRecognitionDebitAccountId;

    private LongFilter leaseRecognitionCreditAccountId;

    private LongFilter leaseRepaymentDebitAccountId;

    private LongFilter leaseRepaymentCreditAccountId;

    private LongFilter rouRecognitionCreditAccountId;

    private LongFilter rouRecognitionDebitAccountId;

    private LongFilter assetCategoryId;

    private LongFilter serviceOutletId;

    private LongFilter mainDealerId;

    private LongFilter leaseContractId;

    private Boolean distinct;

    public LeaseTemplateCriteria() {}

    public LeaseTemplateCriteria(LeaseTemplateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.templateTitle = other.templateTitle == null ? null : other.templateTitle.copy();
        this.assetAccountId = other.assetAccountId == null ? null : other.assetAccountId.copy();
        this.depreciationAccountId = other.depreciationAccountId == null ? null : other.depreciationAccountId.copy();
        this.accruedDepreciationAccountId = other.accruedDepreciationAccountId == null ? null : other.accruedDepreciationAccountId.copy();
        this.interestPaidTransferDebitAccountId =
            other.interestPaidTransferDebitAccountId == null ? null : other.interestPaidTransferDebitAccountId.copy();
        this.interestPaidTransferCreditAccountId =
            other.interestPaidTransferCreditAccountId == null ? null : other.interestPaidTransferCreditAccountId.copy();
        this.interestAccruedDebitAccountId = other.interestAccruedDebitAccountId == null ? null : other.interestAccruedDebitAccountId.copy();
        this.interestAccruedCreditAccountId =
            other.interestAccruedCreditAccountId == null ? null : other.interestAccruedCreditAccountId.copy();
        this.leaseRecognitionDebitAccountId =
            other.leaseRecognitionDebitAccountId == null ? null : other.leaseRecognitionDebitAccountId.copy();
        this.leaseRecognitionCreditAccountId =
            other.leaseRecognitionCreditAccountId == null ? null : other.leaseRecognitionCreditAccountId.copy();
        this.leaseRepaymentDebitAccountId =
            other.leaseRepaymentDebitAccountId == null ? null : other.leaseRepaymentDebitAccountId.copy();
        this.leaseRepaymentCreditAccountId =
            other.leaseRepaymentCreditAccountId == null ? null : other.leaseRepaymentCreditAccountId.copy();
        this.rouRecognitionCreditAccountId =
            other.rouRecognitionCreditAccountId == null ? null : other.rouRecognitionCreditAccountId.copy();
        this.rouRecognitionDebitAccountId =
            other.rouRecognitionDebitAccountId == null ? null : other.rouRecognitionDebitAccountId.copy();
        this.assetCategoryId = other.assetCategoryId == null ? null : other.assetCategoryId.copy();
        this.serviceOutletId = other.serviceOutletId == null ? null : other.serviceOutletId.copy();
        this.mainDealerId = other.mainDealerId == null ? null : other.mainDealerId.copy();
        this.leaseContractId = other.leaseContractId == null ? null : other.leaseContractId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaseTemplateCriteria copy() {
        return new LeaseTemplateCriteria(this);
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

    public StringFilter getTemplateTitle() {
        return templateTitle;
    }

    public StringFilter templateTitle() {
        if (templateTitle == null) {
            templateTitle = new StringFilter();
        }
        return templateTitle;
    }

    public void setTemplateTitle(StringFilter templateTitle) {
        this.templateTitle = templateTitle;
    }

    public LongFilter getAssetAccountId() {
        return assetAccountId;
    }

    public LongFilter assetAccountId() {
        if (assetAccountId == null) {
            assetAccountId = new LongFilter();
        }
        return assetAccountId;
    }

    public void setAssetAccountId(LongFilter assetAccountId) {
        this.assetAccountId = assetAccountId;
    }

    public LongFilter getDepreciationAccountId() {
        return depreciationAccountId;
    }

    public LongFilter depreciationAccountId() {
        if (depreciationAccountId == null) {
            depreciationAccountId = new LongFilter();
        }
        return depreciationAccountId;
    }

    public void setDepreciationAccountId(LongFilter depreciationAccountId) {
        this.depreciationAccountId = depreciationAccountId;
    }

    public LongFilter getAccruedDepreciationAccountId() {
        return accruedDepreciationAccountId;
    }

    public LongFilter accruedDepreciationAccountId() {
        if (accruedDepreciationAccountId == null) {
            accruedDepreciationAccountId = new LongFilter();
        }
        return accruedDepreciationAccountId;
    }

    public void setAccruedDepreciationAccountId(LongFilter accruedDepreciationAccountId) {
        this.accruedDepreciationAccountId = accruedDepreciationAccountId;
    }

    public LongFilter getInterestPaidTransferDebitAccountId() {
        return interestPaidTransferDebitAccountId;
    }

    public LongFilter interestPaidTransferDebitAccountId() {
        if (interestPaidTransferDebitAccountId == null) {
            interestPaidTransferDebitAccountId = new LongFilter();
        }
        return interestPaidTransferDebitAccountId;
    }

    public void setInterestPaidTransferDebitAccountId(LongFilter interestPaidTransferDebitAccountId) {
        this.interestPaidTransferDebitAccountId = interestPaidTransferDebitAccountId;
    }

    public LongFilter getInterestPaidTransferCreditAccountId() {
        return interestPaidTransferCreditAccountId;
    }

    public LongFilter interestPaidTransferCreditAccountId() {
        if (interestPaidTransferCreditAccountId == null) {
            interestPaidTransferCreditAccountId = new LongFilter();
        }
        return interestPaidTransferCreditAccountId;
    }

    public void setInterestPaidTransferCreditAccountId(LongFilter interestPaidTransferCreditAccountId) {
        this.interestPaidTransferCreditAccountId = interestPaidTransferCreditAccountId;
    }

    public LongFilter getInterestAccruedDebitAccountId() {
        return interestAccruedDebitAccountId;
    }

    public LongFilter interestAccruedDebitAccountId() {
        if (interestAccruedDebitAccountId == null) {
            interestAccruedDebitAccountId = new LongFilter();
        }
        return interestAccruedDebitAccountId;
    }

    public void setInterestAccruedDebitAccountId(LongFilter interestAccruedDebitAccountId) {
        this.interestAccruedDebitAccountId = interestAccruedDebitAccountId;
    }

    public LongFilter getInterestAccruedCreditAccountId() {
        return interestAccruedCreditAccountId;
    }

    public LongFilter interestAccruedCreditAccountId() {
        if (interestAccruedCreditAccountId == null) {
            interestAccruedCreditAccountId = new LongFilter();
        }
        return interestAccruedCreditAccountId;
    }

    public void setInterestAccruedCreditAccountId(LongFilter interestAccruedCreditAccountId) {
        this.interestAccruedCreditAccountId = interestAccruedCreditAccountId;
    }

    public LongFilter getLeaseRecognitionDebitAccountId() {
        return leaseRecognitionDebitAccountId;
    }

    public LongFilter leaseRecognitionDebitAccountId() {
        if (leaseRecognitionDebitAccountId == null) {
            leaseRecognitionDebitAccountId = new LongFilter();
        }
        return leaseRecognitionDebitAccountId;
    }

    public void setLeaseRecognitionDebitAccountId(LongFilter leaseRecognitionDebitAccountId) {
        this.leaseRecognitionDebitAccountId = leaseRecognitionDebitAccountId;
    }

    public LongFilter getLeaseRecognitionCreditAccountId() {
        return leaseRecognitionCreditAccountId;
    }

    public LongFilter leaseRecognitionCreditAccountId() {
        if (leaseRecognitionCreditAccountId == null) {
            leaseRecognitionCreditAccountId = new LongFilter();
        }
        return leaseRecognitionCreditAccountId;
    }

    public void setLeaseRecognitionCreditAccountId(LongFilter leaseRecognitionCreditAccountId) {
        this.leaseRecognitionCreditAccountId = leaseRecognitionCreditAccountId;
    }

    public LongFilter getLeaseRepaymentDebitAccountId() {
        return leaseRepaymentDebitAccountId;
    }

    public LongFilter leaseRepaymentDebitAccountId() {
        if (leaseRepaymentDebitAccountId == null) {
            leaseRepaymentDebitAccountId = new LongFilter();
        }
        return leaseRepaymentDebitAccountId;
    }

    public void setLeaseRepaymentDebitAccountId(LongFilter leaseRepaymentDebitAccountId) {
        this.leaseRepaymentDebitAccountId = leaseRepaymentDebitAccountId;
    }

    public LongFilter getLeaseRepaymentCreditAccountId() {
        return leaseRepaymentCreditAccountId;
    }

    public LongFilter leaseRepaymentCreditAccountId() {
        if (leaseRepaymentCreditAccountId == null) {
            leaseRepaymentCreditAccountId = new LongFilter();
        }
        return leaseRepaymentCreditAccountId;
    }

    public void setLeaseRepaymentCreditAccountId(LongFilter leaseRepaymentCreditAccountId) {
        this.leaseRepaymentCreditAccountId = leaseRepaymentCreditAccountId;
    }

    public LongFilter getRouRecognitionCreditAccountId() {
        return rouRecognitionCreditAccountId;
    }

    public LongFilter rouRecognitionCreditAccountId() {
        if (rouRecognitionCreditAccountId == null) {
            rouRecognitionCreditAccountId = new LongFilter();
        }
        return rouRecognitionCreditAccountId;
    }

    public void setRouRecognitionCreditAccountId(LongFilter rouRecognitionCreditAccountId) {
        this.rouRecognitionCreditAccountId = rouRecognitionCreditAccountId;
    }

    public LongFilter getRouRecognitionDebitAccountId() {
        return rouRecognitionDebitAccountId;
    }

    public LongFilter rouRecognitionDebitAccountId() {
        if (rouRecognitionDebitAccountId == null) {
            rouRecognitionDebitAccountId = new LongFilter();
        }
        return rouRecognitionDebitAccountId;
    }

    public void setRouRecognitionDebitAccountId(LongFilter rouRecognitionDebitAccountId) {
        this.rouRecognitionDebitAccountId = rouRecognitionDebitAccountId;
    }

    public LongFilter getAssetCategoryId() {
        return assetCategoryId;
    }

    public LongFilter assetCategoryId() {
        if (assetCategoryId == null) {
            assetCategoryId = new LongFilter();
        }
        return assetCategoryId;
    }

    public void setAssetCategoryId(LongFilter assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
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

    public LongFilter getMainDealerId() {
        return mainDealerId;
    }

    public LongFilter mainDealerId() {
        if (mainDealerId == null) {
            mainDealerId = new LongFilter();
        }
        return mainDealerId;
    }

    public void setMainDealerId(LongFilter mainDealerId) {
        this.mainDealerId = mainDealerId;
    }

    public LongFilter getLeaseContractId() {
        return leaseContractId;
    }

    public LongFilter leaseContractId() {
        if (leaseContractId == null) {
            leaseContractId = new LongFilter();
        }
        return leaseContractId;
    }

    public void setLeaseContractId(LongFilter leaseContractId) {
        this.leaseContractId = leaseContractId;
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
        final LeaseTemplateCriteria that = (LeaseTemplateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(templateTitle, that.templateTitle) &&
            Objects.equals(assetAccountId, that.assetAccountId) &&
            Objects.equals(depreciationAccountId, that.depreciationAccountId) &&
            Objects.equals(accruedDepreciationAccountId, that.accruedDepreciationAccountId) &&
            Objects.equals(interestPaidTransferDebitAccountId, that.interestPaidTransferDebitAccountId) &&
            Objects.equals(interestPaidTransferCreditAccountId, that.interestPaidTransferCreditAccountId) &&
            Objects.equals(interestAccruedDebitAccountId, that.interestAccruedDebitAccountId) &&
            Objects.equals(interestAccruedCreditAccountId, that.interestAccruedCreditAccountId) &&
            Objects.equals(leaseRecognitionDebitAccountId, that.leaseRecognitionDebitAccountId) &&
            Objects.equals(leaseRecognitionCreditAccountId, that.leaseRecognitionCreditAccountId) &&
            Objects.equals(leaseRepaymentDebitAccountId, that.leaseRepaymentDebitAccountId) &&
            Objects.equals(leaseRepaymentCreditAccountId, that.leaseRepaymentCreditAccountId) &&
            Objects.equals(rouRecognitionCreditAccountId, that.rouRecognitionCreditAccountId) &&
            Objects.equals(rouRecognitionDebitAccountId, that.rouRecognitionDebitAccountId) &&
            Objects.equals(assetCategoryId, that.assetCategoryId) &&
            Objects.equals(serviceOutletId, that.serviceOutletId) &&
            Objects.equals(mainDealerId, that.mainDealerId) &&
            Objects.equals(leaseContractId, that.leaseContractId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            templateTitle,
            assetAccountId,
            depreciationAccountId,
            accruedDepreciationAccountId,
            interestPaidTransferDebitAccountId,
            interestPaidTransferCreditAccountId,
            interestAccruedDebitAccountId,
            interestAccruedCreditAccountId,
            leaseRecognitionDebitAccountId,
            leaseRecognitionCreditAccountId,
            leaseRepaymentDebitAccountId,
            leaseRepaymentCreditAccountId,
            rouRecognitionCreditAccountId,
            rouRecognitionDebitAccountId,
            assetCategoryId,
            serviceOutletId,
            mainDealerId,
            leaseContractId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseTemplateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (templateTitle != null ? "templateTitle=" + templateTitle + ", " : "") +
            (assetAccountId != null ? "assetAccountId=" + assetAccountId + ", " : "") +
            (depreciationAccountId != null ? "depreciationAccountId=" + depreciationAccountId + ", " : "") +
            (accruedDepreciationAccountId != null ? "accruedDepreciationAccountId=" + accruedDepreciationAccountId + ", " : "") +
            (interestPaidTransferDebitAccountId != null ? "interestPaidTransferDebitAccountId=" + interestPaidTransferDebitAccountId + ", " : "") +
            (interestPaidTransferCreditAccountId != null ? "interestPaidTransferCreditAccountId=" + interestPaidTransferCreditAccountId + ", " : "") +
            (interestAccruedDebitAccountId != null ? "interestAccruedDebitAccountId=" + interestAccruedDebitAccountId + ", " : "") +
            (interestAccruedCreditAccountId != null ? "interestAccruedCreditAccountId=" + interestAccruedCreditAccountId + ", " : "") +
            (leaseRecognitionDebitAccountId != null ? "leaseRecognitionDebitAccountId=" + leaseRecognitionDebitAccountId + ", " : "") +
            (leaseRecognitionCreditAccountId != null ? "leaseRecognitionCreditAccountId=" + leaseRecognitionCreditAccountId + ", " : "") +
            (leaseRepaymentDebitAccountId != null ? "leaseRepaymentDebitAccountId=" + leaseRepaymentDebitAccountId + ", " : "") +
            (leaseRepaymentCreditAccountId != null ? "leaseRepaymentCreditAccountId=" + leaseRepaymentCreditAccountId + ", " : "") +
            (rouRecognitionCreditAccountId != null ? "rouRecognitionCreditAccountId=" + rouRecognitionCreditAccountId + ", " : "") +
            (rouRecognitionDebitAccountId != null ? "rouRecognitionDebitAccountId=" + rouRecognitionDebitAccountId + ", " : "") +
            (assetCategoryId != null ? "assetCategoryId=" + assetCategoryId + ", " : "") +
            (serviceOutletId != null ? "serviceOutletId=" + serviceOutletId + ", " : "") +
            (mainDealerId != null ? "mainDealerId=" + mainDealerId + ", " : "") +
            (leaseContractId != null ? "leaseContractId=" + leaseContractId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
