package io.github.erp.domain;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
 * A CounterpartyType.
 */
@Entity
@Table(name = "counterparty_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "counterpartytype")
public class CounterpartyType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "counterparty_type_code", nullable = false, unique = true)
    private String counterpartyTypeCode;

    @NotNull
    @Column(name = "counter_party_type", nullable = false, unique = true)
    private String counterPartyType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "counterparty_type_description")
    private String counterpartyTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CounterpartyType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCounterpartyTypeCode() {
        return this.counterpartyTypeCode;
    }

    public CounterpartyType counterpartyTypeCode(String counterpartyTypeCode) {
        this.setCounterpartyTypeCode(counterpartyTypeCode);
        return this;
    }

    public void setCounterpartyTypeCode(String counterpartyTypeCode) {
        this.counterpartyTypeCode = counterpartyTypeCode;
    }

    public String getCounterPartyType() {
        return this.counterPartyType;
    }

    public CounterpartyType counterPartyType(String counterPartyType) {
        this.setCounterPartyType(counterPartyType);
        return this;
    }

    public void setCounterPartyType(String counterPartyType) {
        this.counterPartyType = counterPartyType;
    }

    public String getCounterpartyTypeDescription() {
        return this.counterpartyTypeDescription;
    }

    public CounterpartyType counterpartyTypeDescription(String counterpartyTypeDescription) {
        this.setCounterpartyTypeDescription(counterpartyTypeDescription);
        return this;
    }

    public void setCounterpartyTypeDescription(String counterpartyTypeDescription) {
        this.counterpartyTypeDescription = counterpartyTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CounterpartyType)) {
            return false;
        }
        return id != null && id.equals(((CounterpartyType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CounterpartyType{" +
            "id=" + getId() +
            ", counterpartyTypeCode='" + getCounterpartyTypeCode() + "'" +
            ", counterPartyType='" + getCounterPartyType() + "'" +
            ", counterpartyTypeDescription='" + getCounterpartyTypeDescription() + "'" +
            "}";
    }
}
