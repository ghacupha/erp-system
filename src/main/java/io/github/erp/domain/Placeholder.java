package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Placeholder.
 */
@Entity
@Table(name = "placeholder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "placeholder")
public class Placeholder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @Column(name = "compilation_token")
    private String compilationToken;

    @ManyToOne
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Placeholder containingPlaceholder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Placeholder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Placeholder description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToken() {
        return this.token;
    }

    public Placeholder token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public Placeholder fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public Placeholder compilationToken(String compilationToken) {
        this.setCompilationToken(compilationToken);
        return this;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public Placeholder getContainingPlaceholder() {
        return this.containingPlaceholder;
    }

    public void setContainingPlaceholder(Placeholder placeholder) {
        this.containingPlaceholder = placeholder;
    }

    public Placeholder containingPlaceholder(Placeholder placeholder) {
        this.setContainingPlaceholder(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Placeholder)) {
            return false;
        }
        return id != null && id.equals(((Placeholder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Placeholder{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", token='" + getToken() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            "}";
    }
}
