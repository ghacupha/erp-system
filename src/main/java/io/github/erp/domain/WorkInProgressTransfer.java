package io.github.erp.domain;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.WorkInProgressTransferType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkInProgressTransfer.
 */
@Entity
@Table(name = "work_in_progress_transfer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workinprogresstransfer")
public class WorkInProgressTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "target_asset_number")
    private String targetAssetNumber;

    @NotNull
    @Column(name = "transfer_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal transferAmount;

    @NotNull
    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_type", nullable = false)
    private WorkInProgressTransferType transferType;

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_transfer__placeholder",
        joinColumns = @JoinColumn(name = "work_in_progress_transfer_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_work_in_progress_transfer__business_document",
        joinColumns = @JoinColumn(name = "work_in_progress_transfer_id"),
        inverseJoinColumns = @JoinColumn(name = "business_document_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private Set<BusinessDocument> businessDocuments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "depreciationMethod", "placeholders" }, allowSetters = true)
    private AssetCategory assetCategory;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "placeholders",
            "workInProgressGroup",
            "settlementCurrency",
            "workProjectRegister",
            "businessDocuments",
            "assetAccessories",
            "assetWarranties",
            "invoice",
            "outletCode",
            "settlementTransaction",
            "purchaseOrder",
            "deliveryNote",
            "jobSheet",
            "dealer",
        },
        allowSetters = true
    )
    private WorkInProgressRegistration workInProgressRegistration;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private ServiceOutlet serviceOutlet;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "placeholders",
            "settlementCurrency",
            "paymentLabels",
            "paymentCategory",
            "groupSettlement",
            "biller",
            "paymentInvoices",
            "signatories",
            "businessDocuments",
        },
        allowSetters = true
    )
    private Settlement settlement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dealers", "settlementCurrency", "placeholders", "businessDocuments" }, allowSetters = true)
    private WorkProjectRegister workProjectRegister;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkInProgressTransfer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public WorkInProgressTransfer description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetAssetNumber() {
        return this.targetAssetNumber;
    }

    public WorkInProgressTransfer targetAssetNumber(String targetAssetNumber) {
        this.setTargetAssetNumber(targetAssetNumber);
        return this;
    }

    public void setTargetAssetNumber(String targetAssetNumber) {
        this.targetAssetNumber = targetAssetNumber;
    }

    public BigDecimal getTransferAmount() {
        return this.transferAmount;
    }

    public WorkInProgressTransfer transferAmount(BigDecimal transferAmount) {
        this.setTransferAmount(transferAmount);
        return this;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public LocalDate getTransferDate() {
        return this.transferDate;
    }

    public WorkInProgressTransfer transferDate(LocalDate transferDate) {
        this.setTransferDate(transferDate);
        return this;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
    }

    public WorkInProgressTransferType getTransferType() {
        return this.transferType;
    }

    public WorkInProgressTransfer transferType(WorkInProgressTransferType transferType) {
        this.setTransferType(transferType);
        return this;
    }

    public void setTransferType(WorkInProgressTransferType transferType) {
        this.transferType = transferType;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public WorkInProgressTransfer placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public WorkInProgressTransfer addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public WorkInProgressTransfer removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<BusinessDocument> getBusinessDocuments() {
        return this.businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocument> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public WorkInProgressTransfer businessDocuments(Set<BusinessDocument> businessDocuments) {
        this.setBusinessDocuments(businessDocuments);
        return this;
    }

    public WorkInProgressTransfer addBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.add(businessDocument);
        return this;
    }

    public WorkInProgressTransfer removeBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.remove(businessDocument);
        return this;
    }

    public AssetCategory getAssetCategory() {
        return this.assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public WorkInProgressTransfer assetCategory(AssetCategory assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public WorkInProgressRegistration getWorkInProgressRegistration() {
        return this.workInProgressRegistration;
    }

    public void setWorkInProgressRegistration(WorkInProgressRegistration workInProgressRegistration) {
        this.workInProgressRegistration = workInProgressRegistration;
    }

    public WorkInProgressTransfer workInProgressRegistration(WorkInProgressRegistration workInProgressRegistration) {
        this.setWorkInProgressRegistration(workInProgressRegistration);
        return this;
    }

    public ServiceOutlet getServiceOutlet() {
        return this.serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public WorkInProgressTransfer serviceOutlet(ServiceOutlet serviceOutlet) {
        this.setServiceOutlet(serviceOutlet);
        return this;
    }

    public Settlement getSettlement() {
        return this.settlement;
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

    public WorkInProgressTransfer settlement(Settlement settlement) {
        this.setSettlement(settlement);
        return this;
    }

    public WorkProjectRegister getWorkProjectRegister() {
        return this.workProjectRegister;
    }

    public void setWorkProjectRegister(WorkProjectRegister workProjectRegister) {
        this.workProjectRegister = workProjectRegister;
    }

    public WorkInProgressTransfer workProjectRegister(WorkProjectRegister workProjectRegister) {
        this.setWorkProjectRegister(workProjectRegister);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkInProgressTransfer)) {
            return false;
        }
        return id != null && id.equals(((WorkInProgressTransfer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressTransfer{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", targetAssetNumber='" + getTargetAssetNumber() + "'" +
            ", transferAmount=" + getTransferAmount() +
            ", transferDate='" + getTransferDate() + "'" +
            ", transferType='" + getTransferType() + "'" +
            "}";
    }
}
