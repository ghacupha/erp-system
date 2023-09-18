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
import org.hibernate.annotations.Type;

/**
 * A DepartmentType.
 */
@Entity
@Table(name = "department_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "departmenttype")
public class DepartmentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "department_type_code", nullable = false, unique = true)
    private String departmentTypeCode;

    @NotNull
    @Column(name = "department_type", nullable = false, unique = true)
    private String departmentType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "department_type_details")
    private String departmentTypeDetails;

    @ManyToMany
    @JoinTable(
        name = "rel_department_type__placeholder",
        joinColumns = @JoinColumn(name = "department_type_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DepartmentType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentTypeCode() {
        return this.departmentTypeCode;
    }

    public DepartmentType departmentTypeCode(String departmentTypeCode) {
        this.setDepartmentTypeCode(departmentTypeCode);
        return this;
    }

    public void setDepartmentTypeCode(String departmentTypeCode) {
        this.departmentTypeCode = departmentTypeCode;
    }

    public String getDepartmentType() {
        return this.departmentType;
    }

    public DepartmentType departmentType(String departmentType) {
        this.setDepartmentType(departmentType);
        return this;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType;
    }

    public String getDepartmentTypeDetails() {
        return this.departmentTypeDetails;
    }

    public DepartmentType departmentTypeDetails(String departmentTypeDetails) {
        this.setDepartmentTypeDetails(departmentTypeDetails);
        return this;
    }

    public void setDepartmentTypeDetails(String departmentTypeDetails) {
        this.departmentTypeDetails = departmentTypeDetails;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public DepartmentType placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public DepartmentType addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public DepartmentType removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentType)) {
            return false;
        }
        return id != null && id.equals(((DepartmentType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentType{" +
            "id=" + getId() +
            ", departmentTypeCode='" + getDepartmentTypeCode() + "'" +
            ", departmentType='" + getDepartmentType() + "'" +
            ", departmentTypeDetails='" + getDepartmentTypeDetails() + "'" +
            "}";
    }
}
