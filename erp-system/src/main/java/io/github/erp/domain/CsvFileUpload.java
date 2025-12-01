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
 * Lightweight representation of a CSV file stored on the file system. Unlike the
 * legacy {@link FileUpload} entity this metadata points to a location on disk and
 * tracks processing status for batch jobs.
 */
@Entity
@Table(name = "csv_file_upload")
public class CsvFileUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @NotNull
    @Column(name = "stored_file_name", nullable = false, unique = true)
    private String storedFileName;

    @NotNull
    @Column(name = "file_path", nullable = false, unique = true)
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content_type")
    private String contentType;

    @NotNull
    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    @Column(name = "processed")
    private Boolean processed = Boolean.FALSE;

    @Column(name = "checksum")
    private String checksum;

    @OneToOne(mappedBy = "csvFileUpload")
    private LeaseLiabilityScheduleFileUpload leaseLiabilityScheduleFileUpload;

    @OneToOne(mappedBy = "csvFileUpload")
    private LeasePaymentUpload leasePaymentUpload;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CsvFileUpload id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public CsvFileUpload originalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        return this;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public CsvFileUpload storedFileName(String storedFileName) {
        this.storedFileName = storedFileName;
        return this;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public CsvFileUpload filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public CsvFileUpload fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public CsvFileUpload contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public CsvFileUpload uploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
        return this;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public CsvFileUpload processed(Boolean processed) {
        this.processed = processed;
        return this;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public String getChecksum() {
        return checksum;
    }

    public CsvFileUpload checksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public LeaseLiabilityScheduleFileUpload getLeaseLiabilityScheduleFileUpload() {
        return leaseLiabilityScheduleFileUpload;
    }

    public void setLeaseLiabilityScheduleFileUpload(LeaseLiabilityScheduleFileUpload leaseLiabilityScheduleFileUpload) {
        this.leaseLiabilityScheduleFileUpload = leaseLiabilityScheduleFileUpload;
    }

    public LeasePaymentUpload getLeasePaymentUpload() {
        return leasePaymentUpload;
    }

    public void setLeasePaymentUpload(LeasePaymentUpload leasePaymentUpload) {
        this.leasePaymentUpload = leasePaymentUpload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CsvFileUpload)) {
            return false;
        }
        return id != null && id.equals(((CsvFileUpload) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CsvFileUpload{" +
            "id=" + getId() +
            ", originalFileName='" + getOriginalFileName() + '\'' +
            ", storedFileName='" + getStoredFileName() + '\'' +
            ", filePath='" + getFilePath() + '\'' +
            ", fileSize=" + getFileSize() +
            ", contentType='" + getContentType() + '\'' +
            ", uploadedAt=" + getUploadedAt() +
            ", processed=" + getProcessed() +
            ", checksum='" + getChecksum() + '\'' +
            '}';
    }
}

