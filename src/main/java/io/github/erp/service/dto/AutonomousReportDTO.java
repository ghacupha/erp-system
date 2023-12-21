package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AutonomousReport} entity.
 */
public class AutonomousReportDTO implements Serializable {

    private Long id;

    @NotNull
    private String reportName;

    private String reportParameters;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private UUID reportFilename;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;

    @NotNull
    private String fileChecksum;

    private Boolean reportTampered;

    private Set<UniversallyUniqueMappingDTO> reportMappings = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private ApplicationUserDTO createdBy;

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

    public String getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(String reportParameters) {
        this.reportParameters = reportParameters;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getReportFilename() {
        return reportFilename;
    }

    public void setReportFilename(UUID reportFilename) {
        this.reportFilename = reportFilename;
    }

    public byte[] getReportFile() {
        return reportFile;
    }

    public void setReportFile(byte[] reportFile) {
        this.reportFile = reportFile;
    }

    public String getReportFileContentType() {
        return reportFileContentType;
    }

    public void setReportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
    }

    public String getFileChecksum() {
        return fileChecksum;
    }

    public void setFileChecksum(String fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    public Boolean getReportTampered() {
        return reportTampered;
    }

    public void setReportTampered(Boolean reportTampered) {
        this.reportTampered = reportTampered;
    }

    public Set<UniversallyUniqueMappingDTO> getReportMappings() {
        return reportMappings;
    }

    public void setReportMappings(Set<UniversallyUniqueMappingDTO> reportMappings) {
        this.reportMappings = reportMappings;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public ApplicationUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ApplicationUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutonomousReportDTO)) {
            return false;
        }

        AutonomousReportDTO autonomousReportDTO = (AutonomousReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, autonomousReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AutonomousReportDTO{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportParameters='" + getReportParameters() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", reportFilename='" + getReportFilename() + "'" +
            ", reportFile='" + getReportFile() + "'" +
            ", fileChecksum='" + getFileChecksum() + "'" +
            ", reportTampered='" + getReportTampered() + "'" +
            ", reportMappings=" + getReportMappings() +
            ", placeholders=" + getPlaceholders() +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
