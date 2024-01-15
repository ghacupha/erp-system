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
 * A CustomerComplaintStatusType.
 */
@Entity
@Table(name = "customer_complaint_status_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "customercomplaintstatustype")
public class CustomerComplaintStatusType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "customer_complaint_status_type_code", nullable = false, unique = true)
    private String customerComplaintStatusTypeCode;

    @NotNull
    @Column(name = "customer_complaint_status_type", nullable = false)
    private String customerComplaintStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "customer_complaint_status_type_details")
    private String customerComplaintStatusTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomerComplaintStatusType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerComplaintStatusTypeCode() {
        return this.customerComplaintStatusTypeCode;
    }

    public CustomerComplaintStatusType customerComplaintStatusTypeCode(String customerComplaintStatusTypeCode) {
        this.setCustomerComplaintStatusTypeCode(customerComplaintStatusTypeCode);
        return this;
    }

    public void setCustomerComplaintStatusTypeCode(String customerComplaintStatusTypeCode) {
        this.customerComplaintStatusTypeCode = customerComplaintStatusTypeCode;
    }

    public String getCustomerComplaintStatusType() {
        return this.customerComplaintStatusType;
    }

    public CustomerComplaintStatusType customerComplaintStatusType(String customerComplaintStatusType) {
        this.setCustomerComplaintStatusType(customerComplaintStatusType);
        return this;
    }

    public void setCustomerComplaintStatusType(String customerComplaintStatusType) {
        this.customerComplaintStatusType = customerComplaintStatusType;
    }

    public String getCustomerComplaintStatusTypeDetails() {
        return this.customerComplaintStatusTypeDetails;
    }

    public CustomerComplaintStatusType customerComplaintStatusTypeDetails(String customerComplaintStatusTypeDetails) {
        this.setCustomerComplaintStatusTypeDetails(customerComplaintStatusTypeDetails);
        return this;
    }

    public void setCustomerComplaintStatusTypeDetails(String customerComplaintStatusTypeDetails) {
        this.customerComplaintStatusTypeDetails = customerComplaintStatusTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerComplaintStatusType)) {
            return false;
        }
        return id != null && id.equals(((CustomerComplaintStatusType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerComplaintStatusType{" +
            "id=" + getId() +
            ", customerComplaintStatusTypeCode='" + getCustomerComplaintStatusTypeCode() + "'" +
            ", customerComplaintStatusType='" + getCustomerComplaintStatusType() + "'" +
            ", customerComplaintStatusTypeDetails='" + getCustomerComplaintStatusTypeDetails() + "'" +
            "}";
    }
}
