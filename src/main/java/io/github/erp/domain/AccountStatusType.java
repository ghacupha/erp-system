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
