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
 * A PartyRelationType.
 */
@Entity
@Table(name = "party_relation_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "partyrelationtype")
public class PartyRelationType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "party_relation_type_code", nullable = false, unique = true)
    private String partyRelationTypeCode;

    @NotNull
    @Column(name = "party_relation_type", nullable = false)
    private String partyRelationType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "party_relation_type_description")
    private String partyRelationTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PartyRelationType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartyRelationTypeCode() {
        return this.partyRelationTypeCode;
    }

    public PartyRelationType partyRelationTypeCode(String partyRelationTypeCode) {
        this.setPartyRelationTypeCode(partyRelationTypeCode);
        return this;
    }

    public void setPartyRelationTypeCode(String partyRelationTypeCode) {
        this.partyRelationTypeCode = partyRelationTypeCode;
    }

    public String getPartyRelationType() {
        return this.partyRelationType;
    }

    public PartyRelationType partyRelationType(String partyRelationType) {
        this.setPartyRelationType(partyRelationType);
        return this;
    }

    public void setPartyRelationType(String partyRelationType) {
        this.partyRelationType = partyRelationType;
    }

    public String getPartyRelationTypeDescription() {
        return this.partyRelationTypeDescription;
    }

    public PartyRelationType partyRelationTypeDescription(String partyRelationTypeDescription) {
        this.setPartyRelationTypeDescription(partyRelationTypeDescription);
        return this;
    }

    public void setPartyRelationTypeDescription(String partyRelationTypeDescription) {
        this.partyRelationTypeDescription = partyRelationTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartyRelationType)) {
            return false;
        }
        return id != null && id.equals(((PartyRelationType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyRelationType{" +
            "id=" + getId() +
            ", partyRelationTypeCode='" + getPartyRelationTypeCode() + "'" +
            ", partyRelationType='" + getPartyRelationType() + "'" +
            ", partyRelationTypeDescription='" + getPartyRelationTypeDescription() + "'" +
            "}";
    }
}
