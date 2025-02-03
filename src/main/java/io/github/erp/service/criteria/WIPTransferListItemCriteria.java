package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.WIPTransferListItem} entity. This class is used
 * in {@link io.github.erp.web.rest.WIPTransferListItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /wip-transfer-list-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WIPTransferListItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter wipSequence;

    private StringFilter wipParticulars;

    private StringFilter transferType;

    private StringFilter transferSettlement;

    private LocalDateFilter transferSettlementDate;

    private BigDecimalFilter transferAmount;

    private LocalDateFilter wipTransferDate;

    private StringFilter originalSettlement;

    private LocalDateFilter originalSettlementDate;

    private StringFilter assetCategory;

    private StringFilter serviceOutlet;

    private StringFilter workProject;

    private Boolean distinct;

    public WIPTransferListItemCriteria() {}

    public WIPTransferListItemCriteria(WIPTransferListItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.wipSequence = other.wipSequence == null ? null : other.wipSequence.copy();
        this.wipParticulars = other.wipParticulars == null ? null : other.wipParticulars.copy();
        this.transferType = other.transferType == null ? null : other.transferType.copy();
        this.transferSettlement = other.transferSettlement == null ? null : other.transferSettlement.copy();
        this.transferSettlementDate = other.transferSettlementDate == null ? null : other.transferSettlementDate.copy();
        this.transferAmount = other.transferAmount == null ? null : other.transferAmount.copy();
        this.wipTransferDate = other.wipTransferDate == null ? null : other.wipTransferDate.copy();
        this.originalSettlement = other.originalSettlement == null ? null : other.originalSettlement.copy();
        this.originalSettlementDate = other.originalSettlementDate == null ? null : other.originalSettlementDate.copy();
        this.assetCategory = other.assetCategory == null ? null : other.assetCategory.copy();
        this.serviceOutlet = other.serviceOutlet == null ? null : other.serviceOutlet.copy();
        this.workProject = other.workProject == null ? null : other.workProject.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WIPTransferListItemCriteria copy() {
        return new WIPTransferListItemCriteria(this);
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

    public LongFilter getWipSequence() {
        return wipSequence;
    }

    public LongFilter wipSequence() {
        if (wipSequence == null) {
            wipSequence = new LongFilter();
        }
        return wipSequence;
    }

    public void setWipSequence(LongFilter wipSequence) {
        this.wipSequence = wipSequence;
    }

    public StringFilter getWipParticulars() {
        return wipParticulars;
    }

    public StringFilter wipParticulars() {
        if (wipParticulars == null) {
            wipParticulars = new StringFilter();
        }
        return wipParticulars;
    }

    public void setWipParticulars(StringFilter wipParticulars) {
        this.wipParticulars = wipParticulars;
    }

    public StringFilter getTransferType() {
        return transferType;
    }

    public StringFilter transferType() {
        if (transferType == null) {
            transferType = new StringFilter();
        }
        return transferType;
    }

    public void setTransferType(StringFilter transferType) {
        this.transferType = transferType;
    }

    public StringFilter getTransferSettlement() {
        return transferSettlement;
    }

    public StringFilter transferSettlement() {
        if (transferSettlement == null) {
            transferSettlement = new StringFilter();
        }
        return transferSettlement;
    }

    public void setTransferSettlement(StringFilter transferSettlement) {
        this.transferSettlement = transferSettlement;
    }

    public LocalDateFilter getTransferSettlementDate() {
        return transferSettlementDate;
    }

    public LocalDateFilter transferSettlementDate() {
        if (transferSettlementDate == null) {
            transferSettlementDate = new LocalDateFilter();
        }
        return transferSettlementDate;
    }

    public void setTransferSettlementDate(LocalDateFilter transferSettlementDate) {
        this.transferSettlementDate = transferSettlementDate;
    }

    public BigDecimalFilter getTransferAmount() {
        return transferAmount;
    }

    public BigDecimalFilter transferAmount() {
        if (transferAmount == null) {
            transferAmount = new BigDecimalFilter();
        }
        return transferAmount;
    }

    public void setTransferAmount(BigDecimalFilter transferAmount) {
        this.transferAmount = transferAmount;
    }

    public LocalDateFilter getWipTransferDate() {
        return wipTransferDate;
    }

    public LocalDateFilter wipTransferDate() {
        if (wipTransferDate == null) {
            wipTransferDate = new LocalDateFilter();
        }
        return wipTransferDate;
    }

    public void setWipTransferDate(LocalDateFilter wipTransferDate) {
        this.wipTransferDate = wipTransferDate;
    }

    public StringFilter getOriginalSettlement() {
        return originalSettlement;
    }

    public StringFilter originalSettlement() {
        if (originalSettlement == null) {
            originalSettlement = new StringFilter();
        }
        return originalSettlement;
    }

    public void setOriginalSettlement(StringFilter originalSettlement) {
        this.originalSettlement = originalSettlement;
    }

    public LocalDateFilter getOriginalSettlementDate() {
        return originalSettlementDate;
    }

    public LocalDateFilter originalSettlementDate() {
        if (originalSettlementDate == null) {
            originalSettlementDate = new LocalDateFilter();
        }
        return originalSettlementDate;
    }

    public void setOriginalSettlementDate(LocalDateFilter originalSettlementDate) {
        this.originalSettlementDate = originalSettlementDate;
    }

    public StringFilter getAssetCategory() {
        return assetCategory;
    }

    public StringFilter assetCategory() {
        if (assetCategory == null) {
            assetCategory = new StringFilter();
        }
        return assetCategory;
    }

    public void setAssetCategory(StringFilter assetCategory) {
        this.assetCategory = assetCategory;
    }

    public StringFilter getServiceOutlet() {
        return serviceOutlet;
    }

    public StringFilter serviceOutlet() {
        if (serviceOutlet == null) {
            serviceOutlet = new StringFilter();
        }
        return serviceOutlet;
    }

    public void setServiceOutlet(StringFilter serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public StringFilter getWorkProject() {
        return workProject;
    }

    public StringFilter workProject() {
        if (workProject == null) {
            workProject = new StringFilter();
        }
        return workProject;
    }

    public void setWorkProject(StringFilter workProject) {
        this.workProject = workProject;
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
        final WIPTransferListItemCriteria that = (WIPTransferListItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(wipSequence, that.wipSequence) &&
            Objects.equals(wipParticulars, that.wipParticulars) &&
            Objects.equals(transferType, that.transferType) &&
            Objects.equals(transferSettlement, that.transferSettlement) &&
            Objects.equals(transferSettlementDate, that.transferSettlementDate) &&
            Objects.equals(transferAmount, that.transferAmount) &&
            Objects.equals(wipTransferDate, that.wipTransferDate) &&
            Objects.equals(originalSettlement, that.originalSettlement) &&
            Objects.equals(originalSettlementDate, that.originalSettlementDate) &&
            Objects.equals(assetCategory, that.assetCategory) &&
            Objects.equals(serviceOutlet, that.serviceOutlet) &&
            Objects.equals(workProject, that.workProject) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            wipSequence,
            wipParticulars,
            transferType,
            transferSettlement,
            transferSettlementDate,
            transferAmount,
            wipTransferDate,
            originalSettlement,
            originalSettlementDate,
            assetCategory,
            serviceOutlet,
            workProject,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WIPTransferListItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (wipSequence != null ? "wipSequence=" + wipSequence + ", " : "") +
            (wipParticulars != null ? "wipParticulars=" + wipParticulars + ", " : "") +
            (transferType != null ? "transferType=" + transferType + ", " : "") +
            (transferSettlement != null ? "transferSettlement=" + transferSettlement + ", " : "") +
            (transferSettlementDate != null ? "transferSettlementDate=" + transferSettlementDate + ", " : "") +
            (transferAmount != null ? "transferAmount=" + transferAmount + ", " : "") +
            (wipTransferDate != null ? "wipTransferDate=" + wipTransferDate + ", " : "") +
            (originalSettlement != null ? "originalSettlement=" + originalSettlement + ", " : "") +
            (originalSettlementDate != null ? "originalSettlementDate=" + originalSettlementDate + ", " : "") +
            (assetCategory != null ? "assetCategory=" + assetCategory + ", " : "") +
            (serviceOutlet != null ? "serviceOutlet=" + serviceOutlet + ", " : "") +
            (workProject != null ? "workProject=" + workProject + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
