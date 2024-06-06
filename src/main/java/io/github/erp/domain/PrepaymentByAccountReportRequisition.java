package io.github.erp.domain;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PrepaymentByAccountReportRequisition.
 */
@Entity
@Table(name = "prepayment_by_acc_rpt_rx")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prepaymentbyaccountreportrequisition")
public class PrepaymentByAccountReportRequisition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "request_id")
    private UUID requestId;

    @NotNull
    @Column(name = "time_of_requisition", nullable = false)
    private ZonedDateTime timeOfRequisition;

    @Column(name = "file_checksum")
    private String fileChecksum;

    @Column(name = "filename")
    private UUID filename;

    @Column(name = "report_parameters")
    private String reportParameters;

    @Lob
    @Column(name = "report_file")
    private byte[] reportFile;

    @Column(name = "report_file_content_type")
    private String reportFileContentType;

    @NotNull
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @Column(name = "tampered")
    private Boolean tampered;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser requestedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser lastAccessedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrepaymentByAccountReportRequisition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRequestId() {
        return this.requestId;
    }

    public PrepaymentByAccountReportRequisition requestId(UUID requestId) {
        this.setRequestId(requestId);
        return this;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public ZonedDateTime getTimeOfRequisition() {
        return this.timeOfRequisition;
    }

    public PrepaymentByAccountReportRequisition timeOfRequisition(ZonedDateTime timeOfRequisition) {
        this.setTimeOfRequisition(timeOfRequisition);
        return this;
    }

    public void setTimeOfRequisition(ZonedDateTime timeOfRequisition) {
        this.timeOfRequisition = timeOfRequisition;
    }

    public String getFileChecksum() {
        return this.fileChecksum;
    }

    public PrepaymentByAccountReportRequisition fileChecksum(String fileChecksum) {
        this.setFileChecksum(fileChecksum);
        return this;
    }

    public void setFileChecksum(String fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    public UUID getFilename() {
        return this.filename;
    }

    public PrepaymentByAccountReportRequisition filename(UUID filename) {
        this.setFilename(filename);
        return this;
    }

    public void setFilename(UUID filename) {
        this.filename = filename;
    }

    public String getReportParameters() {
        return this.reportParameters;
    }

    public PrepaymentByAccountReportRequisition reportParameters(String reportParameters) {
        this.setReportParameters(reportParameters);
        return this;
    }

    public void setReportParameters(String reportParameters) {
        this.reportParameters = reportParameters;
    }

    public byte[] getReportFile() {
        return this.reportFile;
    }

    public PrepaymentByAccountReportRequisition reportFile(byte[] reportFile) {
        this.setReportFile(reportFile);
        return this;
    }

    public void setReportFile(byte[] reportFile) {
        this.reportFile = reportFile;
    }

    public String getReportFileContentType() {
        return this.reportFileContentType;
    }

    public PrepaymentByAccountReportRequisition reportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
        return this;
    }

    public void setReportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
    }

    public LocalDate getReportDate() {
        return this.reportDate;
    }

    public PrepaymentByAccountReportRequisition reportDate(LocalDate reportDate) {
        this.setReportDate(reportDate);
        return this;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public Boolean getTampered() {
        return this.tampered;
    }

    public PrepaymentByAccountReportRequisition tampered(Boolean tampered) {
        this.setTampered(tampered);
        return this;
    }

    public void setTampered(Boolean tampered) {
        this.tampered = tampered;
    }

    public ApplicationUser getRequestedBy() {
        return this.requestedBy;
    }

    public void setRequestedBy(ApplicationUser applicationUser) {
        this.requestedBy = applicationUser;
    }

    public PrepaymentByAccountReportRequisition requestedBy(ApplicationUser applicationUser) {
        this.setRequestedBy(applicationUser);
        return this;
    }

    public ApplicationUser getLastAccessedBy() {
        return this.lastAccessedBy;
    }

    public void setLastAccessedBy(ApplicationUser applicationUser) {
        this.lastAccessedBy = applicationUser;
    }

    public PrepaymentByAccountReportRequisition lastAccessedBy(ApplicationUser applicationUser) {
        this.setLastAccessedBy(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentByAccountReportRequisition)) {
            return false;
        }
        return id != null && id.equals(((PrepaymentByAccountReportRequisition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentByAccountReportRequisition{" +
            "id=" + getId() +
            ", requestId='" + getRequestId() + "'" +
            ", timeOfRequisition='" + getTimeOfRequisition() + "'" +
            ", fileChecksum='" + getFileChecksum() + "'" +
            ", filename='" + getFilename() + "'" +
            ", reportParameters='" + getReportParameters() + "'" +
            ", reportFile='" + getReportFile() + "'" +
            ", reportFileContentType='" + getReportFileContentType() + "'" +
            ", reportDate='" + getReportDate() + "'" +
            ", tampered='" + getTampered() + "'" +
            "}";
    }
}
