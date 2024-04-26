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
