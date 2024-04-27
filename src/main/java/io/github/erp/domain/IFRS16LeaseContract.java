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
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IFRS16LeaseContract.
 */
@Entity
@Table(name = "ifrs16lease_contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ifrs16leasecontract")
public class IFRS16LeaseContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "booking_id", nullable = false, unique = true)
    private String bookingId;

    @NotNull
    @Column(name = "lease_title", nullable = false)
    private String leaseTitle;

    @Column(name = "short_title", unique = true)
    private String shortTitle;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "inception_date", nullable = false)
    private LocalDate inceptionDate;

    @NotNull
    @Column(name = "commencement_date", nullable = false)
    private LocalDate commencementDate;

    @Column(name = "serial_number")
    private UUID serialNumber;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private ServiceOutlet superintendentServiceOutlet;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer mainDealer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings", "fiscalQuarter" }, allowSetters = true)
    private FiscalMonth firstReportingPeriod;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings", "fiscalQuarter" }, allowSetters = true)
    private FiscalMonth lastReportingPeriod;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private BusinessDocument leaseContractDocument;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private BusinessDocument leaseContractCalculations;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IFRS16LeaseContract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return this.bookingId;
    }

    public IFRS16LeaseContract bookingId(String bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getLeaseTitle() {
        return this.leaseTitle;
    }

    public IFRS16LeaseContract leaseTitle(String leaseTitle) {
        this.setLeaseTitle(leaseTitle);
        return this;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public String getShortTitle() {
        return this.shortTitle;
    }

    public IFRS16LeaseContract shortTitle(String shortTitle) {
        this.setShortTitle(shortTitle);
        return this;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getDescription() {
        return this.description;
    }

    public IFRS16LeaseContract description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getInceptionDate() {
        return this.inceptionDate;
    }

    public IFRS16LeaseContract inceptionDate(LocalDate inceptionDate) {
        this.setInceptionDate(inceptionDate);
        return this;
    }

    public void setInceptionDate(LocalDate inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public LocalDate getCommencementDate() {
        return this.commencementDate;
    }

    public IFRS16LeaseContract commencementDate(LocalDate commencementDate) {
        this.setCommencementDate(commencementDate);
        return this;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public UUID getSerialNumber() {
        return this.serialNumber;
    }

    public IFRS16LeaseContract serialNumber(UUID serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(UUID serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ServiceOutlet getSuperintendentServiceOutlet() {
        return this.superintendentServiceOutlet;
    }

    public void setSuperintendentServiceOutlet(ServiceOutlet serviceOutlet) {
        this.superintendentServiceOutlet = serviceOutlet;
    }

    public IFRS16LeaseContract superintendentServiceOutlet(ServiceOutlet serviceOutlet) {
        this.setSuperintendentServiceOutlet(serviceOutlet);
        return this;
    }

    public Dealer getMainDealer() {
        return this.mainDealer;
    }

    public void setMainDealer(Dealer dealer) {
        this.mainDealer = dealer;
    }

    public IFRS16LeaseContract mainDealer(Dealer dealer) {
        this.setMainDealer(dealer);
        return this;
    }

    public FiscalMonth getFirstReportingPeriod() {
        return this.firstReportingPeriod;
    }

    public void setFirstReportingPeriod(FiscalMonth fiscalMonth) {
        this.firstReportingPeriod = fiscalMonth;
    }

    public IFRS16LeaseContract firstReportingPeriod(FiscalMonth fiscalMonth) {
        this.setFirstReportingPeriod(fiscalMonth);
        return this;
    }

    public FiscalMonth getLastReportingPeriod() {
        return this.lastReportingPeriod;
    }

    public void setLastReportingPeriod(FiscalMonth fiscalMonth) {
        this.lastReportingPeriod = fiscalMonth;
    }

    public IFRS16LeaseContract lastReportingPeriod(FiscalMonth fiscalMonth) {
        this.setLastReportingPeriod(fiscalMonth);
        return this;
    }

    public BusinessDocument getLeaseContractDocument() {
        return this.leaseContractDocument;
    }

    public void setLeaseContractDocument(BusinessDocument businessDocument) {
        this.leaseContractDocument = businessDocument;
    }

    public IFRS16LeaseContract leaseContractDocument(BusinessDocument businessDocument) {
        this.setLeaseContractDocument(businessDocument);
        return this;
    }

    public BusinessDocument getLeaseContractCalculations() {
        return this.leaseContractCalculations;
    }

    public void setLeaseContractCalculations(BusinessDocument businessDocument) {
        this.leaseContractCalculations = businessDocument;
    }

    public IFRS16LeaseContract leaseContractCalculations(BusinessDocument businessDocument) {
        this.setLeaseContractCalculations(businessDocument);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IFRS16LeaseContract)) {
            return false;
        }
        return id != null && id.equals(((IFRS16LeaseContract) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IFRS16LeaseContract{" +
            "id=" + getId() +
            ", bookingId='" + getBookingId() + "'" +
            ", leaseTitle='" + getLeaseTitle() + "'" +
            ", shortTitle='" + getShortTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", inceptionDate='" + getInceptionDate() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            "}";
    }
}
