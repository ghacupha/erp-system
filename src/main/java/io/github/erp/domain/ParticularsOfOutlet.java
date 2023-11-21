package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ParticularsOfOutlet.
 */
@Entity
@Table(name = "particulars_of_outlet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "particularsofoutlet")
public class ParticularsOfOutlet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "business_reporting_date", nullable = false)
    private LocalDate businessReportingDate;

    @NotNull
    @Column(name = "outlet_name", nullable = false, unique = true)
    private String outletName;

    @NotNull
    @Column(name = "town", nullable = false)
    private String town;

    @NotNull
    @Column(name = "iso_6709_latitute", nullable = false)
    private Double iso6709Latitute;

    @NotNull
    @Column(name = "iso_6709_longitude", nullable = false)
    private Double iso6709Longitude;

    @NotNull
    @Column(name = "cbk_approval_date", nullable = false)
    private LocalDate cbkApprovalDate;

    @NotNull
    @Column(name = "outlet_opening_date", nullable = false)
    private LocalDate outletOpeningDate;

    @Column(name = "outlet_closure_date")
    private LocalDate outletClosureDate;

    @NotNull
    @Column(name = "license_fee_payable", precision = 21, scale = 2, nullable = false)
    private BigDecimal licenseFeePayable;

    @ManyToOne(optional = false)
    @NotNull
    private CountySubCountyCode subCountyCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private BankBranchCode outletId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private OutletType typeOfOutlet;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private OutletStatus outletStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ParticularsOfOutlet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBusinessReportingDate() {
        return this.businessReportingDate;
    }

    public ParticularsOfOutlet businessReportingDate(LocalDate businessReportingDate) {
        this.setBusinessReportingDate(businessReportingDate);
        return this;
    }

    public void setBusinessReportingDate(LocalDate businessReportingDate) {
        this.businessReportingDate = businessReportingDate;
    }

    public String getOutletName() {
        return this.outletName;
    }

    public ParticularsOfOutlet outletName(String outletName) {
        this.setOutletName(outletName);
        return this;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getTown() {
        return this.town;
    }

    public ParticularsOfOutlet town(String town) {
        this.setTown(town);
        return this;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Double getIso6709Latitute() {
        return this.iso6709Latitute;
    }

    public ParticularsOfOutlet iso6709Latitute(Double iso6709Latitute) {
        this.setIso6709Latitute(iso6709Latitute);
        return this;
    }

    public void setIso6709Latitute(Double iso6709Latitute) {
        this.iso6709Latitute = iso6709Latitute;
    }

    public Double getIso6709Longitude() {
        return this.iso6709Longitude;
    }

    public ParticularsOfOutlet iso6709Longitude(Double iso6709Longitude) {
        this.setIso6709Longitude(iso6709Longitude);
        return this;
    }

    public void setIso6709Longitude(Double iso6709Longitude) {
        this.iso6709Longitude = iso6709Longitude;
    }

    public LocalDate getCbkApprovalDate() {
        return this.cbkApprovalDate;
    }

    public ParticularsOfOutlet cbkApprovalDate(LocalDate cbkApprovalDate) {
        this.setCbkApprovalDate(cbkApprovalDate);
        return this;
    }

    public void setCbkApprovalDate(LocalDate cbkApprovalDate) {
        this.cbkApprovalDate = cbkApprovalDate;
    }

    public LocalDate getOutletOpeningDate() {
        return this.outletOpeningDate;
    }

    public ParticularsOfOutlet outletOpeningDate(LocalDate outletOpeningDate) {
        this.setOutletOpeningDate(outletOpeningDate);
        return this;
    }

    public void setOutletOpeningDate(LocalDate outletOpeningDate) {
        this.outletOpeningDate = outletOpeningDate;
    }

    public LocalDate getOutletClosureDate() {
        return this.outletClosureDate;
    }

    public ParticularsOfOutlet outletClosureDate(LocalDate outletClosureDate) {
        this.setOutletClosureDate(outletClosureDate);
        return this;
    }

    public void setOutletClosureDate(LocalDate outletClosureDate) {
        this.outletClosureDate = outletClosureDate;
    }

    public BigDecimal getLicenseFeePayable() {
        return this.licenseFeePayable;
    }

    public ParticularsOfOutlet licenseFeePayable(BigDecimal licenseFeePayable) {
        this.setLicenseFeePayable(licenseFeePayable);
        return this;
    }

    public void setLicenseFeePayable(BigDecimal licenseFeePayable) {
        this.licenseFeePayable = licenseFeePayable;
    }

    public CountySubCountyCode getSubCountyCode() {
        return this.subCountyCode;
    }

    public void setSubCountyCode(CountySubCountyCode countySubCountyCode) {
        this.subCountyCode = countySubCountyCode;
    }

    public ParticularsOfOutlet subCountyCode(CountySubCountyCode countySubCountyCode) {
        this.setSubCountyCode(countySubCountyCode);
        return this;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public ParticularsOfOutlet bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public BankBranchCode getOutletId() {
        return this.outletId;
    }

    public void setOutletId(BankBranchCode bankBranchCode) {
        this.outletId = bankBranchCode;
    }

    public ParticularsOfOutlet outletId(BankBranchCode bankBranchCode) {
        this.setOutletId(bankBranchCode);
        return this;
    }

    public OutletType getTypeOfOutlet() {
        return this.typeOfOutlet;
    }

    public void setTypeOfOutlet(OutletType outletType) {
        this.typeOfOutlet = outletType;
    }

    public ParticularsOfOutlet typeOfOutlet(OutletType outletType) {
        this.setTypeOfOutlet(outletType);
        return this;
    }

    public OutletStatus getOutletStatus() {
        return this.outletStatus;
    }

    public void setOutletStatus(OutletStatus outletStatus) {
        this.outletStatus = outletStatus;
    }

    public ParticularsOfOutlet outletStatus(OutletStatus outletStatus) {
        this.setOutletStatus(outletStatus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticularsOfOutlet)) {
            return false;
        }
        return id != null && id.equals(((ParticularsOfOutlet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticularsOfOutlet{" +
            "id=" + getId() +
            ", businessReportingDate='" + getBusinessReportingDate() + "'" +
            ", outletName='" + getOutletName() + "'" +
            ", town='" + getTown() + "'" +
            ", iso6709Latitute=" + getIso6709Latitute() +
            ", iso6709Longitude=" + getIso6709Longitude() +
            ", cbkApprovalDate='" + getCbkApprovalDate() + "'" +
            ", outletOpeningDate='" + getOutletOpeningDate() + "'" +
            ", outletClosureDate='" + getOutletClosureDate() + "'" +
            ", licenseFeePayable=" + getLicenseFeePayable() +
            "}";
    }
}
