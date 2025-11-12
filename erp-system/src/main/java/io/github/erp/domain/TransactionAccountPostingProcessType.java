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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransactionAccountPostingProcessType.
 */
@Entity
@Table(name = "trx_account_posting_prx_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "transactionaccountpostingprocesstype")
public class TransactionAccountPostingProcessType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "accountLedger" }, allowSetters = true)
    private TransactionAccountCategory debitAccountType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "accountLedger" }, allowSetters = true)
    private TransactionAccountCategory creditAccountType;

    @ManyToMany
    @JoinTable(
        name = "rel_trx_account_posting_prx_type__placeholder",
        joinColumns = @JoinColumn(name = "trx_account_posting_prx_type_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransactionAccountPostingProcessType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TransactionAccountPostingProcessType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransactionAccountCategory getDebitAccountType() {
        return this.debitAccountType;
    }

    public void setDebitAccountType(TransactionAccountCategory transactionAccountCategory) {
        this.debitAccountType = transactionAccountCategory;
    }

    public TransactionAccountPostingProcessType debitAccountType(TransactionAccountCategory transactionAccountCategory) {
        this.setDebitAccountType(transactionAccountCategory);
        return this;
    }

    public TransactionAccountCategory getCreditAccountType() {
        return this.creditAccountType;
    }

    public void setCreditAccountType(TransactionAccountCategory transactionAccountCategory) {
        this.creditAccountType = transactionAccountCategory;
    }

    public TransactionAccountPostingProcessType creditAccountType(TransactionAccountCategory transactionAccountCategory) {
        this.setCreditAccountType(transactionAccountCategory);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public TransactionAccountPostingProcessType placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public TransactionAccountPostingProcessType addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public TransactionAccountPostingProcessType removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionAccountPostingProcessType)) {
            return false;
        }
        return id != null && id.equals(((TransactionAccountPostingProcessType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountPostingProcessType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
