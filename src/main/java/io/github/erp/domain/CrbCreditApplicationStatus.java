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
 * A CrbCreditApplicationStatus.
 */
@Entity
@Table(name = "crb_credit_application_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbcreditapplicationstatus")
public class CrbCreditApplicationStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "crb_credit_application_status_type_code", nullable = false, unique = true)
    private String crbCreditApplicationStatusTypeCode;

    @NotNull
    @Column(name = "crb_credit_application_status_type", nullable = false, unique = true)
    private String crbCreditApplicationStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "crb_credit_application_status_details")
    private String crbCreditApplicationStatusDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbCreditApplicationStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrbCreditApplicationStatusTypeCode() {
        return this.crbCreditApplicationStatusTypeCode;
    }

    public CrbCreditApplicationStatus crbCreditApplicationStatusTypeCode(String crbCreditApplicationStatusTypeCode) {
        this.setCrbCreditApplicationStatusTypeCode(crbCreditApplicationStatusTypeCode);
        return this;
    }

    public void setCrbCreditApplicationStatusTypeCode(String crbCreditApplicationStatusTypeCode) {
        this.crbCreditApplicationStatusTypeCode = crbCreditApplicationStatusTypeCode;
    }

    public String getCrbCreditApplicationStatusType() {
        return this.crbCreditApplicationStatusType;
    }

    public CrbCreditApplicationStatus crbCreditApplicationStatusType(String crbCreditApplicationStatusType) {
        this.setCrbCreditApplicationStatusType(crbCreditApplicationStatusType);
        return this;
    }

    public void setCrbCreditApplicationStatusType(String crbCreditApplicationStatusType) {
        this.crbCreditApplicationStatusType = crbCreditApplicationStatusType;
    }

    public String getCrbCreditApplicationStatusDetails() {
        return this.crbCreditApplicationStatusDetails;
    }

    public CrbCreditApplicationStatus crbCreditApplicationStatusDetails(String crbCreditApplicationStatusDetails) {
        this.setCrbCreditApplicationStatusDetails(crbCreditApplicationStatusDetails);
        return this;
    }

    public void setCrbCreditApplicationStatusDetails(String crbCreditApplicationStatusDetails) {
        this.crbCreditApplicationStatusDetails = crbCreditApplicationStatusDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbCreditApplicationStatus)) {
            return false;
        }
        return id != null && id.equals(((CrbCreditApplicationStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbCreditApplicationStatus{" +
            "id=" + getId() +
            ", crbCreditApplicationStatusTypeCode='" + getCrbCreditApplicationStatusTypeCode() + "'" +
            ", crbCreditApplicationStatusType='" + getCrbCreditApplicationStatusType() + "'" +
            ", crbCreditApplicationStatusDetails='" + getCrbCreditApplicationStatusDetails() + "'" +
            "}";
    }
}
