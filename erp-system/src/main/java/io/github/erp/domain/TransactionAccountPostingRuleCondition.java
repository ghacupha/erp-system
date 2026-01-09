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
import io.github.erp.domain.enumeration.PostingRuleConditionOperator;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransactionAccountPostingRuleCondition.
 */
@Entity
@Table(name = "trx_account_posting_rule_condition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "transactionaccountpostingrulecondition")
public class TransactionAccountPostingRuleCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "condition_key", nullable = false)
    private String conditionKey;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "condition_operator", nullable = false)
    private PostingRuleConditionOperator conditionOperator;

    @NotNull
    @Column(name = "condition_value", nullable = false)
    private String conditionValue;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "debitAccountType",
            "creditAccountType",
            "transactionContext",
            "postingRuleConditions",
            "postingRuleTemplates",
        },
        allowSetters = true
    )
    private TransactionAccountPostingRule postingRule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransactionAccountPostingRuleCondition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConditionKey() {
        return this.conditionKey;
    }

    public TransactionAccountPostingRuleCondition conditionKey(String conditionKey) {
        this.setConditionKey(conditionKey);
        return this;
    }

    public void setConditionKey(String conditionKey) {
        this.conditionKey = conditionKey;
    }

    public PostingRuleConditionOperator getConditionOperator() {
        return this.conditionOperator;
    }

    public TransactionAccountPostingRuleCondition conditionOperator(PostingRuleConditionOperator conditionOperator) {
        this.setConditionOperator(conditionOperator);
        return this;
    }

    public void setConditionOperator(PostingRuleConditionOperator conditionOperator) {
        this.conditionOperator = conditionOperator;
    }

    public String getConditionValue() {
        return this.conditionValue;
    }

    public TransactionAccountPostingRuleCondition conditionValue(String conditionValue) {
        this.setConditionValue(conditionValue);
        return this;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }

    public TransactionAccountPostingRule getPostingRule() {
        return this.postingRule;
    }

    public void setPostingRule(TransactionAccountPostingRule transactionAccountPostingRule) {
        this.postingRule = transactionAccountPostingRule;
    }

    public TransactionAccountPostingRuleCondition postingRule(TransactionAccountPostingRule transactionAccountPostingRule) {
        this.setPostingRule(transactionAccountPostingRule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionAccountPostingRuleCondition)) {
            return false;
        }
        return id != null && id.equals(((TransactionAccountPostingRuleCondition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountPostingRuleCondition{" +
            "id=" + getId() +
            ", conditionKey='" + getConditionKey() + "'" +
            ", conditionOperator='" + getConditionOperator() + "'" +
            ", conditionValue='" + getConditionValue() + "'" +
            "}";
    }
}
