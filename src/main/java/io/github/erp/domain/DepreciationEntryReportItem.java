package io.github.erp.domain;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column(name = "service_outlet")
    private String serviceOutlet;

    @Column(name = "asset_category")
    private String assetCategory;

    @Column(name = "depreciation_method")
    private String depreciationMethod;

    @Column(name = "depreciation_period")
    private String depreciationPeriod;

    @Column(name = "fiscal_month_code")
    private String fiscalMonthCode;

    @Column(name = "asset_registration_cost", precision = 21, scale = 2)
    private BigDecimal assetRegistrationCost;

    @Column(name = "depreciation_amount", precision = 21, scale = 2)
    private BigDecimal depreciationAmount;

    @Column(name = "elapsed_months")
    private Long elapsedMonths;

    @Column(name = "prior_months")
    private Long priorMonths;

    @Column(name = "useful_life_years", precision = 21, scale = 2)
    private BigDecimal usefulLifeYears;

    @Column(name = "previous_nbv", precision = 21, scale = 2)
    private BigDecimal previousNBV;

    @Column(name = "net_book_value", precision = 21, scale = 2)
    private BigDecimal netBookValue;

    @Column(name = "depreciation_period_start_date")
    private LocalDate depreciationPeriodStartDate;

    @Column(name = "depreciation_period_end_date")
    private LocalDate depreciationPeriodEndDate;

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

    public String getServiceOutlet() {
        return this.serviceOutlet;
    }

    public DepreciationEntryReportItem serviceOutlet(String serviceOutlet) {
        this.setServiceOutlet(serviceOutlet);
        return this;
    }

    public void setServiceOutlet(String serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
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

    public String getDepreciationPeriod() {
        return this.depreciationPeriod;
    }

    public DepreciationEntryReportItem depreciationPeriod(String depreciationPeriod) {
        this.setDepreciationPeriod(depreciationPeriod);
        return this;
    }

    public void setDepreciationPeriod(String depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
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

    public Long getElapsedMonths() {
        return this.elapsedMonths;
    }

    public DepreciationEntryReportItem elapsedMonths(Long elapsedMonths) {
        this.setElapsedMonths(elapsedMonths);
        return this;
    }

    public void setElapsedMonths(Long elapsedMonths) {
        this.elapsedMonths = elapsedMonths;
    }

    public Long getPriorMonths() {
        return this.priorMonths;
    }

    public DepreciationEntryReportItem priorMonths(Long priorMonths) {
        this.setPriorMonths(priorMonths);
        return this;
    }

    public void setPriorMonths(Long priorMonths) {
        this.priorMonths = priorMonths;
    }

    public BigDecimal getUsefulLifeYears() {
        return this.usefulLifeYears;
    }

    public DepreciationEntryReportItem usefulLifeYears(BigDecimal usefulLifeYears) {
        this.setUsefulLifeYears(usefulLifeYears);
        return this;
    }

    public void setUsefulLifeYears(BigDecimal usefulLifeYears) {
        this.usefulLifeYears = usefulLifeYears;
    }

    public BigDecimal getPreviousNBV() {
        return this.previousNBV;
    }

    public DepreciationEntryReportItem previousNBV(BigDecimal previousNBV) {
        this.setPreviousNBV(previousNBV);
        return this;
    }

    public void setPreviousNBV(BigDecimal previousNBV) {
        this.previousNBV = previousNBV;
    }

    public BigDecimal getNetBookValue() {
        return this.netBookValue;
    }

    public DepreciationEntryReportItem netBookValue(BigDecimal netBookValue) {
        this.setNetBookValue(netBookValue);
        return this;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public LocalDate getDepreciationPeriodStartDate() {
        return this.depreciationPeriodStartDate;
    }

    public DepreciationEntryReportItem depreciationPeriodStartDate(LocalDate depreciationPeriodStartDate) {
        this.setDepreciationPeriodStartDate(depreciationPeriodStartDate);
        return this;
    }

    public void setDepreciationPeriodStartDate(LocalDate depreciationPeriodStartDate) {
        this.depreciationPeriodStartDate = depreciationPeriodStartDate;
    }

    public LocalDate getDepreciationPeriodEndDate() {
        return this.depreciationPeriodEndDate;
    }

    public DepreciationEntryReportItem depreciationPeriodEndDate(LocalDate depreciationPeriodEndDate) {
        this.setDepreciationPeriodEndDate(depreciationPeriodEndDate);
        return this;
    }

    public void setDepreciationPeriodEndDate(LocalDate depreciationPeriodEndDate) {
        this.depreciationPeriodEndDate = depreciationPeriodEndDate;
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
            ", serviceOutlet='" + getServiceOutlet() + "'" +
            ", assetCategory='" + getAssetCategory() + "'" +
            ", depreciationMethod='" + getDepreciationMethod() + "'" +
            ", depreciationPeriod='" + getDepreciationPeriod() + "'" +
            ", fiscalMonthCode='" + getFiscalMonthCode() + "'" +
            ", assetRegistrationCost=" + getAssetRegistrationCost() +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", elapsedMonths=" + getElapsedMonths() +
            ", priorMonths=" + getPriorMonths() +
            ", usefulLifeYears=" + getUsefulLifeYears() +
            ", previousNBV=" + getPreviousNBV() +
            ", netBookValue=" + getNetBookValue() +
            ", depreciationPeriodStartDate='" + getDepreciationPeriodStartDate() + "'" +
            ", depreciationPeriodEndDate='" + getDepreciationPeriodEndDate() + "'" +
            "}";
    }
}
