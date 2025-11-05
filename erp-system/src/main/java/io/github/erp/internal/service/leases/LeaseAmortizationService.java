package io.github.erp.internal.service.leases;

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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import io.github.erp.service.dto.LeaseAmortizationCalculationDTO;
import io.github.erp.service.dto.LeaseAmortizationScheduleDTO;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.dto.LeaseRepaymentPeriodDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// @Transactional // this might mess up with the transaction manager used by the batch
public class LeaseAmortizationService implements LeaseAmortizationCompilationService {

    private static final RoundingMode ROUND_HALF_EVEN = RoundingMode.HALF_EVEN;

    private final InternalLeaseRepaymentPeriodService leaseRepaymentPeriodService;
    private final InternalLeasePaymentService leasePaymentService;
    private final InternalLeaseLiabilityService leaseLiabilityService;
    private final InternalLeaseAmortizationCalculationService leaseAmortizationCalculationService;
    private final InternalLeaseAmortizationScheduleService internalLeaseAmortizationScheduleService;
    private final InternalIFRS16LeaseContractService leaseContractService;

    public LeaseAmortizationService(
        InternalLeaseRepaymentPeriodService leaseRepaymentPeriodService,
        InternalLeaseLiabilityService leaseLiabilityService,
        InternalLeaseAmortizationCalculationService leaseAmortizationCalculationService,
        InternalLeasePaymentService leasePaymentService,
        InternalLeaseAmortizationScheduleService internalLeaseAmortizationScheduleService,
        InternalIFRS16LeaseContractService leaseContractService) {
        this.leaseRepaymentPeriodService = leaseRepaymentPeriodService;
        this.leaseLiabilityService = leaseLiabilityService;
        this.leaseAmortizationCalculationService = leaseAmortizationCalculationService;
        this.leasePaymentService = leasePaymentService;
        this.internalLeaseAmortizationScheduleService = internalLeaseAmortizationScheduleService;
        this.leaseContractService = leaseContractService;
    }

    public List<LeaseLiabilityScheduleItemDTO> generateAmortizationSchedule(Long leaseLiabilityId, Long compilationId) {
        Optional<LeaseLiabilityDTO> leaseLiabilityOpt = leaseLiabilityService.findOne(leaseLiabilityId);

        if (leaseLiabilityOpt.isEmpty()) {
            throw new IllegalArgumentException("Lease Liability id # " + leaseLiabilityId + " not found");
        }

        LeaseLiabilityDTO leaseLiability = leaseLiabilityOpt.get();

        Optional<IFRS16LeaseContractDTO> ifrs16LeaseContractOpt = leaseContractService.findOne(leaseLiability.getLeaseContract().getId());

        if (ifrs16LeaseContractOpt.isEmpty()) {
            throw new IllegalArgumentException("IFRS16 Contract id # " + leaseLiability.getLeaseContract().getId() + " not found");
        }

        IFRS16LeaseContractDTO ifrs16LeaseContract = ifrs16LeaseContractOpt.get();

        Optional<LeaseAmortizationCalculationDTO> leaseAmortizationCalculationOpt =
            leaseAmortizationCalculationService.findByLeaseLiabilityId(leaseLiabilityId);

        if (leaseAmortizationCalculationOpt.isEmpty()) {
            throw new IllegalArgumentException("Lease Amortization Calculation for Lease Liability id # " + leaseLiabilityId + "not found");
        }

        LeaseAmortizationCalculationDTO calculation = leaseAmortizationCalculationOpt.get();

        Optional<LeaseAmortizationScheduleDTO> scheduleOpt = internalLeaseAmortizationScheduleService.findOneByBookingId(ifrs16LeaseContract.getBookingId());

        if (scheduleOpt.isEmpty()) {
            throw new IllegalArgumentException("Lease Amortization Schedule for Lease Booking id # " + ifrs16LeaseContract.getBookingId() + "not found");
        }

        LeaseAmortizationScheduleDTO leaseAmortizationSchedule = scheduleOpt.get();

        return calculateAmortizationSchedule(calculation, leaseLiability, ifrs16LeaseContract, leaseAmortizationSchedule, compilationId);
    }

