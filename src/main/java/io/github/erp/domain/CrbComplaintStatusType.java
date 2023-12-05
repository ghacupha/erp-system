package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
 * A CrbComplaintStatusType.
 */
@Entity
@Table(name = "crb_complaint_status_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbcomplaintstatustype")
public class CrbComplaintStatusType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "complaint_status_type_code", nullable = false, unique = true)
    private String complaintStatusTypeCode;

    @NotNull
    @Column(name = "complaint_status_type", nullable = false, unique = true)
    private String complaintStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "complaint_status_details")
    private String complaintStatusDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbComplaintStatusType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplaintStatusTypeCode() {
        return this.complaintStatusTypeCode;
    }

    public CrbComplaintStatusType complaintStatusTypeCode(String complaintStatusTypeCode) {
        this.setComplaintStatusTypeCode(complaintStatusTypeCode);
        return this;
    }

    public void setComplaintStatusTypeCode(String complaintStatusTypeCode) {
        this.complaintStatusTypeCode = complaintStatusTypeCode;
    }

    public String getComplaintStatusType() {
        return this.complaintStatusType;
    }

    public CrbComplaintStatusType complaintStatusType(String complaintStatusType) {
        this.setComplaintStatusType(complaintStatusType);
        return this;
    }

    public void setComplaintStatusType(String complaintStatusType) {
        this.complaintStatusType = complaintStatusType;
    }

    public String getComplaintStatusDetails() {
        return this.complaintStatusDetails;
    }

    public CrbComplaintStatusType complaintStatusDetails(String complaintStatusDetails) {
        this.setComplaintStatusDetails(complaintStatusDetails);
        return this;
    }

    public void setComplaintStatusDetails(String complaintStatusDetails) {
        this.complaintStatusDetails = complaintStatusDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbComplaintStatusType)) {
            return false;
        }
        return id != null && id.equals(((CrbComplaintStatusType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbComplaintStatusType{" +
            "id=" + getId() +
            ", complaintStatusTypeCode='" + getComplaintStatusTypeCode() + "'" +
            ", complaintStatusType='" + getComplaintStatusType() + "'" +
            ", complaintStatusDetails='" + getComplaintStatusDetails() + "'" +
            "}";
    }
}
