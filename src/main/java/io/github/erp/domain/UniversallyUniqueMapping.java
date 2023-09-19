package io.github.erp.domain;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UniversallyUniqueMapping.
 */
@Entity
@Table(name = "universally_unique_mapping")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "universallyuniquemapping")
public class UniversallyUniqueMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "universal_key", nullable = false, unique = true)
    private String universalKey;

    @Column(name = "mapped_value")
    private String mappedValue;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private UniversallyUniqueMapping parentMapping;

    @ManyToMany
    @JoinTable(
        name = "rel_universally_unique_mapping__placeholder",
        joinColumns = @JoinColumn(name = "universally_unique_mapping_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UniversallyUniqueMapping id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniversalKey() {
        return this.universalKey;
    }

    public UniversallyUniqueMapping universalKey(String universalKey) {
        this.setUniversalKey(universalKey);
        return this;
    }

    public void setUniversalKey(String universalKey) {
        this.universalKey = universalKey;
    }

    public String getMappedValue() {
        return this.mappedValue;
    }

    public UniversallyUniqueMapping mappedValue(String mappedValue) {
        this.setMappedValue(mappedValue);
        return this;
    }

    public void setMappedValue(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    public UniversallyUniqueMapping getParentMapping() {
        return this.parentMapping;
    }

    public void setParentMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.parentMapping = universallyUniqueMapping;
    }

    public UniversallyUniqueMapping parentMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.setParentMapping(universallyUniqueMapping);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public UniversallyUniqueMapping placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public UniversallyUniqueMapping addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public UniversallyUniqueMapping removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniversallyUniqueMapping)) {
            return false;
        }
        return id != null && id.equals(((UniversallyUniqueMapping) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniversallyUniqueMapping{" +
            "id=" + getId() +
            ", universalKey='" + getUniversalKey() + "'" +
            ", mappedValue='" + getMappedValue() + "'" +
            "}";
    }
}
