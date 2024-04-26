package io.github.erp.service.dto;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
