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
import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.domain.LeasePayment;
import io.github.erp.domain.enumeration.LiabilityTimeGranularity;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class PresentValueCalculatorTest {

    private final PresentValueCalculator calculator = new PresentValueCalculator();

    @Test
    void calculatesMonthlyPresentValuesFromFirstPaymentMonth() {
        LeasePayment janPayment = new LeasePayment().paymentAmount(new BigDecimal("100"))
            .paymentDate(LocalDate.of(2024, 1, 15));
        LeasePayment febPayment = new LeasePayment().paymentAmount(new BigDecimal("100"))
            .paymentDate(LocalDate.of(2024, 2, 15));
        LeasePayment marPayment = new LeasePayment().paymentAmount(new BigDecimal("100"))
            .paymentDate(LocalDate.of(2024, 3, 15));

        List<PresentValueLine> lines = calculator.calculate(
            Arrays.asList(febPayment, marPayment, janPayment),
            new BigDecimal("0.12"),
            LiabilityTimeGranularity.MONTHLY
        );

        assertThat(lines).hasSize(3);
        BigDecimal monthlyRate = new BigDecimal("0.12").divide(new BigDecimal("12"), new MathContext(20, RoundingMode.HALF_EVEN));
        BigDecimal firstExpected = janPayment
            .getPaymentAmount()
            .divide(BigDecimal.ONE.add(monthlyRate), 21, RoundingMode.HALF_EVEN)
            .setScale(2, RoundingMode.HALF_EVEN);
        assertThat(lines.get(0).getPresentValue()).isEqualByComparingTo(firstExpected);
    }

    @Test
    void alignsQuarterlyStepsToAnchorMonth() {
        LeasePayment janPayment = new LeasePayment().paymentAmount(new BigDecimal("500"))
            .paymentDate(LocalDate.of(2024, 1, 5));
        LeasePayment aprPayment = new LeasePayment().paymentAmount(new BigDecimal("500"))
            .paymentDate(LocalDate.of(2024, 4, 5));

        List<PresentValueLine> lines = calculator.calculate(
            Arrays.asList(janPayment, aprPayment),
            new BigDecimal("0.08"),
            LiabilityTimeGranularity.QUARTERLY
        );

        BigDecimal quarterlyRate = new BigDecimal("0.08").divide(new BigDecimal("4"), new MathContext(20, RoundingMode.HALF_EVEN));
        BigDecimal firstPV = janPayment
            .getPaymentAmount()
            .divide(BigDecimal.ONE.add(quarterlyRate), 21, RoundingMode.HALF_EVEN)
            .setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal secondPV = aprPayment
            .getPaymentAmount()
            .divide(BigDecimal.ONE.add(quarterlyRate).pow(2, new MathContext(20, RoundingMode.HALF_EVEN)), 21, RoundingMode.HALF_EVEN)
            .setScale(2, RoundingMode.HALF_EVEN);

        assertThat(lines.get(0).getPresentValue()).isEqualByComparingTo(firstPV);
        assertThat(lines.get(1).getPresentValue()).isEqualByComparingTo(secondPV);
        assertThat(lines.get(0).getSequenceNumber()).isEqualTo(1);
        assertThat(lines.get(1).getSequenceNumber()).isEqualTo(2);
    }
}
