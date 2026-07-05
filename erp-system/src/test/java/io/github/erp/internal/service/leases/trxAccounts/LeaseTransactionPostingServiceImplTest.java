package io.github.erp.internal.service.leases.trxAccounts;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.domain.LeaseRepaymentPeriod;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.domain.TransactionDetails;
import io.github.erp.internal.repository.InternalApplicationUserRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityScheduleItemRepository;
import io.github.erp.internal.repository.InternalRouDepreciationEntryRepository;
import io.github.erp.internal.repository.InternalRouInitialDirectCostRepository;
import io.github.erp.internal.repository.InternalTransactionDetailsRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.github.erp.internal.service.posting.PostingRuleEvaluator;
import io.github.erp.internal.service.posting.PostingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeaseTransactionPostingServiceImplTest {

    @Mock
    private InternalTransactionDetailsRepository transactionDetailsRepository;

    @Mock
    private InternalLeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository;

    @Mock
    private InternalLeaseLiabilityRepository leaseLiabilityRepository;

    @Mock
    private InternalRouInitialDirectCostRepository rouInitialDirectCostRepository;

    @Mock
    private InternalRouDepreciationEntryRepository rouDepreciationEntryRepository;

    @Mock
    private InternalApplicationUserRepository applicationUserRepository;

    private LeaseTransactionPostingServiceImpl leaseTransactionPostingService;

    @Mock
    private PostingRuleEvaluator postingRuleEvaluator;

    @BeforeEach
    void setUp() {
        leaseTransactionPostingService = new LeaseTransactionPostingServiceImpl(
            transactionDetailsRepository,
            leaseLiabilityScheduleItemRepository,
            leaseLiabilityRepository,
            rouInitialDirectCostRepository,
            rouDepreciationEntryRepository,
            applicationUserRepository,
            postingRuleEvaluator
        );
    }

    @Test
    void postLeaseRepaymentBuildsDetailsFromRules() {
        UUID requisitionId = UUID.randomUUID();
        long postedById = 10L;

        ApplicationUser postedBy = new ApplicationUser();
        postedBy.setId(postedById);

        IFRS16LeaseContract leaseContract = new IFRS16LeaseContract();
        leaseContract.setId(55L);
        leaseContract.setShortTitle("Lease-A");

        TransactionAccount leaseLiability = new TransactionAccount();
        leaseLiability.setId(1L);
        TransactionAccount lessorsAccount = new TransactionAccount();
        lessorsAccount.setId(2L);

        FiscalMonth fiscalMonth = new FiscalMonth();
        fiscalMonth.setFiscalMonthCode("YM2024-01");
        fiscalMonth.setEndDate(LocalDate.of(2024, 1, 31));

        LeaseRepaymentPeriod leasePeriod = new LeaseRepaymentPeriod();
        leasePeriod.setFiscalMonth(fiscalMonth);

        LeaseLiabilityScheduleItem scheduleItem = new LeaseLiabilityScheduleItem();
        scheduleItem.setLeaseContract(leaseContract);
        scheduleItem.setLeasePeriod(leasePeriod);
        scheduleItem.setCashPayment(new BigDecimal("1200.00"));

        TransactionDetails details = new TransactionDetails();
        details.setEntryId(991L);
        details.setPostingId(requisitionId);
        details.setTransactionType("Lease Repayment");
        details.setAmount(new BigDecimal("1200.00"));
        details.setDescription("Lease-A 2024-01REPAYMENT");
        details.setTransactionDate(LocalDate.of(2024, 1, 31));
        details.setDebitAccount(leaseLiability);
        details.setCreditAccount(lessorsAccount);

        when(applicationUserRepository.findById(postedById)).thenReturn(Optional.of(postedBy));
        when(leaseLiabilityScheduleItemRepository.findAllWithLeaseContractAndPeriod()).thenReturn(List.of(scheduleItem));
        when(postingRuleEvaluator.evaluate(any())).thenReturn(List.of(details));
        when(transactionDetailsRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        leaseTransactionPostingService.postLeaseRepayment(requisitionId, postedById);

        ArgumentCaptor<PostingContext> contextCaptor = ArgumentCaptor.forClass(PostingContext.class);
        verify(postingRuleEvaluator).evaluate(contextCaptor.capture());
        PostingContext context = contextCaptor.getValue();
        assertThat(context.getModule()).isEqualTo("LEASE");
        assertThat(context.getEventType()).isEqualTo("LEASE_REPAYMENT");
        assertThat(context.getTransactionType()).isEqualTo("Lease Repayment");
        assertThat(context.getTransactionDate()).isEqualTo(LocalDate.of(2024, 1, 31));
        assertThat(context.getDescription()).isEqualTo("Lease-A 2024-01REPAYMENT");
        assertThat(context.getAmount()).isEqualByComparingTo("1200.00");
        assertThat(context.getPostingId()).isEqualTo(requisitionId);
        assertThat(context.getPostedBy()).isSameAs(postedBy);
        assertThat(context.getAttributes()).containsEntry("leaseContractId", "55");

        ArgumentCaptor<List<TransactionDetails>> captor = ArgumentCaptor.forClass(List.class);
        verify(transactionDetailsRepository).saveAll(captor.capture());

        List<TransactionDetails> saved = captor.getValue();
        assertThat(saved).hasSize(1);
        assertThat(saved.get(0)).isSameAs(details);
        assertThat(saved.get(0).getDebitAccount()).isEqualTo(leaseLiability);
        assertThat(saved.get(0).getCreditAccount()).isEqualTo(lessorsAccount);
    }
}
