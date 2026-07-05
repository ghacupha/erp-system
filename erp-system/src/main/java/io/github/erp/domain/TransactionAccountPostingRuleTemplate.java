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
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransactionAccountPostingRuleTemplate.
 */
@Entity
@Table(name = "trx_account_posting_rule_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "transactionaccountpostingruletemplate")
public class TransactionAccountPostingRuleTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "line_description")
    private String lineDescription;

    @Column(name = "amount_multiplier", precision = 21, scale = 6)
    private BigDecimal amountMultiplier;

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

    public TransactionAccountPostingRuleTemplate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLineDescription() {
        return this.lineDescription;
    }

    public TransactionAccountPostingRuleTemplate lineDescription(String lineDescription) {
        this.setLineDescription(lineDescription);
        return this;
    }

    public void setLineDescription(String lineDescription) {
        this.lineDescription = lineDescription;
    }

    public BigDecimal getAmountMultiplier() {
        return this.amountMultiplier;
    }

    public TransactionAccountPostingRuleTemplate amountMultiplier(BigDecimal amountMultiplier) {
        this.setAmountMultiplier(amountMultiplier);
        return this;
    }

    public void setAmountMultiplier(BigDecimal amountMultiplier) {
        this.amountMultiplier = amountMultiplier;
    }

    public TransactionAccount getDebitAccount() {
        return this.debitAccount;
    }

    public void setDebitAccount(TransactionAccount transactionAccount) {
        this.debitAccount = transactionAccount;
    }

    public TransactionAccountPostingRuleTemplate debitAccount(TransactionAccount transactionAccount) {
        this.setDebitAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getCreditAccount() {
        return this.creditAccount;
    }

    public void setCreditAccount(TransactionAccount transactionAccount) {
        this.creditAccount = transactionAccount;
    }

    public TransactionAccountPostingRuleTemplate creditAccount(TransactionAccount transactionAccount) {
        this.setCreditAccount(transactionAccount);
        return this;
    }

    public TransactionAccountPostingRule getPostingRule() {
        return this.postingRule;
    }

    public void setPostingRule(TransactionAccountPostingRule transactionAccountPostingRule) {
        this.postingRule = transactionAccountPostingRule;
    }

    public TransactionAccountPostingRuleTemplate postingRule(TransactionAccountPostingRule transactionAccountPostingRule) {
        this.setPostingRule(transactionAccountPostingRule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionAccountPostingRuleTemplate)) {
            return false;
        }
        return id != null && id.equals(((TransactionAccountPostingRuleTemplate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountPostingRuleTemplate{" +
            "id=" + getId() +
            ", lineDescription='" + getLineDescription() + "'" +
            ", amountMultiplier=" + getAmountMultiplier() +
            "}";
    }
}