    private List<LeaseLiabilityScheduleItemDTO> calculateAmortizationSchedule(
        LeaseAmortizationCalculationDTO calculation,
        LeaseLiabilityDTO leaseLiability,
        IFRS16LeaseContractDTO ifrs16LeaseContract,
        LeaseAmortizationScheduleDTO leaseAmortizationSchedule,
        Long compilationId
    ) {

        List<LeaseLiabilityScheduleItemDTO> scheduleItems = new ArrayList<>();
        LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO;
        if (compilationId != null) {
            leaseLiabilityCompilationDTO = new LeaseLiabilityCompilationDTO();
            leaseLiabilityCompilationDTO.setId(compilationId);
        } else {
            leaseLiabilityCompilationDTO = null;
        }
        BigDecimal monthlyRate = calculation.getInterestRate().divide(BigDecimal.valueOf(12), ROUND_HALF_EVEN);
        var openingBalanceRef = new Object() {
            BigDecimal openingBalance = leaseLiability.getLiabilityAmount();
            BigDecimal interestPayableOpening = BigDecimal.ZERO;
        };

        Optional<List<LeaseRepaymentPeriodDTO>> leasePeriods = leaseRepaymentPeriodService.findLeasePeriods(ifrs16LeaseContract.getCommencementDate(), calculation.getNumberOfPeriods());

        Optional<List<LeasePaymentDTO>> leasePayments = leasePaymentService.findPaymentsByContractId(ifrs16LeaseContract.getId());

        if (leasePayments.isEmpty()) {
            throw new IllegalArgumentException("No lease-payments prescribed for lease-booking # " + ifrs16LeaseContract.getBookingId() + ". Please record lease-payments and try again");
        }

        leasePeriods.ifPresent( periods -> {

            List<LeaseRepaymentPeriodDTO> sortedPeriods = periods.stream().sorted(Comparator.comparing(LeaseRepaymentPeriodDTO::getStartDate)).collect(Collectors.toList());

            for (int period = 0; period < sortedPeriods.size(); period++) {

                BigDecimal interestPayableOpening = openingBalanceRef.interestPayableOpening;

                BigDecimal interestAccrued = openingBalanceRef.openingBalance.multiply(monthlyRate);
                BigDecimal totalPayment = calculateMonthlyPayment(leasePayments.get(), sortedPeriods.get(period));


                BigDecimal interestPayment = interestPayableOpening.add(interestAccrued).min(totalPayment).max(BigDecimal.ZERO);

                BigDecimal principalPayment = totalPayment.subtract(interestPayment).max(BigDecimal.ZERO);

                BigDecimal closingBalance = openingBalanceRef.openingBalance.subtract(principalPayment);
                BigDecimal interestPayableClosing = openingBalanceRef.interestPayableOpening.add(interestAccrued).subtract(interestPayment);

                LeaseLiabilityScheduleItemDTO item = new LeaseLiabilityScheduleItemDTO();
                item.setSequenceNumber(period);
                item.setOpeningBalance(openingBalanceRef.openingBalance);
                item.setOutstandingBalance(closingBalance);
                item.setCashPayment(totalPayment);
                item.setPrincipalPayment(principalPayment);
                item.setInterestPayment(interestPayment);
                item.setInterestPayableOpening(interestPayableOpening);
                item.setInterestAccrued(interestAccrued);
                item.setInterestPayableClosing(interestPayableClosing);
                item.setLeaseLiability(leaseLiability);
                item.setLeaseContract(ifrs16LeaseContract);
                item.setLeaseAmortizationSchedule(leaseAmortizationSchedule);
                item.setActive(Boolean.TRUE);
                item.setLeaseLiabilityCompilation(leaseLiabilityCompilationDTO);

                // TODO CHANGE TO LEASE REPAYMENT PERIOD
                item.setLeasePeriod(sortedPeriods.get(period));

                scheduleItems.add(item);

                // Update balances for the next period
                openingBalanceRef.openingBalance = closingBalance;
                openingBalanceRef.interestPayableOpening = interestPayableClosing;
            }
        });

        return scheduleItems;
    }

    private BigDecimal calculateMonthlyPayment(List<LeasePaymentDTO> leasePayments, LeaseRepaymentPeriodDTO currentPeriod) {

        // Pick adjacent lease-payment instance amount, return Zero otherwise
        // Does the current period have a payment?
        return findPaymentAmountWithinPeriod(leasePayments, currentPeriod);
    }

    private BigDecimal findPaymentAmountWithinPeriod(List<LeasePaymentDTO> leasePayments, LeaseRepaymentPeriodDTO leasePeriod) {
        for (LeasePaymentDTO leasePayment : leasePayments) {
            if (isPaymentDateWithinPeriod(leasePayment.getPaymentDate(), leasePeriod)) {
                return leasePayment.getPaymentAmount();
            }
        }
        return BigDecimal.ZERO;
    }

    private static boolean isPaymentDateWithinPeriod(LocalDate paymentDate, LeaseRepaymentPeriodDTO leasePeriod) {
        return (paymentDate.isEqual(leasePeriod.getStartDate()) || paymentDate.isAfter(leasePeriod.getStartDate())) &&
            (paymentDate.isEqual(leasePeriod.getEndDate()) || paymentDate.isBefore(leasePeriod.getEndDate()));
    }
}
