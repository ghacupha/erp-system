package io.github.erp.domain;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssetDisposal.
 */
@Entity
@Table(name = "asset_disposal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetdisposal")
public class AssetDisposal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_disposal_reference")
    private UUID assetDisposalReference;

    @Column(name = "description")
    private String description;

    @Column(name = "asset_cost", precision = 21, scale = 2)
    private BigDecimal assetCost;

    @Column(name = "historical_cost", precision = 21, scale = 2)
    private BigDecimal historicalCost;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "accrued_depreciation", precision = 21, scale = 2, nullable = false)
    private BigDecimal accruedDepreciation;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "net_book_value", precision = 21, scale = 2, nullable = false)
    private BigDecimal netBookValue;

    @Column(name = "decommissioning_date")
    private LocalDate decommissioningDate;

    @NotNull
    @Column(name = "disposal_date", nullable = false)
    private LocalDate disposalDate;

    @Column(name = "dormant")
    private Boolean dormant;

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
    private ApplicationUser modifiedBy;

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

    @ManyToMany
    @JoinTable(
        name = "rel_asset_disposal__placeholder",
        joinColumns = @JoinColumn(name = "asset_disposal_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

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
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private AssetRegistration assetDisposed;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetDisposal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getAssetDisposalReference() {
        return this.assetDisposalReference;
    }

    public AssetDisposal assetDisposalReference(UUID assetDisposalReference) {
        this.setAssetDisposalReference(assetDisposalReference);
        return this;
    }

    public void setAssetDisposalReference(UUID assetDisposalReference) {
        this.assetDisposalReference = assetDisposalReference;
    }

    public String getDescription() {
        return this.description;
    }

    public AssetDisposal description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAssetCost() {
        return this.assetCost;
    }

    public AssetDisposal assetCost(BigDecimal assetCost) {
        this.setAssetCost(assetCost);
        return this;
    }

    public void setAssetCost(BigDecimal assetCost) {
        this.assetCost = assetCost;
    }

    public BigDecimal getHistoricalCost() {
        return this.historicalCost;
    }

    public AssetDisposal historicalCost(BigDecimal historicalCost) {
        this.setHistoricalCost(historicalCost);
        return this;
    }

    public void setHistoricalCost(BigDecimal historicalCost) {
        this.historicalCost = historicalCost;
    }

    public BigDecimal getAccruedDepreciation() {
        return this.accruedDepreciation;
    }

    public AssetDisposal accruedDepreciation(BigDecimal accruedDepreciation) {
        this.setAccruedDepreciation(accruedDepreciation);
        return this;
    }

    public void setAccruedDepreciation(BigDecimal accruedDepreciation) {
        this.accruedDepreciation = accruedDepreciation;
    }

    public BigDecimal getNetBookValue() {
        return this.netBookValue;
    }

    public AssetDisposal netBookValue(BigDecimal netBookValue) {
        this.setNetBookValue(netBookValue);
        return this;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public LocalDate getDecommissioningDate() {
        return this.decommissioningDate;
    }

    public AssetDisposal decommissioningDate(LocalDate decommissioningDate) {
        this.setDecommissioningDate(decommissioningDate);
        return this;
    }

    public void setDecommissioningDate(LocalDate decommissioningDate) {
        this.decommissioningDate = decommissioningDate;
    }

    public LocalDate getDisposalDate() {
        return this.disposalDate;
    }

    public AssetDisposal disposalDate(LocalDate disposalDate) {
        this.setDisposalDate(disposalDate);
        return this;
    }

    public void setDisposalDate(LocalDate disposalDate) {
        this.disposalDate = disposalDate;
    }

    public Boolean getDormant() {
        return this.dormant;
    }

    public AssetDisposal dormant(Boolean dormant) {
        this.setDormant(dormant);
        return this;
    }

    public void setDormant(Boolean dormant) {
        this.dormant = dormant;
    }

    public ApplicationUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(ApplicationUser applicationUser) {
        this.createdBy = applicationUser;
    }

    public AssetDisposal createdBy(ApplicationUser applicationUser) {
        this.setCreatedBy(applicationUser);
        return this;
    }

    public ApplicationUser getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(ApplicationUser applicationUser) {
        this.modifiedBy = applicationUser;
    }

    public AssetDisposal modifiedBy(ApplicationUser applicationUser) {
        this.setModifiedBy(applicationUser);
        return this;
    }

    public ApplicationUser getLastAccessedBy() {
        return this.lastAccessedBy;
    }

    public void setLastAccessedBy(ApplicationUser applicationUser) {
        this.lastAccessedBy = applicationUser;
    }

    public AssetDisposal lastAccessedBy(ApplicationUser applicationUser) {
        this.setLastAccessedBy(applicationUser);
        return this;
    }

    public DepreciationPeriod getEffectivePeriod() {
        return this.effectivePeriod;
    }

    public void setEffectivePeriod(DepreciationPeriod depreciationPeriod) {
        this.effectivePeriod = depreciationPeriod;
    }

    public AssetDisposal effectivePeriod(DepreciationPeriod depreciationPeriod) {
        this.setEffectivePeriod(depreciationPeriod);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public AssetDisposal placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public AssetDisposal addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public AssetDisposal removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public AssetRegistration getAssetDisposed() {
        return this.assetDisposed;
    }

    public void setAssetDisposed(AssetRegistration assetRegistration) {
        this.assetDisposed = assetRegistration;
    }

    public AssetDisposal assetDisposed(AssetRegistration assetRegistration) {
        this.setAssetDisposed(assetRegistration);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetDisposal)) {
            return false;
        }
        return id != null && id.equals(((AssetDisposal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetDisposal{" +
            "id=" + getId() +
            ", assetDisposalReference='" + getAssetDisposalReference() + "'" +
            ", description='" + getDescription() + "'" +
            ", assetCost=" + getAssetCost() +
            ", historicalCost=" + getHistoricalCost() +
            ", accruedDepreciation=" + getAccruedDepreciation() +
            ", netBookValue=" + getNetBookValue() +
            ", decommissioningDate='" + getDecommissioningDate() + "'" +
            ", disposalDate='" + getDisposalDate() + "'" +
            ", dormant='" + getDormant() + "'" +
            "}";
    }
}
