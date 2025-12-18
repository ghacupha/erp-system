package io.github.erp.internal.service.leases.schedule;

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
import static org.mockito.Mockito.when;

import io.github.erp.domain.LeasePayment;
import io.github.erp.domain.enumeration.LiabilityTimeGranularity;
import io.github.erp.erp.leases.liability.enumeration.PresentValueCalculator;
import io.github.erp.erp.leases.liability.enumeration.PresentValueLine;
import io.github.erp.internal.service.leases.InternalIFRS16LeaseContractService;
import io.github.erp.internal.service.leases.InternalLeaseAmortizationCalculationService;
import io.github.erp.internal.service.leases.InternalLeaseAmortizationScheduleService;
import io.github.erp.internal.service.leases.InternalLeaseLiabilityService;
import io.github.erp.internal.service.leases.InternalLeasePaymentService;
import io.github.erp.internal.service.leases.InternalLeaseRepaymentPeriodService;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import io.github.erp.service.dto.LeaseAmortizationCalculationDTO;
import io.github.erp.service.dto.LeaseAmortizationScheduleDTO;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.dto.LeaseRepaymentPeriodDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeaseLiabilityEndToEndTest {

    private final PresentValueCalculator presentValueCalculator = new PresentValueCalculator();

    @Mock
    private InternalLeaseRepaymentPeriodService leaseRepaymentPeriodService;

    @Mock
    private InternalLeasePaymentService leasePaymentService;

    @Mock
    private InternalLeaseLiabilityService leaseLiabilityService;

    @Mock
    private InternalLeaseAmortizationCalculationService leaseAmortizationCalculationService;

    @Mock
    private InternalLeaseAmortizationScheduleService leaseAmortizationScheduleService;

    @Mock
    private InternalIFRS16LeaseContractService leaseContractService;

    private LeaseAmortizationService leaseAmortizationService;

    @BeforeEach
    void setUp() {
        leaseAmortizationService =
            new LeaseAmortizationService(
                leaseRepaymentPeriodService,
                leaseLiabilityService,
                leaseAmortizationCalculationService,
                leasePaymentService,
                leaseAmortizationScheduleService,
                leaseContractService
            );
    }

    @Test
    void leaseLiabilityScheduleSettlesOutstandingBalanceWhenSeededWithPresentValue() {
        BigDecimal annualRate = new BigDecimal("0.12");
        LocalDate presentValueDate = LocalDate.of(2021, 1, 1);

        LeasePayment janPayment = new LeasePayment().paymentAmount(new BigDecimal("120")).paymentDate(LocalDate.of(2021, 1, 15));
        LeasePayment febPayment = new LeasePayment().paymentAmount(new BigDecimal("120")).paymentDate(LocalDate.of(2021, 2, 15));
        LeasePayment marPayment = new LeasePayment().paymentAmount(new BigDecimal("120")).paymentDate(LocalDate.of(2021, 3, 15));

        List<PresentValueLine> presentValueLines =
            presentValueCalculator.calculate(
                List.of(janPayment, febPayment, marPayment),
                annualRate,
                LiabilityTimeGranularity.MONTHLY,
                presentValueDate
            );

        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("12"), 10, RoundingMode.HALF_EVEN);

        BigDecimal initialLiability = presentValueLines
            .stream()
            .map(PresentValueLine::getPresentValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            // Align the present value seed with the amortization schedule, which charges
            // interest at the start of the first period instead of discounting two periods.
            .multiply(BigDecimal.ONE.add(monthlyRate))
            .setScale(2, RoundingMode.HALF_EVEN);

        IFRS16LeaseContractDTO contract = new IFRS16LeaseContractDTO();
        contract.setId(11L);
        contract.setBookingId("BK-11");
        contract.setLeaseTitle("Test Lease");
        contract.setInceptionDate(presentValueDate);
        contract.setCommencementDate(presentValueDate);

        LeaseLiabilityDTO leaseLiability = new LeaseLiabilityDTO();
        leaseLiability.setId(21L);
        leaseLiability.setLeaseId("LL-21");
        leaseLiability.setLiabilityAmount(initialLiability);
        leaseLiability.setStartDate(presentValueDate);
        leaseLiability.setEndDate(presentValueDate.plusMonths(3));
        leaseLiability.setInterestRate(annualRate);
        leaseLiability.setLeaseContract(contract);

        LeaseAmortizationCalculationDTO calculation = new LeaseAmortizationCalculationDTO();
        calculation.setInterestRate(annualRate);
        calculation.setLeaseAmount(initialLiability);
        calculation.setNumberOfPeriods(3);

        List<LeaseRepaymentPeriodDTO> leaseRepaymentPeriods = buildMonthlyLeasePeriods(presentValueDate, 3);
        List<LeasePaymentDTO> leasePayments = buildLeasePaymentsFromDomain(List.of(janPayment, febPayment, marPayment), contract);

        when(leaseLiabilityService.findOne(leaseLiability.getId())).thenReturn(Optional.of(leaseLiability));
        when(leaseContractService.findOne(contract.getId())).thenReturn(Optional.of(contract));
        when(leaseAmortizationCalculationService.findByLeaseLiabilityId(leaseLiability.getId())).thenReturn(Optional.of(calculation));
        when(leaseAmortizationScheduleService.save(any(LeaseAmortizationScheduleDTO.class))).thenAnswer(invocation -> {
            LeaseAmortizationScheduleDTO scheduleDTO = invocation.getArgument(0);
            scheduleDTO.setId(99L);
            return scheduleDTO;
        });
        when(leaseRepaymentPeriodService.findLeasePeriods(contract.getCommencementDate(), calculation.getNumberOfPeriods()))
            .thenReturn(Optional.of(leaseRepaymentPeriods));
        when(leasePaymentService.findPaymentsByContractId(contract.getId())).thenReturn(Optional.of(leasePayments));

        List<LeaseLiabilityScheduleItemDTO> scheduleItems = leaseAmortizationService.generateAmortizationSchedule(leaseLiability.getId(), null);

        LeaseLiabilityScheduleItemDTO finalItem = scheduleItems.get(scheduleItems.size() - 1);
        assertThat(finalItem.getOutstandingBalance().abs()).isLessThan(new BigDecimal("0.01"));
        assertThat(finalItem.getInterestPayableClosing().abs()).isLessThan(new BigDecimal("0.01"));
    }

    private static List<LeaseRepaymentPeriodDTO> buildMonthlyLeasePeriods(LocalDate startDate, int numberOfPeriods) {
        List<LeaseRepaymentPeriodDTO> periods = new ArrayList<>();
        for (int i = 0; i < numberOfPeriods; i++) {
            LeaseRepaymentPeriodDTO period = new LeaseRepaymentPeriodDTO();
            period.setSequenceNumber((long) i);
            LocalDate periodStart = startDate.plusMonths(i);
            period.setStartDate(periodStart);
            period.setEndDate(periodStart.plusMonths(1).minusDays(1));
            period.setPeriodCode("P" + (i + 1));
            periods.add(period);
        }
        return periods;
    }

    private static List<LeasePaymentDTO> buildLeasePaymentsFromDomain(List<LeasePayment> payments, IFRS16LeaseContractDTO contract) {
        return payments
            .stream()
            .map(payment -> {
                LeasePaymentDTO dto = new LeasePaymentDTO();
                dto.setPaymentAmount(payment.getPaymentAmount());
                dto.setPaymentDate(payment.getPaymentDate());
                dto.setLeaseContract(contract);
                return dto;
            })
            .collect(Collectors.toList());
    }
}
