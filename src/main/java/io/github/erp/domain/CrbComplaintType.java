package io.github.erp.domain;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
 * A CrbComplaintType.
 */
@Entity
@Table(name = "crb_complaint_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbcomplainttype")
public class CrbComplaintType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "complaint_type_code", nullable = false, unique = true)
    private String complaintTypeCode;

    @NotNull
    @Column(name = "complaint_type", nullable = false, unique = true)
    private String complaintType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "complaint_type_details")
    private String complaintTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbComplaintType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplaintTypeCode() {
        return this.complaintTypeCode;
    }

    public CrbComplaintType complaintTypeCode(String complaintTypeCode) {
        this.setComplaintTypeCode(complaintTypeCode);
        return this;
    }

    public void setComplaintTypeCode(String complaintTypeCode) {
        this.complaintTypeCode = complaintTypeCode;
    }

    public String getComplaintType() {
        return this.complaintType;
    }

    public CrbComplaintType complaintType(String complaintType) {
        this.setComplaintType(complaintType);
        return this;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getComplaintTypeDetails() {
        return this.complaintTypeDetails;
    }

    public CrbComplaintType complaintTypeDetails(String complaintTypeDetails) {
        this.setComplaintTypeDetails(complaintTypeDetails);
        return this;
    }

    public void setComplaintTypeDetails(String complaintTypeDetails) {
        this.complaintTypeDetails = complaintTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbComplaintType)) {
            return false;
        }
        return id != null && id.equals(((CrbComplaintType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbComplaintType{" +
            "id=" + getId() +
            ", complaintTypeCode='" + getComplaintTypeCode() + "'" +
            ", complaintType='" + getComplaintType() + "'" +
            ", complaintTypeDetails='" + getComplaintTypeDetails() + "'" +
            "}";
    }
}
