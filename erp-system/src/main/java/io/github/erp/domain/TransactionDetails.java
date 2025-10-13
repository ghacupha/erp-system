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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransactionDetails.
 */
@Entity
@Table(name = "transaction_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "transactiondetails")
public class TransactionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "entry_id", nullable = false, unique = true)
    private Long entryId;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "posting_id")
    private UUID postingId;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "modified_at")
    private ZonedDateTime modifiedAt;

    @Column(name = "transaction_type")
    private String transactionType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount debitAccount;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount creditAccount;

    @ManyToMany
    @JoinTable(
        name = "rel_transaction_details__placeholder",
        joinColumns = @JoinColumn(name = "transaction_details_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser postedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransactionDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEntryId() {
        return this.entryId;
    }

    public TransactionDetails entryId(Long entryId) {
        this.setEntryId(entryId);
        return this;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public TransactionDetails transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return this.description;
    }

    public TransactionDetails description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public TransactionDetails amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public TransactionDetails isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UUID getPostingId() {
        return this.postingId;
    }

    public TransactionDetails postingId(UUID postingId) {
        this.setPostingId(postingId);
        return this;
    }

    public void setPostingId(UUID postingId) {
        this.postingId = postingId;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public TransactionDetails createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getModifiedAt() {
        return this.modifiedAt;
    }

    public TransactionDetails modifiedAt(ZonedDateTime modifiedAt) {
        this.setModifiedAt(modifiedAt);
        return this;
    }

    public void setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public TransactionDetails transactionType(String transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionAccount getDebitAccount() {
        return this.debitAccount;
    }

    public void setDebitAccount(TransactionAccount transactionAccount) {
        this.debitAccount = transactionAccount;
    }

    public TransactionDetails debitAccount(TransactionAccount transactionAccount) {
        this.setDebitAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getCreditAccount() {
        return this.creditAccount;
    }

    public void setCreditAccount(TransactionAccount transactionAccount) {
        this.creditAccount = transactionAccount;
    }

    public TransactionDetails creditAccount(TransactionAccount transactionAccount) {
        this.setCreditAccount(transactionAccount);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public TransactionDetails placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public TransactionDetails addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public TransactionDetails removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public ApplicationUser getPostedBy() {
        return this.postedBy;
    }

    public void setPostedBy(ApplicationUser applicationUser) {
        this.postedBy = applicationUser;
    }

    public TransactionDetails postedBy(ApplicationUser applicationUser) {
        this.setPostedBy(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDetails)) {
            return false;
        }
        return id != null && id.equals(((TransactionDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionDetails{" +
            "id=" + getId() +
            ", entryId=" + getEntryId() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", postingId='" + getPostingId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            "}";
    }
}
