package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
 * A FinancialDerivativeTypeCode.
 */
@Entity
@Table(name = "financial_derivative_type_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "financialderivativetypecode")
public class FinancialDerivativeTypeCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "financial_derivative_type_code", nullable = false, unique = true)
    private String financialDerivativeTypeCode;

    @NotNull
    @Column(name = "financial_derivative_type", nullable = false, unique = true)
    private String financialDerivativeType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "financial_derivative_type_details")
    private String financialDerivativeTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FinancialDerivativeTypeCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFinancialDerivativeTypeCode() {
        return this.financialDerivativeTypeCode;
    }

    public FinancialDerivativeTypeCode financialDerivativeTypeCode(String financialDerivativeTypeCode) {
        this.setFinancialDerivativeTypeCode(financialDerivativeTypeCode);
        return this;
    }

    public void setFinancialDerivativeTypeCode(String financialDerivativeTypeCode) {
        this.financialDerivativeTypeCode = financialDerivativeTypeCode;
    }

    public String getFinancialDerivativeType() {
        return this.financialDerivativeType;
    }

    public FinancialDerivativeTypeCode financialDerivativeType(String financialDerivativeType) {
        this.setFinancialDerivativeType(financialDerivativeType);
        return this;
    }

    public void setFinancialDerivativeType(String financialDerivativeType) {
        this.financialDerivativeType = financialDerivativeType;
    }

    public String getFinancialDerivativeTypeDetails() {
        return this.financialDerivativeTypeDetails;
    }

    public FinancialDerivativeTypeCode financialDerivativeTypeDetails(String financialDerivativeTypeDetails) {
        this.setFinancialDerivativeTypeDetails(financialDerivativeTypeDetails);
        return this;
    }

    public void setFinancialDerivativeTypeDetails(String financialDerivativeTypeDetails) {
        this.financialDerivativeTypeDetails = financialDerivativeTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinancialDerivativeTypeCode)) {
            return false;
        }
        return id != null && id.equals(((FinancialDerivativeTypeCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialDerivativeTypeCode{" +
            "id=" + getId() +
            ", financialDerivativeTypeCode='" + getFinancialDerivativeTypeCode() + "'" +
            ", financialDerivativeType='" + getFinancialDerivativeType() + "'" +
            ", financialDerivativeTypeDetails='" + getFinancialDerivativeTypeDetails() + "'" +
            "}";
    }
}
