package io.github.erp.service.dto;

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
            "}";
    }
}
