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
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PrepaymentMapping.
 */
@Entity
@Table(name = "prepayment_mapping")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prepaymentmapping")
public class PrepaymentMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "parameter_key", nullable = false, unique = true)
    private String parameterKey;

    @NotNull
    @Column(name = "parameter_guid", nullable = false, unique = true)
    private UUID parameterGuid;

    @NotNull
    @Column(name = "parameter", nullable = false)
    private String parameter;

    @ManyToMany
    @JoinTable(
        name = "rel_prepayment_mapping__placeholder",
        joinColumns = @JoinColumn(name = "prepayment_mapping_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrepaymentMapping id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParameterKey() {
        return this.parameterKey;
    }

    public PrepaymentMapping parameterKey(String parameterKey) {
        this.setParameterKey(parameterKey);
        return this;
    }

    public void setParameterKey(String parameterKey) {
        this.parameterKey = parameterKey;
    }

    public UUID getParameterGuid() {
        return this.parameterGuid;
    }

    public PrepaymentMapping parameterGuid(UUID parameterGuid) {
        this.setParameterGuid(parameterGuid);
        return this;
    }

    public void setParameterGuid(UUID parameterGuid) {
        this.parameterGuid = parameterGuid;
    }

    public String getParameter() {
        return this.parameter;
    }

    public PrepaymentMapping parameter(String parameter) {
        this.setParameter(parameter);
        return this;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PrepaymentMapping placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PrepaymentMapping addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public PrepaymentMapping removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentMapping)) {
            return false;
        }
        return id != null && id.equals(((PrepaymentMapping) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentMapping{" +
            "id=" + getId() +
            ", parameterKey='" + getParameterKey() + "'" +
            ", parameterGuid='" + getParameterGuid() + "'" +
            ", parameter='" + getParameter() + "'" +
            "}";
    }
}
