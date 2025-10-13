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
import org.hibernate.annotations.Type;

/**
 * A CrbAmountCategoryBand.
 */
@Entity
@Table(name = "crb_amount_category_band")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbamountcategoryband")
public class CrbAmountCategoryBand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount_category_band_code", nullable = false, unique = true)
    private String amountCategoryBandCode;

    @NotNull
    @Column(name = "amount_category_band", nullable = false, unique = true)
    private String amountCategoryBand;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "amount_category_band_details")
    private String amountCategoryBandDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbAmountCategoryBand id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmountCategoryBandCode() {
        return this.amountCategoryBandCode;
    }

    public CrbAmountCategoryBand amountCategoryBandCode(String amountCategoryBandCode) {
        this.setAmountCategoryBandCode(amountCategoryBandCode);
        return this;
    }

    public void setAmountCategoryBandCode(String amountCategoryBandCode) {
        this.amountCategoryBandCode = amountCategoryBandCode;
    }

    public String getAmountCategoryBand() {
        return this.amountCategoryBand;
    }

    public CrbAmountCategoryBand amountCategoryBand(String amountCategoryBand) {
        this.setAmountCategoryBand(amountCategoryBand);
        return this;
    }

    public void setAmountCategoryBand(String amountCategoryBand) {
        this.amountCategoryBand = amountCategoryBand;
    }

    public String getAmountCategoryBandDetails() {
        return this.amountCategoryBandDetails;
    }

    public CrbAmountCategoryBand amountCategoryBandDetails(String amountCategoryBandDetails) {
        this.setAmountCategoryBandDetails(amountCategoryBandDetails);
        return this;
    }

    public void setAmountCategoryBandDetails(String amountCategoryBandDetails) {
        this.amountCategoryBandDetails = amountCategoryBandDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbAmountCategoryBand)) {
            return false;
        }
        return id != null && id.equals(((CrbAmountCategoryBand) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbAmountCategoryBand{" +
            "id=" + getId() +
            ", amountCategoryBandCode='" + getAmountCategoryBandCode() + "'" +
            ", amountCategoryBand='" + getAmountCategoryBand() + "'" +
            ", amountCategoryBandDetails='" + getAmountCategoryBandDetails() + "'" +
            "}";
    }
}
