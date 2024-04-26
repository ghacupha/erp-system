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
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A ExecutiveCategoryType.
 */
@Entity
@Table(name = "executive_category_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "executivecategorytype")
public class ExecutiveCategoryType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "director_category_type_code", nullable = false, unique = true)
    private String directorCategoryTypeCode;

    @NotNull
    @Column(name = "director_category_type", nullable = false, unique = true)
    private String directorCategoryType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "director_category_type_details")
    private String directorCategoryTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExecutiveCategoryType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirectorCategoryTypeCode() {
        return this.directorCategoryTypeCode;
    }

    public ExecutiveCategoryType directorCategoryTypeCode(String directorCategoryTypeCode) {
        this.setDirectorCategoryTypeCode(directorCategoryTypeCode);
        return this;
    }

    public void setDirectorCategoryTypeCode(String directorCategoryTypeCode) {
        this.directorCategoryTypeCode = directorCategoryTypeCode;
    }

    public String getDirectorCategoryType() {
        return this.directorCategoryType;
    }

    public ExecutiveCategoryType directorCategoryType(String directorCategoryType) {
        this.setDirectorCategoryType(directorCategoryType);
        return this;
    }

    public void setDirectorCategoryType(String directorCategoryType) {
        this.directorCategoryType = directorCategoryType;
    }

    public String getDirectorCategoryTypeDetails() {
        return this.directorCategoryTypeDetails;
    }

    public ExecutiveCategoryType directorCategoryTypeDetails(String directorCategoryTypeDetails) {
        this.setDirectorCategoryTypeDetails(directorCategoryTypeDetails);
        return this;
    }

    public void setDirectorCategoryTypeDetails(String directorCategoryTypeDetails) {
        this.directorCategoryTypeDetails = directorCategoryTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExecutiveCategoryType)) {
            return false;
        }
        return id != null && id.equals(((ExecutiveCategoryType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExecutiveCategoryType{" +
            "id=" + getId() +
            ", directorCategoryTypeCode='" + getDirectorCategoryTypeCode() + "'" +
            ", directorCategoryType='" + getDirectorCategoryType() + "'" +
            ", directorCategoryTypeDetails='" + getDirectorCategoryTypeDetails() + "'" +
            "}";
    }
}
