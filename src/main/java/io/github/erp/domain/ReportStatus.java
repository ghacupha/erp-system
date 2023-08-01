package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportStatus.
 */
@Entity
@Table(name = "report_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reportstatus")
public class ReportStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "report_name")
    private String reportName;

    @Column(name = "report_id")
    private UUID reportId;

    @ManyToMany
    @JoinTable(
        name = "rel_report_status__placeholder",
        joinColumns = @JoinColumn(name = "report_status_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders", "parameters" }, allowSetters = true)
    private ProcessStatus processStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return this.reportName;
    }

    public ReportStatus reportName(String reportName) {
        this.setReportName(reportName);
        return this;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public UUID getReportId() {
        return this.reportId;
    }

    public ReportStatus reportId(UUID reportId) {
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

    public ReportStatus placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public ReportStatus addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public ReportStatus removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public ProcessStatus getProcessStatus() {
        return this.processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public ReportStatus processStatus(ProcessStatus processStatus) {
        this.setProcessStatus(processStatus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportStatus)) {
            return false;
        }
        return id != null && id.equals(((ReportStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportStatus{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportId='" + getReportId() + "'" +
            "}";
    }
}
