package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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
 * A CrbAgentServiceType.
 */
@Entity
@Table(name = "crb_agent_service_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbagentservicetype")
public class CrbAgentServiceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "agent_service_type_code", nullable = false, unique = true)
    private String agentServiceTypeCode;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "agent_service_type_details")
    private String agentServiceTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbAgentServiceType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentServiceTypeCode() {
        return this.agentServiceTypeCode;
    }

    public CrbAgentServiceType agentServiceTypeCode(String agentServiceTypeCode) {
        this.setAgentServiceTypeCode(agentServiceTypeCode);
        return this;
    }

    public void setAgentServiceTypeCode(String agentServiceTypeCode) {
        this.agentServiceTypeCode = agentServiceTypeCode;
    }

    public String getAgentServiceTypeDetails() {
        return this.agentServiceTypeDetails;
    }

    public CrbAgentServiceType agentServiceTypeDetails(String agentServiceTypeDetails) {
        this.setAgentServiceTypeDetails(agentServiceTypeDetails);
        return this;
    }

    public void setAgentServiceTypeDetails(String agentServiceTypeDetails) {
        this.agentServiceTypeDetails = agentServiceTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbAgentServiceType)) {
            return false;
        }
        return id != null && id.equals(((CrbAgentServiceType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbAgentServiceType{" +
            "id=" + getId() +
            ", agentServiceTypeCode='" + getAgentServiceTypeCode() + "'" +
            ", agentServiceTypeDetails='" + getAgentServiceTypeDetails() + "'" +
            "}";
    }
}
