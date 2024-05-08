package io.github.erp.domain;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import org.hibernate.annotations.Type;

/**
 * A CategoryOfSecurity.
 */
@Entity
@Table(name = "category_of_security")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "categoryofsecurity")
public class CategoryOfSecurity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "category_of_security", nullable = false, unique = true)
    private String categoryOfSecurity;

    @NotNull
    @Column(name = "category_of_security_details", nullable = false, unique = true)
    private String categoryOfSecurityDetails;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "category_of_security_description")
    private String categoryOfSecurityDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CategoryOfSecurity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryOfSecurity() {
        return this.categoryOfSecurity;
    }

    public CategoryOfSecurity categoryOfSecurity(String categoryOfSecurity) {
        this.setCategoryOfSecurity(categoryOfSecurity);
        return this;
    }

    public void setCategoryOfSecurity(String categoryOfSecurity) {
        this.categoryOfSecurity = categoryOfSecurity;
    }

    public String getCategoryOfSecurityDetails() {
        return this.categoryOfSecurityDetails;
    }

    public CategoryOfSecurity categoryOfSecurityDetails(String categoryOfSecurityDetails) {
        this.setCategoryOfSecurityDetails(categoryOfSecurityDetails);
        return this;
    }

    public void setCategoryOfSecurityDetails(String categoryOfSecurityDetails) {
        this.categoryOfSecurityDetails = categoryOfSecurityDetails;
    }

    public String getCategoryOfSecurityDescription() {
        return this.categoryOfSecurityDescription;
    }

    public CategoryOfSecurity categoryOfSecurityDescription(String categoryOfSecurityDescription) {
        this.setCategoryOfSecurityDescription(categoryOfSecurityDescription);
        return this;
    }

    public void setCategoryOfSecurityDescription(String categoryOfSecurityDescription) {
        this.categoryOfSecurityDescription = categoryOfSecurityDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryOfSecurity)) {
            return false;
        }
        return id != null && id.equals(((CategoryOfSecurity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryOfSecurity{" +
            "id=" + getId() +
            ", categoryOfSecurity='" + getCategoryOfSecurity() + "'" +
            ", categoryOfSecurityDetails='" + getCategoryOfSecurityDetails() + "'" +
            ", categoryOfSecurityDescription='" + getCategoryOfSecurityDescription() + "'" +
            "}";
    }
}
