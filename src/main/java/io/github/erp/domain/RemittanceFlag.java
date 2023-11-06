package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.domain.enumeration.RemittanceType;
import io.github.erp.domain.enumeration.RemittanceTypeFlag;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A RemittanceFlag.
 */
@Entity
@Table(name = "remittance_flag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "remittanceflag")
public class RemittanceFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "remittance_type_flag", nullable = false)
    private RemittanceTypeFlag remittanceTypeFlag;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "remittance_type", nullable = false)
    private RemittanceType remittanceType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "remittance_type_details")
    private String remittanceTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RemittanceFlag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RemittanceTypeFlag getRemittanceTypeFlag() {
        return this.remittanceTypeFlag;
    }

    public RemittanceFlag remittanceTypeFlag(RemittanceTypeFlag remittanceTypeFlag) {
        this.setRemittanceTypeFlag(remittanceTypeFlag);
        return this;
    }

    public void setRemittanceTypeFlag(RemittanceTypeFlag remittanceTypeFlag) {
        this.remittanceTypeFlag = remittanceTypeFlag;
    }

    public RemittanceType getRemittanceType() {
        return this.remittanceType;
    }

    public RemittanceFlag remittanceType(RemittanceType remittanceType) {
        this.setRemittanceType(remittanceType);
        return this;
    }

    public void setRemittanceType(RemittanceType remittanceType) {
        this.remittanceType = remittanceType;
    }

    public String getRemittanceTypeDetails() {
        return this.remittanceTypeDetails;
    }

    public RemittanceFlag remittanceTypeDetails(String remittanceTypeDetails) {
        this.setRemittanceTypeDetails(remittanceTypeDetails);
        return this;
    }

    public void setRemittanceTypeDetails(String remittanceTypeDetails) {
        this.remittanceTypeDetails = remittanceTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RemittanceFlag)) {
            return false;
        }
        return id != null && id.equals(((RemittanceFlag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RemittanceFlag{" +
            "id=" + getId() +
            ", remittanceTypeFlag='" + getRemittanceTypeFlag() + "'" +
            ", remittanceType='" + getRemittanceType() + "'" +
            ", remittanceTypeDetails='" + getRemittanceTypeDetails() + "'" +
            "}";
    }
}
