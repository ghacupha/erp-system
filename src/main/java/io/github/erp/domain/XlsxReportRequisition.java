package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.ReportStatusTypes;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A XlsxReportRequisition.
 */
@Entity
@Table(name = "xlsx_report_requisition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "xlsxreportrequisition")
public class XlsxReportRequisition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "report_name", nullable = false, unique = true)
    private String reportName;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @NotNull
    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "report_file_checksum")
    private String reportFileChecksum;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_status")
    private ReportStatusTypes reportStatus;

    @NotNull
    @Column(name = "report_id", nullable = false, unique = true)
    private UUID reportId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private ReportTemplate reportTemplate;

    @ManyToMany
    @JoinTable(
        name = "rel_xlsx_report_requisition__placeholder",
        joinColumns = @JoinColumn(name = "xlsx_report_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_xlsx_report_requisition__value_maps",
        joinColumns = @JoinColumn(name = "xlsx_report_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "value_maps_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<UniversallyUniqueMapping> valueMaps = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public XlsxReportRequisition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return this.reportName;
    }

    public XlsxReportRequisition reportName(String reportName) {
        this.setReportName(reportName);
        return this;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public LocalDate getReportDate() {
        return this.reportDate;
    }

    public XlsxReportRequisition reportDate(LocalDate reportDate) {
        this.setReportDate(reportDate);
        return this;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public XlsxReportRequisition userPassword(String userPassword) {
        this.setUserPassword(userPassword);
        return this;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getReportFileChecksum() {
        return this.reportFileChecksum;
    }

    public XlsxReportRequisition reportFileChecksum(String reportFileChecksum) {
        this.setReportFileChecksum(reportFileChecksum);
        return this;
    }

    public void setReportFileChecksum(String reportFileChecksum) {
        this.reportFileChecksum = reportFileChecksum;
    }

    public ReportStatusTypes getReportStatus() {
        return this.reportStatus;
    }

    public XlsxReportRequisition reportStatus(ReportStatusTypes reportStatus) {
        this.setReportStatus(reportStatus);
        return this;
    }

    public void setReportStatus(ReportStatusTypes reportStatus) {
        this.reportStatus = reportStatus;
    }

    public UUID getReportId() {
        return this.reportId;
    }

    public XlsxReportRequisition reportId(UUID reportId) {
        this.setReportId(reportId);
        return this;
    }

    public void setReportId(UUID reportId) {
        this.reportId = reportId;
    }

    public ReportTemplate getReportTemplate() {
        return this.reportTemplate;
    }

    public void setReportTemplate(ReportTemplate reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    public XlsxReportRequisition reportTemplate(ReportTemplate reportTemplate) {
        this.setReportTemplate(reportTemplate);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public XlsxReportRequisition placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public XlsxReportRequisition addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public XlsxReportRequisition removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<UniversallyUniqueMapping> getValueMaps() {
        return this.valueMaps;
    }

    public void setValueMaps(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.valueMaps = universallyUniqueMappings;
    }

    public XlsxReportRequisition valueMaps(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setValueMaps(universallyUniqueMappings);
        return this;
    }

    public XlsxReportRequisition addValueMaps(UniversallyUniqueMapping universallyUniqueMapping) {
        this.valueMaps.add(universallyUniqueMapping);
        return this;
    }

    public XlsxReportRequisition removeValueMaps(UniversallyUniqueMapping universallyUniqueMapping) {
        this.valueMaps.remove(universallyUniqueMapping);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XlsxReportRequisition)) {
            return false;
        }
        return id != null && id.equals(((XlsxReportRequisition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "XlsxReportRequisition{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportDate='" + getReportDate() + "'" +
            ", userPassword='" + getUserPassword() + "'" +
            ", reportFileChecksum='" + getReportFileChecksum() + "'" +
            ", reportStatus='" + getReportStatus() + "'" +
            ", reportId='" + getReportId() + "'" +
            "}";
    }
}
