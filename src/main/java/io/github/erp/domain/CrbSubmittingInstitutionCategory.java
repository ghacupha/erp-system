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
 * A CrbSubmittingInstitutionCategory.
 */
@Entity
@Table(name = "crb_submitting_institution_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbsubmittinginstitutioncategory")
public class CrbSubmittingInstitutionCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "submitting_institution_category_type_code", nullable = false, unique = true)
    private String submittingInstitutionCategoryTypeCode;

    @NotNull
    @Column(name = "submitting_institution_category_type", nullable = false)
    private String submittingInstitutionCategoryType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "submitting_institution_category_details")
    private String submittingInstitutionCategoryDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbSubmittingInstitutionCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubmittingInstitutionCategoryTypeCode() {
        return this.submittingInstitutionCategoryTypeCode;
    }

    public CrbSubmittingInstitutionCategory submittingInstitutionCategoryTypeCode(String submittingInstitutionCategoryTypeCode) {
        this.setSubmittingInstitutionCategoryTypeCode(submittingInstitutionCategoryTypeCode);
        return this;
    }

    public void setSubmittingInstitutionCategoryTypeCode(String submittingInstitutionCategoryTypeCode) {
        this.submittingInstitutionCategoryTypeCode = submittingInstitutionCategoryTypeCode;
    }

    public String getSubmittingInstitutionCategoryType() {
        return this.submittingInstitutionCategoryType;
    }

    public CrbSubmittingInstitutionCategory submittingInstitutionCategoryType(String submittingInstitutionCategoryType) {
        this.setSubmittingInstitutionCategoryType(submittingInstitutionCategoryType);
        return this;
    }

    public void setSubmittingInstitutionCategoryType(String submittingInstitutionCategoryType) {
        this.submittingInstitutionCategoryType = submittingInstitutionCategoryType;
    }

    public String getSubmittingInstitutionCategoryDetails() {
        return this.submittingInstitutionCategoryDetails;
    }

    public CrbSubmittingInstitutionCategory submittingInstitutionCategoryDetails(String submittingInstitutionCategoryDetails) {
        this.setSubmittingInstitutionCategoryDetails(submittingInstitutionCategoryDetails);
        return this;
    }

    public void setSubmittingInstitutionCategoryDetails(String submittingInstitutionCategoryDetails) {
        this.submittingInstitutionCategoryDetails = submittingInstitutionCategoryDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbSubmittingInstitutionCategory)) {
            return false;
        }
        return id != null && id.equals(((CrbSubmittingInstitutionCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbSubmittingInstitutionCategory{" +
            "id=" + getId() +
            ", submittingInstitutionCategoryTypeCode='" + getSubmittingInstitutionCategoryTypeCode() + "'" +
            ", submittingInstitutionCategoryType='" + getSubmittingInstitutionCategoryType() + "'" +
            ", submittingInstitutionCategoryDetails='" + getSubmittingInstitutionCategoryDetails() + "'" +
            "}";
    }
}
