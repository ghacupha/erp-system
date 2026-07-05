package io.github.erp.domain;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
 * A CustomerType.
 */
@Entity
@Table(name = "customer_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "customertype")
public class CustomerType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_type_code", unique = true)
    private String customerTypeCode;

    @Column(name = "customer_type", unique = true)
    private String customerType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "customer_type_description")
    private String customerTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomerType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerTypeCode() {
        return this.customerTypeCode;
    }

    public CustomerType customerTypeCode(String customerTypeCode) {
        this.setCustomerTypeCode(customerTypeCode);
        return this;
    }

    public void setCustomerTypeCode(String customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }

    public String getCustomerType() {
        return this.customerType;
    }

    public CustomerType customerType(String customerType) {
        this.setCustomerType(customerType);
        return this;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerTypeDescription() {
        return this.customerTypeDescription;
    }

    public CustomerType customerTypeDescription(String customerTypeDescription) {
        this.setCustomerTypeDescription(customerTypeDescription);
        return this;
    }

    public void setCustomerTypeDescription(String customerTypeDescription) {
        this.customerTypeDescription = customerTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerType)) {
            return false;
        }
        return id != null && id.equals(((CustomerType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerType{" +
            "id=" + getId() +
            ", customerTypeCode='" + getCustomerTypeCode() + "'" +
            ", customerType='" + getCustomerType() + "'" +
            ", customerTypeDescription='" + getCustomerTypeDescription() + "'" +
            "}";
    }
}
