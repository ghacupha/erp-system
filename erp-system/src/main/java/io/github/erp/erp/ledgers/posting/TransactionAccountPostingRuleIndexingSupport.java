package io.github.erp.erp.ledgers.posting;

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

import io.github.erp.domain.Placeholder;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.domain.TransactionAccountCategory;
import io.github.erp.domain.TransactionAccountPostingRule;
import io.github.erp.domain.TransactionAccountPostingRuleCondition;
import io.github.erp.domain.TransactionAccountPostingRuleTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionAccountPostingRuleIndexingSupport {

    public TransactionAccountPostingRule sanitizePostingRule(TransactionAccountPostingRule source) {
        if (source == null) {
            return null;
        }

        TransactionAccountPostingRule sanitized = new TransactionAccountPostingRule();
        sanitized.setId(source.getId());
        sanitized.setName(source.getName());
        sanitized.setIdentifier(source.getIdentifier());
        sanitized.setModule(source.getModule());
        sanitized.setEventType(source.getEventType());
        sanitized.setVarianceType(source.getVarianceType());
        sanitized.setInvoiceTiming(source.getInvoiceTiming());
        sanitized.setDebitAccountType(sanitizeCategory(source.getDebitAccountType()));
        sanitized.setCreditAccountType(sanitizeCategory(source.getCreditAccountType()));
        sanitized.setTransactionContext(sanitizePlaceholder(source.getTransactionContext()));
        return sanitized;
    }

    public TransactionAccountPostingRuleTemplate sanitizeTemplate(TransactionAccountPostingRuleTemplate source) {
        if (source == null) {
            return null;
        }

        TransactionAccountPostingRuleTemplate sanitized = new TransactionAccountPostingRuleTemplate();
        sanitized.setId(source.getId());
        sanitized.setLineDescription(source.getLineDescription());
        sanitized.setAmountMultiplier(source.getAmountMultiplier());
        sanitized.setDebitAccount(sanitizeAccount(source.getDebitAccount()));
        sanitized.setCreditAccount(sanitizeAccount(source.getCreditAccount()));
        return sanitized;
    }

    public TransactionAccountPostingRuleCondition sanitizeCondition(TransactionAccountPostingRuleCondition source) {
        if (source == null) {
            return null;
        }

        TransactionAccountPostingRuleCondition sanitized = new TransactionAccountPostingRuleCondition();
        sanitized.setId(source.getId());
        sanitized.setConditionKey(source.getConditionKey());
        sanitized.setConditionOperator(source.getConditionOperator());
        sanitized.setConditionValue(source.getConditionValue());
        return sanitized;
    }

    private TransactionAccountCategory sanitizeCategory(TransactionAccountCategory source) {
        if (source == null) {
            return null;
        }

        TransactionAccountCategory sanitized = new TransactionAccountCategory();
        sanitized.setId(source.getId());
        sanitized.setName(source.getName());
        sanitized.setTransactionAccountPostingType(source.getTransactionAccountPostingType());
        return sanitized;
    }

    private TransactionAccount sanitizeAccount(TransactionAccount source) {
        if (source == null) {
            return null;
        }

        TransactionAccount sanitized = new TransactionAccount();
        sanitized.setId(source.getId());
        sanitized.setAccountName(source.getAccountName());
        sanitized.setAccountNumber(source.getAccountNumber());
        return sanitized;
    }

    private Placeholder sanitizePlaceholder(Placeholder source) {
        if (source == null) {
            return null;
        }

        Placeholder sanitized = new Placeholder();
        sanitized.setId(source.getId());
        sanitized.setDescription(source.getDescription());
        return sanitized;
    }
}
