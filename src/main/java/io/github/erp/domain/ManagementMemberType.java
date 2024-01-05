package io.github.erp.domain;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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

/**
 * A ManagementMemberType.
 */
@Entity
@Table(name = "management_member_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "managementmembertype")
public class ManagementMemberType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "management_member_type_code", nullable = false, unique = true)
    private String managementMemberTypeCode;

    @NotNull
    @Column(name = "management_member_type", nullable = false, unique = true)
    private String managementMemberType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ManagementMemberType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManagementMemberTypeCode() {
        return this.managementMemberTypeCode;
    }

    public ManagementMemberType managementMemberTypeCode(String managementMemberTypeCode) {
        this.setManagementMemberTypeCode(managementMemberTypeCode);
        return this;
    }

    public void setManagementMemberTypeCode(String managementMemberTypeCode) {
        this.managementMemberTypeCode = managementMemberTypeCode;
    }

    public String getManagementMemberType() {
        return this.managementMemberType;
    }

    public ManagementMemberType managementMemberType(String managementMemberType) {
        this.setManagementMemberType(managementMemberType);
        return this;
    }

    public void setManagementMemberType(String managementMemberType) {
        this.managementMemberType = managementMemberType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManagementMemberType)) {
            return false;
        }
        return id != null && id.equals(((ManagementMemberType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagementMemberType{" +
            "id=" + getId() +
            ", managementMemberTypeCode='" + getManagementMemberTypeCode() + "'" +
            ", managementMemberType='" + getManagementMemberType() + "'" +
            "}";
    }
}
