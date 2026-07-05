package io.github.erp.domain;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssetGeneralAdjustment.
 */
@Entity
@Table(name = "asset_general_adjustment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetgeneraladjustment")
public class AssetGeneralAdjustment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "devaluation_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal devaluationAmount;

    @NotNull
    @Column(name = "adjustment_date", nullable = false)
    private LocalDate adjustmentDate;

    @NotNull
    @Column(name = "time_of_creation", nullable = false)
    private ZonedDateTime timeOfCreation;

    @NotNull
    @Column(name = "adjustment_reference_id", nullable = false, unique = true)
    private UUID adjustmentReferenceId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "previousPeriod", "fiscalMonth" }, allowSetters = true)
    private DepreciationPeriod effectivePeriod;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "placeholders",
            "paymentInvoices",
            "otherRelatedServiceOutlets",
            "otherRelatedSettlements",
            "assetCategory",
            "purchaseOrders",
            "deliveryNotes",
            "jobSheets",
            "dealer",
            "designatedUsers",
            "settlementCurrency",
            "businessDocuments",
            "assetWarranties",
            "universallyUniqueMappings",
            "assetAccessories",
            "mainServiceOutlet",
            "acquiringTransaction",
        },
        allowSetters = true
    )
    private AssetRegistration assetRegistration;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser createdBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser lastModifiedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser lastAccessedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Placeholder placeholder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetGeneralAdjustment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public AssetGeneralAdjustment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDevaluationAmount() {
        return this.devaluationAmount;
    }

    public AssetGeneralAdjustment devaluationAmount(BigDecimal devaluationAmount) {
        this.setDevaluationAmount(devaluationAmount);
        return this;
    }

    public void setDevaluationAmount(BigDecimal devaluationAmount) {
        this.devaluationAmount = devaluationAmount;
    }

    public LocalDate getAdjustmentDate() {
        return this.adjustmentDate;
    }

    public AssetGeneralAdjustment adjustmentDate(LocalDate adjustmentDate) {
        this.setAdjustmentDate(adjustmentDate);
        return this;
    }

    public void setAdjustmentDate(LocalDate adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public ZonedDateTime getTimeOfCreation() {
        return this.timeOfCreation;
    }

    public AssetGeneralAdjustment timeOfCreation(ZonedDateTime timeOfCreation) {
        this.setTimeOfCreation(timeOfCreation);
        return this;
    }

    public void setTimeOfCreation(ZonedDateTime timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public UUID getAdjustmentReferenceId() {
        return this.adjustmentReferenceId;
    }

    public AssetGeneralAdjustment adjustmentReferenceId(UUID adjustmentReferenceId) {
        this.setAdjustmentReferenceId(adjustmentReferenceId);
        return this;
    }

    public void setAdjustmentReferenceId(UUID adjustmentReferenceId) {
        this.adjustmentReferenceId = adjustmentReferenceId;
    }

    public DepreciationPeriod getEffectivePeriod() {
        return this.effectivePeriod;
    }

    public void setEffectivePeriod(DepreciationPeriod depreciationPeriod) {
        this.effectivePeriod = depreciationPeriod;
    }

    public AssetGeneralAdjustment effectivePeriod(DepreciationPeriod depreciationPeriod) {
        this.setEffectivePeriod(depreciationPeriod);
        return this;
    }

    public AssetRegistration getAssetRegistration() {
        return this.assetRegistration;
    }

    public void setAssetRegistration(AssetRegistration assetRegistration) {
        this.assetRegistration = assetRegistration;
    }

    public AssetGeneralAdjustment assetRegistration(AssetRegistration assetRegistration) {
        this.setAssetRegistration(assetRegistration);
        return this;
    }

    public ApplicationUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(ApplicationUser applicationUser) {
        this.createdBy = applicationUser;
    }

    public AssetGeneralAdjustment createdBy(ApplicationUser applicationUser) {
        this.setCreatedBy(applicationUser);
        return this;
    }

    public ApplicationUser getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(ApplicationUser applicationUser) {
        this.lastModifiedBy = applicationUser;
    }

    public AssetGeneralAdjustment lastModifiedBy(ApplicationUser applicationUser) {
        this.setLastModifiedBy(applicationUser);
        return this;
    }

    public ApplicationUser getLastAccessedBy() {
        return this.lastAccessedBy;
    }

    public void setLastAccessedBy(ApplicationUser applicationUser) {
        this.lastAccessedBy = applicationUser;
    }

    public AssetGeneralAdjustment lastAccessedBy(ApplicationUser applicationUser) {
        this.setLastAccessedBy(applicationUser);
        return this;
    }

    public Placeholder getPlaceholder() {
        return this.placeholder;
    }

    public void setPlaceholder(Placeholder placeholder) {
        this.placeholder = placeholder;
    }

    public AssetGeneralAdjustment placeholder(Placeholder placeholder) {
        this.setPlaceholder(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetGeneralAdjustment)) {
            return false;
        }
        return id != null && id.equals(((AssetGeneralAdjustment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetGeneralAdjustment{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", devaluationAmount=" + getDevaluationAmount() +
            ", adjustmentDate='" + getAdjustmentDate() + "'" +
            ", timeOfCreation='" + getTimeOfCreation() + "'" +
            ", adjustmentReferenceId='" + getAdjustmentReferenceId() + "'" +
            "}";
    }
}
