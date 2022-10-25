package io.github.erp.domain;

/*-
 * Erp System - Mark III No 2 (Caleb Series) Server ver 0.1.2-SNAPSHOT
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
import io.github.erp.domain.enumeration.ReportStatusTypes;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A ReportRequisition.
 */
@Entity
@Table(name = "report_requisition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reportrequisition")
public class ReportRequisition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "report_name", nullable = false, unique = true)
    private String reportName;

    @NotNull
    @Column(name = "report_request_time", nullable = false, unique = true)
    private ZonedDateTime reportRequestTime;

    @NotNull
    @Size(min = 6)
    @Column(name = "report_password", nullable = false)
    private String reportPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_status")
    private ReportStatusTypes reportStatus;

    @NotNull
    @Column(name = "report_id", nullable = false, unique = true)
    private UUID reportId;

    @Lob
    @Column(name = "report_file_attachment")
    private byte[] reportFileAttachment;

    @Column(name = "report_file_attachment_content_type")
    private String reportFileAttachmentContentType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "report_file_check_sum")
    private String reportFileCheckSum;

    @Lob
    @Column(name = "report_notes")
    private byte[] reportNotes;

    @Column(name = "report_notes_content_type")
    private String reportNotesContentType;

    @ManyToMany
    @JoinTable(
        name = "rel_report_requisition__placeholders",
        joinColumns = @JoinColumn(name = "report_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholders_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_report_requisition__parameters",
        joinColumns = @JoinColumn(name = "report_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "parameters_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> parameters = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private ReportTemplate reportTemplate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "systemContentType", "placeholders" }, allowSetters = true)
    private ReportContentType reportContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportRequisition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return this.reportName;
    }

    public ReportRequisition reportName(String reportName) {
        this.setReportName(reportName);
        return this;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public ZonedDateTime getReportRequestTime() {
        return this.reportRequestTime;
    }

    public ReportRequisition reportRequestTime(ZonedDateTime reportRequestTime) {
        this.setReportRequestTime(reportRequestTime);
        return this;
    }

    public void setReportRequestTime(ZonedDateTime reportRequestTime) {
        this.reportRequestTime = reportRequestTime;
    }

    public String getReportPassword() {
        return this.reportPassword;
    }

    public ReportRequisition reportPassword(String reportPassword) {
        this.setReportPassword(reportPassword);
        return this;
    }

    public void setReportPassword(String reportPassword) {
        this.reportPassword = reportPassword;
    }

    public ReportStatusTypes getReportStatus() {
        return this.reportStatus;
    }

    public ReportRequisition reportStatus(ReportStatusTypes reportStatus) {
        this.setReportStatus(reportStatus);
        return this;
    }

    public void setReportStatus(ReportStatusTypes reportStatus) {
        this.reportStatus = reportStatus;
    }

    public UUID getReportId() {
        return this.reportId;
    }

    public ReportRequisition reportId(UUID reportId) {
        this.setReportId(reportId);
        return this;
    }

    public void setReportId(UUID reportId) {
        this.reportId = reportId;
    }

    public byte[] getReportFileAttachment() {
        return this.reportFileAttachment;
    }

    public ReportRequisition reportFileAttachment(byte[] reportFileAttachment) {
        this.setReportFileAttachment(reportFileAttachment);
        return this;
    }

    public void setReportFileAttachment(byte[] reportFileAttachment) {
        this.reportFileAttachment = reportFileAttachment;
    }

    public String getReportFileAttachmentContentType() {
        return this.reportFileAttachmentContentType;
    }

    public ReportRequisition reportFileAttachmentContentType(String reportFileAttachmentContentType) {
        this.reportFileAttachmentContentType = reportFileAttachmentContentType;
        return this;
    }

    public void setReportFileAttachmentContentType(String reportFileAttachmentContentType) {
        this.reportFileAttachmentContentType = reportFileAttachmentContentType;
    }

    public String getReportFileCheckSum() {
        return this.reportFileCheckSum;
    }

    public ReportRequisition reportFileCheckSum(String reportFileCheckSum) {
        this.setReportFileCheckSum(reportFileCheckSum);
        return this;
    }

    public void setReportFileCheckSum(String reportFileCheckSum) {
        this.reportFileCheckSum = reportFileCheckSum;
    }

    public byte[] getReportNotes() {
        return this.reportNotes;
    }

    public ReportRequisition reportNotes(byte[] reportNotes) {
        this.setReportNotes(reportNotes);
        return this;
    }

    public void setReportNotes(byte[] reportNotes) {
        this.reportNotes = reportNotes;
    }

    public String getReportNotesContentType() {
        return this.reportNotesContentType;
    }

    public ReportRequisition reportNotesContentType(String reportNotesContentType) {
        this.reportNotesContentType = reportNotesContentType;
        return this;
    }

    public void setReportNotesContentType(String reportNotesContentType) {
        this.reportNotesContentType = reportNotesContentType;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public ReportRequisition placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public ReportRequisition addPlaceholders(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public ReportRequisition removePlaceholders(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<UniversallyUniqueMapping> getParameters() {
        return this.parameters;
    }

    public void setParameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.parameters = universallyUniqueMappings;
    }

    public ReportRequisition parameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setParameters(universallyUniqueMappings);
        return this;
    }

    public ReportRequisition addParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.parameters.add(universallyUniqueMapping);
        return this;
    }

    public ReportRequisition removeParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.parameters.remove(universallyUniqueMapping);
        return this;
    }

    public ReportTemplate getReportTemplate() {
        return this.reportTemplate;
    }

    public void setReportTemplate(ReportTemplate reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    public ReportRequisition reportTemplate(ReportTemplate reportTemplate) {
        this.setReportTemplate(reportTemplate);
        return this;
    }

    public ReportContentType getReportContentType() {
        return this.reportContentType;
    }

    public void setReportContentType(ReportContentType reportContentType) {
        this.reportContentType = reportContentType;
    }

    public ReportRequisition reportContentType(ReportContentType reportContentType) {
        this.setReportContentType(reportContentType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportRequisition)) {
            return false;
        }
        return id != null && id.equals(((ReportRequisition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportRequisition{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportRequestTime='" + getReportRequestTime() + "'" +
            ", reportPassword='" + getReportPassword() + "'" +
            ", reportStatus='" + getReportStatus() + "'" +
            ", reportId='" + getReportId() + "'" +
            ", reportFileAttachment='" + getReportFileAttachment() + "'" +
            ", reportFileAttachmentContentType='" + getReportFileAttachmentContentType() + "'" +
            ", reportFileCheckSum='" + getReportFileCheckSum() + "'" +
            ", reportNotes='" + getReportNotes() + "'" +
            ", reportNotesContentType='" + getReportNotesContentType() + "'" +
            "}";
    }
}
