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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.github.erp.domain.LeaseAmortizationCalculation;
import io.github.erp.domain.LeaseLiability;
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.internal.repository.InternalLeaseAmortizationCalculationRepository;
import io.github.erp.repository.LeaseLiabilityRepository;
import io.github.erp.repository.LeaseLiabilityScheduleItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LeaseAmortizationService {

    private final LeaseLiabilityRepository leaseLiabilityRepository;
    private final InternalLeaseAmortizationCalculationRepository leaseAmortizationCalculationRepository;
    private final LeaseLiabilityScheduleItemRepository leaseAmortizationScheduleItemRepository;

    public LeaseAmortizationService(LeaseLiabilityRepository leaseLiabilityRepository,
                                    InternalLeaseAmortizationCalculationRepository leaseAmortizationCalculationRepository,
                                    LeaseLiabilityScheduleItemRepository leaseAmortizationScheduleItemRepository) {
        this.leaseLiabilityRepository = leaseLiabilityRepository;
        this.leaseAmortizationCalculationRepository = leaseAmortizationCalculationRepository;
        this.leaseAmortizationScheduleItemRepository = leaseAmortizationScheduleItemRepository;
    }

    public void generateAmortizationSchedule(Long leaseLiabilityId) {
        Optional<LeaseLiability> leaseLiabilityOpt = leaseLiabilityRepository.findById(leaseLiabilityId);
        if (leaseLiabilityOpt.isEmpty()) {
            throw new IllegalArgumentException("Lease Liability not found");
        }

        LeaseLiability leaseLiability = leaseLiabilityOpt.get();
        Optional<LeaseAmortizationCalculation> leaseAmortizationCalculationOpt =
            leaseAmortizationCalculationRepository.findByLeaseLiabilityId(leaseLiabilityId);

        if (leaseAmortizationCalculationOpt.isEmpty()) {
            throw new IllegalArgumentException("Lease Amortization Calculation not found");
        }

        LeaseAmortizationCalculation calculation = leaseAmortizationCalculationOpt.get();

        BigDecimal principal = leaseLiability.getLiabilityAmount();
        BigDecimal interestRate = BigDecimal.valueOf(calculation.getInterestRate());
        int periods = calculation.getNumberOfPeriods();

        // TODO
        List<LeaseLiabilityScheduleItem> scheduleItems = calculateAmortizationSchedule(
            principal, interestRate, periods);

        // Save schedule items
        leaseAmortizationScheduleItemRepository.saveAll(scheduleItems);
    }

    private List<LeaseLiabilityScheduleItem> calculateAmortizationSchedule(
        BigDecimal principal, BigDecimal interestRate, int periods) {

        List<LeaseLiabilityScheduleItem> scheduleItems = new ArrayList<>();
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_EVEN);
        BigDecimal openingBalance = principal;
        BigDecimal interestPayableOpening = BigDecimal.ZERO;

        for (int period = 1; period <= periods; period++) {
            BigDecimal interestAccrued = openingBalance.multiply(monthlyRate);
            BigDecimal totalPayment = calculateMonthlyPayment(principal, interestRate, periods);
            BigDecimal principalPayment = totalPayment.subtract(interestAccrued);
            BigDecimal closingBalance = openingBalance.subtract(principalPayment);
            BigDecimal interestPayableClosing = interestPayableOpening.add(interestAccrued).subtract(interestAccrued);

            LeaseLiabilityScheduleItem item = new LeaseLiabilityScheduleItem();
            item.setSequenceNumber(period);
            item.setOpeningBalance(openingBalance);
            item.setOutstandingBalance(closingBalance);
            item.setCashPayment(totalPayment);
            item.setPrincipalPayment(principalPayment);
            item.setInterestPayment(interestAccrued);
            item.setInterestPayableOpening(interestPayableOpening);
            item.setInterestAccrued(interestAccrued);
            item.setInterestPayableClosing(interestPayableClosing);

            scheduleItems.add(item);

            // Update balances for the next period
            openingBalance = closingBalance;
            interestPayableOpening = interestPayableClosing;
        }
        return scheduleItems;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal interestRate, int periods) {
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_EVEN);
        BigDecimal numerator = monthlyRate.multiply(principal);
        BigDecimal denominator = BigDecimal.ONE.subtract(BigDecimal.ONE.divide(
            (BigDecimal.ONE.add(monthlyRate)).pow(periods), BigDecimal.ROUND_HALF_EVEN));
        return numerator.divide(denominator, BigDecimal.ROUND_HALF_EVEN);
    }
}
