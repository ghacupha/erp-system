package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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
 * A DTO for the {@link io.github.erp.domain.ExcelReportExport} entity.
 */
public class ExcelReportExportDTO implements Serializable {

    private Long id;

    @NotNull
    private String reportName;

    @NotNull
    private String reportPassword;

    @Lob
    private byte[] reportNotes;

    private String reportNotesContentType;

    @Lob
    private String fileCheckSum;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;

    @NotNull
    private ZonedDateTime reportTimeStamp;

    @NotNull
    private UUID reportId;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> parameters = new HashSet<>();

    private ReportStatusDTO reportStatus;

    private SecurityClearanceDTO securityClearance;

    private ApplicationUserDTO reportCreator;

    private DealerDTO organization;

    private DealerDTO department;

    private SystemModuleDTO systemModule;

    private ReportDesignDTO reportDesign;

    private AlgorithmDTO fileCheckSumAlgorithm;

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

    public String getReportPassword() {
        return reportPassword;
    }

    public void setReportPassword(String reportPassword) {
        this.reportPassword = reportPassword;
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

    public String getFileCheckSum() {
        return fileCheckSum;
    }

    public void setFileCheckSum(String fileCheckSum) {
        this.fileCheckSum = fileCheckSum;
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

    public ZonedDateTime getReportTimeStamp() {
        return reportTimeStamp;
    }

    public void setReportTimeStamp(ZonedDateTime reportTimeStamp) {
        this.reportTimeStamp = reportTimeStamp;
    }

    public UUID getReportId() {
        return reportId;
    }

    public void setReportId(UUID reportId) {
        this.reportId = reportId;
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

    public ReportStatusDTO getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatusDTO reportStatus) {
        this.reportStatus = reportStatus;
    }

    public SecurityClearanceDTO getSecurityClearance() {
        return securityClearance;
    }

    public void setSecurityClearance(SecurityClearanceDTO securityClearance) {
        this.securityClearance = securityClearance;
    }

    public ApplicationUserDTO getReportCreator() {
        return reportCreator;
    }

    public void setReportCreator(ApplicationUserDTO reportCreator) {
        this.reportCreator = reportCreator;
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

    public SystemModuleDTO getSystemModule() {
        return systemModule;
    }

    public void setSystemModule(SystemModuleDTO systemModule) {
        this.systemModule = systemModule;
    }

    public ReportDesignDTO getReportDesign() {
        return reportDesign;
    }

    public void setReportDesign(ReportDesignDTO reportDesign) {
        this.reportDesign = reportDesign;
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
        if (!(o instanceof ExcelReportExportDTO)) {
            return false;
        }

        ExcelReportExportDTO excelReportExportDTO = (ExcelReportExportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, excelReportExportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExcelReportExportDTO{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportPassword='" + getReportPassword() + "'" +
            ", reportNotes='" + getReportNotes() + "'" +
            ", fileCheckSum='" + getFileCheckSum() + "'" +
            ", reportFile='" + getReportFile() + "'" +
            ", reportTimeStamp='" + getReportTimeStamp() + "'" +
            ", reportId='" + getReportId() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", parameters=" + getParameters() +
            ", reportStatus=" + getReportStatus() +
            ", securityClearance=" + getSecurityClearance() +
            ", reportCreator=" + getReportCreator() +
            ", organization=" + getOrganization() +
            ", department=" + getDepartment() +
            ", systemModule=" + getSystemModule() +
            ", reportDesign=" + getReportDesign() +
            ", fileCheckSumAlgorithm=" + getFileCheckSumAlgorithm() +
            "}";
    }
}
