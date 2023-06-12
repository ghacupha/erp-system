package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 16 (Caleb Series) Server ver 1.2.7
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
import io.github.erp.domain.enumeration.ReportStatusTypes;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ReportRequisition} entity.
 */
public class ReportRequisitionDTO implements Serializable {

    private Long id;

    @NotNull
    private String reportName;

    @NotNull
    private ZonedDateTime reportRequestTime;

    @NotNull
    @Size(min = 6)
    private String reportPassword;

    private ReportStatusTypes reportStatus;

    @NotNull
    private UUID reportId;

    @Lob
    private byte[] reportFileAttachment;

    private String reportFileAttachmentContentType;

    @Lob
    private String reportFileCheckSum;

    @Lob
    private byte[] reportNotes;

    private String reportNotesContentType;
    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> parameters = new HashSet<>();

    private ReportTemplateDTO reportTemplate;

    private ReportContentTypeDTO reportContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public ZonedDateTime getReportRequestTime() {
        return reportRequestTime;
    }

    public void setReportRequestTime(ZonedDateTime reportRequestTime) {
        this.reportRequestTime = reportRequestTime;
    }

    public String getReportPassword() {
        return reportPassword;
    }

    public void setReportPassword(String reportPassword) {
        this.reportPassword = reportPassword;
    }

    public ReportStatusTypes getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatusTypes reportStatus) {
        this.reportStatus = reportStatus;
    }

    public UUID getReportId() {
        return reportId;
    }

    public void setReportId(UUID reportId) {
        this.reportId = reportId;
    }

    public byte[] getReportFileAttachment() {
        return reportFileAttachment;
    }

    public void setReportFileAttachment(byte[] reportFileAttachment) {
        this.reportFileAttachment = reportFileAttachment;
    }

    public String getReportFileAttachmentContentType() {
        return reportFileAttachmentContentType;
    }

    public void setReportFileAttachmentContentType(String reportFileAttachmentContentType) {
        this.reportFileAttachmentContentType = reportFileAttachmentContentType;
    }

    public String getReportFileCheckSum() {
        return reportFileCheckSum;
    }

    public void setReportFileCheckSum(String reportFileCheckSum) {
        this.reportFileCheckSum = reportFileCheckSum;
    }

    public byte[] getReportNotes() {
        return reportNotes;
    }

    public void setReportNotes(byte[] reportNotes) {
        this.reportNotes = reportNotes;
    }

    public String getReportNotesContentType() {
        return reportNotesContentType;
    }

    public void setReportNotesContentType(String reportNotesContentType) {
        this.reportNotesContentType = reportNotesContentType;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<UniversallyUniqueMappingDTO> getParameters() {
        return parameters;
    }

    public void setParameters(Set<UniversallyUniqueMappingDTO> parameters) {
        this.parameters = parameters;
    }

    public ReportTemplateDTO getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(ReportTemplateDTO reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    public ReportContentTypeDTO getReportContentType() {
        return reportContentType;
    }

    public void setReportContentType(ReportContentTypeDTO reportContentType) {
        this.reportContentType = reportContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportRequisitionDTO)) {
            return false;
        }

        ReportRequisitionDTO reportRequisitionDTO = (ReportRequisitionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportRequisitionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportRequisitionDTO{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportRequestTime='" + getReportRequestTime() + "'" +
            ", reportPassword='" + getReportPassword() + "'" +
            ", reportStatus='" + getReportStatus() + "'" +
            ", reportId='" + getReportId() + "'" +
            ", reportFileAttachment='" + getReportFileAttachment() + "'" +
            ", reportFileCheckSum='" + getReportFileCheckSum() + "'" +
            ", reportNotes='" + getReportNotes() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", parameters=" + getParameters() +
            ", reportTemplate=" + getReportTemplate() +
            ", reportContentType=" + getReportContentType() +
            "}";
    }
}
