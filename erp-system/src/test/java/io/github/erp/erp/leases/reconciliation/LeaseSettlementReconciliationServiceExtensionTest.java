package io.github.erp.erp.leases.reconciliation;

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
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeasePayment;
import io.github.erp.domain.LeaseRepaymentPeriod;
import io.github.erp.domain.LeaseSettlement;
import io.github.erp.domain.enumeration.ReconciliationStatusType;
import io.github.erp.internal.repository.InternalApplicationUserRepository;
import io.github.erp.internal.service.posting.PostingContext;
import io.github.erp.repository.LeasePaymentRepository;
import io.github.erp.repository.LeaseRepaymentPeriodRepository;
import io.github.erp.repository.LeaseSettlementRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeaseSettlementReconciliationServiceExtensionTest {

    @Mock
    private LeaseSettlementRepository leaseSettlementRepository;

    @Mock
    private LeasePaymentRepository leasePaymentRepository;

    @Mock
    private LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository;

    @Mock
    private InternalApplicationUserRepository applicationUserRepository;

    private LeaseSettlementReconciliationServiceExtension reconciliationService;

    @BeforeEach
    void setUp() {
        reconciliationService = new LeaseSettlementReconciliationServiceExtension(
            leaseSettlementRepository,
            leasePaymentRepository,
            leaseRepaymentPeriodRepository,
            applicationUserRepository
        );
    }

    @Test
    void reconcilePeriodBuildsPostingContextAndUpdatesSettlement() {
        LeaseRepaymentPeriod period = new LeaseRepaymentPeriod();
        period.setId(99L);
        period.setPeriodCode("2024-06");
        period.setStartDate(LocalDate.of(2024, 6, 1));
        period.setEndDate(LocalDate.of(2024, 6, 30));

        IFRS16LeaseContract leaseContract = new IFRS16LeaseContract();
        leaseContract.setId(12L);

        LeasePayment leasePayment = new LeasePayment();
        leasePayment.setId(22L);
        leasePayment.setLeaseContract(leaseContract);
        leasePayment.setPaymentAmount(new BigDecimal("1000.00"));
        leasePayment.setPaymentDate(LocalDate.of(2024, 6, 30));
        leasePayment.setActive(Boolean.TRUE);

        LeaseSettlement leaseSettlement = new LeaseSettlement();
        leaseSettlement.setId(77L);
        leaseSettlement.setLeaseContract(leaseContract);
        leaseSettlement.setPeriod(period);
        leaseSettlement.setSettlementDate(LocalDate.of(2024, 6, 30));
        leaseSettlement.setInvoiceAmount(new BigDecimal("800.00"));
        leaseSettlement.setInvoiceReference("LS-INV-TEST");
        leaseSettlement.setReconciliationStatus(ReconciliationStatusType.PENDING);

        ApplicationUser postedBy = new ApplicationUser();
        postedBy.setId(3L);

        when(leaseRepaymentPeriodRepository.findById(99L)).thenReturn(Optional.of(period));
        when(leaseSettlementRepository.findAllByPeriodId(99L)).thenReturn(List.of(leaseSettlement));
        when(leasePaymentRepository.findAllByPaymentDateBetweenAndActiveTrue(period.getStartDate(), period.getEndDate()))
            .thenReturn(List.of(leasePayment));
        when(applicationUserRepository.findById(3L)).thenReturn(Optional.of(postedBy));

        List<PostingContext> contexts = reconciliationService.reconcilePeriod(99L, 3L);

        assertThat(contexts).hasSize(1);
        PostingContext context = contexts.get(0);
        assertThat(context.getVarianceType()).isEqualTo("PARTIAL");
        assertThat(context.getInvoiceTiming()).isEqualTo("ON_TIME");
        assertThat(context.getAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(leaseSettlement.getVarianceAmount()).isEqualByComparingTo(new BigDecimal("-200.00"));
        assertThat(leaseSettlement.getReconciliationStatus()).isEqualTo(ReconciliationStatusType.VARIANCE);
        assertThat(leaseSettlement.getPostingId()).isNotNull();
    }
}
