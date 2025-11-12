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
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TARecognitionROURule.
 */
@Entity
@Table(name = "tarecognitionrourule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tarecognitionrourule")
public class TARecognitionROURule implements Serializable {

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
    @Column(name = "identifier", nullable = false, unique = true)
    private UUID identifier;

    @JsonIgnoreProperties(
        value = {
            "superintendentServiceOutlet",
            "mainDealer",
            "firstReportingPeriod",
            "lastReportingPeriod",
            "leaseContractDocument",
            "leaseContractCalculations",
            "leasePayments",
        },
        allowSetters = true
    )
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private IFRS16LeaseContract leaseContract;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount debit;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount credit;

    @ManyToMany
    @JoinTable(
        name = "rel_tarecognitionrourule__placeholder",
        joinColumns = @JoinColumn(name = "tarecognitionrourule_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TARecognitionROURule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TARecognitionROURule name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getIdentifier() {
        return this.identifier;
    }

    public TARecognitionROURule identifier(UUID identifier) {
        this.setIdentifier(identifier);
        return this;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public IFRS16LeaseContract getLeaseContract() {
        return this.leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.leaseContract = iFRS16LeaseContract;
    }

    public TARecognitionROURule leaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.setLeaseContract(iFRS16LeaseContract);
        return this;
    }

    public TransactionAccount getDebit() {
        return this.debit;
    }

    public void setDebit(TransactionAccount transactionAccount) {
        this.debit = transactionAccount;
    }

    public TARecognitionROURule debit(TransactionAccount transactionAccount) {
        this.setDebit(transactionAccount);
        return this;
    }

    public TransactionAccount getCredit() {
        return this.credit;
    }

    public void setCredit(TransactionAccount transactionAccount) {
        this.credit = transactionAccount;
    }

    public TARecognitionROURule credit(TransactionAccount transactionAccount) {
        this.setCredit(transactionAccount);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public TARecognitionROURule placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public TARecognitionROURule addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public TARecognitionROURule removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TARecognitionROURule)) {
            return false;
        }
        return id != null && id.equals(((TARecognitionROURule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TARecognitionROURule{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            "}";
    }
}
