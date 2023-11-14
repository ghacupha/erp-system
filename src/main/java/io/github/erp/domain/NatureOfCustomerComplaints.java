package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
