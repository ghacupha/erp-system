package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RouAssetNBVReport.
 */
@Entity
@Table(name = "rou_assetnbvreport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "rouassetnbvreport")
public class RouAssetNBVReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "request_id", nullable = false, unique = true)
    private UUID requestId;

    @Column(name = "time_of_request")
    private ZonedDateTime timeOfRequest;

    @Column(name = "report_is_compiled")
    private Boolean reportIsCompiled;

    @Column(name = "file_checksum")
    private String fileChecksum;

    @Column(name = "tampered")
    private Boolean tampered;

    @Column(name = "filename", unique = true)
    private UUID filename;

    @Column(name = "report_parameters")
    private String reportParameters;

    @Lob
    @Column(name = "report_file")
    private byte[] reportFile;

    @Column(name = "report_file_content_type")
    private String reportFileContentType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalMonth" }, allowSetters = true)
    private LeasePeriod leasePeriod;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser requestedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouAssetNBVReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRequestId() {
        return this.requestId;
    }

    public RouAssetNBVReport requestId(UUID requestId) {
        this.setRequestId(requestId);
        return this;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public ZonedDateTime getTimeOfRequest() {
        return this.timeOfRequest;
    }

    public RouAssetNBVReport timeOfRequest(ZonedDateTime timeOfRequest) {
        this.setTimeOfRequest(timeOfRequest);
        return this;
    }

    public void setTimeOfRequest(ZonedDateTime timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public Boolean getReportIsCompiled() {
        return this.reportIsCompiled;
    }

    public RouAssetNBVReport reportIsCompiled(Boolean reportIsCompiled) {
        this.setReportIsCompiled(reportIsCompiled);
        return this;
    }

    public void setReportIsCompiled(Boolean reportIsCompiled) {
        this.reportIsCompiled = reportIsCompiled;
    }

    public String getFileChecksum() {
        return this.fileChecksum;
    }

    public RouAssetNBVReport fileChecksum(String fileChecksum) {
        this.setFileChecksum(fileChecksum);
        return this;
    }

    public void setFileChecksum(String fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    public Boolean getTampered() {
        return this.tampered;
    }

    public RouAssetNBVReport tampered(Boolean tampered) {
        this.setTampered(tampered);
        return this;
    }

    public void setTampered(Boolean tampered) {
        this.tampered = tampered;
    }

    public UUID getFilename() {
        return this.filename;
    }

    public RouAssetNBVReport filename(UUID filename) {
        this.setFilename(filename);
        return this;
    }

    public void setFilename(UUID filename) {
        this.filename = filename;
    }

    public String getReportParameters() {
        return this.reportParameters;
    }

    public RouAssetNBVReport reportParameters(String reportParameters) {
        this.setReportParameters(reportParameters);
        return this;
    }

    public void setReportParameters(String reportParameters) {
        this.reportParameters = reportParameters;
    }

    public byte[] getReportFile() {
        return this.reportFile;
    }

    public RouAssetNBVReport reportFile(byte[] reportFile) {
        this.setReportFile(reportFile);
        return this;
    }

    public void setReportFile(byte[] reportFile) {
        this.reportFile = reportFile;
    }

    public String getReportFileContentType() {
        return this.reportFileContentType;
    }

    public RouAssetNBVReport reportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
        return this;
    }

    public void setReportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
    }

    public LeasePeriod getLeasePeriod() {
        return this.leasePeriod;
    }

    public void setLeasePeriod(LeasePeriod leasePeriod) {
        this.leasePeriod = leasePeriod;
    }

    public RouAssetNBVReport leasePeriod(LeasePeriod leasePeriod) {
        this.setLeasePeriod(leasePeriod);
        return this;
    }

    public ApplicationUser getRequestedBy() {
        return this.requestedBy;
    }

    public void setRequestedBy(ApplicationUser applicationUser) {
        this.requestedBy = applicationUser;
    }

    public RouAssetNBVReport requestedBy(ApplicationUser applicationUser) {
        this.setRequestedBy(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouAssetNBVReport)) {
            return false;
        }
        return id != null && id.equals(((RouAssetNBVReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouAssetNBVReport{" +
            "id=" + getId() +
            ", requestId='" + getRequestId() + "'" +
            ", timeOfRequest='" + getTimeOfRequest() + "'" +
            ", reportIsCompiled='" + getReportIsCompiled() + "'" +
            ", fileChecksum='" + getFileChecksum() + "'" +
            ", tampered='" + getTampered() + "'" +
            ", filename='" + getFilename() + "'" +
            ", reportParameters='" + getReportParameters() + "'" +
            ", reportFile='" + getReportFile() + "'" +
            ", reportFileContentType='" + getReportFileContentType() + "'" +
            "}";
    }
}
