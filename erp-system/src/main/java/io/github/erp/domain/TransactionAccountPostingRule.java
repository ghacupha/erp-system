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
    @JsonIgnoreProperties(value = { "placeholders", "accountLedger" }, allowSetters = true)
    private TransactionAccountCategory debitAccountType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "accountLedger" }, allowSetters = true)
    private TransactionAccountCategory creditAccountType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Placeholder transactionContext;

    @Column(name = "module")
    private String module;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "variance_type")
    private String varianceType;

    @Column(name = "invoice_timing")
    private String invoiceTiming;

    @OneToMany(mappedBy = "postingRule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "postingRule" }, allowSetters = true)
    private Set<TransactionAccountPostingRuleCondition> postingRuleConditions = new HashSet<>();

    @OneToMany(mappedBy = "postingRule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "postingRule" }, allowSetters = true)
    private Set<TransactionAccountPostingRuleTemplate> postingRuleTemplates = new HashSet<>();

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

    public String getModule() {
        return this.module;
    }

    public TransactionAccountPostingRule module(String module) {
        this.setModule(module);
        return this;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getEventType() {
        return this.eventType;
    }

    public TransactionAccountPostingRule eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getVarianceType() {
        return this.varianceType;
    }

    public TransactionAccountPostingRule varianceType(String varianceType) {
        this.setVarianceType(varianceType);
        return this;
    }

    public void setVarianceType(String varianceType) {
        this.varianceType = varianceType;
    }

    public String getInvoiceTiming() {
        return this.invoiceTiming;
    }

    public TransactionAccountPostingRule invoiceTiming(String invoiceTiming) {
        this.setInvoiceTiming(invoiceTiming);
        return this;
    }

    public void setInvoiceTiming(String invoiceTiming) {
        this.invoiceTiming = invoiceTiming;
    }

    public Set<TransactionAccountPostingRuleCondition> getPostingRuleConditions() {
        return this.postingRuleConditions;
    }

    public void setPostingRuleConditions(Set<TransactionAccountPostingRuleCondition> postingRuleConditions) {
        if (this.postingRuleConditions != null) {
            this.postingRuleConditions.forEach(condition -> condition.setPostingRule(null));
        }
        if (postingRuleConditions != null) {
            postingRuleConditions.forEach(condition -> condition.setPostingRule(this));
        }
        this.postingRuleConditions = postingRuleConditions;
    }

    public TransactionAccountPostingRule postingRuleConditions(Set<TransactionAccountPostingRuleCondition> postingRuleConditions) {
        this.setPostingRuleConditions(postingRuleConditions);
        return this;
    }

    public TransactionAccountPostingRule addPostingRuleCondition(TransactionAccountPostingRuleCondition condition) {
        this.postingRuleConditions.add(condition);
        condition.setPostingRule(this);
        return this;
    }

    public TransactionAccountPostingRule removePostingRuleCondition(TransactionAccountPostingRuleCondition condition) {
        this.postingRuleConditions.remove(condition);
        condition.setPostingRule(null);
        return this;
    }

    public Set<TransactionAccountPostingRuleTemplate> getPostingRuleTemplates() {
        return this.postingRuleTemplates;
    }

    public void setPostingRuleTemplates(Set<TransactionAccountPostingRuleTemplate> postingRuleTemplates) {
        if (this.postingRuleTemplates != null) {
            this.postingRuleTemplates.forEach(template -> template.setPostingRule(null));
        }
        if (postingRuleTemplates != null) {
            postingRuleTemplates.forEach(template -> template.setPostingRule(this));
        }
        this.postingRuleTemplates = postingRuleTemplates;
    }

    public TransactionAccountPostingRule postingRuleTemplates(Set<TransactionAccountPostingRuleTemplate> postingRuleTemplates) {
        this.setPostingRuleTemplates(postingRuleTemplates);
        return this;
    }

    public TransactionAccountPostingRule addPostingRuleTemplate(TransactionAccountPostingRuleTemplate template) {
        this.postingRuleTemplates.add(template);
        template.setPostingRule(this);
        return this;
    }

    public TransactionAccountPostingRule removePostingRuleTemplate(TransactionAccountPostingRuleTemplate template) {
        this.postingRuleTemplates.remove(template);
        template.setPostingRule(null);
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
            ", module='" + getModule() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", varianceType='" + getVarianceType() + "'" +
            ", invoiceTiming='" + getInvoiceTiming() + "'" +
            "}";
    }
}
