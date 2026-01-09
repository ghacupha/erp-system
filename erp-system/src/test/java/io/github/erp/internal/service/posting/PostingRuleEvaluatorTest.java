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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.domain.TransactionAccountPostingRule;
import io.github.erp.domain.TransactionAccountPostingRuleCondition;
import io.github.erp.domain.TransactionAccountPostingRuleTemplate;
import io.github.erp.domain.TransactionDetails;
import io.github.erp.domain.enumeration.PostingRuleConditionOperator;
import io.github.erp.internal.service.leases.trxAccounts.TransactionEntryIdGenerator;
import io.github.erp.repository.TransactionAccountPostingRuleRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostingRuleEvaluatorTest {

    @Mock
    private TransactionAccountPostingRuleRepository postingRuleRepository;

    @Mock
    private TransactionEntryIdGenerator transactionEntryIdGenerator;

    private PostingRuleEvaluator postingRuleEvaluator;

    @BeforeEach
    void setUp() {
        postingRuleEvaluator = new PostingRuleEvaluator(postingRuleRepository, transactionEntryIdGenerator);
    }

    @Test
    void evaluateBuildsTransactionDetailsFromTemplates() {
        TransactionAccountPostingRule rule = new TransactionAccountPostingRule();
        rule.setName("Lease repayment rule");
        rule.setModule("LEASE");
        rule.setEventType("LEASE_REPAYMENT");

        TransactionAccount debitAccount = new TransactionAccount().id(1L);
        TransactionAccount creditAccount = new TransactionAccount().id(2L);

        TransactionAccountPostingRuleTemplate template = new TransactionAccountPostingRuleTemplate();
        template.setDebitAccount(debitAccount);
        template.setCreditAccount(creditAccount);
        template.setAmountMultiplier(BigDecimal.ONE);
        template.setPostingRule(rule);
        rule.setPostingRuleTemplates(Set.of(template));

        TransactionAccountPostingRuleCondition condition = new TransactionAccountPostingRuleCondition();
        condition.setConditionKey("leaseContractId");
        condition.setConditionOperator(PostingRuleConditionOperator.EQUALS);
        condition.setConditionValue("42");
        condition.setPostingRule(rule);
        rule.setPostingRuleConditions(Set.of(condition));

        when(postingRuleRepository.findByModuleAndEventType("LEASE", "LEASE_REPAYMENT")).thenReturn(List.of(rule));
        when(transactionEntryIdGenerator.nextEntryId()).thenReturn(100L);

        PostingContext context = PostingContext
            .builder()
            .module("LEASE")
            .eventType("LEASE_REPAYMENT")
            .transactionType("Lease Repayment")
            .transactionDate(LocalDate.of(2024, 1, 1))
            .description("Lease repayment")
            .amount(new BigDecimal("100.00"))
            .postingId(UUID.randomUUID())
            .postedBy(new ApplicationUser().id(7L))
            .attribute("leaseContractId", "42")
            .build();

        List<TransactionDetails> details = postingRuleEvaluator.evaluate(context);

        assertThat(details).hasSize(1);
        TransactionDetails detail = details.get(0);
        assertThat(detail.getEntryId()).isEqualTo(100L);
        assertThat(detail.getDebitAccount()).isSameAs(debitAccount);
        assertThat(detail.getCreditAccount()).isSameAs(creditAccount);
        assertThat(detail.getAmount()).isEqualByComparingTo("100.00");
    }
}
