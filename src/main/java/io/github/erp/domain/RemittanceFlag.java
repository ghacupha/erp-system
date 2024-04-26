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
