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
 * A CommitteeType.
 */
@Entity
@Table(name = "committee_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "committeetype")
public class CommitteeType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "committee_type_code", nullable = false, unique = true)
    private String committeeTypeCode;

    @Column(name = "committee_type")
    private String committeeType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "committee_type_details")
    private String committeeTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CommitteeType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommitteeTypeCode() {
        return this.committeeTypeCode;
    }

    public CommitteeType committeeTypeCode(String committeeTypeCode) {
        this.setCommitteeTypeCode(committeeTypeCode);
        return this;
    }

    public void setCommitteeTypeCode(String committeeTypeCode) {
        this.committeeTypeCode = committeeTypeCode;
    }

    public String getCommitteeType() {
        return this.committeeType;
    }

    public CommitteeType committeeType(String committeeType) {
        this.setCommitteeType(committeeType);
        return this;
    }

    public void setCommitteeType(String committeeType) {
        this.committeeType = committeeType;
    }

    public String getCommitteeTypeDetails() {
        return this.committeeTypeDetails;
    }

    public CommitteeType committeeTypeDetails(String committeeTypeDetails) {
        this.setCommitteeTypeDetails(committeeTypeDetails);
        return this;
    }

    public void setCommitteeTypeDetails(String committeeTypeDetails) {
        this.committeeTypeDetails = committeeTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommitteeType)) {
            return false;
        }
        return id != null && id.equals(((CommitteeType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommitteeType{" +
            "id=" + getId() +
            ", committeeTypeCode='" + getCommitteeTypeCode() + "'" +
            ", committeeType='" + getCommitteeType() + "'" +
            ", committeeTypeDetails='" + getCommitteeTypeDetails() + "'" +
            "}";
    }
}
