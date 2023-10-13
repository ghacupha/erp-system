package io.github.erp.domain;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
 * A UltimateBeneficiaryTypes.
 */
@Entity
@Table(name = "ultimate_beneficiary_types")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ultimatebeneficiarytypes")
public class UltimateBeneficiaryTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ultimate_beneficiary_type_code", nullable = false, unique = true)
    private String ultimateBeneficiaryTypeCode;

    @NotNull
    @Column(name = "ultimate_beneficiary_type", nullable = false, unique = true)
    private String ultimateBeneficiaryType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "ultimate_beneficiary_type_details")
    private String ultimateBeneficiaryTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UltimateBeneficiaryTypes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUltimateBeneficiaryTypeCode() {
        return this.ultimateBeneficiaryTypeCode;
    }

    public UltimateBeneficiaryTypes ultimateBeneficiaryTypeCode(String ultimateBeneficiaryTypeCode) {
        this.setUltimateBeneficiaryTypeCode(ultimateBeneficiaryTypeCode);
        return this;
    }

    public void setUltimateBeneficiaryTypeCode(String ultimateBeneficiaryTypeCode) {
        this.ultimateBeneficiaryTypeCode = ultimateBeneficiaryTypeCode;
    }

    public String getUltimateBeneficiaryType() {
        return this.ultimateBeneficiaryType;
    }

    public UltimateBeneficiaryTypes ultimateBeneficiaryType(String ultimateBeneficiaryType) {
        this.setUltimateBeneficiaryType(ultimateBeneficiaryType);
        return this;
    }

    public void setUltimateBeneficiaryType(String ultimateBeneficiaryType) {
        this.ultimateBeneficiaryType = ultimateBeneficiaryType;
    }

    public String getUltimateBeneficiaryTypeDetails() {
        return this.ultimateBeneficiaryTypeDetails;
    }

    public UltimateBeneficiaryTypes ultimateBeneficiaryTypeDetails(String ultimateBeneficiaryTypeDetails) {
        this.setUltimateBeneficiaryTypeDetails(ultimateBeneficiaryTypeDetails);
        return this;
    }

    public void setUltimateBeneficiaryTypeDetails(String ultimateBeneficiaryTypeDetails) {
        this.ultimateBeneficiaryTypeDetails = ultimateBeneficiaryTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UltimateBeneficiaryTypes)) {
            return false;
        }
        return id != null && id.equals(((UltimateBeneficiaryTypes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UltimateBeneficiaryTypes{" +
            "id=" + getId() +
            ", ultimateBeneficiaryTypeCode='" + getUltimateBeneficiaryTypeCode() + "'" +
            ", ultimateBeneficiaryType='" + getUltimateBeneficiaryType() + "'" +
            ", ultimateBeneficiaryTypeDetails='" + getUltimateBeneficiaryTypeDetails() + "'" +
            "}";
    }
}
