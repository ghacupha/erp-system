package io.github.erp.domain;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
 * A AssetRevaluation.
 */
@Entity
@Table(name = "asset_revaluation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetrevaluation")
public class AssetRevaluation implements Serializable {

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
    @Column(name = "revaluation_date", nullable = false)
    private LocalDate revaluationDate;

    @Column(name = "revaluation_reference_id")
    private UUID revaluationReferenceId;

    @Column(name = "time_of_creation")
    private ZonedDateTime timeOfCreation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer revaluer;

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
    private AssetRegistration revaluedAsset;

    @ManyToMany
    @JoinTable(
        name = "rel_asset_revaluation__placeholder",
        joinColumns = @JoinColumn(name = "asset_revaluation_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetRevaluation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public AssetRevaluation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDevaluationAmount() {
        return this.devaluationAmount;
    }

    public AssetRevaluation devaluationAmount(BigDecimal devaluationAmount) {
        this.setDevaluationAmount(devaluationAmount);
        return this;
    }

    public void setDevaluationAmount(BigDecimal devaluationAmount) {
        this.devaluationAmount = devaluationAmount;
    }

    public LocalDate getRevaluationDate() {
        return this.revaluationDate;
    }

    public AssetRevaluation revaluationDate(LocalDate revaluationDate) {
        this.setRevaluationDate(revaluationDate);
        return this;
    }

    public void setRevaluationDate(LocalDate revaluationDate) {
        this.revaluationDate = revaluationDate;
    }

    public UUID getRevaluationReferenceId() {
        return this.revaluationReferenceId;
    }

    public AssetRevaluation revaluationReferenceId(UUID revaluationReferenceId) {
        this.setRevaluationReferenceId(revaluationReferenceId);
        return this;
    }

    public void setRevaluationReferenceId(UUID revaluationReferenceId) {
        this.revaluationReferenceId = revaluationReferenceId;
    }

    public ZonedDateTime getTimeOfCreation() {
        return this.timeOfCreation;
    }

    public AssetRevaluation timeOfCreation(ZonedDateTime timeOfCreation) {
        this.setTimeOfCreation(timeOfCreation);
        return this;
    }

    public void setTimeOfCreation(ZonedDateTime timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public Dealer getRevaluer() {
        return this.revaluer;
    }

    public void setRevaluer(Dealer dealer) {
        this.revaluer = dealer;
    }

    public AssetRevaluation revaluer(Dealer dealer) {
        this.setRevaluer(dealer);
        return this;
    }

    public ApplicationUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(ApplicationUser applicationUser) {
        this.createdBy = applicationUser;
    }

    public AssetRevaluation createdBy(ApplicationUser applicationUser) {
        this.setCreatedBy(applicationUser);
        return this;
    }

    public ApplicationUser getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(ApplicationUser applicationUser) {
        this.lastModifiedBy = applicationUser;
    }

    public AssetRevaluation lastModifiedBy(ApplicationUser applicationUser) {
        this.setLastModifiedBy(applicationUser);
        return this;
    }

    public ApplicationUser getLastAccessedBy() {
        return this.lastAccessedBy;
    }

    public void setLastAccessedBy(ApplicationUser applicationUser) {
        this.lastAccessedBy = applicationUser;
    }

    public AssetRevaluation lastAccessedBy(ApplicationUser applicationUser) {
        this.setLastAccessedBy(applicationUser);
        return this;
    }

    public DepreciationPeriod getEffectivePeriod() {
        return this.effectivePeriod;
    }

    public void setEffectivePeriod(DepreciationPeriod depreciationPeriod) {
        this.effectivePeriod = depreciationPeriod;
    }

    public AssetRevaluation effectivePeriod(DepreciationPeriod depreciationPeriod) {
        this.setEffectivePeriod(depreciationPeriod);
        return this;
    }

    public AssetRegistration getRevaluedAsset() {
        return this.revaluedAsset;
    }

    public void setRevaluedAsset(AssetRegistration assetRegistration) {
        this.revaluedAsset = assetRegistration;
    }

    public AssetRevaluation revaluedAsset(AssetRegistration assetRegistration) {
        this.setRevaluedAsset(assetRegistration);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public AssetRevaluation placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public AssetRevaluation addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public AssetRevaluation removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetRevaluation)) {
            return false;
        }
        return id != null && id.equals(((AssetRevaluation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetRevaluation{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", devaluationAmount=" + getDevaluationAmount() +
            ", revaluationDate='" + getRevaluationDate() + "'" +
            ", revaluationReferenceId='" + getRevaluationReferenceId() + "'" +
            ", timeOfCreation='" + getTimeOfCreation() + "'" +
            "}";
    }
}
