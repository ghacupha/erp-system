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
 * A StaffRoleType.
 */
@Entity
@Table(name = "staff_role_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "staffroletype")
public class StaffRoleType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "staff_role_type_code", nullable = false, unique = true)
    private String staffRoleTypeCode;

    @NotNull
    @Column(name = "staff_role_type", nullable = false, unique = true)
    private String staffRoleType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "staff_role_type_details")
    private String staffRoleTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StaffRoleType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffRoleTypeCode() {
        return this.staffRoleTypeCode;
    }

    public StaffRoleType staffRoleTypeCode(String staffRoleTypeCode) {
        this.setStaffRoleTypeCode(staffRoleTypeCode);
        return this;
    }

    public void setStaffRoleTypeCode(String staffRoleTypeCode) {
        this.staffRoleTypeCode = staffRoleTypeCode;
    }

    public String getStaffRoleType() {
        return this.staffRoleType;
    }

    public StaffRoleType staffRoleType(String staffRoleType) {
        this.setStaffRoleType(staffRoleType);
        return this;
    }

    public void setStaffRoleType(String staffRoleType) {
        this.staffRoleType = staffRoleType;
    }

    public String getStaffRoleTypeDetails() {
        return this.staffRoleTypeDetails;
    }

    public StaffRoleType staffRoleTypeDetails(String staffRoleTypeDetails) {
        this.setStaffRoleTypeDetails(staffRoleTypeDetails);
        return this;
    }

    public void setStaffRoleTypeDetails(String staffRoleTypeDetails) {
        this.staffRoleTypeDetails = staffRoleTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaffRoleType)) {
            return false;
        }
        return id != null && id.equals(((StaffRoleType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffRoleType{" +
            "id=" + getId() +
            ", staffRoleTypeCode='" + getStaffRoleTypeCode() + "'" +
            ", staffRoleType='" + getStaffRoleType() + "'" +
            ", staffRoleTypeDetails='" + getStaffRoleTypeDetails() + "'" +
            "}";
    }
}
