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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransactionAccountPostingRule.
 */
@Entity
@Table(name = "trx_account_posting_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "transactionaccountpostingrule")
public class TransactionAccountPostingRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "identifier", unique = true)
    private UUID identifier;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private TransactionAccountCategory debitAccountType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private TransactionAccountCategory creditAccountType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Placeholder transactionContext;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransactionAccountPostingRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TransactionAccountPostingRule name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getIdentifier() {
        return this.identifier;
    }

    public TransactionAccountPostingRule identifier(UUID identifier) {
        this.setIdentifier(identifier);
        return this;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public TransactionAccountCategory getDebitAccountType() {
        return this.debitAccountType;
    }

    public void setDebitAccountType(TransactionAccountCategory transactionAccountCategory) {
        this.debitAccountType = transactionAccountCategory;
    }

    public TransactionAccountPostingRule debitAccountType(TransactionAccountCategory transactionAccountCategory) {
        this.setDebitAccountType(transactionAccountCategory);
        return this;
    }

    public TransactionAccountCategory getCreditAccountType() {
        return this.creditAccountType;
    }

    public void setCreditAccountType(TransactionAccountCategory transactionAccountCategory) {
        this.creditAccountType = transactionAccountCategory;
    }

    public TransactionAccountPostingRule creditAccountType(TransactionAccountCategory transactionAccountCategory) {
        this.setCreditAccountType(transactionAccountCategory);
        return this;
    }

    public Placeholder getTransactionContext() {
        return this.transactionContext;
    }

    public void setTransactionContext(Placeholder placeholder) {
        this.transactionContext = placeholder;
    }

    public TransactionAccountPostingRule transactionContext(Placeholder placeholder) {
        this.setTransactionContext(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionAccountPostingRule)) {
            return false;
        }
        return id != null && id.equals(((TransactionAccountPostingRule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountPostingRule{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            "}";
    }
}
