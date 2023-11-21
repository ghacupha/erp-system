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
 * A CrbCreditFacilityType.
 */
@Entity
@Table(name = "crb_credit_facility_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbcreditfacilitytype")
public class CrbCreditFacilityType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "credit_facility_type_code", nullable = false, unique = true)
    private String creditFacilityTypeCode;

    @NotNull
    @Column(name = "credit_facility_type", nullable = false, unique = true)
    private String creditFacilityType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "credit_facility_description")
    private String creditFacilityDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbCreditFacilityType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditFacilityTypeCode() {
        return this.creditFacilityTypeCode;
    }

    public CrbCreditFacilityType creditFacilityTypeCode(String creditFacilityTypeCode) {
        this.setCreditFacilityTypeCode(creditFacilityTypeCode);
        return this;
    }

    public void setCreditFacilityTypeCode(String creditFacilityTypeCode) {
        this.creditFacilityTypeCode = creditFacilityTypeCode;
    }

    public String getCreditFacilityType() {
        return this.creditFacilityType;
    }

    public CrbCreditFacilityType creditFacilityType(String creditFacilityType) {
        this.setCreditFacilityType(creditFacilityType);
        return this;
    }

    public void setCreditFacilityType(String creditFacilityType) {
        this.creditFacilityType = creditFacilityType;
    }

    public String getCreditFacilityDescription() {
        return this.creditFacilityDescription;
    }

    public CrbCreditFacilityType creditFacilityDescription(String creditFacilityDescription) {
        this.setCreditFacilityDescription(creditFacilityDescription);
        return this;
    }

    public void setCreditFacilityDescription(String creditFacilityDescription) {
        this.creditFacilityDescription = creditFacilityDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbCreditFacilityType)) {
            return false;
        }
        return id != null && id.equals(((CrbCreditFacilityType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbCreditFacilityType{" +
            "id=" + getId() +
            ", creditFacilityTypeCode='" + getCreditFacilityTypeCode() + "'" +
            ", creditFacilityType='" + getCreditFacilityType() + "'" +
            ", creditFacilityDescription='" + getCreditFacilityDescription() + "'" +
            "}";
    }
}
