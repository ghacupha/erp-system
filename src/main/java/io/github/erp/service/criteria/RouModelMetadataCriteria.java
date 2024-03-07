package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
 * Criteria class for the {@link io.github.erp.domain.RouModelMetadata} entity. This class is used
 * in {@link io.github.erp.web.rest.RouModelMetadataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-model-metadata?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouModelMetadataCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter modelTitle;

    private BigDecimalFilter modelVersion;

    private StringFilter description;

    private IntegerFilter leaseTermPeriods;

    private BigDecimalFilter leaseAmount;

    private UUIDFilter rouModelReference;

    private LongFilter ifrs16LeaseContractId;

    private LongFilter assetAccountId;

    private LongFilter depreciationAccountId;

    private LongFilter accruedDepreciationAccountId;

    private LongFilter assetCategoryId;

    private LongFilter documentAttachmentsId;

    private Boolean distinct;

    public RouModelMetadataCriteria() {}

    public RouModelMetadataCriteria(RouModelMetadataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.modelTitle = other.modelTitle == null ? null : other.modelTitle.copy();
        this.modelVersion = other.modelVersion == null ? null : other.modelVersion.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.leaseTermPeriods = other.leaseTermPeriods == null ? null : other.leaseTermPeriods.copy();
        this.leaseAmount = other.leaseAmount == null ? null : other.leaseAmount.copy();
        this.rouModelReference = other.rouModelReference == null ? null : other.rouModelReference.copy();
        this.ifrs16LeaseContractId = other.ifrs16LeaseContractId == null ? null : other.ifrs16LeaseContractId.copy();
        this.assetAccountId = other.assetAccountId == null ? null : other.assetAccountId.copy();
        this.depreciationAccountId = other.depreciationAccountId == null ? null : other.depreciationAccountId.copy();
        this.accruedDepreciationAccountId = other.accruedDepreciationAccountId == null ? null : other.accruedDepreciationAccountId.copy();
        this.assetCategoryId = other.assetCategoryId == null ? null : other.assetCategoryId.copy();
        this.documentAttachmentsId = other.documentAttachmentsId == null ? null : other.documentAttachmentsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouModelMetadataCriteria copy() {
        return new RouModelMetadataCriteria(this);
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

    public StringFilter getModelTitle() {
        return modelTitle;
    }

    public StringFilter modelTitle() {
        if (modelTitle == null) {
            modelTitle = new StringFilter();
        }
        return modelTitle;
    }

    public void setModelTitle(StringFilter modelTitle) {
        this.modelTitle = modelTitle;
    }

    public BigDecimalFilter getModelVersion() {
        return modelVersion;
    }

    public BigDecimalFilter modelVersion() {
        if (modelVersion == null) {
            modelVersion = new BigDecimalFilter();
        }
        return modelVersion;
    }

    public void setModelVersion(BigDecimalFilter modelVersion) {
        this.modelVersion = modelVersion;
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

    public IntegerFilter getLeaseTermPeriods() {
        return leaseTermPeriods;
    }

    public IntegerFilter leaseTermPeriods() {
        if (leaseTermPeriods == null) {
            leaseTermPeriods = new IntegerFilter();
        }
        return leaseTermPeriods;
    }

    public void setLeaseTermPeriods(IntegerFilter leaseTermPeriods) {
        this.leaseTermPeriods = leaseTermPeriods;
    }

    public BigDecimalFilter getLeaseAmount() {
        return leaseAmount;
    }

    public BigDecimalFilter leaseAmount() {
        if (leaseAmount == null) {
            leaseAmount = new BigDecimalFilter();
        }
        return leaseAmount;
    }

    public void setLeaseAmount(BigDecimalFilter leaseAmount) {
        this.leaseAmount = leaseAmount;
    }

    public UUIDFilter getRouModelReference() {
        return rouModelReference;
    }

    public UUIDFilter rouModelReference() {
        if (rouModelReference == null) {
            rouModelReference = new UUIDFilter();
        }
        return rouModelReference;
    }

    public void setRouModelReference(UUIDFilter rouModelReference) {
        this.rouModelReference = rouModelReference;
    }

    public LongFilter getIfrs16LeaseContractId() {
        return ifrs16LeaseContractId;
    }

    public LongFilter ifrs16LeaseContractId() {
        if (ifrs16LeaseContractId == null) {
            ifrs16LeaseContractId = new LongFilter();
        }
        return ifrs16LeaseContractId;
    }

    public void setIfrs16LeaseContractId(LongFilter ifrs16LeaseContractId) {
        this.ifrs16LeaseContractId = ifrs16LeaseContractId;
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

    public LongFilter getDocumentAttachmentsId() {
        return documentAttachmentsId;
    }

    public LongFilter documentAttachmentsId() {
        if (documentAttachmentsId == null) {
            documentAttachmentsId = new LongFilter();
        }
        return documentAttachmentsId;
    }

    public void setDocumentAttachmentsId(LongFilter documentAttachmentsId) {
        this.documentAttachmentsId = documentAttachmentsId;
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
        final RouModelMetadataCriteria that = (RouModelMetadataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(modelTitle, that.modelTitle) &&
            Objects.equals(modelVersion, that.modelVersion) &&
            Objects.equals(description, that.description) &&
            Objects.equals(leaseTermPeriods, that.leaseTermPeriods) &&
            Objects.equals(leaseAmount, that.leaseAmount) &&
            Objects.equals(rouModelReference, that.rouModelReference) &&
            Objects.equals(ifrs16LeaseContractId, that.ifrs16LeaseContractId) &&
            Objects.equals(assetAccountId, that.assetAccountId) &&
            Objects.equals(depreciationAccountId, that.depreciationAccountId) &&
            Objects.equals(accruedDepreciationAccountId, that.accruedDepreciationAccountId) &&
            Objects.equals(assetCategoryId, that.assetCategoryId) &&
            Objects.equals(documentAttachmentsId, that.documentAttachmentsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            modelTitle,
            modelVersion,
            description,
            leaseTermPeriods,
            leaseAmount,
            rouModelReference,
            ifrs16LeaseContractId,
            assetAccountId,
            depreciationAccountId,
            accruedDepreciationAccountId,
            assetCategoryId,
            documentAttachmentsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouModelMetadataCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (modelTitle != null ? "modelTitle=" + modelTitle + ", " : "") +
            (modelVersion != null ? "modelVersion=" + modelVersion + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (leaseTermPeriods != null ? "leaseTermPeriods=" + leaseTermPeriods + ", " : "") +
            (leaseAmount != null ? "leaseAmount=" + leaseAmount + ", " : "") +
            (rouModelReference != null ? "rouModelReference=" + rouModelReference + ", " : "") +
            (ifrs16LeaseContractId != null ? "ifrs16LeaseContractId=" + ifrs16LeaseContractId + ", " : "") +
            (assetAccountId != null ? "assetAccountId=" + assetAccountId + ", " : "") +
            (depreciationAccountId != null ? "depreciationAccountId=" + depreciationAccountId + ", " : "") +
            (accruedDepreciationAccountId != null ? "accruedDepreciationAccountId=" + accruedDepreciationAccountId + ", " : "") +
            (assetCategoryId != null ? "assetCategoryId=" + assetCategoryId + ", " : "") +
            (documentAttachmentsId != null ? "documentAttachmentsId=" + documentAttachmentsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
