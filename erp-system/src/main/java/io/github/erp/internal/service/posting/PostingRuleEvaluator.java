package io.github.erp.internal.service.posting;

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
import io.github.erp.domain.TransactionAccountPostingRule;
import io.github.erp.domain.TransactionAccountPostingRuleCondition;
import io.github.erp.domain.TransactionAccountPostingRuleTemplate;
import io.github.erp.domain.TransactionDetails;
import io.github.erp.domain.enumeration.PostingRuleConditionOperator;
import io.github.erp.internal.service.leases.trxAccounts.TransactionEntryIdGenerator;
import io.github.erp.repository.TransactionAccountPostingRuleRepository;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostingRuleEvaluator {

    private final TransactionAccountPostingRuleRepository postingRuleRepository;
    private final TransactionEntryIdGenerator transactionEntryIdGenerator;

    public PostingRuleEvaluator(
        TransactionAccountPostingRuleRepository postingRuleRepository,
        TransactionEntryIdGenerator transactionEntryIdGenerator
    ) {
        this.postingRuleRepository = postingRuleRepository;
        this.transactionEntryIdGenerator = transactionEntryIdGenerator;
    }

    public List<TransactionDetails> evaluate(PostingContext context) {
        validateContext(context);
        List<TransactionAccountPostingRule> candidates = postingRuleRepository.findByModuleAndEventType(
            context.getModule(),
            context.getEventType()
        );
        TransactionAccountPostingRule selectedRule = candidates
            .stream()
            .filter(rule -> matchesRule(rule, context))
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "No posting rule found for module " + context.getModule() + " and event " + context.getEventType()
                    )
            );
        Set<TransactionAccountPostingRuleTemplate> templates = Optional
            .ofNullable(selectedRule.getPostingRuleTemplates())
            .orElse(Collections.emptySet());
        if (templates.isEmpty()) {
            throw new IllegalStateException("Posting rule " + selectedRule.getName() + " has no templates configured.");
        }
        return templates.stream().map(template -> buildDetail(template, context)).collect(Collectors.toList());
    }

    private void validateContext(PostingContext context) {
        Objects.requireNonNull(context, "Posting context is required.");
        Objects.requireNonNull(context.getModule(), "Posting context module is required.");
        Objects.requireNonNull(context.getEventType(), "Posting context event type is required.");
        Objects.requireNonNull(context.getAmount(), "Posting context amount is required.");
        Objects.requireNonNull(context.getTransactionDate(), "Posting context transaction date is required.");
        Objects.requireNonNull(context.getPostedBy(), "Posting context posted by is required.");
    }

    private boolean matchesRule(TransactionAccountPostingRule rule, PostingContext context) {
        if (!matchesOptional(rule.getVarianceType(), context.getVarianceType())) {
            return false;
        }
        if (!matchesOptional(rule.getInvoiceTiming(), context.getInvoiceTiming())) {
            return false;
        }
        if (rule.getTransactionContext() != null) {
            if (context.getPlaceholders() == null) {
                return false;
            }
            boolean matches = context
                .getPlaceholders()
                .stream()
                .anyMatch(placeholder -> placeholder.getId().equals(rule.getTransactionContext().getId()));
            if (!matches) {
                return false;
            }
        }
        for (TransactionAccountPostingRuleCondition condition : Optional.ofNullable(rule.getPostingRuleConditions()).orElse(Collections.emptySet())) {
            if (!matchesCondition(condition, context.getAttributes())) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesOptional(String ruleValue, String contextValue) {
        if (ruleValue == null || ruleValue.isBlank()) {
            return true;
        }
        return contextValue != null && ruleValue.equalsIgnoreCase(contextValue);
    }

    private boolean matchesCondition(TransactionAccountPostingRuleCondition condition, Map<String, String> attributes) {
        String contextValue = attributes.get(condition.getConditionKey());
        if (contextValue == null) {
            return false;
        }
        String expectedValue = condition.getConditionValue();
        PostingRuleConditionOperator operator = condition.getConditionOperator();
        if (operator == PostingRuleConditionOperator.EQUALS) {
            return contextValue.equalsIgnoreCase(expectedValue);
        }
        if (operator == PostingRuleConditionOperator.NOT_EQUALS) {
            return !contextValue.equalsIgnoreCase(expectedValue);
        }
        if (operator == PostingRuleConditionOperator.CONTAINS) {
            return contextValue.toLowerCase().contains(expectedValue.toLowerCase());
        }
        return false;
    }

    private TransactionDetails buildDetail(TransactionAccountPostingRuleTemplate template, PostingContext context) {
        if (template.getDebitAccount() == null || template.getCreditAccount() == null) {
            throw new IllegalStateException("Posting template missing debit or credit account.");
        }
        TransactionDetails details = new TransactionDetails();
        details.setEntryId(transactionEntryIdGenerator.nextEntryId());
        details.setPostingId(context.getPostingId());
        details.setPostedBy(context.getPostedBy());
        details.setTransactionType(context.getTransactionType());
        details.setTransactionDate(context.getTransactionDate());
        details.setDescription(template.getLineDescription() != null ? template.getLineDescription() : context.getDescription());
        details.setAmount(applyMultiplier(context.getAmount(), template.getAmountMultiplier()));
        details.setDebitAccount(template.getDebitAccount());
        details.setCreditAccount(template.getCreditAccount());
        details.setIsDeleted(Boolean.FALSE);
        details.setCreatedAt(ZonedDateTime.now());
        if (context.getPlaceholders() != null) {
            details.setPlaceholders(context.getPlaceholders());
        }
        return details;
    }

    private BigDecimal applyMultiplier(BigDecimal amount, BigDecimal multiplier) {
        if (multiplier == null) {
            return amount;
        }
        return amount.multiply(multiplier);
    }
}
