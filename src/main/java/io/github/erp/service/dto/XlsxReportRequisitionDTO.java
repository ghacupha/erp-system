package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0-SNAPSHOT
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
import io.github.erp.domain.enumeration.ReportStatusTypes;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.XlsxReportRequisition} entity.
 */
public class XlsxReportRequisitionDTO implements Serializable {

    private Long id;

    @NotNull
    private String reportName;

    private LocalDate reportDate;

    @NotNull
    private String userPassword;

    private String reportFileChecksum;

    private ReportStatusTypes reportStatus;

    @NotNull
    private UUID reportId;

    private ReportTemplateDTO reportTemplate;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> parameters = new HashSet<>();

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

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getReportFileChecksum() {
        return reportFileChecksum;
    }

    public void setReportFileChecksum(String reportFileChecksum) {
        this.reportFileChecksum = reportFileChecksum;
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

    public ReportTemplateDTO getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(ReportTemplateDTO reportTemplate) {
        this.reportTemplate = reportTemplate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XlsxReportRequisitionDTO)) {
            return false;
        }

        XlsxReportRequisitionDTO xlsxReportRequisitionDTO = (XlsxReportRequisitionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, xlsxReportRequisitionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "XlsxReportRequisitionDTO{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportDate='" + getReportDate() + "'" +
            ", userPassword='" + getUserPassword() + "'" +
            ", reportFileChecksum='" + getReportFileChecksum() + "'" +
            ", reportStatus='" + getReportStatus() + "'" +
            ", reportId='" + getReportId() + "'" +
            ", reportTemplate=" + getReportTemplate() +
            ", placeholders=" + getPlaceholders() +
            ", parameters=" + getParameters() +
            "}";
    }
}
