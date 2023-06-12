package io.github.erp.domain;

/*-
 * Erp System - Mark III No 16 (Caleb Series) Server ver 1.2.7
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
