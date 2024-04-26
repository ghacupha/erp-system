package io.github.erp.domain;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
