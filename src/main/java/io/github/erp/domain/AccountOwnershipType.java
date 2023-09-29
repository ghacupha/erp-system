package io.github.erp.domain;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
 * A AccountOwnershipType.
 */
@Entity
@Table(name = "account_ownership_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accountownershiptype")
public class AccountOwnershipType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "account_ownership_type_code", nullable = false, unique = true)
    private String accountOwnershipTypeCode;

    @NotNull
    @Column(name = "account_ownership_type", nullable = false)
    private String accountOwnershipType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountOwnershipType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountOwnershipTypeCode() {
        return this.accountOwnershipTypeCode;
    }

    public AccountOwnershipType accountOwnershipTypeCode(String accountOwnershipTypeCode) {
        this.setAccountOwnershipTypeCode(accountOwnershipTypeCode);
        return this;
    }

    public void setAccountOwnershipTypeCode(String accountOwnershipTypeCode) {
        this.accountOwnershipTypeCode = accountOwnershipTypeCode;
    }

    public String getAccountOwnershipType() {
        return this.accountOwnershipType;
    }

    public AccountOwnershipType accountOwnershipType(String accountOwnershipType) {
        this.setAccountOwnershipType(accountOwnershipType);
        return this;
    }

    public void setAccountOwnershipType(String accountOwnershipType) {
        this.accountOwnershipType = accountOwnershipType;
    }

    public String getDescription() {
        return this.description;
    }

    public AccountOwnershipType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountOwnershipType)) {
            return false;
        }
        return id != null && id.equals(((AccountOwnershipType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountOwnershipType{" +
            "id=" + getId() +
            ", accountOwnershipTypeCode='" + getAccountOwnershipTypeCode() + "'" +
            ", accountOwnershipType='" + getAccountOwnershipType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
