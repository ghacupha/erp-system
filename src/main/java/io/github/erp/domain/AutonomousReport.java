package io.github.erp.domain;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

/**
 * A AutonomousReport.
 */
@Entity
@Table(name = "autonomous_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "autonomousreport")
public class AutonomousReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "report_name", nullable = false)
    private String reportName;

    @Column(name = "report_parameters")
    private String reportParameters;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "report_filename", nullable = false, unique = true)
    private UUID reportFilename;

    @Lob
    @Column(name = "report_file")
    private byte[] reportFile;

    @Column(name = "report_file_content_type")
    private String reportFileContentType;

    @NotNull
    @Column(name = "file_checksum", nullable = false, unique = true)
    private String fileChecksum;

    @Column(name = "report_tampered")
    private Boolean reportTampered;

    @ManyToMany
    @JoinTable(
        name = "rel_autonomous_report__report_mapping",
        joinColumns = @JoinColumn(name = "autonomous_report_id"),
        inverseJoinColumns = @JoinColumn(name = "report_mapping_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> reportMappings = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_autonomous_report__placeholder",
        joinColumns = @JoinColumn(name = "autonomous_report_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AutonomousReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return this.reportName;
    }

    public AutonomousReport reportName(String reportName) {
        this.setReportName(reportName);
        return this;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportParameters() {
        return this.reportParameters;
    }

    public AutonomousReport reportParameters(String reportParameters) {
        this.setReportParameters(reportParameters);
        return this;
    }

    public void setReportParameters(String reportParameters) {
        this.reportParameters = reportParameters;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public AutonomousReport createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getReportFilename() {
        return this.reportFilename;
    }

    public AutonomousReport reportFilename(UUID reportFilename) {
        this.setReportFilename(reportFilename);
        return this;
    }

    public void setReportFilename(UUID reportFilename) {
        this.reportFilename = reportFilename;
    }

    public byte[] getReportFile() {
        return this.reportFile;
    }

    public AutonomousReport reportFile(byte[] reportFile) {
        this.setReportFile(reportFile);
        return this;
    }

    public void setReportFile(byte[] reportFile) {
        this.reportFile = reportFile;
    }

    public String getReportFileContentType() {
        return this.reportFileContentType;
    }

    public AutonomousReport reportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
        return this;
    }

    public void setReportFileContentType(String reportFileContentType) {
        this.reportFileContentType = reportFileContentType;
    }

    public String getFileChecksum() {
        return this.fileChecksum;
    }

    public AutonomousReport fileChecksum(String fileChecksum) {
        this.setFileChecksum(fileChecksum);
        return this;
    }

    public void setFileChecksum(String fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    public Boolean getReportTampered() {
        return this.reportTampered;
    }

    public AutonomousReport reportTampered(Boolean reportTampered) {
        this.setReportTampered(reportTampered);
        return this;
    }

    public void setReportTampered(Boolean reportTampered) {
        this.reportTampered = reportTampered;
    }

    public Set<UniversallyUniqueMapping> getReportMappings() {
        return this.reportMappings;
    }

    public void setReportMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.reportMappings = universallyUniqueMappings;
    }

    public AutonomousReport reportMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setReportMappings(universallyUniqueMappings);
        return this;
    }

    public AutonomousReport addReportMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.reportMappings.add(universallyUniqueMapping);
        return this;
    }

    public AutonomousReport removeReportMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.reportMappings.remove(universallyUniqueMapping);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public AutonomousReport placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public AutonomousReport addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public AutonomousReport removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public ApplicationUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(ApplicationUser applicationUser) {
        this.createdBy = applicationUser;
    }

    public AutonomousReport createdBy(ApplicationUser applicationUser) {
        this.setCreatedBy(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutonomousReport)) {
            return false;
        }
        return id != null && id.equals(((AutonomousReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AutonomousReport{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportParameters='" + getReportParameters() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", reportFilename='" + getReportFilename() + "'" +
            ", reportFile='" + getReportFile() + "'" +
            ", reportFileContentType='" + getReportFileContentType() + "'" +
            ", fileChecksum='" + getFileChecksum() + "'" +
            ", reportTampered='" + getReportTampered() + "'" +
            "}";
    }
}
