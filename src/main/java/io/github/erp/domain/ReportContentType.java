package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportContentType.
 */
@Entity
@Table(name = "report_content_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reportcontenttype")
public class ReportContentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "report_type_name", nullable = false, unique = true)
    private String reportTypeName;

    @NotNull
    @Column(name = "report_file_extension", nullable = false)
    private String reportFileExtension;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "sysMaps" }, allowSetters = true)
    private SystemContentType systemContentType;

    @ManyToMany
    @JoinTable(
        name = "rel_report_content_type__placeholder",
        joinColumns = @JoinColumn(name = "report_content_type_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportContentType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportTypeName() {
        return this.reportTypeName;
    }

    public ReportContentType reportTypeName(String reportTypeName) {
        this.setReportTypeName(reportTypeName);
        return this;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    public String getReportFileExtension() {
        return this.reportFileExtension;
    }

    public ReportContentType reportFileExtension(String reportFileExtension) {
        this.setReportFileExtension(reportFileExtension);
        return this;
    }

    public void setReportFileExtension(String reportFileExtension) {
        this.reportFileExtension = reportFileExtension;
    }

    public SystemContentType getSystemContentType() {
        return this.systemContentType;
    }

    public void setSystemContentType(SystemContentType systemContentType) {
        this.systemContentType = systemContentType;
    }

    public ReportContentType systemContentType(SystemContentType systemContentType) {
        this.setSystemContentType(systemContentType);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public ReportContentType placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public ReportContentType addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public ReportContentType removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportContentType)) {
            return false;
        }
        return id != null && id.equals(((ReportContentType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportContentType{" +
            "id=" + getId() +
            ", reportTypeName='" + getReportTypeName() + "'" +
            ", reportFileExtension='" + getReportFileExtension() + "'" +
            "}";
    }
}
