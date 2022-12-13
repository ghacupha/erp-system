package io.github.erp.domain;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.7-SNAPSHOT
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
import io.github.erp.domain.enumeration.DepreciationTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A DepreciationMethod.
 */
@Entity
@Table(name = "depreciation_method")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "depreciationmethod")
public class DepreciationMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "depreciation_method_name", nullable = false, unique = true)
    private String depreciationMethodName;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "depreciation_type", nullable = false, unique = true)
    private DepreciationTypes depreciationType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "remarks")
    private String remarks;

    @ManyToMany
    @JoinTable(
        name = "rel_depreciation_method__placeholder",
        joinColumns = @JoinColumn(name = "depreciation_method_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DepreciationMethod id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepreciationMethodName() {
        return this.depreciationMethodName;
    }

    public DepreciationMethod depreciationMethodName(String depreciationMethodName) {
        this.setDepreciationMethodName(depreciationMethodName);
        return this;
    }

    public void setDepreciationMethodName(String depreciationMethodName) {
        this.depreciationMethodName = depreciationMethodName;
    }

    public String getDescription() {
        return this.description;
    }

    public DepreciationMethod description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DepreciationTypes getDepreciationType() {
        return this.depreciationType;
    }

    public DepreciationMethod depreciationType(DepreciationTypes depreciationType) {
        this.setDepreciationType(depreciationType);
        return this;
    }

    public void setDepreciationType(DepreciationTypes depreciationType) {
        this.depreciationType = depreciationType;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public DepreciationMethod remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public DepreciationMethod placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public DepreciationMethod addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public DepreciationMethod removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationMethod)) {
            return false;
        }
        return id != null && id.equals(((DepreciationMethod) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationMethod{" +
            "id=" + getId() +
            ", depreciationMethodName='" + getDepreciationMethodName() + "'" +
            ", description='" + getDescription() + "'" +
            ", depreciationType='" + getDepreciationType() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
