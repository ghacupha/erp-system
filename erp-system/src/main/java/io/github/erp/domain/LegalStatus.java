package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
 * A LegalStatus.
 */
@Entity
@Table(name = "legal_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "legalstatus")
public class LegalStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "legal_status_code", nullable = false, unique = true)
    private String legalStatusCode;

    @NotNull
    @Column(name = "legal_status_type", nullable = false)
    private String legalStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "legal_status_description")
    private String legalStatusDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LegalStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLegalStatusCode() {
        return this.legalStatusCode;
    }

    public LegalStatus legalStatusCode(String legalStatusCode) {
        this.setLegalStatusCode(legalStatusCode);
        return this;
    }

    public void setLegalStatusCode(String legalStatusCode) {
        this.legalStatusCode = legalStatusCode;
    }

    public String getLegalStatusType() {
        return this.legalStatusType;
    }

    public LegalStatus legalStatusType(String legalStatusType) {
        this.setLegalStatusType(legalStatusType);
        return this;
    }

    public void setLegalStatusType(String legalStatusType) {
        this.legalStatusType = legalStatusType;
    }

    public String getLegalStatusDescription() {
        return this.legalStatusDescription;
    }

    public LegalStatus legalStatusDescription(String legalStatusDescription) {
        this.setLegalStatusDescription(legalStatusDescription);
        return this;
    }

    public void setLegalStatusDescription(String legalStatusDescription) {
        this.legalStatusDescription = legalStatusDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LegalStatus)) {
            return false;
        }
        return id != null && id.equals(((LegalStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LegalStatus{" +
            "id=" + getId() +
            ", legalStatusCode='" + getLegalStatusCode() + "'" +
            ", legalStatusType='" + getLegalStatusType() + "'" +
            ", legalStatusDescription='" + getLegalStatusDescription() + "'" +
            "}";
    }
}
