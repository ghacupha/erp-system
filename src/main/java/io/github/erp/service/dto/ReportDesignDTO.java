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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ReportDesign} entity.
 */
public class ReportDesignDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID catalogueNumber;

    @NotNull
    private String designation;

    @Lob
    private String description;

    @Lob
    private byte[] notes;

    private String notesContentType;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;

    private String reportFileChecksum;

    private Set<UniversallyUniqueMappingDTO> parameters = new HashSet<>();

    private SecurityClearanceDTO securityClearance;

    private ApplicationUserDTO reportDesigner;

    private DealerDTO organization;

    private DealerDTO department;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private SystemModuleDTO systemModule;

    private AlgorithmDTO fileCheckSumAlgorithm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getCatalogueNumber() {
        return catalogueNumber;
    }

    public void setCatalogueNumber(UUID catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getNotes() {
        return notes;
    }

    public void setNotes(byte[] notes) {
        this.notes = notes;
    }

    public String getNotesContentType() {
        return notesContentType;
    }

    public void setNotesContentType(String notesContentType) {
        this.notesContentType = notesContentType;
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

    public String getReportFileChecksum() {
        return reportFileChecksum;
    }

    public void setReportFileChecksum(String reportFileChecksum) {
        this.reportFileChecksum = reportFileChecksum;
    }

    public Set<UniversallyUniqueMappingDTO> getParameters() {
        return parameters;
    }

    public void setParameters(Set<UniversallyUniqueMappingDTO> parameters) {
        this.parameters = parameters;
    }

    public SecurityClearanceDTO getSecurityClearance() {
        return securityClearance;
    }

    public void setSecurityClearance(SecurityClearanceDTO securityClearance) {
        this.securityClearance = securityClearance;
    }

    public ApplicationUserDTO getReportDesigner() {
        return reportDesigner;
    }

    public void setReportDesigner(ApplicationUserDTO reportDesigner) {
        this.reportDesigner = reportDesigner;
    }

    public DealerDTO getOrganization() {
        return organization;
    }

    public void setOrganization(DealerDTO organization) {
        this.organization = organization;
    }

    public DealerDTO getDepartment() {
        return department;
    }

    public void setDepartment(DealerDTO department) {
        this.department = department;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public SystemModuleDTO getSystemModule() {
        return systemModule;
    }

    public void setSystemModule(SystemModuleDTO systemModule) {
        this.systemModule = systemModule;
    }

    public AlgorithmDTO getFileCheckSumAlgorithm() {
        return fileCheckSumAlgorithm;
    }

    public void setFileCheckSumAlgorithm(AlgorithmDTO fileCheckSumAlgorithm) {
        this.fileCheckSumAlgorithm = fileCheckSumAlgorithm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDesignDTO)) {
            return false;
        }

        ReportDesignDTO reportDesignDTO = (ReportDesignDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportDesignDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDesignDTO{" +
            "id=" + getId() +
            ", catalogueNumber='" + getCatalogueNumber() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", reportFile='" + getReportFile() + "'" +
            ", reportFileChecksum='" + getReportFileChecksum() + "'" +
            ", parameters=" + getParameters() +
            ", securityClearance=" + getSecurityClearance() +
            ", reportDesigner=" + getReportDesigner() +
            ", organization=" + getOrganization() +
            ", department=" + getDepartment() +
            ", placeholders=" + getPlaceholders() +
            ", systemModule=" + getSystemModule() +
            ", fileCheckSumAlgorithm=" + getFileCheckSumAlgorithm() +
            "}";
    }
}
