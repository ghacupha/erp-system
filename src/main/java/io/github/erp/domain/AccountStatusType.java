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
import io.github.erp.domain.enumeration.AccountStatusTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A AccountStatusType.
 */
@Entity
@Table(name = "account_status_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accountstatustype")
public class AccountStatusType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "account_status_code", nullable = false, unique = true)
    private String accountStatusCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status_type", nullable = false)
    private AccountStatusTypes accountStatusType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "account_status_description")
    private String accountStatusDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountStatusType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountStatusCode() {
        return this.accountStatusCode;
    }

    public AccountStatusType accountStatusCode(String accountStatusCode) {
        this.setAccountStatusCode(accountStatusCode);
        return this;
    }

    public void setAccountStatusCode(String accountStatusCode) {
        this.accountStatusCode = accountStatusCode;
    }

    public AccountStatusTypes getAccountStatusType() {
        return this.accountStatusType;
    }

    public AccountStatusType accountStatusType(AccountStatusTypes accountStatusType) {
        this.setAccountStatusType(accountStatusType);
        return this;
    }

    public void setAccountStatusType(AccountStatusTypes accountStatusType) {
        this.accountStatusType = accountStatusType;
    }

    public String getAccountStatusDescription() {
        return this.accountStatusDescription;
    }

    public AccountStatusType accountStatusDescription(String accountStatusDescription) {
        this.setAccountStatusDescription(accountStatusDescription);
        return this;
    }

    public void setAccountStatusDescription(String accountStatusDescription) {
        this.accountStatusDescription = accountStatusDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountStatusType)) {
            return false;
        }
        return id != null && id.equals(((AccountStatusType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountStatusType{" +
            "id=" + getId() +
            ", accountStatusCode='" + getAccountStatusCode() + "'" +
            ", accountStatusType='" + getAccountStatusType() + "'" +
            ", accountStatusDescription='" + getAccountStatusDescription() + "'" +
            "}";
    }
}
