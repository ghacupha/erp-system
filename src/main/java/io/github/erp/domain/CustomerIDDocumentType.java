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
 * A CustomerIDDocumentType.
 */
@Entity
@Table(name = "customeriddocument_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "customeriddocumenttype")
public class CustomerIDDocumentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_code", nullable = false, unique = true)
    private String documentCode;

    @NotNull
    @Column(name = "document_type", nullable = false, unique = true)
    private String documentType;

    @Column(name = "document_type_description")
    private String documentTypeDescription;

    @ManyToMany
    @JoinTable(
        name = "rel_customeriddocument_type__placeholder",
        joinColumns = @JoinColumn(name = "customeriddocument_type_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomerIDDocumentType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentCode() {
        return this.documentCode;
    }

    public CustomerIDDocumentType documentCode(String documentCode) {
        this.setDocumentCode(documentCode);
        return this;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    public CustomerIDDocumentType documentType(String documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentTypeDescription() {
        return this.documentTypeDescription;
    }

    public CustomerIDDocumentType documentTypeDescription(String documentTypeDescription) {
        this.setDocumentTypeDescription(documentTypeDescription);
        return this;
    }

    public void setDocumentTypeDescription(String documentTypeDescription) {
        this.documentTypeDescription = documentTypeDescription;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public CustomerIDDocumentType placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public CustomerIDDocumentType addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public CustomerIDDocumentType removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerIDDocumentType)) {
            return false;
        }
        return id != null && id.equals(((CustomerIDDocumentType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerIDDocumentType{" +
            "id=" + getId() +
            ", documentCode='" + getDocumentCode() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", documentTypeDescription='" + getDocumentTypeDescription() + "'" +
            "}";
    }
}
