package io.github.erp.domain;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
 * A SecurityTenure.
 */
@Entity
@Table(name = "security_tenure")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "securitytenure")
public class SecurityTenure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "security_tenure_code", nullable = false, unique = true)
    private String securityTenureCode;

    @NotNull
    @Column(name = "security_tenure_type", nullable = false, unique = true)
    private String securityTenureType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "security_tenure_details")
    private String securityTenureDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SecurityTenure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecurityTenureCode() {
        return this.securityTenureCode;
    }

    public SecurityTenure securityTenureCode(String securityTenureCode) {
        this.setSecurityTenureCode(securityTenureCode);
        return this;
    }

    public void setSecurityTenureCode(String securityTenureCode) {
        this.securityTenureCode = securityTenureCode;
    }

    public String getSecurityTenureType() {
        return this.securityTenureType;
    }

    public SecurityTenure securityTenureType(String securityTenureType) {
        this.setSecurityTenureType(securityTenureType);
        return this;
    }

    public void setSecurityTenureType(String securityTenureType) {
        this.securityTenureType = securityTenureType;
    }

    public String getSecurityTenureDetails() {
        return this.securityTenureDetails;
    }

    public SecurityTenure securityTenureDetails(String securityTenureDetails) {
        this.setSecurityTenureDetails(securityTenureDetails);
        return this;
    }

    public void setSecurityTenureDetails(String securityTenureDetails) {
        this.securityTenureDetails = securityTenureDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityTenure)) {
            return false;
        }
        return id != null && id.equals(((SecurityTenure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityTenure{" +
            "id=" + getId() +
            ", securityTenureCode='" + getSecurityTenureCode() + "'" +
            ", securityTenureType='" + getSecurityTenureType() + "'" +
            ", securityTenureDetails='" + getSecurityTenureDetails() + "'" +
            "}";
    }
}
