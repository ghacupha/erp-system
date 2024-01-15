package io.github.erp.domain;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
 * A InsiderCategoryTypes.
 */
@Entity
@Table(name = "insider_category_types")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "insidercategorytypes")
public class InsiderCategoryTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "insider_category_type_code", nullable = false, unique = true)
    private String insiderCategoryTypeCode;

    @NotNull
    @Column(name = "insider_category_type_detail", nullable = false)
    private String insiderCategoryTypeDetail;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "insider_category_description")
    private String insiderCategoryDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InsiderCategoryTypes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInsiderCategoryTypeCode() {
        return this.insiderCategoryTypeCode;
    }

    public InsiderCategoryTypes insiderCategoryTypeCode(String insiderCategoryTypeCode) {
        this.setInsiderCategoryTypeCode(insiderCategoryTypeCode);
        return this;
    }

    public void setInsiderCategoryTypeCode(String insiderCategoryTypeCode) {
        this.insiderCategoryTypeCode = insiderCategoryTypeCode;
    }

    public String getInsiderCategoryTypeDetail() {
        return this.insiderCategoryTypeDetail;
    }

    public InsiderCategoryTypes insiderCategoryTypeDetail(String insiderCategoryTypeDetail) {
        this.setInsiderCategoryTypeDetail(insiderCategoryTypeDetail);
        return this;
    }

    public void setInsiderCategoryTypeDetail(String insiderCategoryTypeDetail) {
        this.insiderCategoryTypeDetail = insiderCategoryTypeDetail;
    }

    public String getInsiderCategoryDescription() {
        return this.insiderCategoryDescription;
    }

    public InsiderCategoryTypes insiderCategoryDescription(String insiderCategoryDescription) {
        this.setInsiderCategoryDescription(insiderCategoryDescription);
        return this;
    }

    public void setInsiderCategoryDescription(String insiderCategoryDescription) {
        this.insiderCategoryDescription = insiderCategoryDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsiderCategoryTypes)) {
            return false;
        }
        return id != null && id.equals(((InsiderCategoryTypes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsiderCategoryTypes{" +
            "id=" + getId() +
            ", insiderCategoryTypeCode='" + getInsiderCategoryTypeCode() + "'" +
            ", insiderCategoryTypeDetail='" + getInsiderCategoryTypeDetail() + "'" +
            ", insiderCategoryDescription='" + getInsiderCategoryDescription() + "'" +
            "}";
    }
}
