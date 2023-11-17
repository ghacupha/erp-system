package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
        name = "rel_xlsx_report_requisition__parameters",
        joinColumns = @JoinColumn(name = "xlsx_report_requisition_id"),
        inverseJoinColumns = @JoinColumn(name = "parameters_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> parameters = new HashSet<>();

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

    public Set<UniversallyUniqueMapping> getParameters() {
        return this.parameters;
    }

    public void setParameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.parameters = universallyUniqueMappings;
    }

    public XlsxReportRequisition parameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setParameters(universallyUniqueMappings);
        return this;
    }

    public XlsxReportRequisition addParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.parameters.add(universallyUniqueMapping);
        return this;
    }

    public XlsxReportRequisition removeParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.parameters.remove(universallyUniqueMapping);
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
