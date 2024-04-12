package io.github.erp.domain;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
 * A AssetWriteOff.
 */
@Entity
@Table(name = "asset_write_off")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetwriteoff")
public class AssetWriteOff implements Serializable {

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
    @Column(name = "write_off_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal writeOffAmount;

    @NotNull
    @Column(name = "write_off_date", nullable = false)
    private LocalDate writeOffDate;

    @Column(name = "write_off_reference_id")
    private UUID writeOffReferenceId;

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
        name = "rel_asset_write_off__placeholder",
        joinColumns = @JoinColumn(name = "asset_write_off_id"),
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
    private AssetRegistration assetWrittenOff;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssetWriteOff id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public AssetWriteOff description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getWriteOffAmount() {
        return this.writeOffAmount;
    }

    public AssetWriteOff writeOffAmount(BigDecimal writeOffAmount) {
        this.setWriteOffAmount(writeOffAmount);
        return this;
    }

    public void setWriteOffAmount(BigDecimal writeOffAmount) {
        this.writeOffAmount = writeOffAmount;
    }

    public LocalDate getWriteOffDate() {
        return this.writeOffDate;
    }

    public AssetWriteOff writeOffDate(LocalDate writeOffDate) {
        this.setWriteOffDate(writeOffDate);
        return this;
    }

    public void setWriteOffDate(LocalDate writeOffDate) {
        this.writeOffDate = writeOffDate;
    }

    public UUID getWriteOffReferenceId() {
        return this.writeOffReferenceId;
    }

    public AssetWriteOff writeOffReferenceId(UUID writeOffReferenceId) {
        this.setWriteOffReferenceId(writeOffReferenceId);
        return this;
    }

    public void setWriteOffReferenceId(UUID writeOffReferenceId) {
        this.writeOffReferenceId = writeOffReferenceId;
    }

    public ApplicationUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(ApplicationUser applicationUser) {
        this.createdBy = applicationUser;
    }

    public AssetWriteOff createdBy(ApplicationUser applicationUser) {
        this.setCreatedBy(applicationUser);
        return this;
    }

    public ApplicationUser getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(ApplicationUser applicationUser) {
        this.modifiedBy = applicationUser;
    }

    public AssetWriteOff modifiedBy(ApplicationUser applicationUser) {
        this.setModifiedBy(applicationUser);
        return this;
    }

    public ApplicationUser getLastAccessedBy() {
        return this.lastAccessedBy;
    }

    public void setLastAccessedBy(ApplicationUser applicationUser) {
        this.lastAccessedBy = applicationUser;
    }

    public AssetWriteOff lastAccessedBy(ApplicationUser applicationUser) {
        this.setLastAccessedBy(applicationUser);
        return this;
    }

    public DepreciationPeriod getEffectivePeriod() {
        return this.effectivePeriod;
    }

    public void setEffectivePeriod(DepreciationPeriod depreciationPeriod) {
        this.effectivePeriod = depreciationPeriod;
    }

    public AssetWriteOff effectivePeriod(DepreciationPeriod depreciationPeriod) {
        this.setEffectivePeriod(depreciationPeriod);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public AssetWriteOff placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public AssetWriteOff addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public AssetWriteOff removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public AssetRegistration getAssetWrittenOff() {
        return this.assetWrittenOff;
    }

    public void setAssetWrittenOff(AssetRegistration assetRegistration) {
        this.assetWrittenOff = assetRegistration;
    }

    public AssetWriteOff assetWrittenOff(AssetRegistration assetRegistration) {
        this.setAssetWrittenOff(assetRegistration);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetWriteOff)) {
            return false;
        }
        return id != null && id.equals(((AssetWriteOff) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetWriteOff{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", writeOffAmount=" + getWriteOffAmount() +
            ", writeOffDate='" + getWriteOffDate() + "'" +
            ", writeOffReferenceId='" + getWriteOffReferenceId() + "'" +
            "}";
    }
}
