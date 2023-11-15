package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
 * A StaffCurrentEmploymentStatus.
 */
@Entity
@Table(name = "staff_current_employment_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "staffcurrentemploymentstatus")
public class StaffCurrentEmploymentStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "staff_current_employment_status_type_code", nullable = false, unique = true)
    private String staffCurrentEmploymentStatusTypeCode;

    @NotNull
    @Column(name = "staff_current_employment_status_type", nullable = false)
    private String staffCurrentEmploymentStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "staff_current_employment_status_type_details")
    private String staffCurrentEmploymentStatusTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StaffCurrentEmploymentStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffCurrentEmploymentStatusTypeCode() {
        return this.staffCurrentEmploymentStatusTypeCode;
    }

    public StaffCurrentEmploymentStatus staffCurrentEmploymentStatusTypeCode(String staffCurrentEmploymentStatusTypeCode) {
        this.setStaffCurrentEmploymentStatusTypeCode(staffCurrentEmploymentStatusTypeCode);
        return this;
    }

    public void setStaffCurrentEmploymentStatusTypeCode(String staffCurrentEmploymentStatusTypeCode) {
        this.staffCurrentEmploymentStatusTypeCode = staffCurrentEmploymentStatusTypeCode;
    }

    public String getStaffCurrentEmploymentStatusType() {
        return this.staffCurrentEmploymentStatusType;
    }

    public StaffCurrentEmploymentStatus staffCurrentEmploymentStatusType(String staffCurrentEmploymentStatusType) {
        this.setStaffCurrentEmploymentStatusType(staffCurrentEmploymentStatusType);
        return this;
    }

    public void setStaffCurrentEmploymentStatusType(String staffCurrentEmploymentStatusType) {
        this.staffCurrentEmploymentStatusType = staffCurrentEmploymentStatusType;
    }

    public String getStaffCurrentEmploymentStatusTypeDetails() {
        return this.staffCurrentEmploymentStatusTypeDetails;
    }

    public StaffCurrentEmploymentStatus staffCurrentEmploymentStatusTypeDetails(String staffCurrentEmploymentStatusTypeDetails) {
        this.setStaffCurrentEmploymentStatusTypeDetails(staffCurrentEmploymentStatusTypeDetails);
        return this;
    }

    public void setStaffCurrentEmploymentStatusTypeDetails(String staffCurrentEmploymentStatusTypeDetails) {
        this.staffCurrentEmploymentStatusTypeDetails = staffCurrentEmploymentStatusTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaffCurrentEmploymentStatus)) {
            return false;
        }
        return id != null && id.equals(((StaffCurrentEmploymentStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffCurrentEmploymentStatus{" +
            "id=" + getId() +
            ", staffCurrentEmploymentStatusTypeCode='" + getStaffCurrentEmploymentStatusTypeCode() + "'" +
            ", staffCurrentEmploymentStatusType='" + getStaffCurrentEmploymentStatusType() + "'" +
            ", staffCurrentEmploymentStatusTypeDetails='" + getStaffCurrentEmploymentStatusTypeDetails() + "'" +
            "}";
    }
}
