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
 * A NatureOfCustomerComplaints.
 */
@Entity
@Table(name = "nature_of_customer_complaints")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "natureofcustomercomplaints")
public class NatureOfCustomerComplaints implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nature_of_complaint_type_code", nullable = false, unique = true)
    private String natureOfComplaintTypeCode;

    @NotNull
    @Column(name = "nature_of_complaint_type", nullable = false, unique = true)
    private String natureOfComplaintType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "nature_of_complaint_type_details")
    private String natureOfComplaintTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NatureOfCustomerComplaints id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNatureOfComplaintTypeCode() {
        return this.natureOfComplaintTypeCode;
    }

    public NatureOfCustomerComplaints natureOfComplaintTypeCode(String natureOfComplaintTypeCode) {
        this.setNatureOfComplaintTypeCode(natureOfComplaintTypeCode);
        return this;
    }

    public void setNatureOfComplaintTypeCode(String natureOfComplaintTypeCode) {
        this.natureOfComplaintTypeCode = natureOfComplaintTypeCode;
    }

    public String getNatureOfComplaintType() {
        return this.natureOfComplaintType;
    }

    public NatureOfCustomerComplaints natureOfComplaintType(String natureOfComplaintType) {
        this.setNatureOfComplaintType(natureOfComplaintType);
        return this;
    }

    public void setNatureOfComplaintType(String natureOfComplaintType) {
        this.natureOfComplaintType = natureOfComplaintType;
    }

    public String getNatureOfComplaintTypeDetails() {
        return this.natureOfComplaintTypeDetails;
    }

    public NatureOfCustomerComplaints natureOfComplaintTypeDetails(String natureOfComplaintTypeDetails) {
        this.setNatureOfComplaintTypeDetails(natureOfComplaintTypeDetails);
        return this;
    }

    public void setNatureOfComplaintTypeDetails(String natureOfComplaintTypeDetails) {
        this.natureOfComplaintTypeDetails = natureOfComplaintTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NatureOfCustomerComplaints)) {
            return false;
        }
        return id != null && id.equals(((NatureOfCustomerComplaints) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NatureOfCustomerComplaints{" +
            "id=" + getId() +
            ", natureOfComplaintTypeCode='" + getNatureOfComplaintTypeCode() + "'" +
            ", natureOfComplaintType='" + getNatureOfComplaintType() + "'" +
            ", natureOfComplaintTypeDetails='" + getNatureOfComplaintTypeDetails() + "'" +
            "}";
    }
}
