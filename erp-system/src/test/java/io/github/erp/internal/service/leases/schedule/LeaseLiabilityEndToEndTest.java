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
import static org.mockito.Mockito.reset;
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
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

        runScenario(
            11L,
            21L,
            "BK-11",
            "LL-21",
            "Multi-year Quarterly Lease",
            LocalDate.of(2024, Month.SEPTEMBER, 1),
            annualRate,
            buildQuarterlyPayments()
        );

        runScenario(
            1023L,
            10231L,
            "BK-1023",
            "LL-1023",
            "Lease 1023 Quarterly",
            LocalDate.of(2024, Month.JULY, 1),
            annualRate,
            buildLease1023Payments()
        );

        runScenario(
            1026L,
            10261L,
            "BK-1026",
            "LL-1026",
            "Lease 1026 Quarterly",
            LocalDate.of(2024, Month.DECEMBER, 1),
            annualRate,
            buildLease1026Payments()
        );

        runScenario(
            1018L,
            10181L,
            "BK-1018",
            "LL-1018",
            "Lease 1018 Quarterly",
            LocalDate.of(2021, Month.SEPTEMBER, 30),
            annualRate,
            buildLease1018Payments()
        );

        runScenario(
            1016L,
            10161L,
            "BK-1016",
            "LL-1016",
            "Lease 1016 Single Payout",
            LocalDate.of(2021, Month.SEPTEMBER, 30),
            annualRate,
            buildLease1016Payments()
        );

        runScenario(
            1017L,
            10171L,
            "BK-1017",
            "LL-1017",
            "Lease 1017 Quarterly Escalation",
            LocalDate.of(2021, Month.DECEMBER, 31),
            annualRate,
            buildLease1017Payments()
        );
    }

    private void runScenario(
        long contractId,
        long leaseLiabilityId,
        String bookingId,
        String leaseId,
        String leaseTitle,
        LocalDate presentValueDate,
        BigDecimal annualRate,
        List<LeasePayment> leasePayments
    ) {
        resetMocks();

        LocalDate finalPaymentDate = leasePayments.get(leasePayments.size() - 1).getPaymentDate();
        int numberOfPeriods = monthsBetweenInclusive(presentValueDate, finalPaymentDate);

        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("12"), RoundingMode.HALF_EVEN);
        BigDecimal initialLiability = calibratePresentValueSeed(presentValueDate, leasePayments, monthlyRate);

        IFRS16LeaseContractDTO contract = new IFRS16LeaseContractDTO();
        contract.setId(contractId);
        contract.setBookingId(bookingId);
        contract.setLeaseTitle(leaseTitle);
        contract.setInceptionDate(presentValueDate);
        contract.setCommencementDate(presentValueDate);

        LeaseLiabilityDTO leaseLiability = new LeaseLiabilityDTO();
        leaseLiability.setId(leaseLiabilityId);
        leaseLiability.setLeaseId(leaseId);
        leaseLiability.setLiabilityAmount(initialLiability);
        leaseLiability.setStartDate(presentValueDate);
        leaseLiability.setEndDate(presentValueDate.plusMonths(numberOfPeriods).minusDays(1));
        leaseLiability.setInterestRate(annualRate);
        leaseLiability.setLeaseContract(contract);

        LeaseAmortizationCalculationDTO calculation = new LeaseAmortizationCalculationDTO();
        calculation.setInterestRate(annualRate);
        calculation.setLeaseAmount(initialLiability);
        calculation.setNumberOfPeriods(numberOfPeriods);

        List<LeaseRepaymentPeriodDTO> leaseRepaymentPeriods = buildMonthlyLeasePeriods(presentValueDate, calculation.getNumberOfPeriods());
        List<LeasePaymentDTO> leasePaymentDTOs = buildLeasePaymentsFromDomain(leasePayments, contract);

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
        when(leasePaymentService.findPaymentsByContractId(contract.getId())).thenReturn(Optional.of(leasePaymentDTOs));

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

    private static List<LeasePayment> buildQuarterlyPayments() {
        return List.of(
            payment("2024-09-01", "4553951.20"),
            payment("2024-12-01", "4553951.20"),
            payment("2025-03-01", "4553951.20"),
            payment("2025-06-01", "4553951.20"),
            payment("2025-09-01", "4880153.64"),
            payment("2025-12-01", "4880153.64"),
            payment("2026-03-01", "4880153.64"),
            payment("2026-06-01", "4880153.64"),
            payment("2026-09-01", "5230820.48"),
            payment("2026-12-01", "5230820.48"),
            payment("2027-03-01", "5230820.48"),
            payment("2027-06-01", "5230820.48"),
            payment("2027-09-01", "5607788.00"),
            payment("2027-12-01", "5607788.00"),
            payment("2028-03-01", "5607788.00"),
            payment("2028-06-01", "5607788.00"),
            payment("2028-09-01", "6013028.20"),
            payment("2028-12-01", "6013028.20"),
            payment("2029-03-01", "6013028.20"),
            payment("2029-06-01", "6013028.20"),
            payment("2029-09-01", "6448660.40"),
            payment("2029-12-01", "6448660.40"),
            payment("2030-03-01", "6448660.40"),
            payment("2030-06-01", "6448660.40"),
            payment("2030-09-01", "6916966.32"),
            payment("2030-12-01", "6916966.32"),
            payment("2031-03-01", "6916966.32"),
            payment("2031-06-01", "6916966.32")
        );
    }

    private static List<LeasePayment> buildLease1023Payments() {
        return List.of(
            payment("2024-07-01", "390841.12"),
            payment("2024-10-01", "390841.12"),
            payment("2025-01-01", "390841.12"),
            payment("2025-04-01", "390841.12"),
            payment("2025-07-01", "416480.60"),
            payment("2025-10-01", "416480.60"),
            payment("2026-01-01", "416480.60"),
            payment("2026-04-01", "416480.60"),
            payment("2026-07-01", "444042.20"),
            payment("2026-10-01", "444042.20"),
            payment("2027-01-01", "444042.20"),
            payment("2027-04-01", "444042.20"),
            payment("2027-07-01", "473672.08"),
            payment("2027-10-01", "473672.08"),
            payment("2028-01-01", "473672.08"),
            payment("2028-04-01", "473672.08"),
            payment("2028-07-01", "505523.36"),
            payment("2028-10-01", "505523.36"),
            payment("2029-01-01", "505523.36"),
            payment("2029-04-01", "505523.36"),
            payment("2029-07-01", "539764.24")
        );
    }

    private static List<LeasePayment> buildLease1026Payments() {
        return List.of(
            payment("2024-12-01", "701736.20"),
            payment("2025-03-01", "701736.20"),
            payment("2025-06-01", "701736.20"),
            payment("2025-09-01", "701736.20"),
            payment("2025-12-01", "701736.20"),
            payment("2026-03-01", "701736.20"),
            payment("2026-06-01", "701736.20"),
            payment("2026-09-01", "701736.20")
        );
    }

    private static List<LeasePayment> buildLease1018Payments() {
        return List.of(
            payment("2021-09-30", "0.00"),
            payment("2021-12-31", "287183.52"),
            payment("2022-03-31", "287183.52"),
            payment("2022-06-30", "287183.52"),
            payment("2022-09-30", "287183.52"),
            payment("2022-12-31", "287183.52"),
            payment("2023-03-31", "287183.52"),
            payment("2023-06-30", "287183.52"),
            payment("2023-09-30", "287183.52"),
            payment("2023-12-31", "287183.52"),
            payment("2024-03-31", "287183.52"),
            payment("2024-06-30", "287183.52"),
            payment("2024-09-30", "287183.52"),
            payment("2024-12-31", "287183.52"),
            payment("2025-03-31", "287183.52"),
            payment("2025-06-30", "287183.52"),
            payment("2025-09-30", "287183.52"),
            payment("2025-12-31", "287183.52"),
            payment("2026-03-31", "287183.52"),
            payment("2026-06-30", "287183.52"),
            payment("2026-09-30", "287183.52"),
            payment("2026-12-31", "287183.52"),
            payment("2027-03-31", "287183.52"),
            payment("2027-06-30", "287183.52")
        );
    }

    private static List<LeasePayment> buildLease1016Payments() {
        return List.of(payment("2021-09-30", "0.00"), payment("2024-09-30", "10769382.00"));
    }

    private static List<LeasePayment> buildLease1017Payments() {
        return List.of(
            payment("2021-12-31", "0.00"),
            payment("2022-03-31", "1782421.20"),
            payment("2022-06-30", "1782421.20"),
            payment("2022-09-30", "1782421.20"),
            payment("2022-12-31", "1782421.20"),
            payment("2023-03-31", "1782421.20"),
            payment("2023-06-30", "1782421.20"),
            payment("2023-09-30", "1782421.20"),
            payment("2023-12-31", "1916102.79"),
            payment("2024-03-31", "1916102.79"),
            payment("2024-06-30", "1916102.79"),
            payment("2024-09-30", "1916102.79"),
            payment("2024-12-31", "1916102.79"),
            payment("2025-03-31", "1916102.79"),
            payment("2025-06-30", "1916102.79"),
            payment("2025-09-30", "1916102.79"),
            payment("2025-12-31", "2049784.38"),
            payment("2026-03-31", "2049784.38"),
            payment("2026-06-30", "2049784.38"),
            payment("2026-09-30", "2049784.38"),
            payment("2026-12-31", "2049784.38")
        );
    }

    private static LeasePayment payment(String date, String amount) {
        return new LeasePayment().paymentAmount(new BigDecimal(amount)).paymentDate(LocalDate.parse(date));
    }


    private BigDecimal calibratePresentValueSeed(LocalDate presentValueDate, List<LeasePayment> payments, BigDecimal monthlyRate) {
        List<PresentValueLine> presentValueLines =
            presentValueCalculator.calculate(payments, monthlyRate.multiply(new BigDecimal("12")), LiabilityTimeGranularity.MONTHLY, presentValueDate);

        BigDecimal lowerBound = presentValueLines
            .stream()
            .map(PresentValueLine::getPresentValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal upperBound = lowerBound.multiply(new BigDecimal("1.2"));
        while (computeClosingBalance(lowerBound, monthlyRate, payments, presentValueDate).compareTo(BigDecimal.ZERO) > 0) {
            upperBound = lowerBound;
            lowerBound = lowerBound.multiply(new BigDecimal("0.9"));
        }
        while (computeClosingBalance(upperBound, monthlyRate, payments, presentValueDate).compareTo(BigDecimal.ZERO) < 0) {
            upperBound = upperBound.multiply(new BigDecimal("1.1"));
        }

        for (int i = 0; i < 40; i++) {
            BigDecimal mid = lowerBound.add(upperBound).divide(new BigDecimal("2"), 10, RoundingMode.HALF_EVEN);
            BigDecimal closingBalance = computeClosingBalance(mid, monthlyRate, payments, presentValueDate);
            if (closingBalance.compareTo(BigDecimal.ZERO) > 0) {
                upperBound = mid;
            } else {
                lowerBound = mid;
            }
        }

        return upperBound.setScale(2, RoundingMode.HALF_EVEN);
    }


    private BigDecimal computeClosingBalance(BigDecimal openingBalance, BigDecimal monthlyRate, List<LeasePayment> payments, LocalDate startDate) {
        Map<YearMonth, BigDecimal> paymentsByMonth = payments
            .stream()
            .collect(
                Collectors.groupingBy(
                    payment -> YearMonth.from(payment.getPaymentDate()),
                    LinkedHashMap::new,
                    Collectors.mapping(LeasePayment::getPaymentAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                )
            );

        BigDecimal balance = openingBalance;
        BigDecimal interestPayable = BigDecimal.ZERO;

        YearMonth cursor = YearMonth.from(startDate);
        YearMonth finalPeriod = YearMonth.from(payments.get(payments.size() - 1).getPaymentDate());

        while (!cursor.isAfter(finalPeriod)) {
            BigDecimal interestAccrued = balance.multiply(monthlyRate);
            BigDecimal totalPayment = paymentsByMonth.getOrDefault(cursor, BigDecimal.ZERO);

            BigDecimal interestPayment = interestPayable.add(interestAccrued).min(totalPayment).max(BigDecimal.ZERO);
            BigDecimal principalPayment = totalPayment.subtract(interestPayment).max(BigDecimal.ZERO);

            balance = balance.subtract(principalPayment);
            interestPayable = interestPayable.add(interestAccrued).subtract(interestPayment);

            cursor = cursor.plusMonths(1);
        }

        return balance;
    }

    private int monthsBetweenInclusive(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.MONTHS.between(startDate, endDate) + 1;
    }

    private void resetMocks() {
        reset(
            leaseRepaymentPeriodService,
            leasePaymentService,
            leaseLiabilityService,
            leaseAmortizationCalculationService,
            leaseAmortizationScheduleService,
            leaseContractService
        );
    }
}
