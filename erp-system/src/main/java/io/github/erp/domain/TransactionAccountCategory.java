package io.github.erp.domain;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.enumeration.transactionAccountPostingTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransactionAccountCategory.
 */
@Entity
@Table(name = "transaction_account_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "transactionaccountcategory")
public class TransactionAccountCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_account_posting_type", nullable = false)
    private transactionAccountPostingTypes transactionAccountPostingType;

    @ManyToMany
    @JoinTable(
        name = "rel_transaction_account_category__placeholder",
        joinColumns = @JoinColumn(name = "transaction_account_category_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private TransactionAccountLedger accountLedger;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransactionAccountCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TransactionAccountCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public transactionAccountPostingTypes getTransactionAccountPostingType() {
        return this.transactionAccountPostingType;
    }

    public TransactionAccountCategory transactionAccountPostingType(transactionAccountPostingTypes transactionAccountPostingType) {
        this.setTransactionAccountPostingType(transactionAccountPostingType);
        return this;
    }

    public void setTransactionAccountPostingType(transactionAccountPostingTypes transactionAccountPostingType) {
        this.transactionAccountPostingType = transactionAccountPostingType;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public TransactionAccountCategory placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public TransactionAccountCategory addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public TransactionAccountCategory removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public TransactionAccountLedger getAccountLedger() {
        return this.accountLedger;
    }

    public void setAccountLedger(TransactionAccountLedger transactionAccountLedger) {
        this.accountLedger = transactionAccountLedger;
    }

    public TransactionAccountCategory accountLedger(TransactionAccountLedger transactionAccountLedger) {
        this.setAccountLedger(transactionAccountLedger);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionAccountCategory)) {
            return false;
        }
        return id != null && id.equals(((TransactionAccountCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", transactionAccountPostingType='" + getTransactionAccountPostingType() + "'" +
            "}";
    }
}
