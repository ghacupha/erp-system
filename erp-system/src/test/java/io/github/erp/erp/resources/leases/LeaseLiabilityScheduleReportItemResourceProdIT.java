package io.github.erp.erp.resources.leases;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.erp.IntegrationTest;
import io.github.erp.erp.resources.leases.LeaseLiabilityScheduleReportItemResourceProdIT.TestLeaseInterestPaidTransferSummaryInternal;
import io.github.erp.internal.model.LeaseInterestPaidTransferSummaryInternal;
import io.github.erp.internal.repository.InternalLeaseLiabilityScheduleReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleReportItemSearchRepository;
import io.github.erp.service.LeaseLiabilityScheduleReportItemQueryService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link LeaseLiabilityScheduleReportItemResourceProd} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(roles = { "LEASE_MANAGER" })
class LeaseLiabilityScheduleReportItemResourceProdIT {

    private static final String INTEREST_PAID_TRANSFER_SUMMARY_ENDPOINT =
        "/api/leases/lease-liability-schedule-report-items/interest-paid-transfer-summary/{leasePeriodId}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InternalLeaseLiabilityScheduleReportItemRepository internalLeaseLiabilityScheduleReportItemRepository;

    @MockBean
    private LeaseLiabilityScheduleReportItemQueryService leaseLiabilityScheduleReportItemQueryService;

    @MockBean
    private LeaseLiabilityScheduleReportItemSearchRepository leaseLiabilityScheduleReportItemSearchRepository;

    @Test
    void getLeaseInterestPaidTransferSummary_omitsZeroInterestRowsAndReturnsAccounts() throws Exception {
        long leasePeriodId = 4242L;

        LeaseInterestPaidTransferSummaryInternal positiveRow = new TestLeaseInterestPaidTransferSummaryInternal(
            "LEASE-001",
            "Acme Dealer",
            "LEASE-001 Main Warehouse",
            "100-200",
            "200-300",
            new BigDecimal("125.50")
        );

        LeaseInterestPaidTransferSummaryInternal zeroRow = new TestLeaseInterestPaidTransferSummaryInternal(
            "LEASE-002",
            "Zero Dealer",
            "LEASE-002 Satellite",
            "400-500",
            "500-600",
            BigDecimal.ZERO
        );

        when(internalLeaseLiabilityScheduleReportItemRepository.getLeaseInterestPaidTransferSummary(leasePeriodId))
            .thenReturn(List.of(positiveRow, zeroRow));

        mockMvc
            .perform(get(INTEREST_PAID_TRANSFER_SUMMARY_ENDPOINT, leasePeriodId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].leaseId").value("LEASE-001"))
            .andExpect(jsonPath("$[0].dealerName").value("Acme Dealer"))
            .andExpect(jsonPath("$[0].creditAccount").value("100-200"))
            .andExpect(jsonPath("$[0].debitAccount").value("200-300"))
            .andExpect(jsonPath("$[0].interestAmount").value(125.50));

        Mockito
            .verify(internalLeaseLiabilityScheduleReportItemRepository)
            .getLeaseInterestPaidTransferSummary(leasePeriodId);
    }

    /**
     * Simple implementation of the projection interface used for stubbing repository responses.
     */
    static class TestLeaseInterestPaidTransferSummaryInternal implements LeaseInterestPaidTransferSummaryInternal {

        private final String leaseId;
        private final String dealerName;
        private final String narration;
        private final String creditAccount;
        private final String debitAccount;
        private final BigDecimal interestAmount;

        TestLeaseInterestPaidTransferSummaryInternal(
            String leaseId,
            String dealerName,
            String narration,
            String creditAccount,
            String debitAccount,
            BigDecimal interestAmount
        ) {
            this.leaseId = leaseId;
            this.dealerName = dealerName;
            this.narration = narration;
            this.creditAccount = creditAccount;
            this.debitAccount = debitAccount;
            this.interestAmount = interestAmount;
        }

        @Override
        public String getLeaseId() {
            return leaseId;
        }

        @Override
        public String getDealerName() {
            return dealerName;
        }

        @Override
        public String getNarration() {
            return narration;
        }

        @Override
        public String getCreditAccount() {
            return creditAccount;
        }

        @Override
        public String getDebitAccount() {
            return debitAccount;
        }

        @Override
        public BigDecimal getInterestAmount() {
            return interestAmount;
        }
    }
}
