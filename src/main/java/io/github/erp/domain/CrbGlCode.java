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

/**
 * A CrbGlCode.
 */
@Entity
@Table(name = "crb_gl_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbglcode")
public class CrbGlCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "gl_code", nullable = false, unique = true)
    private String glCode;

    @NotNull
    @Column(name = "gl_description", nullable = false)
    private String glDescription;

    @NotNull
    @Column(name = "gl_type", nullable = false)
    private String glType;

    @NotNull
    @Column(name = "institution_category", nullable = false)
    private String institutionCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbGlCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGlCode() {
        return this.glCode;
    }

    public CrbGlCode glCode(String glCode) {
        this.setGlCode(glCode);
        return this;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public String getGlDescription() {
        return this.glDescription;
    }

    public CrbGlCode glDescription(String glDescription) {
        this.setGlDescription(glDescription);
        return this;
    }

    public void setGlDescription(String glDescription) {
        this.glDescription = glDescription;
    }

    public String getGlType() {
        return this.glType;
    }

    public CrbGlCode glType(String glType) {
        this.setGlType(glType);
        return this;
    }

    public void setGlType(String glType) {
        this.glType = glType;
    }

    public String getInstitutionCategory() {
        return this.institutionCategory;
    }

    public CrbGlCode institutionCategory(String institutionCategory) {
        this.setInstitutionCategory(institutionCategory);
        return this;
    }

    public void setInstitutionCategory(String institutionCategory) {
        this.institutionCategory = institutionCategory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbGlCode)) {
            return false;
        }
        return id != null && id.equals(((CrbGlCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbGlCode{" +
            "id=" + getId() +
            ", glCode='" + getGlCode() + "'" +
            ", glDescription='" + getGlDescription() + "'" +
            ", glType='" + getGlType() + "'" +
            ", institutionCategory='" + getInstitutionCategory() + "'" +
            "}";
    }
}
