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

/**
 * A BusinessDocument.
 */
@Entity
@Table(name = "business_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "businessdocument")
public class BusinessDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_title", nullable = false, unique = true)
    private String documentTitle;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "document_serial", nullable = false, unique = true)
    private UUID documentSerial;

    @Column(name = "last_modified")
    private ZonedDateTime lastModified;

    @NotNull
    @Column(name = "attachment_file_path", nullable = false)
    private String attachmentFilePath;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser createdBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser lastModifiedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer originatingDepartment;

    @ManyToMany
    @JoinTable(
        name = "rel_business_document__application_mappings",
        joinColumns = @JoinColumn(name = "business_document_id"),
        inverseJoinColumns = @JoinColumn(name = "application_mappings_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> applicationMappings = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_business_document__placeholder",
        joinColumns = @JoinColumn(name = "business_document_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusinessDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentTitle() {
        return this.documentTitle;
    }

    public BusinessDocument documentTitle(String documentTitle) {
        this.setDocumentTitle(documentTitle);
        return this;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDescription() {
        return this.description;
    }

    public BusinessDocument description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getDocumentSerial() {
        return this.documentSerial;
    }

    public BusinessDocument documentSerial(UUID documentSerial) {
        this.setDocumentSerial(documentSerial);
        return this;
    }

    public void setDocumentSerial(UUID documentSerial) {
        this.documentSerial = documentSerial;
    }

    public ZonedDateTime getLastModified() {
        return this.lastModified;
    }

    public BusinessDocument lastModified(ZonedDateTime lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getAttachmentFilePath() {
        return this.attachmentFilePath;
    }

    public BusinessDocument attachmentFilePath(String attachmentFilePath) {
        this.setAttachmentFilePath(attachmentFilePath);
        return this;
    }

    public void setAttachmentFilePath(String attachmentFilePath) {
        this.attachmentFilePath = attachmentFilePath;
    }

    public ApplicationUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(ApplicationUser applicationUser) {
        this.createdBy = applicationUser;
    }

    public BusinessDocument createdBy(ApplicationUser applicationUser) {
        this.setCreatedBy(applicationUser);
        return this;
    }

    public ApplicationUser getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(ApplicationUser applicationUser) {
        this.lastModifiedBy = applicationUser;
    }

    public BusinessDocument lastModifiedBy(ApplicationUser applicationUser) {
        this.setLastModifiedBy(applicationUser);
        return this;
    }

    public Dealer getOriginatingDepartment() {
        return this.originatingDepartment;
    }

    public void setOriginatingDepartment(Dealer dealer) {
        this.originatingDepartment = dealer;
    }

    public BusinessDocument originatingDepartment(Dealer dealer) {
        this.setOriginatingDepartment(dealer);
        return this;
    }

    public Set<UniversallyUniqueMapping> getApplicationMappings() {
        return this.applicationMappings;
    }

    public void setApplicationMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.applicationMappings = universallyUniqueMappings;
    }

    public BusinessDocument applicationMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setApplicationMappings(universallyUniqueMappings);
        return this;
    }

    public BusinessDocument addApplicationMappings(UniversallyUniqueMapping universallyUniqueMapping) {
        this.applicationMappings.add(universallyUniqueMapping);
        return this;
    }

    public BusinessDocument removeApplicationMappings(UniversallyUniqueMapping universallyUniqueMapping) {
        this.applicationMappings.remove(universallyUniqueMapping);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public BusinessDocument placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public BusinessDocument addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public BusinessDocument removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessDocument)) {
            return false;
        }
        return id != null && id.equals(((BusinessDocument) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessDocument{" +
            "id=" + getId() +
            ", documentTitle='" + getDocumentTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", documentSerial='" + getDocumentSerial() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", attachmentFilePath='" + getAttachmentFilePath() + "'" +
            "}";
    }
}
