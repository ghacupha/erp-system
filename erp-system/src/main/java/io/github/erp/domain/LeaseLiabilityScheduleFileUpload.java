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

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Metadata describing a lease liability schedule file upload. The record links the
 * file metadata with the target entities required to enrich the CSV rows prior to
 * persistence.
 */
@Entity
@Table(name = "lease_liability_schedule_file_upload")
public class LeaseLiabilityScheduleFileUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "lease_liability_id", nullable = false)
    private Long leaseLiabilityId;

    @Column(name = "lease_amortization_schedule_id")
    private Long leaseAmortizationScheduleId;

    @NotNull
    @Column(name = "lease_liability_compilation_id", nullable = false)
    private Long leaseLiabilityCompilationId;

    @Column(name = "upload_status")
    private String uploadStatus;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @OneToOne(optional = false)
    @JoinColumn(name = "csv_file_upload_id", nullable = false, unique = true)
    private CsvFileUpload csvFileUpload;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LeaseLiabilityScheduleFileUpload id(Long id) {
        this.id = id;
        return this;
    }

    public Long getLeaseLiabilityId() {
        return leaseLiabilityId;
    }

    public LeaseLiabilityScheduleFileUpload leaseLiabilityId(Long leaseLiabilityId) {
        this.leaseLiabilityId = leaseLiabilityId;
        return this;
    }

    public void setLeaseLiabilityId(Long leaseLiabilityId) {
        this.leaseLiabilityId = leaseLiabilityId;
    }

    public Long getLeaseAmortizationScheduleId() {
        return leaseAmortizationScheduleId;
    }

    public LeaseLiabilityScheduleFileUpload leaseAmortizationScheduleId(Long leaseAmortizationScheduleId) {
        this.leaseAmortizationScheduleId = leaseAmortizationScheduleId;
        return this;
    }

    public void setLeaseAmortizationScheduleId(Long leaseAmortizationScheduleId) {
        this.leaseAmortizationScheduleId = leaseAmortizationScheduleId;
    }

    public Long getLeaseLiabilityCompilationId() {
        return leaseLiabilityCompilationId;
    }

    public LeaseLiabilityScheduleFileUpload leaseLiabilityCompilationId(Long leaseLiabilityCompilationId) {
        this.leaseLiabilityCompilationId = leaseLiabilityCompilationId;
        return this;
    }

    public void setLeaseLiabilityCompilationId(Long leaseLiabilityCompilationId) {
        this.leaseLiabilityCompilationId = leaseLiabilityCompilationId;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public LeaseLiabilityScheduleFileUpload uploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
        return this;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public LeaseLiabilityScheduleFileUpload createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public CsvFileUpload getCsvFileUpload() {
        return csvFileUpload;
    }

    public LeaseLiabilityScheduleFileUpload csvFileUpload(CsvFileUpload csvFileUpload) {
        this.csvFileUpload = csvFileUpload;
        return this;
    }

    public void setCsvFileUpload(CsvFileUpload csvFileUpload) {
        this.csvFileUpload = csvFileUpload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityScheduleFileUpload)) {
            return false;
        }
        return id != null && id.equals(((LeaseLiabilityScheduleFileUpload) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "LeaseLiabilityScheduleFileUpload{" +
            "id=" + getId() +
            ", leaseLiabilityId=" + getLeaseLiabilityId() +
            ", leaseAmortizationScheduleId=" + getLeaseAmortizationScheduleId() +
            ", leaseLiabilityCompilationId=" + getLeaseLiabilityCompilationId() +
            ", uploadStatus='" + getUploadStatus() + '\'' +
            ", createdAt=" + getCreatedAt() +
            '}';
    }
}

