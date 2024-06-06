package io.github.erp.domain;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
 * A UltimateBeneficiaryCategory.
 */
@Entity
@Table(name = "ultimate_beneficiary_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ultimatebeneficiarycategory")
public class UltimateBeneficiaryCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ultimate_beneficiary_category_type_code", nullable = false, unique = true)
    private String ultimateBeneficiaryCategoryTypeCode;

    @NotNull
    @Column(name = "ultimate_beneficiary_type", nullable = false)
    private String ultimateBeneficiaryType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "ultimate_beneficiary_category_type_details")
    private String ultimateBeneficiaryCategoryTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UltimateBeneficiaryCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUltimateBeneficiaryCategoryTypeCode() {
        return this.ultimateBeneficiaryCategoryTypeCode;
    }

    public UltimateBeneficiaryCategory ultimateBeneficiaryCategoryTypeCode(String ultimateBeneficiaryCategoryTypeCode) {
        this.setUltimateBeneficiaryCategoryTypeCode(ultimateBeneficiaryCategoryTypeCode);
        return this;
    }

    public void setUltimateBeneficiaryCategoryTypeCode(String ultimateBeneficiaryCategoryTypeCode) {
        this.ultimateBeneficiaryCategoryTypeCode = ultimateBeneficiaryCategoryTypeCode;
    }

    public String getUltimateBeneficiaryType() {
        return this.ultimateBeneficiaryType;
    }

    public UltimateBeneficiaryCategory ultimateBeneficiaryType(String ultimateBeneficiaryType) {
        this.setUltimateBeneficiaryType(ultimateBeneficiaryType);
        return this;
    }

    public void setUltimateBeneficiaryType(String ultimateBeneficiaryType) {
        this.ultimateBeneficiaryType = ultimateBeneficiaryType;
    }

    public String getUltimateBeneficiaryCategoryTypeDetails() {
        return this.ultimateBeneficiaryCategoryTypeDetails;
    }

    public UltimateBeneficiaryCategory ultimateBeneficiaryCategoryTypeDetails(String ultimateBeneficiaryCategoryTypeDetails) {
        this.setUltimateBeneficiaryCategoryTypeDetails(ultimateBeneficiaryCategoryTypeDetails);
        return this;
    }

    public void setUltimateBeneficiaryCategoryTypeDetails(String ultimateBeneficiaryCategoryTypeDetails) {
        this.ultimateBeneficiaryCategoryTypeDetails = ultimateBeneficiaryCategoryTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UltimateBeneficiaryCategory)) {
            return false;
        }
        return id != null && id.equals(((UltimateBeneficiaryCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UltimateBeneficiaryCategory{" +
            "id=" + getId() +
            ", ultimateBeneficiaryCategoryTypeCode='" + getUltimateBeneficiaryCategoryTypeCode() + "'" +
            ", ultimateBeneficiaryType='" + getUltimateBeneficiaryType() + "'" +
            ", ultimateBeneficiaryCategoryTypeDetails='" + getUltimateBeneficiaryCategoryTypeDetails() + "'" +
            "}";
    }
}
