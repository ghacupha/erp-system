package io.github.erp.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RouModelMetadata.
 */
@Entity
@Table(name = "rou_model_metadata")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "roumodelmetadata")
public class RouModelMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "model_title", nullable = false)
    private String modelTitle;

    @NotNull
    @Column(name = "model_version", precision = 21, scale = 2, nullable = false)
    private BigDecimal modelVersion;

    @Column(name = "description")
    private String description;

    @Column(name = "lease_term_periods")
    private Integer leaseTermPeriods;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "lease_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal leaseAmount;

    @NotNull
    @Column(name = "rou_model_reference", nullable = false, unique = true)
    private UUID rouModelReference;

    @Column(name = "commencement_date")
    private LocalDate commencementDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "has_been_fully_amortised")
    private Boolean hasBeenFullyAmortised;

    @Column(name = "has_been_decommissioned")
    private Boolean hasBeenDecommissioned;

    @Column(name = "batch_job_identifier")
    private UUID batchJobIdentifier;

    @Column(name = "depreciation_amount_step_identifier")
    private UUID depreciationAmountStepIdentifier;

    @Column(name = "outstanding_amount_step_identifier")
    private UUID outstandingAmountStepIdentifier;

    @Column(name = "flag_amortised_step_identifier")
    private UUID flagAmortisedStepIdentifier;

    @Column(name = "compilation_time")
    private ZonedDateTime compilationTime;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "superintendentServiceOutlet",
            "mainDealer",
            "firstReportingPeriod",
            "lastReportingPeriod",
            "leaseContractDocument",
            "leaseContractCalculations",
            "leasePayments",
        },
        allowSetters = true
    )
    private IFRS16LeaseContract ifrs16LeaseContract;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount assetAccount;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount depreciationAccount;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount accruedDepreciationAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "depreciationMethod", "placeholders" }, allowSetters = true)
    private AssetCategory assetCategory;

    @ManyToMany
    @JoinTable(
        name = "rel_rou_model_metadata__document_attachments",
        joinColumns = @JoinColumn(name = "rou_model_metadata_id"),
        inverseJoinColumns = @JoinColumn(name = "document_attachments_id")
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
    private Set<BusinessDocument> documentAttachments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouModelMetadata id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelTitle() {
        return this.modelTitle;
    }

    public RouModelMetadata modelTitle(String modelTitle) {
        this.setModelTitle(modelTitle);
        return this;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    public BigDecimal getModelVersion() {
        return this.modelVersion;
    }

    public RouModelMetadata modelVersion(BigDecimal modelVersion) {
        this.setModelVersion(modelVersion);
        return this;
    }

    public void setModelVersion(BigDecimal modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getDescription() {
        return this.description;
    }

    public RouModelMetadata description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLeaseTermPeriods() {
        return this.leaseTermPeriods;
    }

    public RouModelMetadata leaseTermPeriods(Integer leaseTermPeriods) {
        this.setLeaseTermPeriods(leaseTermPeriods);
        return this;
    }

    public void setLeaseTermPeriods(Integer leaseTermPeriods) {
        this.leaseTermPeriods = leaseTermPeriods;
    }

    public BigDecimal getLeaseAmount() {
        return this.leaseAmount;
    }

    public RouModelMetadata leaseAmount(BigDecimal leaseAmount) {
        this.setLeaseAmount(leaseAmount);
        return this;
    }

    public void setLeaseAmount(BigDecimal leaseAmount) {
        this.leaseAmount = leaseAmount;
    }

    public UUID getRouModelReference() {
        return this.rouModelReference;
    }

    public RouModelMetadata rouModelReference(UUID rouModelReference) {
        this.setRouModelReference(rouModelReference);
        return this;
    }

    public void setRouModelReference(UUID rouModelReference) {
        this.rouModelReference = rouModelReference;
    }

    public LocalDate getCommencementDate() {
        return this.commencementDate;
    }

    public RouModelMetadata commencementDate(LocalDate commencementDate) {
        this.setCommencementDate(commencementDate);
        return this;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public RouModelMetadata expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getHasBeenFullyAmortised() {
        return this.hasBeenFullyAmortised;
    }

    public RouModelMetadata hasBeenFullyAmortised(Boolean hasBeenFullyAmortised) {
        this.setHasBeenFullyAmortised(hasBeenFullyAmortised);
        return this;
    }

    public void setHasBeenFullyAmortised(Boolean hasBeenFullyAmortised) {
        this.hasBeenFullyAmortised = hasBeenFullyAmortised;
    }

    public Boolean getHasBeenDecommissioned() {
        return this.hasBeenDecommissioned;
    }

    public RouModelMetadata hasBeenDecommissioned(Boolean hasBeenDecommissioned) {
        this.setHasBeenDecommissioned(hasBeenDecommissioned);
        return this;
    }

    public void setHasBeenDecommissioned(Boolean hasBeenDecommissioned) {
        this.hasBeenDecommissioned = hasBeenDecommissioned;
    }

    public UUID getBatchJobIdentifier() {
        return this.batchJobIdentifier;
    }

    public RouModelMetadata batchJobIdentifier(UUID batchJobIdentifier) {
        this.setBatchJobIdentifier(batchJobIdentifier);
        return this;
    }

    public void setBatchJobIdentifier(UUID batchJobIdentifier) {
        this.batchJobIdentifier = batchJobIdentifier;
    }

    public UUID getDepreciationAmountStepIdentifier() {
        return this.depreciationAmountStepIdentifier;
    }

    public RouModelMetadata depreciationAmountStepIdentifier(UUID depreciationAmountStepIdentifier) {
        this.setDepreciationAmountStepIdentifier(depreciationAmountStepIdentifier);
        return this;
    }

    public void setDepreciationAmountStepIdentifier(UUID depreciationAmountStepIdentifier) {
        this.depreciationAmountStepIdentifier = depreciationAmountStepIdentifier;
    }

    public UUID getOutstandingAmountStepIdentifier() {
        return this.outstandingAmountStepIdentifier;
    }

    public RouModelMetadata outstandingAmountStepIdentifier(UUID outstandingAmountStepIdentifier) {
        this.setOutstandingAmountStepIdentifier(outstandingAmountStepIdentifier);
        return this;
    }

    public void setOutstandingAmountStepIdentifier(UUID outstandingAmountStepIdentifier) {
        this.outstandingAmountStepIdentifier = outstandingAmountStepIdentifier;
    }

    public UUID getFlagAmortisedStepIdentifier() {
        return this.flagAmortisedStepIdentifier;
    }

    public RouModelMetadata flagAmortisedStepIdentifier(UUID flagAmortisedStepIdentifier) {
        this.setFlagAmortisedStepIdentifier(flagAmortisedStepIdentifier);
        return this;
    }

    public void setFlagAmortisedStepIdentifier(UUID flagAmortisedStepIdentifier) {
        this.flagAmortisedStepIdentifier = flagAmortisedStepIdentifier;
    }

    public ZonedDateTime getCompilationTime() {
        return this.compilationTime;
    }

    public RouModelMetadata compilationTime(ZonedDateTime compilationTime) {
        this.setCompilationTime(compilationTime);
        return this;
    }

    public void setCompilationTime(ZonedDateTime compilationTime) {
        this.compilationTime = compilationTime;
    }

    public IFRS16LeaseContract getIfrs16LeaseContract() {
        return this.ifrs16LeaseContract;
    }

    public void setIfrs16LeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.ifrs16LeaseContract = iFRS16LeaseContract;
    }

    public RouModelMetadata ifrs16LeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.setIfrs16LeaseContract(iFRS16LeaseContract);
        return this;
    }

    public TransactionAccount getAssetAccount() {
        return this.assetAccount;
    }

    public void setAssetAccount(TransactionAccount transactionAccount) {
        this.assetAccount = transactionAccount;
    }

    public RouModelMetadata assetAccount(TransactionAccount transactionAccount) {
        this.setAssetAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getDepreciationAccount() {
        return this.depreciationAccount;
    }

    public void setDepreciationAccount(TransactionAccount transactionAccount) {
        this.depreciationAccount = transactionAccount;
    }

    public RouModelMetadata depreciationAccount(TransactionAccount transactionAccount) {
        this.setDepreciationAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getAccruedDepreciationAccount() {
        return this.accruedDepreciationAccount;
    }

    public void setAccruedDepreciationAccount(TransactionAccount transactionAccount) {
        this.accruedDepreciationAccount = transactionAccount;
    }

    public RouModelMetadata accruedDepreciationAccount(TransactionAccount transactionAccount) {
        this.setAccruedDepreciationAccount(transactionAccount);
        return this;
    }

    public AssetCategory getAssetCategory() {
        return this.assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public RouModelMetadata assetCategory(AssetCategory assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public Set<BusinessDocument> getDocumentAttachments() {
        return this.documentAttachments;
    }

    public void setDocumentAttachments(Set<BusinessDocument> businessDocuments) {
        this.documentAttachments = businessDocuments;
    }

    public RouModelMetadata documentAttachments(Set<BusinessDocument> businessDocuments) {
        this.setDocumentAttachments(businessDocuments);
        return this;
    }

    public RouModelMetadata addDocumentAttachments(BusinessDocument businessDocument) {
        this.documentAttachments.add(businessDocument);
        return this;
    }

    public RouModelMetadata removeDocumentAttachments(BusinessDocument businessDocument) {
        this.documentAttachments.remove(businessDocument);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouModelMetadata)) {
            return false;
        }
        return id != null && id.equals(((RouModelMetadata) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouModelMetadata{" +
            "id=" + getId() +
            ", modelTitle='" + getModelTitle() + "'" +
            ", modelVersion=" + getModelVersion() +
            ", description='" + getDescription() + "'" +
            ", leaseTermPeriods=" + getLeaseTermPeriods() +
            ", leaseAmount=" + getLeaseAmount() +
            ", rouModelReference='" + getRouModelReference() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", hasBeenFullyAmortised='" + getHasBeenFullyAmortised() + "'" +
            ", hasBeenDecommissioned='" + getHasBeenDecommissioned() + "'" +
            ", batchJobIdentifier='" + getBatchJobIdentifier() + "'" +
            ", depreciationAmountStepIdentifier='" + getDepreciationAmountStepIdentifier() + "'" +
            ", outstandingAmountStepIdentifier='" + getOutstandingAmountStepIdentifier() + "'" +
            ", flagAmortisedStepIdentifier='" + getFlagAmortisedStepIdentifier() + "'" +
            ", compilationTime='" + getCompilationTime() + "'" +
            "}";
    }
}
