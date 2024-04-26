package io.github.erp.domain;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
