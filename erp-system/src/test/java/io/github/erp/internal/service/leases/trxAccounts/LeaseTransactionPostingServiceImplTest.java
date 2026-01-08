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
import io.github.erp.domain.TALeaseRepaymentRule;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.domain.TransactionDetails;
import io.github.erp.internal.repository.InternalApplicationUserRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityScheduleItemRepository;
import io.github.erp.internal.repository.InternalRouDepreciationEntryRepository;
import io.github.erp.internal.repository.InternalRouInitialDirectCostRepository;
import io.github.erp.internal.repository.InternalTAAmortizationRuleRepository;
import io.github.erp.internal.repository.InternalTAInterestPaidTransferRuleRepository;
import io.github.erp.internal.repository.InternalTALeaseInterestAccrualRuleRepository;
import io.github.erp.internal.repository.InternalTALeaseRecognitionRuleRepository;
import io.github.erp.internal.repository.InternalTALeaseRepaymentRuleRepository;
import io.github.erp.internal.repository.InternalTransactionDetailsRepository;
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
    private InternalTALeaseRepaymentRuleRepository leaseRepaymentRuleRepository;

    @Mock
    private InternalTALeaseInterestAccrualRuleRepository leaseInterestAccrualRuleRepository;

    @Mock
    private InternalTAInterestPaidTransferRuleRepository interestPaidTransferRuleRepository;

    @Mock
    private InternalTALeaseRecognitionRuleRepository leaseRecognitionRuleRepository;

    @Mock
    private InternalTAAmortizationRuleRepository amortizationRuleRepository;

    @Mock
    private InternalApplicationUserRepository applicationUserRepository;

    @Mock
    private TransactionEntryIdGenerator transactionEntryIdGenerator;

    private LeaseTransactionPostingServiceImpl leaseTransactionPostingService;

    @BeforeEach
    void setUp() {
        leaseTransactionPostingService = new LeaseTransactionPostingServiceImpl(
            transactionDetailsRepository,
            leaseLiabilityScheduleItemRepository,
            leaseLiabilityRepository,
            rouInitialDirectCostRepository,
            rouDepreciationEntryRepository,
            leaseRepaymentRuleRepository,
            leaseInterestAccrualRuleRepository,
            interestPaidTransferRuleRepository,
            leaseRecognitionRuleRepository,
            amortizationRuleRepository,
            applicationUserRepository,
            transactionEntryIdGenerator
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

        TransactionAccount debitAccount = new TransactionAccount();
        debitAccount.setId(1L);
        TransactionAccount creditAccount = new TransactionAccount();
        creditAccount.setId(2L);

        TALeaseRepaymentRule rule = new TALeaseRepaymentRule();
        rule.setLeaseContract(leaseContract);
        rule.setDebit(debitAccount);
        rule.setCredit(creditAccount);

        FiscalMonth fiscalMonth = new FiscalMonth();
        fiscalMonth.setFiscalMonthCode("YM2024-01");
        fiscalMonth.setEndDate(LocalDate.of(2024, 1, 31));

        LeaseRepaymentPeriod leasePeriod = new LeaseRepaymentPeriod();
        leasePeriod.setFiscalMonth(fiscalMonth);

        LeaseLiabilityScheduleItem scheduleItem = new LeaseLiabilityScheduleItem();
        scheduleItem.setLeaseContract(leaseContract);
        scheduleItem.setLeasePeriod(leasePeriod);
        scheduleItem.setCashPayment(new BigDecimal("1200.00"));

        when(applicationUserRepository.findById(postedById)).thenReturn(Optional.of(postedBy));
        when(leaseRepaymentRuleRepository.findAll()).thenReturn(List.of(rule));
        when(leaseLiabilityScheduleItemRepository.findAllWithLeaseContractAndPeriod()).thenReturn(List.of(scheduleItem));
        when(transactionEntryIdGenerator.nextEntryId()).thenReturn(991L);
        when(transactionDetailsRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        leaseTransactionPostingService.postLeaseRepayment(requisitionId, postedById);

        ArgumentCaptor<List<TransactionDetails>> captor = ArgumentCaptor.forClass(List.class);
        verify(transactionDetailsRepository).saveAll(captor.capture());

        List<TransactionDetails> saved = captor.getValue();
        assertThat(saved).hasSize(1);
        TransactionDetails details = saved.get(0);
        assertThat(details.getEntryId()).isEqualTo(991L);
        assertThat(details.getPostingId()).isEqualTo(requisitionId);
        assertThat(details.getTransactionType()).isEqualTo("Lease Repayment");
        assertThat(details.getAmount()).isEqualByComparingTo("1200.00");
        assertThat(details.getDebitAccount()).isEqualTo(debitAccount);
        assertThat(details.getCreditAccount()).isEqualTo(creditAccount);
        assertThat(details.getDescription()).isEqualTo("Lease-A 2024-01REPAYMENT");
        assertThat(details.getTransactionDate()).isEqualTo(LocalDate.of(2024, 1, 31));
        assertThat(details.getCreatedAt()).isNotNull();
    }
}
