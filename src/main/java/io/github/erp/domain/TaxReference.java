package io.github.erp.domain;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.5-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.taxReferenceTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TaxReference.
 */
@Entity
@Table(name = "tax_reference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "taxreference")
public class TaxReference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tax_name", unique = true)
    private String taxName;

    @Column(name = "tax_description")
    private String taxDescription;

    @NotNull
    @Column(name = "tax_percentage", nullable = false)
    private Double taxPercentage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tax_reference_type", nullable = false, unique = true)
    private taxReferenceTypes taxReferenceType;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @Column(name = "compilation_token")
    private String compilationToken;

    @ManyToMany
    @JoinTable(
        name = "rel_tax_reference__placeholder",
        joinColumns = @JoinColumn(name = "tax_reference_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TaxReference id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxName() {
        return this.taxName;
    }

    public TaxReference taxName(String taxName) {
        this.setTaxName(taxName);
        return this;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxDescription() {
        return this.taxDescription;
    }

    public TaxReference taxDescription(String taxDescription) {
        this.setTaxDescription(taxDescription);
        return this;
    }

    public void setTaxDescription(String taxDescription) {
        this.taxDescription = taxDescription;
    }

    public Double getTaxPercentage() {
        return this.taxPercentage;
    }

    public TaxReference taxPercentage(Double taxPercentage) {
        this.setTaxPercentage(taxPercentage);
        return this;
    }

    public void setTaxPercentage(Double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public taxReferenceTypes getTaxReferenceType() {
        return this.taxReferenceType;
    }

    public TaxReference taxReferenceType(taxReferenceTypes taxReferenceType) {
        this.setTaxReferenceType(taxReferenceType);
        return this;
    }

    public void setTaxReferenceType(taxReferenceTypes taxReferenceType) {
        this.taxReferenceType = taxReferenceType;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public TaxReference fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public TaxReference compilationToken(String compilationToken) {
        this.setCompilationToken(compilationToken);
        return this;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public TaxReference placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public TaxReference addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public TaxReference removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxReference)) {
            return false;
        }
        return id != null && id.equals(((TaxReference) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxReference{" +
            "id=" + getId() +
            ", taxName='" + getTaxName() + "'" +
            ", taxDescription='" + getTaxDescription() + "'" +
            ", taxPercentage=" + getTaxPercentage() +
            ", taxReferenceType='" + getTaxReferenceType() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            "}";
    }
}
