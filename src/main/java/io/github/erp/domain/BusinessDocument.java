package io.github.erp.domain;

/*-
 * Erp System - Mark III No 13 (Caleb Series) Server ver 1.1.3-SNAPSHOT
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

    @NotNull
    @Column(name = "document_file_content_type", nullable = false)
    private String documentFileContentType;

    @Column(name = "file_tampered")
    private Boolean fileTampered;

    @NotNull
    @Column(name = "document_file_checksum", nullable = false, unique = true)
    private String documentFileChecksum;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "parameters" }, allowSetters = true)
    private Algorithm fileChecksumAlgorithm;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "grantedClearances", "placeholders", "systemParameters" }, allowSetters = true)
    private SecurityClearance securityClearance;

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

    public String getDocumentFileContentType() {
        return this.documentFileContentType;
    }

    public BusinessDocument documentFileContentType(String documentFileContentType) {
        this.setDocumentFileContentType(documentFileContentType);
        return this;
    }

    public void setDocumentFileContentType(String documentFileContentType) {
        this.documentFileContentType = documentFileContentType;
    }

    public Boolean getFileTampered() {
        return this.fileTampered;
    }

    public BusinessDocument fileTampered(Boolean fileTampered) {
        this.setFileTampered(fileTampered);
        return this;
    }

    public void setFileTampered(Boolean fileTampered) {
        this.fileTampered = fileTampered;
    }

    public String getDocumentFileChecksum() {
        return this.documentFileChecksum;
    }

    public BusinessDocument documentFileChecksum(String documentFileChecksum) {
        this.setDocumentFileChecksum(documentFileChecksum);
        return this;
    }

    public void setDocumentFileChecksum(String documentFileChecksum) {
        this.documentFileChecksum = documentFileChecksum;
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

    public Algorithm getFileChecksumAlgorithm() {
        return this.fileChecksumAlgorithm;
    }

    public void setFileChecksumAlgorithm(Algorithm algorithm) {
        this.fileChecksumAlgorithm = algorithm;
    }

    public BusinessDocument fileChecksumAlgorithm(Algorithm algorithm) {
        this.setFileChecksumAlgorithm(algorithm);
        return this;
    }

    public SecurityClearance getSecurityClearance() {
        return this.securityClearance;
    }

    public void setSecurityClearance(SecurityClearance securityClearance) {
        this.securityClearance = securityClearance;
    }

    public BusinessDocument securityClearance(SecurityClearance securityClearance) {
        this.setSecurityClearance(securityClearance);
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
            ", documentFileContentType='" + getDocumentFileContentType() + "'" +
            ", fileTampered='" + getFileTampered() + "'" +
            ", documentFileChecksum='" + getDocumentFileChecksum() + "'" +
            "}";
    }
}
