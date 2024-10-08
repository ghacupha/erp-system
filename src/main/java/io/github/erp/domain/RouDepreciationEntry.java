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
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RouDepreciationEntry.
 */
@Entity
@Table(name = "rou_depreciation_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "roudepreciationentry")
public class RouDepreciationEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "depreciation_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal depreciationAmount;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "outstanding_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal outstandingAmount;

    @Column(name = "rou_asset_identifier")
    private UUID rouAssetIdentifier;

    @NotNull
    @Column(name = "rou_depreciation_identifier", nullable = false, unique = true)
    private UUID rouDepreciationIdentifier;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

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

    @Column(name = "invalidated")
    private Boolean invalidated;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount debitAccount;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount creditAccount;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "depreciationMethod", "placeholders" }, allowSetters = true)
    private AssetCategory assetCategory;

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
    private IFRS16LeaseContract leaseContract;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "ifrs16LeaseContract",
            "assetAccount",
            "depreciationAccount",
            "accruedDepreciationAccount",
            "assetCategory",
            "documentAttachments",
        },
        allowSetters = true
    )
    private RouModelMetadata rouMetadata;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalMonth" }, allowSetters = true)
    private LeasePeriod leasePeriod;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouDepreciationEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public RouDepreciationEntry description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDepreciationAmount() {
        return this.depreciationAmount;
    }

    public RouDepreciationEntry depreciationAmount(BigDecimal depreciationAmount) {
        this.setDepreciationAmount(depreciationAmount);
        return this;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return this.outstandingAmount;
    }

    public RouDepreciationEntry outstandingAmount(BigDecimal outstandingAmount) {
        this.setOutstandingAmount(outstandingAmount);
        return this;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public UUID getRouAssetIdentifier() {
        return this.rouAssetIdentifier;
    }

    public RouDepreciationEntry rouAssetIdentifier(UUID rouAssetIdentifier) {
        this.setRouAssetIdentifier(rouAssetIdentifier);
        return this;
    }

    public void setRouAssetIdentifier(UUID rouAssetIdentifier) {
        this.rouAssetIdentifier = rouAssetIdentifier;
    }

    public UUID getRouDepreciationIdentifier() {
        return this.rouDepreciationIdentifier;
    }

    public RouDepreciationEntry rouDepreciationIdentifier(UUID rouDepreciationIdentifier) {
        this.setRouDepreciationIdentifier(rouDepreciationIdentifier);
        return this;
    }

    public void setRouDepreciationIdentifier(UUID rouDepreciationIdentifier) {
        this.rouDepreciationIdentifier = rouDepreciationIdentifier;
    }

    public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

    public RouDepreciationEntry sequenceNumber(Integer sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public RouDepreciationEntry activated(Boolean activated) {
        this.setActivated(activated);
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public RouDepreciationEntry isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UUID getBatchJobIdentifier() {
        return this.batchJobIdentifier;
    }

    public RouDepreciationEntry batchJobIdentifier(UUID batchJobIdentifier) {
        this.setBatchJobIdentifier(batchJobIdentifier);
        return this;
    }

    public void setBatchJobIdentifier(UUID batchJobIdentifier) {
        this.batchJobIdentifier = batchJobIdentifier;
    }

    public UUID getDepreciationAmountStepIdentifier() {
        return this.depreciationAmountStepIdentifier;
    }

    public RouDepreciationEntry depreciationAmountStepIdentifier(UUID depreciationAmountStepIdentifier) {
        this.setDepreciationAmountStepIdentifier(depreciationAmountStepIdentifier);
        return this;
    }

    public void setDepreciationAmountStepIdentifier(UUID depreciationAmountStepIdentifier) {
        this.depreciationAmountStepIdentifier = depreciationAmountStepIdentifier;
    }

    public UUID getOutstandingAmountStepIdentifier() {
        return this.outstandingAmountStepIdentifier;
    }

    public RouDepreciationEntry outstandingAmountStepIdentifier(UUID outstandingAmountStepIdentifier) {
        this.setOutstandingAmountStepIdentifier(outstandingAmountStepIdentifier);
        return this;
    }

    public void setOutstandingAmountStepIdentifier(UUID outstandingAmountStepIdentifier) {
        this.outstandingAmountStepIdentifier = outstandingAmountStepIdentifier;
    }

    public UUID getFlagAmortisedStepIdentifier() {
        return this.flagAmortisedStepIdentifier;
    }

    public RouDepreciationEntry flagAmortisedStepIdentifier(UUID flagAmortisedStepIdentifier) {
        this.setFlagAmortisedStepIdentifier(flagAmortisedStepIdentifier);
        return this;
    }

    public void setFlagAmortisedStepIdentifier(UUID flagAmortisedStepIdentifier) {
        this.flagAmortisedStepIdentifier = flagAmortisedStepIdentifier;
    }

    public ZonedDateTime getCompilationTime() {
        return this.compilationTime;
    }

    public RouDepreciationEntry compilationTime(ZonedDateTime compilationTime) {
        this.setCompilationTime(compilationTime);
        return this;
    }

    public void setCompilationTime(ZonedDateTime compilationTime) {
        this.compilationTime = compilationTime;
    }

    public Boolean getInvalidated() {
        return this.invalidated;
    }

    public RouDepreciationEntry invalidated(Boolean invalidated) {
        this.setInvalidated(invalidated);
        return this;
    }

    public void setInvalidated(Boolean invalidated) {
        this.invalidated = invalidated;
    }

    public TransactionAccount getDebitAccount() {
        return this.debitAccount;
    }

    public void setDebitAccount(TransactionAccount transactionAccount) {
        this.debitAccount = transactionAccount;
    }

    public RouDepreciationEntry debitAccount(TransactionAccount transactionAccount) {
        this.setDebitAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getCreditAccount() {
        return this.creditAccount;
    }

    public void setCreditAccount(TransactionAccount transactionAccount) {
        this.creditAccount = transactionAccount;
    }

    public RouDepreciationEntry creditAccount(TransactionAccount transactionAccount) {
        this.setCreditAccount(transactionAccount);
        return this;
    }

    public AssetCategory getAssetCategory() {
        return this.assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public RouDepreciationEntry assetCategory(AssetCategory assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public IFRS16LeaseContract getLeaseContract() {
        return this.leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.leaseContract = iFRS16LeaseContract;
    }

    public RouDepreciationEntry leaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.setLeaseContract(iFRS16LeaseContract);
        return this;
    }

    public RouModelMetadata getRouMetadata() {
        return this.rouMetadata;
    }

    public void setRouMetadata(RouModelMetadata rouModelMetadata) {
        this.rouMetadata = rouModelMetadata;
    }

    public RouDepreciationEntry rouMetadata(RouModelMetadata rouModelMetadata) {
        this.setRouMetadata(rouModelMetadata);
        return this;
    }

    public LeasePeriod getLeasePeriod() {
        return this.leasePeriod;
    }

    public void setLeasePeriod(LeasePeriod leasePeriod) {
        this.leasePeriod = leasePeriod;
    }

    public RouDepreciationEntry leasePeriod(LeasePeriod leasePeriod) {
        this.setLeasePeriod(leasePeriod);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouDepreciationEntry)) {
            return false;
        }
        return id != null && id.equals(((RouDepreciationEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationEntry{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            ", rouAssetIdentifier='" + getRouAssetIdentifier() + "'" +
            ", rouDepreciationIdentifier='" + getRouDepreciationIdentifier() + "'" +
            ", sequenceNumber=" + getSequenceNumber() +
            ", activated='" + getActivated() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", batchJobIdentifier='" + getBatchJobIdentifier() + "'" +
            ", depreciationAmountStepIdentifier='" + getDepreciationAmountStepIdentifier() + "'" +
            ", outstandingAmountStepIdentifier='" + getOutstandingAmountStepIdentifier() + "'" +
            ", flagAmortisedStepIdentifier='" + getFlagAmortisedStepIdentifier() + "'" +
            ", compilationTime='" + getCompilationTime() + "'" +
            ", invalidated='" + getInvalidated() + "'" +
            "}";
    }
}
