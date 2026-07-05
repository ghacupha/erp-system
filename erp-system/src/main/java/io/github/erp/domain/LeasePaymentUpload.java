package io.github.erp.domain;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Metadata for bulk uploads of lease payments. The record ties a CSV file to
 * the target IFRS16 contract so that payments created from the batch inherit
 * the same activation status.
 */
@Entity
@Table(name = "lease_payment_upload")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leasepaymentupload")
public class LeasePaymentUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "upload_status")
    private String uploadStatus;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @OneToOne(optional = false)
    @JoinColumn(name = "csv_file_upload_id", nullable = false, unique = true)
    private CsvFileUpload csvFileUpload;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "superintendentServiceOutlet",
            "mainDealer",
            "firstReportingPeriod",
            "lastReportingPeriod",
            "leaseContractDocument",
            "leaseContractCalculations",
            "leasePayments",
        },
        allowSetters = true
    )
    @Field(type = FieldType.Object, ignoreFields = { "leasePayments" })
    private IFRS16LeaseContract leaseContract;

    @OneToMany(mappedBy = "leasePaymentUpload")
    @JsonIgnoreProperties(value = { "leasePaymentUpload", "leaseContract" }, allowSetters = true)
    private Set<LeasePayment> leasePayments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return id;
    }

    public LeasePaymentUpload id(Long id) {
        this.id = id;
        return this;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public LeasePaymentUpload uploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
        return this;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public LeasePaymentUpload createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public LeasePaymentUpload active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CsvFileUpload getCsvFileUpload() {
        return csvFileUpload;
    }

    public LeasePaymentUpload csvFileUpload(CsvFileUpload csvFileUpload) {
        this.csvFileUpload = csvFileUpload;
        return this;
    }

    public void setCsvFileUpload(CsvFileUpload csvFileUpload) {
        this.csvFileUpload = csvFileUpload;
    }

    public IFRS16LeaseContract getLeaseContract() {
        return leaseContract;
    }

    public LeasePaymentUpload leaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.leaseContract = iFRS16LeaseContract;
        return this;
    }

    public void setLeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.leaseContract = iFRS16LeaseContract;
    }

    public Set<LeasePayment> getLeasePayments() {
        return leasePayments;
    }

    public void setLeasePayments(Set<LeasePayment> leasePayments) {
        if (this.leasePayments != null) {
            this.leasePayments.forEach(payment -> payment.setLeasePaymentUpload(null));
        }
        if (leasePayments != null) {
            leasePayments.forEach(payment -> payment.setLeasePaymentUpload(this));
        }
        this.leasePayments = leasePayments;
    }

    public LeasePaymentUpload leasePayments(Set<LeasePayment> leasePayments) {
        this.setLeasePayments(leasePayments);
        return this;
    }

    public LeasePaymentUpload addLeasePayment(LeasePayment leasePayment) {
        this.leasePayments.add(leasePayment);
        leasePayment.setLeasePaymentUpload(this);
        return this;
    }

    public LeasePaymentUpload removeLeasePayment(LeasePayment leasePayment) {
        this.leasePayments.remove(leasePayment);
        leasePayment.setLeasePaymentUpload(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeasePaymentUpload)) {
            return false;
        }
        return id != null && id.equals(((LeasePaymentUpload) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "LeasePaymentUpload{" +
            "id=" + getId() +
            ", uploadStatus='" + getUploadStatus() + '\'' +
            ", createdAt=" + getCreatedAt() +
            ", active=" + getActive() +
            '}';
    }
}
