package io.github.erp.domain;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
 * A SecurityClearance.
 */
@Entity
@Table(name = "security_clearance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "securityclearance")
public class SecurityClearance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "clearance_level", nullable = false, unique = true)
    private String clearanceLevel;

    @ManyToMany
    @JoinTable(
        name = "rel_security_clearance__granted_clearances",
        joinColumns = @JoinColumn(name = "security_clearance_id"),
        inverseJoinColumns = @JoinColumn(name = "granted_clearances_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "grantedClearances", "placeholders", "systemParameters" }, allowSetters = true)
    private Set<SecurityClearance> grantedClearances = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_security_clearance__placeholder",
        joinColumns = @JoinColumn(name = "security_clearance_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_security_clearance__system_parameters",
        joinColumns = @JoinColumn(name = "security_clearance_id"),
        inverseJoinColumns = @JoinColumn(name = "system_parameters_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> systemParameters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SecurityClearance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClearanceLevel() {
        return this.clearanceLevel;
    }

    public SecurityClearance clearanceLevel(String clearanceLevel) {
        this.setClearanceLevel(clearanceLevel);
        return this;
    }

    public void setClearanceLevel(String clearanceLevel) {
        this.clearanceLevel = clearanceLevel;
    }

    public Set<SecurityClearance> getGrantedClearances() {
        return this.grantedClearances;
    }

    public void setGrantedClearances(Set<SecurityClearance> securityClearances) {
        this.grantedClearances = securityClearances;
    }

    public SecurityClearance grantedClearances(Set<SecurityClearance> securityClearances) {
        this.setGrantedClearances(securityClearances);
        return this;
    }

    public SecurityClearance addGrantedClearances(SecurityClearance securityClearance) {
        this.grantedClearances.add(securityClearance);
        return this;
    }

    public SecurityClearance removeGrantedClearances(SecurityClearance securityClearance) {
        this.grantedClearances.remove(securityClearance);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public SecurityClearance placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public SecurityClearance addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public SecurityClearance removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<UniversallyUniqueMapping> getSystemParameters() {
        return this.systemParameters;
    }

    public void setSystemParameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.systemParameters = universallyUniqueMappings;
    }

    public SecurityClearance systemParameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setSystemParameters(universallyUniqueMappings);
        return this;
    }

    public SecurityClearance addSystemParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.systemParameters.add(universallyUniqueMapping);
        return this;
    }

    public SecurityClearance removeSystemParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.systemParameters.remove(universallyUniqueMapping);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityClearance)) {
            return false;
        }
        return id != null && id.equals(((SecurityClearance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityClearance{" +
            "id=" + getId() +
            ", clearanceLevel='" + getClearanceLevel() + "'" +
            "}";
    }
}
