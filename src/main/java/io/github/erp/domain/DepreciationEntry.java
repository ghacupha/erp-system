package io.github.erp.domain;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DepreciationEntry.
 */
@Entity
@Table(name = "depreciation_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "depreciationentry")
public class DepreciationEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "posted_at")
    private ZonedDateTime postedAt;

    @Column(name = "depreciation_amount", precision = 21, scale = 2)
    private BigDecimal depreciationAmount;

    @Column(name = "asset_number")
    private Long assetNumber;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private ServiceOutlet serviceOutlet;

    @ManyToOne
    @JsonIgnoreProperties(value = { "depreciationMethod", "placeholders" }, allowSetters = true)
    private AssetCategory assetCategory;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private DepreciationMethod depreciationMethod;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "placeholders",
            "paymentInvoices",
            "serviceOutlets",
            "settlements",
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
        },
        allowSetters = true
    )
    private AssetRegistration assetRegistration;

    @ManyToOne
    @JsonIgnoreProperties(value = { "previousPeriod", "createdBy", "fiscalYear", "fiscalMonth", "fiscalQuarter" }, allowSetters = true)
    private DepreciationPeriod depreciationPeriod;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings", "fiscalQuarter" }, allowSetters = true)
    private FiscalMonth fiscalMonth;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings" }, allowSetters = true)
    private FiscalQuarter fiscalQuarter;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders", "universallyUniqueMappings", "createdBy", "lastUpdatedBy" }, allowSetters = true)
    private FiscalYear fiscalYear;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DepreciationEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getPostedAt() {
        return this.postedAt;
    }

    public DepreciationEntry postedAt(ZonedDateTime postedAt) {
        this.setPostedAt(postedAt);
        return this;
    }

    public void setPostedAt(ZonedDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public BigDecimal getDepreciationAmount() {
        return this.depreciationAmount;
    }

    public DepreciationEntry depreciationAmount(BigDecimal depreciationAmount) {
        this.setDepreciationAmount(depreciationAmount);
        return this;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public Long getAssetNumber() {
        return this.assetNumber;
    }

    public DepreciationEntry assetNumber(Long assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
    }

    public ServiceOutlet getServiceOutlet() {
        return this.serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public DepreciationEntry serviceOutlet(ServiceOutlet serviceOutlet) {
        this.setServiceOutlet(serviceOutlet);
        return this;
    }

    public AssetCategory getAssetCategory() {
        return this.assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public DepreciationEntry assetCategory(AssetCategory assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public DepreciationMethod getDepreciationMethod() {
        return this.depreciationMethod;
    }

    public void setDepreciationMethod(DepreciationMethod depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public DepreciationEntry depreciationMethod(DepreciationMethod depreciationMethod) {
        this.setDepreciationMethod(depreciationMethod);
        return this;
    }

    public AssetRegistration getAssetRegistration() {
        return this.assetRegistration;
    }

    public void setAssetRegistration(AssetRegistration assetRegistration) {
        this.assetRegistration = assetRegistration;
    }

    public DepreciationEntry assetRegistration(AssetRegistration assetRegistration) {
        this.setAssetRegistration(assetRegistration);
        return this;
    }

    public DepreciationPeriod getDepreciationPeriod() {
        return this.depreciationPeriod;
    }

    public void setDepreciationPeriod(DepreciationPeriod depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
    }

    public DepreciationEntry depreciationPeriod(DepreciationPeriod depreciationPeriod) {
        this.setDepreciationPeriod(depreciationPeriod);
        return this;
    }

    public FiscalMonth getFiscalMonth() {
        return this.fiscalMonth;
    }

    public void setFiscalMonth(FiscalMonth fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public DepreciationEntry fiscalMonth(FiscalMonth fiscalMonth) {
        this.setFiscalMonth(fiscalMonth);
        return this;
    }

    public FiscalQuarter getFiscalQuarter() {
        return this.fiscalQuarter;
    }

    public void setFiscalQuarter(FiscalQuarter fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public DepreciationEntry fiscalQuarter(FiscalQuarter fiscalQuarter) {
        this.setFiscalQuarter(fiscalQuarter);
        return this;
    }

    public FiscalYear getFiscalYear() {
        return this.fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public DepreciationEntry fiscalYear(FiscalYear fiscalYear) {
        this.setFiscalYear(fiscalYear);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationEntry)) {
            return false;
        }
        return id != null && id.equals(((DepreciationEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationEntry{" +
            "id=" + getId() +
            ", postedAt='" + getPostedAt() + "'" +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", assetNumber=" + getAssetNumber() +
            "}";
    }
}
