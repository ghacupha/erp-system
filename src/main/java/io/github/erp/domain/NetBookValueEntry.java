package io.github.erp.domain;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
 * A NetBookValueEntry.
 */
@Entity
@Table(name = "net_book_value_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "netbookvalueentry")
public class NetBookValueEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_number")
    private String assetNumber;

    @Column(name = "asset_tag")
    private String assetTag;

    @Column(name = "asset_description")
    private String assetDescription;

    @NotNull
    @Column(name = "nbv_identifier", nullable = false, unique = true)
    private UUID nbvIdentifier;

    @Column(name = "compilation_job_identifier")
    private UUID compilationJobIdentifier;

    @Column(name = "compilation_batch_identifier")
    private UUID compilationBatchIdentifier;

    @Column(name = "elapsed_months")
    private Integer elapsedMonths;

    @Column(name = "prior_months")
    private Integer priorMonths;

    @Column(name = "useful_life_years")
    private Double usefulLifeYears;

    @Column(name = "net_book_value_amount", precision = 21, scale = 2)
    private BigDecimal netBookValueAmount;

    @Column(name = "previous_net_book_value_amount", precision = 21, scale = 2)
    private BigDecimal previousNetBookValueAmount;

    @Column(name = "historical_cost", precision = 21, scale = 2)
    private BigDecimal historicalCost;

    @Column(name = "capitalization_date")
    private LocalDate capitalizationDate;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private ServiceOutlet serviceOutlet;

    @ManyToOne
    @JsonIgnoreProperties(value = { "previousPeriod", "fiscalMonth" }, allowSetters = true)
    private DepreciationPeriod depreciationPeriod;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings", "fiscalQuarter" }, allowSetters = true)
    private FiscalMonth fiscalMonth;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private DepreciationMethod depreciationMethod;

    @ManyToOne
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
    @JsonIgnoreProperties(value = { "depreciationMethod", "placeholders" }, allowSetters = true)
    private AssetCategory assetCategory;

    @ManyToMany
    @JoinTable(
        name = "rel_net_book_value_entry__placeholder",
        joinColumns = @JoinColumn(name = "net_book_value_entry_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NetBookValueEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetNumber() {
        return this.assetNumber;
    }

    public NetBookValueEntry assetNumber(String assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public NetBookValueEntry assetTag(String assetTag) {
        this.setAssetTag(assetTag);
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDescription() {
        return this.assetDescription;
    }

    public NetBookValueEntry assetDescription(String assetDescription) {
        this.setAssetDescription(assetDescription);
        return this;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public UUID getNbvIdentifier() {
        return this.nbvIdentifier;
    }

    public NetBookValueEntry nbvIdentifier(UUID nbvIdentifier) {
        this.setNbvIdentifier(nbvIdentifier);
        return this;
    }

    public void setNbvIdentifier(UUID nbvIdentifier) {
        this.nbvIdentifier = nbvIdentifier;
    }

    public UUID getCompilationJobIdentifier() {
        return this.compilationJobIdentifier;
    }

    public NetBookValueEntry compilationJobIdentifier(UUID compilationJobIdentifier) {
        this.setCompilationJobIdentifier(compilationJobIdentifier);
        return this;
    }

    public void setCompilationJobIdentifier(UUID compilationJobIdentifier) {
        this.compilationJobIdentifier = compilationJobIdentifier;
    }

    public UUID getCompilationBatchIdentifier() {
        return this.compilationBatchIdentifier;
    }

    public NetBookValueEntry compilationBatchIdentifier(UUID compilationBatchIdentifier) {
        this.setCompilationBatchIdentifier(compilationBatchIdentifier);
        return this;
    }

    public void setCompilationBatchIdentifier(UUID compilationBatchIdentifier) {
        this.compilationBatchIdentifier = compilationBatchIdentifier;
    }

    public Integer getElapsedMonths() {
        return this.elapsedMonths;
    }

    public NetBookValueEntry elapsedMonths(Integer elapsedMonths) {
        this.setElapsedMonths(elapsedMonths);
        return this;
    }

    public void setElapsedMonths(Integer elapsedMonths) {
        this.elapsedMonths = elapsedMonths;
    }

    public Integer getPriorMonths() {
        return this.priorMonths;
    }

    public NetBookValueEntry priorMonths(Integer priorMonths) {
        this.setPriorMonths(priorMonths);
        return this;
    }

    public void setPriorMonths(Integer priorMonths) {
        this.priorMonths = priorMonths;
    }

    public Double getUsefulLifeYears() {
        return this.usefulLifeYears;
    }

    public NetBookValueEntry usefulLifeYears(Double usefulLifeYears) {
        this.setUsefulLifeYears(usefulLifeYears);
        return this;
    }

    public void setUsefulLifeYears(Double usefulLifeYears) {
        this.usefulLifeYears = usefulLifeYears;
    }

    public BigDecimal getNetBookValueAmount() {
        return this.netBookValueAmount;
    }

    public NetBookValueEntry netBookValueAmount(BigDecimal netBookValueAmount) {
        this.setNetBookValueAmount(netBookValueAmount);
        return this;
    }

    public void setNetBookValueAmount(BigDecimal netBookValueAmount) {
        this.netBookValueAmount = netBookValueAmount;
    }

    public BigDecimal getPreviousNetBookValueAmount() {
        return this.previousNetBookValueAmount;
    }

    public NetBookValueEntry previousNetBookValueAmount(BigDecimal previousNetBookValueAmount) {
        this.setPreviousNetBookValueAmount(previousNetBookValueAmount);
        return this;
    }

    public void setPreviousNetBookValueAmount(BigDecimal previousNetBookValueAmount) {
        this.previousNetBookValueAmount = previousNetBookValueAmount;
    }

    public BigDecimal getHistoricalCost() {
        return this.historicalCost;
    }

    public NetBookValueEntry historicalCost(BigDecimal historicalCost) {
        this.setHistoricalCost(historicalCost);
        return this;
    }

    public void setHistoricalCost(BigDecimal historicalCost) {
        this.historicalCost = historicalCost;
    }

    public LocalDate getCapitalizationDate() {
        return this.capitalizationDate;
    }

    public NetBookValueEntry capitalizationDate(LocalDate capitalizationDate) {
        this.setCapitalizationDate(capitalizationDate);
        return this;
    }

    public void setCapitalizationDate(LocalDate capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public ServiceOutlet getServiceOutlet() {
        return this.serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public NetBookValueEntry serviceOutlet(ServiceOutlet serviceOutlet) {
        this.setServiceOutlet(serviceOutlet);
        return this;
    }

    public DepreciationPeriod getDepreciationPeriod() {
        return this.depreciationPeriod;
    }

    public void setDepreciationPeriod(DepreciationPeriod depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
    }

    public NetBookValueEntry depreciationPeriod(DepreciationPeriod depreciationPeriod) {
        this.setDepreciationPeriod(depreciationPeriod);
        return this;
    }

    public FiscalMonth getFiscalMonth() {
        return this.fiscalMonth;
    }

    public void setFiscalMonth(FiscalMonth fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public NetBookValueEntry fiscalMonth(FiscalMonth fiscalMonth) {
        this.setFiscalMonth(fiscalMonth);
        return this;
    }

    public DepreciationMethod getDepreciationMethod() {
        return this.depreciationMethod;
    }

    public void setDepreciationMethod(DepreciationMethod depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public NetBookValueEntry depreciationMethod(DepreciationMethod depreciationMethod) {
        this.setDepreciationMethod(depreciationMethod);
        return this;
    }

    public AssetRegistration getAssetRegistration() {
        return this.assetRegistration;
    }

    public void setAssetRegistration(AssetRegistration assetRegistration) {
        this.assetRegistration = assetRegistration;
    }

    public NetBookValueEntry assetRegistration(AssetRegistration assetRegistration) {
        this.setAssetRegistration(assetRegistration);
        return this;
    }

    public AssetCategory getAssetCategory() {
        return this.assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public NetBookValueEntry assetCategory(AssetCategory assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public NetBookValueEntry placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public NetBookValueEntry addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public NetBookValueEntry removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NetBookValueEntry)) {
            return false;
        }
        return id != null && id.equals(((NetBookValueEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NetBookValueEntry{" +
            "id=" + getId() +
            ", assetNumber='" + getAssetNumber() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDescription='" + getAssetDescription() + "'" +
            ", nbvIdentifier='" + getNbvIdentifier() + "'" +
            ", compilationJobIdentifier='" + getCompilationJobIdentifier() + "'" +
            ", compilationBatchIdentifier='" + getCompilationBatchIdentifier() + "'" +
            ", elapsedMonths=" + getElapsedMonths() +
            ", priorMonths=" + getPriorMonths() +
            ", usefulLifeYears=" + getUsefulLifeYears() +
            ", netBookValueAmount=" + getNetBookValueAmount() +
            ", previousNetBookValueAmount=" + getPreviousNetBookValueAmount() +
            ", historicalCost=" + getHistoricalCost() +
            ", capitalizationDate='" + getCapitalizationDate() + "'" +
            "}";
    }
}
