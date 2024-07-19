package io.github.erp.internal.service.leases;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.github.erp.internal.service.rou.InternalLeasePeriodService;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import io.github.erp.service.dto.LeaseAmortizationCalculationDTO;
import io.github.erp.service.dto.LeaseAmortizationScheduleDTO;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.dto.LeasePeriodDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LeaseAmortizationService implements LeaseAmortizationCompilationService {

    private static final RoundingMode ROUND_HALF_EVEN = RoundingMode.HALF_EVEN;

    private final InternalLeasePeriodService leasePeriodService;
    private final InternalLeasePaymentService leasePaymentService;
    private final InternalLeaseLiabilityService leaseLiabilityService;
    private final InternalLeaseAmortizationCalculationService leaseAmortizationCalculationService;
    private final InternalLeaseAmortizationScheduleService internalLeaseAmortizationScheduleService;
    private final InternalIFRS16LeaseContractService leaseContractService;

    public LeaseAmortizationService(
        InternalLeaseLiabilityService leaseLiabilityService,
        InternalLeaseAmortizationCalculationService leaseAmortizationCalculationService,
        InternalLeasePeriodService leasePeriodService,
        InternalLeasePaymentService leasePaymentService,
        InternalLeaseAmortizationScheduleService internalLeaseAmortizationScheduleService,
        InternalIFRS16LeaseContractService leaseContractService) {
        this.leaseLiabilityService = leaseLiabilityService;
        this.leaseAmortizationCalculationService = leaseAmortizationCalculationService;
        this.leasePeriodService = leasePeriodService;
        this.leasePaymentService = leasePaymentService;
        this.internalLeaseAmortizationScheduleService = internalLeaseAmortizationScheduleService;
        this.leaseContractService = leaseContractService;
    }

    public List<LeaseLiabilityScheduleItemDTO> generateAmortizationSchedule(Long leaseLiabilityId) {
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

        BigDecimal principal = leaseLiability.getLiabilityAmount();
        BigDecimal interestRate = calculation.getInterestRate();
        int periods = calculation.getNumberOfPeriods();

        return calculateAmortizationSchedule(principal, interestRate, leaseLiability, ifrs16LeaseContract, leaseAmortizationSchedule);
    }

    private List<LeaseLiabilityScheduleItemDTO> calculateAmortizationSchedule(
        BigDecimal principal, BigDecimal interestRate, LeaseLiabilityDTO leaseLiability, IFRS16LeaseContractDTO ifrs16LeaseContract, LeaseAmortizationScheduleDTO leaseAmortizationSchedule) {

        List<LeaseLiabilityScheduleItemDTO> scheduleItems = new ArrayList<>();
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(12), ROUND_HALF_EVEN);
        var openingBalanceRef = new Object() {
            BigDecimal openingBalance = principal;
            BigDecimal interestPayableOpening = BigDecimal.ZERO;
        };

        Optional<List<LeasePeriodDTO>> leasePeriods = leasePeriodService.findLeasePeriods(ifrs16LeaseContract);

        Optional<List<LeasePaymentDTO>> leasePayments = leasePaymentService.findPaymentsByContractId(ifrs16LeaseContract.getId());

        if (leasePayments.isEmpty()) {
            throw new IllegalArgumentException("No lease-payments prescribed for lease-booking # " + ifrs16LeaseContract.getBookingId() + ". Please record lease-payments and try again");
        }

        leasePeriods.ifPresent(periods -> {
            for (int period = 0; period < periods.size(); period++) {
                BigDecimal interestAccrued = openingBalanceRef.openingBalance.multiply(monthlyRate);
                BigDecimal totalPayment = calculateMonthlyPayment(leasePayments.get(), periods.get(period));
                BigDecimal principalPayment = totalPayment.subtract(interestAccrued).max(BigDecimal.ZERO);
                BigDecimal interestPayment = totalPayment.subtract(principalPayment).max(BigDecimal.ZERO);
                BigDecimal closingBalance = openingBalanceRef.openingBalance.subtract(principalPayment);
                BigDecimal interestPayableClosing = openingBalanceRef.interestPayableOpening.add(interestAccrued).subtract(interestPayment);

                LeaseLiabilityScheduleItemDTO item = new LeaseLiabilityScheduleItemDTO();
                item.setSequenceNumber(period);
                item.setOpeningBalance(openingBalanceRef.openingBalance);
                item.setOutstandingBalance(closingBalance);
                item.setCashPayment(totalPayment);
                item.setPrincipalPayment(principalPayment);
                item.setInterestPayment(interestAccrued);
                item.setInterestPayableOpening(openingBalanceRef.interestPayableOpening);
                item.setInterestAccrued(interestAccrued);
                item.setInterestPayableClosing(interestPayableClosing);
                item.setLeaseLiability(leaseLiability);
                item.setLeaseContract(ifrs16LeaseContract);
                item.setLeaseAmortizationSchedule(leaseAmortizationSchedule);
                item.setLeasePeriod(periods.get(period));

                scheduleItems.add(item);

                // Update balances for the next period
                openingBalanceRef.openingBalance = closingBalance;
                openingBalanceRef.interestPayableOpening = interestPayableClosing;
            }
        });

        return scheduleItems;
    }

    private BigDecimal calculateMonthlyPayment(List<LeasePaymentDTO> leasePayments, LeasePeriodDTO currentPeriod) {

        // Pick adjacent lease-payment instance amount, return Zero otherwise
        // Does the current period have a payment?
        return findPaymentAmountWithinPeriod(leasePayments, currentPeriod);
    }

    private BigDecimal findPaymentAmountWithinPeriod(List<LeasePaymentDTO> leasePayments, LeasePeriodDTO leasePeriod) {
        for (LeasePaymentDTO leasePayment : leasePayments) {
            if (isPaymentDateWithinPeriod(leasePayment.getPaymentDate(), leasePeriod)) {
                return leasePayment.getPaymentAmount();
            }
        }
        return BigDecimal.ZERO;
    }

    private static boolean isPaymentDateWithinPeriod(LocalDate paymentDate, LeasePeriodDTO leasePeriod) {
        return (paymentDate.isEqual(leasePeriod.getStartDate()) || paymentDate.isAfter(leasePeriod.getStartDate())) &&
            (paymentDate.isEqual(leasePeriod.getEndDate()) || paymentDate.isBefore(leasePeriod.getEndDate()));
    }
}
