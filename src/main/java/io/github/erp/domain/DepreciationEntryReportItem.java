package io.github.erp.domain;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DepreciationEntryReportItem.
 */
@Entity
@Table(name = "depreciation_entry_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "depreciationentryreportitem")
public class DepreciationEntryReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_registration_details")
    private String assetRegistrationDetails;

    @Column(name = "posted_at")
    private String postedAt;

    @Column(name = "asset_number")
    private Long assetNumber;

    @Column(name = "service_outlet_code")
    private String serviceOutletCode;

    @Column(name = "asset_category")
    private String assetCategory;

    @Column(name = "depreciation_method")
    private String depreciationMethod;

    @Column(name = "depreciation_period_code")
    private String depreciationPeriodCode;

    @Column(name = "fiscal_month_code")
    private String fiscalMonthCode;

    @Column(name = "asset_registration_cost", precision = 21, scale = 2)
    private BigDecimal assetRegistrationCost;

    @Column(name = "depreciation_amount", precision = 21, scale = 2)
    private BigDecimal depreciationAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DepreciationEntryReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetRegistrationDetails() {
        return this.assetRegistrationDetails;
    }

    public DepreciationEntryReportItem assetRegistrationDetails(String assetRegistrationDetails) {
        this.setAssetRegistrationDetails(assetRegistrationDetails);
        return this;
    }

    public void setAssetRegistrationDetails(String assetRegistrationDetails) {
        this.assetRegistrationDetails = assetRegistrationDetails;
    }

    public String getPostedAt() {
        return this.postedAt;
    }

    public DepreciationEntryReportItem postedAt(String postedAt) {
        this.setPostedAt(postedAt);
        return this;
    }

    public void setPostedAt(String postedAt) {
        this.postedAt = postedAt;
    }

    public Long getAssetNumber() {
        return this.assetNumber;
    }

    public DepreciationEntryReportItem assetNumber(Long assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(Long assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getServiceOutletCode() {
        return this.serviceOutletCode;
    }

    public DepreciationEntryReportItem serviceOutletCode(String serviceOutletCode) {
        this.setServiceOutletCode(serviceOutletCode);
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getAssetCategory() {
        return this.assetCategory;
    }

    public DepreciationEntryReportItem assetCategory(String assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public String getDepreciationMethod() {
        return this.depreciationMethod;
    }

    public DepreciationEntryReportItem depreciationMethod(String depreciationMethod) {
        this.setDepreciationMethod(depreciationMethod);
        return this;
    }

    public void setDepreciationMethod(String depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public String getDepreciationPeriodCode() {
        return this.depreciationPeriodCode;
    }

    public DepreciationEntryReportItem depreciationPeriodCode(String depreciationPeriodCode) {
        this.setDepreciationPeriodCode(depreciationPeriodCode);
        return this;
    }

    public void setDepreciationPeriodCode(String depreciationPeriodCode) {
        this.depreciationPeriodCode = depreciationPeriodCode;
    }

    public String getFiscalMonthCode() {
        return this.fiscalMonthCode;
    }

    public DepreciationEntryReportItem fiscalMonthCode(String fiscalMonthCode) {
        this.setFiscalMonthCode(fiscalMonthCode);
        return this;
    }

    public void setFiscalMonthCode(String fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public BigDecimal getAssetRegistrationCost() {
        return this.assetRegistrationCost;
    }

    public DepreciationEntryReportItem assetRegistrationCost(BigDecimal assetRegistrationCost) {
        this.setAssetRegistrationCost(assetRegistrationCost);
        return this;
    }

    public void setAssetRegistrationCost(BigDecimal assetRegistrationCost) {
        this.assetRegistrationCost = assetRegistrationCost;
    }

    public BigDecimal getDepreciationAmount() {
        return this.depreciationAmount;
    }

    public DepreciationEntryReportItem depreciationAmount(BigDecimal depreciationAmount) {
        this.setDepreciationAmount(depreciationAmount);
        return this;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationEntryReportItem)) {
            return false;
        }
        return id != null && id.equals(((DepreciationEntryReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationEntryReportItem{" +
            "id=" + getId() +
            ", assetRegistrationDetails='" + getAssetRegistrationDetails() + "'" +
            ", postedAt='" + getPostedAt() + "'" +
            ", assetNumber=" + getAssetNumber() +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            ", assetCategory='" + getAssetCategory() + "'" +
            ", depreciationMethod='" + getDepreciationMethod() + "'" +
            ", depreciationPeriodCode='" + getDepreciationPeriodCode() + "'" +
            ", fiscalMonthCode='" + getFiscalMonthCode() + "'" +
            ", assetRegistrationCost=" + getAssetRegistrationCost() +
            ", depreciationAmount=" + getDepreciationAmount() +
            "}";
    }
}
