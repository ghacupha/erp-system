package io.github.erp.domain;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmploymentTerms.
 */
@Entity
@Table(name = "employment_terms")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "employmentterms")
public class EmploymentTerms implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "employment_terms_code", nullable = false, unique = true)
    private String employmentTermsCode;

    @NotNull
    @Column(name = "employment_terms_status", nullable = false, unique = true)
    private String employmentTermsStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmploymentTerms id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmploymentTermsCode() {
        return this.employmentTermsCode;
    }

    public EmploymentTerms employmentTermsCode(String employmentTermsCode) {
        this.setEmploymentTermsCode(employmentTermsCode);
        return this;
    }

    public void setEmploymentTermsCode(String employmentTermsCode) {
        this.employmentTermsCode = employmentTermsCode;
    }

    public String getEmploymentTermsStatus() {
        return this.employmentTermsStatus;
    }

    public EmploymentTerms employmentTermsStatus(String employmentTermsStatus) {
        this.setEmploymentTermsStatus(employmentTermsStatus);
        return this;
    }

    public void setEmploymentTermsStatus(String employmentTermsStatus) {
        this.employmentTermsStatus = employmentTermsStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmploymentTerms)) {
            return false;
        }
        return id != null && id.equals(((EmploymentTerms) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmploymentTerms{" +
            "id=" + getId() +
            ", employmentTermsCode='" + getEmploymentTermsCode() + "'" +
            ", employmentTermsStatus='" + getEmploymentTermsStatus() + "'" +
            "}";
    }
}
