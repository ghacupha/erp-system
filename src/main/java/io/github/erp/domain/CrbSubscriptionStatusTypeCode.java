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
 * A CrbSubscriptionStatusTypeCode.
 */
@Entity
@Table(name = "crb_subscription_status_type_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbsubscriptionstatustypecode")
public class CrbSubscriptionStatusTypeCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "subscription_status_type_code", nullable = false, unique = true)
    private String subscriptionStatusTypeCode;

    @NotNull
    @Column(name = "subscription_status_type", nullable = false, unique = true)
    private String subscriptionStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "subscription_status_type_description")
    private String subscriptionStatusTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbSubscriptionStatusTypeCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubscriptionStatusTypeCode() {
        return this.subscriptionStatusTypeCode;
    }

    public CrbSubscriptionStatusTypeCode subscriptionStatusTypeCode(String subscriptionStatusTypeCode) {
        this.setSubscriptionStatusTypeCode(subscriptionStatusTypeCode);
        return this;
    }

    public void setSubscriptionStatusTypeCode(String subscriptionStatusTypeCode) {
        this.subscriptionStatusTypeCode = subscriptionStatusTypeCode;
    }

    public String getSubscriptionStatusType() {
        return this.subscriptionStatusType;
    }

    public CrbSubscriptionStatusTypeCode subscriptionStatusType(String subscriptionStatusType) {
        this.setSubscriptionStatusType(subscriptionStatusType);
        return this;
    }

    public void setSubscriptionStatusType(String subscriptionStatusType) {
        this.subscriptionStatusType = subscriptionStatusType;
    }

    public String getSubscriptionStatusTypeDescription() {
        return this.subscriptionStatusTypeDescription;
    }

    public CrbSubscriptionStatusTypeCode subscriptionStatusTypeDescription(String subscriptionStatusTypeDescription) {
        this.setSubscriptionStatusTypeDescription(subscriptionStatusTypeDescription);
        return this;
    }

    public void setSubscriptionStatusTypeDescription(String subscriptionStatusTypeDescription) {
        this.subscriptionStatusTypeDescription = subscriptionStatusTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbSubscriptionStatusTypeCode)) {
            return false;
        }
        return id != null && id.equals(((CrbSubscriptionStatusTypeCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbSubscriptionStatusTypeCode{" +
            "id=" + getId() +
            ", subscriptionStatusTypeCode='" + getSubscriptionStatusTypeCode() + "'" +
            ", subscriptionStatusType='" + getSubscriptionStatusType() + "'" +
            ", subscriptionStatusTypeDescription='" + getSubscriptionStatusTypeDescription() + "'" +
            "}";
    }
}
