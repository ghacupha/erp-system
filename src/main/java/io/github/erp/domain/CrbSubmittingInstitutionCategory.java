package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
