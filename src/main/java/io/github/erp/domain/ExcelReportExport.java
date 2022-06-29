package io.github.erp.domain;

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

    @Lob
    @Type(type = "org.hibernate.type.TextType")
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
    private Set<UniversallyUniqueMapping> parameters = new HashSet<>();

    @JsonIgnoreProperties(value = { "placeholders", "processStatus" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ReportStatus reportStatus;

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
