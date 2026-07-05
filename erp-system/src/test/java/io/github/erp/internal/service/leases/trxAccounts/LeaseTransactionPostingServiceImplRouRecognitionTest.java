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
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.RouInitialDirectCost;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.domain.TransactionDetails;
import io.github.erp.internal.repository.InternalApplicationUserRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityScheduleItemRepository;
import io.github.erp.internal.repository.InternalRouDepreciationEntryRepository;
import io.github.erp.internal.repository.InternalRouInitialDirectCostRepository;
import io.github.erp.internal.repository.InternalTransactionDetailsRepository;
import io.github.erp.internal.service.posting.PostingContext;
import io.github.erp.internal.service.posting.PostingRuleEvaluator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeaseTransactionPostingServiceImplRouRecognitionTest {

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

    @Mock
    private PostingRuleEvaluator postingRuleEvaluator;

    private LeaseTransactionPostingServiceImpl leaseTransactionPostingService;

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
    void postLeaseRouRecognitionBuildsDetailsFromRules() {
        UUID requisitionId = UUID.randomUUID();
        long postedById = 10L;

        ApplicationUser postedBy = new ApplicationUser();
        postedBy.setId(postedById);

        IFRS16LeaseContract leaseContract = new IFRS16LeaseContract();
        leaseContract.setId(55L);
        leaseContract.setShortTitle("Lease-A");

        TransactionAccount rouAssetAccount = new TransactionAccount().id(1L);
        TransactionAccount rouCostOffset = new TransactionAccount().id(2L);

        RouInitialDirectCost directCost = new RouInitialDirectCost();
        directCost.setId(88L);
        directCost.setLeaseContract(leaseContract);
        directCost.setTransactionDate(LocalDate.of(2024, 2, 1));
        directCost.setCost(new BigDecimal("1800.00"));
        directCost.setReferenceNumber(123L);

        TransactionDetails details = new TransactionDetails();
        details.setEntryId(991L);
        details.setPostingId(requisitionId);
        details.setTransactionType("ROU Initial Direct Cost Recognition");
        details.setAmount(new BigDecimal("1800.00"));
        details.setDescription("Ref#123 Lease-A INITIAL DIRECT COST");
        details.setTransactionDate(LocalDate.of(2024, 2, 1));
        details.setDebitAccount(rouAssetAccount);
        details.setCreditAccount(rouCostOffset);

        when(applicationUserRepository.findById(postedById)).thenReturn(Optional.of(postedBy));
        when(rouInitialDirectCostRepository.findAllWithAccounts()).thenReturn(List.of(directCost));
        when(postingRuleEvaluator.evaluate(any())).thenReturn(List.of(details));
        when(transactionDetailsRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        leaseTransactionPostingService.postLeaseRouRecognition(requisitionId, postedById);

        ArgumentCaptor<PostingContext> contextCaptor = ArgumentCaptor.forClass(PostingContext.class);
        verify(postingRuleEvaluator).evaluate(contextCaptor.capture());
        PostingContext context = contextCaptor.getValue();
        assertThat(context.getModule()).isEqualTo("LEASE");
        assertThat(context.getEventType()).isEqualTo("LEASE_ROU_RECOGNITION");
        assertThat(context.getTransactionType()).isEqualTo("ROU Initial Direct Cost Recognition");
        assertThat(context.getTransactionDate()).isEqualTo(LocalDate.of(2024, 2, 1));
        assertThat(context.getDescription()).isEqualTo("Ref#123 Lease-A INITIAL DIRECT COST");
        assertThat(context.getAmount()).isEqualByComparingTo("1800.00");
        assertThat(context.getPostingId()).isEqualTo(requisitionId);
        assertThat(context.getPostedBy()).isSameAs(postedBy);
        assertThat(context.getAttributes()).containsEntry("leaseContractId", "55");
        assertThat(context.getAttributes()).containsEntry("rouInitialDirectCostId", "88");

        ArgumentCaptor<List<TransactionDetails>> captor = ArgumentCaptor.forClass(List.class);
        verify(transactionDetailsRepository).saveAll(captor.capture());

        List<TransactionDetails> saved = captor.getValue();
        assertThat(saved).hasSize(1);
        assertThat(saved.get(0)).isSameAs(details);
        assertThat(saved.get(0).getDebitAccount()).isEqualTo(rouAssetAccount);
        assertThat(saved.get(0).getCreditAccount()).isEqualTo(rouCostOffset);
    }
}
