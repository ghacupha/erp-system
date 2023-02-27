package io.github.erp.domain;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.5.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ServiceOutlet.
 */
@Entity
@Table(name = "service_outlet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "serviceoutlet")
public class ServiceOutlet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "outlet_code", nullable = false, unique = true)
    private String outletCode;

    @NotNull
    @Column(name = "outlet_name", nullable = false, unique = true)
    private String outletName;

    @Column(name = "town")
    private String town;

    @Column(name = "parliamentary_constituency")
    private String parliamentaryConstituency;

    @Column(name = "gps_coordinates")
    private String gpsCoordinates;

    @Column(name = "outlet_opening_date")
    private LocalDate outletOpeningDate;

    @Column(name = "regulator_approval_date")
    private LocalDate regulatorApprovalDate;

    @Column(name = "outlet_closure_date")
    private LocalDate outletClosureDate;

    @Column(name = "date_last_modified")
    private LocalDate dateLastModified;

    @Column(name = "license_fee_payable", precision = 21, scale = 2)
    private BigDecimal licenseFeePayable;

    @ManyToMany
    @JoinTable(
        name = "rel_service_outlet__placeholder",
        joinColumns = @JoinColumn(name = "service_outlet_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private BankBranchCode bankCode;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private OutletType outletType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private OutletStatus outletStatus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private CountyCode countyName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private CountyCode subCountyName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceOutlet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutletCode() {
        return this.outletCode;
    }

    public ServiceOutlet outletCode(String outletCode) {
        this.setOutletCode(outletCode);
        return this;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getOutletName() {
        return this.outletName;
    }

    public ServiceOutlet outletName(String outletName) {
        this.setOutletName(outletName);
        return this;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getTown() {
        return this.town;
    }

    public ServiceOutlet town(String town) {
        this.setTown(town);
        return this;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getParliamentaryConstituency() {
        return this.parliamentaryConstituency;
    }

    public ServiceOutlet parliamentaryConstituency(String parliamentaryConstituency) {
        this.setParliamentaryConstituency(parliamentaryConstituency);
        return this;
    }

    public void setParliamentaryConstituency(String parliamentaryConstituency) {
        this.parliamentaryConstituency = parliamentaryConstituency;
    }

    public String getGpsCoordinates() {
        return this.gpsCoordinates;
    }

    public ServiceOutlet gpsCoordinates(String gpsCoordinates) {
        this.setGpsCoordinates(gpsCoordinates);
        return this;
    }

    public void setGpsCoordinates(String gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
    }

    public LocalDate getOutletOpeningDate() {
        return this.outletOpeningDate;
    }

    public ServiceOutlet outletOpeningDate(LocalDate outletOpeningDate) {
        this.setOutletOpeningDate(outletOpeningDate);
        return this;
    }

    public void setOutletOpeningDate(LocalDate outletOpeningDate) {
        this.outletOpeningDate = outletOpeningDate;
    }

    public LocalDate getRegulatorApprovalDate() {
        return this.regulatorApprovalDate;
    }

    public ServiceOutlet regulatorApprovalDate(LocalDate regulatorApprovalDate) {
        this.setRegulatorApprovalDate(regulatorApprovalDate);
        return this;
    }

    public void setRegulatorApprovalDate(LocalDate regulatorApprovalDate) {
        this.regulatorApprovalDate = regulatorApprovalDate;
    }

    public LocalDate getOutletClosureDate() {
        return this.outletClosureDate;
    }

    public ServiceOutlet outletClosureDate(LocalDate outletClosureDate) {
        this.setOutletClosureDate(outletClosureDate);
        return this;
    }

    public void setOutletClosureDate(LocalDate outletClosureDate) {
        this.outletClosureDate = outletClosureDate;
    }

    public LocalDate getDateLastModified() {
        return this.dateLastModified;
    }

    public ServiceOutlet dateLastModified(LocalDate dateLastModified) {
        this.setDateLastModified(dateLastModified);
        return this;
    }

    public void setDateLastModified(LocalDate dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public BigDecimal getLicenseFeePayable() {
        return this.licenseFeePayable;
    }

    public ServiceOutlet licenseFeePayable(BigDecimal licenseFeePayable) {
        this.setLicenseFeePayable(licenseFeePayable);
        return this;
    }

    public void setLicenseFeePayable(BigDecimal licenseFeePayable) {
        this.licenseFeePayable = licenseFeePayable;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public ServiceOutlet placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public ServiceOutlet addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public ServiceOutlet removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public BankBranchCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(BankBranchCode bankBranchCode) {
        this.bankCode = bankBranchCode;
    }

    public ServiceOutlet bankCode(BankBranchCode bankBranchCode) {
        this.setBankCode(bankBranchCode);
        return this;
    }

    public OutletType getOutletType() {
        return this.outletType;
    }

    public void setOutletType(OutletType outletType) {
        this.outletType = outletType;
    }

    public ServiceOutlet outletType(OutletType outletType) {
        this.setOutletType(outletType);
        return this;
    }

    public OutletStatus getOutletStatus() {
        return this.outletStatus;
    }

    public void setOutletStatus(OutletStatus outletStatus) {
        this.outletStatus = outletStatus;
    }

    public ServiceOutlet outletStatus(OutletStatus outletStatus) {
        this.setOutletStatus(outletStatus);
        return this;
    }

    public CountyCode getCountyName() {
        return this.countyName;
    }

    public void setCountyName(CountyCode countyCode) {
        this.countyName = countyCode;
    }

    public ServiceOutlet countyName(CountyCode countyCode) {
        this.setCountyName(countyCode);
        return this;
    }

    public CountyCode getSubCountyName() {
        return this.subCountyName;
    }

    public void setSubCountyName(CountyCode countyCode) {
        this.subCountyName = countyCode;
    }

    public ServiceOutlet subCountyName(CountyCode countyCode) {
        this.setSubCountyName(countyCode);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceOutlet)) {
            return false;
        }
        return id != null && id.equals(((ServiceOutlet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceOutlet{" +
            "id=" + getId() +
            ", outletCode='" + getOutletCode() + "'" +
            ", outletName='" + getOutletName() + "'" +
            ", town='" + getTown() + "'" +
            ", parliamentaryConstituency='" + getParliamentaryConstituency() + "'" +
            ", gpsCoordinates='" + getGpsCoordinates() + "'" +
            ", outletOpeningDate='" + getOutletOpeningDate() + "'" +
            ", regulatorApprovalDate='" + getRegulatorApprovalDate() + "'" +
            ", outletClosureDate='" + getOutletClosureDate() + "'" +
            ", dateLastModified='" + getDateLastModified() + "'" +
            ", licenseFeePayable=" + getLicenseFeePayable() +
            "}";
    }
}
