package io.github.erp.domain;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A ExcelReportExport.
 */
@Entity
@Table(name = "excel_report_export")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "excelreportexport")
public class ExcelReportExport implements Serializable {

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
    @Column(name = "report_password", nullable = false)
    private String reportPassword;

    @Lob
    @Column(name = "report_notes")
    private byte[] reportNotes;

    @Column(name = "report_notes_content_type")
    private String reportNotesContentType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "file_check_sum")
    private String fileCheckSum;

    @Lob
    @Column(name = "report_file")
    private byte[] reportFile;

    @Column(name = "report_file_content_type")
    private String reportFileContentType;

    @NotNull
    @Column(name = "report_time_stamp", nullable = false)
    private ZonedDateTime reportTimeStamp;

    @NotNull
    @Column(name = "report_id", nullable = false, unique = true)
    private UUID reportId;

    @ManyToMany
    @JoinTable(
        name = "rel_excel_report_export__placeholder",
        joinColumns = @JoinColumn(name = "excel_report_export_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_excel_report_export__parameters",
        joinColumns = @JoinColumn(name = "excel_report_export_id"),
        inverseJoinColumns = @JoinColumn(name = "parameters_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> parameters = new HashSet<>();

    @JsonIgnoreProperties(value = { "placeholders", "processStatus" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ReportStatus reportStatus;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "grantedClearances", "placeholders", "systemParameters" }, allowSetters = true)
    private SecurityClearance securityClearance;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser reportCreator;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer organization;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer department;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "applicationMappings" }, allowSetters = true)
    private SystemModule systemModule;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "parameters",
            "securityClearance",
            "reportDesigner",
            "organization",
            "department",
            "placeholders",
            "systemModule",
            "fileCheckSumAlgorithm",
        },
        allowSetters = true
    )
    private ReportDesign reportDesign;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "parameters" }, allowSetters = true)
    private Algorithm fileCheckSumAlgorithm;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExcelReportExport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return this.reportName;
    }

    public ExcelReportExport reportName(String reportName) {
        this.setReportName(reportName);
        return this;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportPassword() {
        return this.reportPassword;
    }

    public ExcelReportExport reportPassword(String reportPassword) {
        this.setReportPassword(reportPassword);
        return this;
    }

    public void setReportPassword(String reportPassword) {
        this.reportPassword = reportPassword;
    }

    public byte[] getReportNotes() {
        return this.reportNotes;
    }

    public ExcelReportExport reportNotes(byte[] reportNotes) {
        this.setReportNotes(reportNotes);
        return this;
    }

    public void setReportNotes(byte[] reportNotes) {
        this.reportNotes = reportNotes;
    }

    public String getReportNotesContentType() {
        return this.reportNotesContentType;
    }

    public ExcelReportExport reportNotesContentType(String reportNotesContentType) {
        this.reportNotesContentType = reportNotesContentType;
        return this;
    }

    public void setReportNotesContentType(String reportNotesContentType) {
        this.reportNotesContentType = reportNotesContentType;
    }

    public String getFileCheckSum() {
        return this.fileCheckSum;
    }

    public ExcelReportExport fileCheckSum(String fileCheckSum) {
        this.setFileCheckSum(fileCheckSum);
        return this;
    }

    public void setFileCheckSum(String fileCheckSum) {
        this.fileCheckSum = fileCheckSum;
    }

    public byte[] getReportFile() {
        return this.reportFile;
    }

    public ExcelReportExport reportFile(byte[] reportFile) {
        this.setReportFile(reportFile);
        return this;
    }

    public void setReportFile(byte[] reportFile) {
        this.reportFile = reportFile;
    }

    public String getReportFileContentType() {
        return this.reportFileContentType;
    }

    public ExcelReportExport reportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
        return this;
    }

    public void setReportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
    }

    public ZonedDateTime getReportTimeStamp() {
        return this.reportTimeStamp;
    }

    public ExcelReportExport reportTimeStamp(ZonedDateTime reportTimeStamp) {
        this.setReportTimeStamp(reportTimeStamp);
        return this;
    }

    public void setReportTimeStamp(ZonedDateTime reportTimeStamp) {
        this.reportTimeStamp = reportTimeStamp;
    }

    public UUID getReportId() {
        return this.reportId;
    }

    public ExcelReportExport reportId(UUID reportId) {
        this.setReportId(reportId);
        return this;
    }

    public void setReportId(UUID reportId) {
        this.reportId = reportId;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public ExcelReportExport placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public ExcelReportExport addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public ExcelReportExport removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<UniversallyUniqueMapping> getParameters() {
        return this.parameters;
    }

    public void setParameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.parameters = universallyUniqueMappings;
    }

    public ExcelReportExport parameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setParameters(universallyUniqueMappings);
        return this;
    }

    public ExcelReportExport addParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.parameters.add(universallyUniqueMapping);
        return this;
    }

    public ExcelReportExport removeParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.parameters.remove(universallyUniqueMapping);
        return this;
    }

    public ReportStatus getReportStatus() {
        return this.reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public ExcelReportExport reportStatus(ReportStatus reportStatus) {
        this.setReportStatus(reportStatus);
        return this;
    }

    public SecurityClearance getSecurityClearance() {
        return this.securityClearance;
    }

    public void setSecurityClearance(SecurityClearance securityClearance) {
        this.securityClearance = securityClearance;
    }

    public ExcelReportExport securityClearance(SecurityClearance securityClearance) {
        this.setSecurityClearance(securityClearance);
        return this;
    }

    public ApplicationUser getReportCreator() {
        return this.reportCreator;
    }

    public void setReportCreator(ApplicationUser applicationUser) {
        this.reportCreator = applicationUser;
    }

    public ExcelReportExport reportCreator(ApplicationUser applicationUser) {
        this.setReportCreator(applicationUser);
        return this;
    }

    public Dealer getOrganization() {
        return this.organization;
    }

    public void setOrganization(Dealer dealer) {
        this.organization = dealer;
    }

    public ExcelReportExport organization(Dealer dealer) {
        this.setOrganization(dealer);
        return this;
    }

    public Dealer getDepartment() {
        return this.department;
    }

    public void setDepartment(Dealer dealer) {
        this.department = dealer;
    }

    public ExcelReportExport department(Dealer dealer) {
        this.setDepartment(dealer);
        return this;
    }

    public SystemModule getSystemModule() {
        return this.systemModule;
    }

    public void setSystemModule(SystemModule systemModule) {
        this.systemModule = systemModule;
    }

    public ExcelReportExport systemModule(SystemModule systemModule) {
        this.setSystemModule(systemModule);
        return this;
    }

    public ReportDesign getReportDesign() {
        return this.reportDesign;
    }

    public void setReportDesign(ReportDesign reportDesign) {
        this.reportDesign = reportDesign;
    }

    public ExcelReportExport reportDesign(ReportDesign reportDesign) {
        this.setReportDesign(reportDesign);
        return this;
    }

    public Algorithm getFileCheckSumAlgorithm() {
        return this.fileCheckSumAlgorithm;
    }

    public void setFileCheckSumAlgorithm(Algorithm algorithm) {
        this.fileCheckSumAlgorithm = algorithm;
    }

    public ExcelReportExport fileCheckSumAlgorithm(Algorithm algorithm) {
        this.setFileCheckSumAlgorithm(algorithm);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExcelReportExport)) {
            return false;
        }
        return id != null && id.equals(((ExcelReportExport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExcelReportExport{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportPassword='" + getReportPassword() + "'" +
            ", reportNotes='" + getReportNotes() + "'" +
            ", reportNotesContentType='" + getReportNotesContentType() + "'" +
            ", fileCheckSum='" + getFileCheckSum() + "'" +
            ", reportFile='" + getReportFile() + "'" +
            ", reportFileContentType='" + getReportFileContentType() + "'" +
            ", reportTimeStamp='" + getReportTimeStamp() + "'" +
            ", reportId='" + getReportId() + "'" +
            "}";
    }
}
