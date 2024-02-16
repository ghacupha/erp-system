package io.github.erp.domain;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A JobSheet.
 */
@Entity
@Table(name = "job_sheet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "jobsheet")
public class JobSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "job_sheet_date")
    private LocalDate jobSheetDate;

    @Column(name = "details")
    private String details;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer biller;

    @ManyToMany
    @JoinTable(
        name = "rel_job_sheet__signatories",
        joinColumns = @JoinColumn(name = "job_sheet_id"),
        inverseJoinColumns = @JoinColumn(name = "signatories_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> signatories = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer contactPerson;

    @ManyToMany
    @JoinTable(
        name = "rel_job_sheet__business_stamps",
        joinColumns = @JoinColumn(name = "job_sheet_id"),
        inverseJoinColumns = @JoinColumn(name = "business_stamps_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "stampHolder", "placeholders" }, allowSetters = true)
    private Set<BusinessStamp> businessStamps = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_job_sheet__placeholder",
        joinColumns = @JoinColumn(name = "job_sheet_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_job_sheet__payment_label",
        joinColumns = @JoinColumn(name = "job_sheet_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_label_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPaymentLabel", "placeholders" }, allowSetters = true)
    private Set<PaymentLabel> paymentLabels = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_job_sheet__business_document",
        joinColumns = @JoinColumn(name = "job_sheet_id"),
        inverseJoinColumns = @JoinColumn(name = "business_document_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<BusinessDocument> businessDocuments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JobSheet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public JobSheet serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getJobSheetDate() {
        return this.jobSheetDate;
    }

    public JobSheet jobSheetDate(LocalDate jobSheetDate) {
        this.setJobSheetDate(jobSheetDate);
        return this;
    }

    public void setJobSheetDate(LocalDate jobSheetDate) {
        this.jobSheetDate = jobSheetDate;
    }

    public String getDetails() {
        return this.details;
    }

    public JobSheet details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public JobSheet remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Dealer getBiller() {
        return this.biller;
    }

    public void setBiller(Dealer dealer) {
        this.biller = dealer;
    }

    public JobSheet biller(Dealer dealer) {
        this.setBiller(dealer);
        return this;
    }

    public Set<Dealer> getSignatories() {
        return this.signatories;
    }

    public void setSignatories(Set<Dealer> dealers) {
        this.signatories = dealers;
    }

    public JobSheet signatories(Set<Dealer> dealers) {
        this.setSignatories(dealers);
        return this;
    }

    public JobSheet addSignatories(Dealer dealer) {
        this.signatories.add(dealer);
        return this;
    }

    public JobSheet removeSignatories(Dealer dealer) {
        this.signatories.remove(dealer);
        return this;
    }

    public Dealer getContactPerson() {
        return this.contactPerson;
    }

    public void setContactPerson(Dealer dealer) {
        this.contactPerson = dealer;
    }

    public JobSheet contactPerson(Dealer dealer) {
        this.setContactPerson(dealer);
        return this;
    }

    public Set<BusinessStamp> getBusinessStamps() {
        return this.businessStamps;
    }

    public void setBusinessStamps(Set<BusinessStamp> businessStamps) {
        this.businessStamps = businessStamps;
    }

    public JobSheet businessStamps(Set<BusinessStamp> businessStamps) {
        this.setBusinessStamps(businessStamps);
        return this;
    }

    public JobSheet addBusinessStamps(BusinessStamp businessStamp) {
        this.businessStamps.add(businessStamp);
        return this;
    }

    public JobSheet removeBusinessStamps(BusinessStamp businessStamp) {
        this.businessStamps.remove(businessStamp);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public JobSheet placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public JobSheet addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public JobSheet removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<PaymentLabel> getPaymentLabels() {
        return this.paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public JobSheet paymentLabels(Set<PaymentLabel> paymentLabels) {
        this.setPaymentLabels(paymentLabels);
        return this;
    }

    public JobSheet addPaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.add(paymentLabel);
        return this;
    }

    public JobSheet removePaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.remove(paymentLabel);
        return this;
    }

    public Set<BusinessDocument> getBusinessDocuments() {
        return this.businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocument> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public JobSheet businessDocuments(Set<BusinessDocument> businessDocuments) {
        this.setBusinessDocuments(businessDocuments);
        return this;
    }

    public JobSheet addBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.add(businessDocument);
        return this;
    }

    public JobSheet removeBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.remove(businessDocument);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobSheet)) {
            return false;
        }
        return id != null && id.equals(((JobSheet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobSheet{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", jobSheetDate='" + getJobSheetDate() + "'" +
            ", details='" + getDetails() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
