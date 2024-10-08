package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
 * A CrbProductServiceFeeType.
 */
@Entity
@Table(name = "crb_product_service_fee_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbproductservicefeetype")
public class CrbProductServiceFeeType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "charge_type_code", nullable = false, unique = true)
    private String chargeTypeCode;

    @Column(name = "charge_type_description", unique = true)
    private String chargeTypeDescription;

    @NotNull
    @Column(name = "charge_type_category", nullable = false, unique = true)
    private String chargeTypeCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbProductServiceFeeType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChargeTypeCode() {
        return this.chargeTypeCode;
    }

    public CrbProductServiceFeeType chargeTypeCode(String chargeTypeCode) {
        this.setChargeTypeCode(chargeTypeCode);
        return this;
    }

    public void setChargeTypeCode(String chargeTypeCode) {
        this.chargeTypeCode = chargeTypeCode;
    }

    public String getChargeTypeDescription() {
        return this.chargeTypeDescription;
    }

    public CrbProductServiceFeeType chargeTypeDescription(String chargeTypeDescription) {
        this.setChargeTypeDescription(chargeTypeDescription);
        return this;
    }

    public void setChargeTypeDescription(String chargeTypeDescription) {
        this.chargeTypeDescription = chargeTypeDescription;
    }

    public String getChargeTypeCategory() {
        return this.chargeTypeCategory;
    }

    public CrbProductServiceFeeType chargeTypeCategory(String chargeTypeCategory) {
        this.setChargeTypeCategory(chargeTypeCategory);
        return this;
    }

    public void setChargeTypeCategory(String chargeTypeCategory) {
        this.chargeTypeCategory = chargeTypeCategory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbProductServiceFeeType)) {
            return false;
        }
        return id != null && id.equals(((CrbProductServiceFeeType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbProductServiceFeeType{" +
            "id=" + getId() +
            ", chargeTypeCode='" + getChargeTypeCode() + "'" +
            ", chargeTypeDescription='" + getChargeTypeDescription() + "'" +
            ", chargeTypeCategory='" + getChargeTypeCategory() + "'" +
            "}";
    }
}
