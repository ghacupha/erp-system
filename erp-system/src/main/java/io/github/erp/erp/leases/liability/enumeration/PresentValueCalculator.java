package io.github.erp.erp.leases.liability.enumeration;

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
import io.github.erp.domain.LeasePayment;
import io.github.erp.domain.enumeration.LiabilityTimeGranularity;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PresentValueCalculator {

    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_EVEN);

    public List<PresentValueLine> calculate(
        List<LeasePayment> leasePayments,
        BigDecimal annualRate,
        LiabilityTimeGranularity granularity
    ) {
        if (leasePayments == null || leasePayments.isEmpty()) {
            throw new IllegalArgumentException("At least one lease payment is required to enumerate present values");
        }

        List<LeasePayment> orderedPayments = leasePayments
            .stream()
            .sorted(Comparator.comparing(LeasePayment::getPaymentDate))
            .collect(Collectors.toList());

        LocalDate anchorDate = YearMonth.from(orderedPayments.get(0).getPaymentDate()).atDay(1);
        BigDecimal periodRate = annualRate.divide(BigDecimal.valueOf(LiabilityTimeGranularity.MONTHLY.getCompoundsPerYear()), MC);

        List<PresentValueLine> lines = new ArrayList<>();
        int sequence = 1;
        YearMonth cursorMonth = YearMonth.from(anchorDate);

        for (LeasePayment payment : orderedPayments) {
            YearMonth paymentMonth = YearMonth.from(payment.getPaymentDate());
            while (cursorMonth.isBefore(paymentMonth)) {
                lines.add(buildPresentValueLine(sequence++, cursorMonth.atDay(1), BigDecimal.ZERO, periodRate));
                cursorMonth = cursorMonth.plusMonths(1);
            }

            lines.add(
                buildPresentValueLine(
                    sequence++,
                    payment.getPaymentDate(),
                    payment.getPaymentAmount() == null ? BigDecimal.ZERO : payment.getPaymentAmount(),
                    periodRate
                )
            );
            cursorMonth = paymentMonth.plusMonths(1);
        }

        return lines;
    }

    private PresentValueLine buildPresentValueLine(int sequence, LocalDate date, BigDecimal amount, BigDecimal periodRate) {
        BigDecimal discountFactor = BigDecimal.ONE.add(periodRate).pow(sequence, MC);
        BigDecimal presentValue = amount.divide(discountFactor, 21, RoundingMode.HALF_EVEN).setScale(2, RoundingMode.HALF_EVEN);
        return new PresentValueLine(sequence, date, amount, periodRate, presentValue);
    }
}
