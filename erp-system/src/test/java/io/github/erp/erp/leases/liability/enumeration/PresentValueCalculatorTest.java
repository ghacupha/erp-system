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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void usesAnchorDateForSequencing() {
        LeasePayment marchPayment = new LeasePayment().paymentAmount(new BigDecimal("300"))
            .paymentDate(LocalDate.of(2019, 3, 15));

        List<PresentValueLine> lines = calculator.calculate(
            Arrays.asList(marchPayment),
            new BigDecimal("0.12"),
            LiabilityTimeGranularity.MONTHLY
        );

        assertThat(lines).hasSize(3);
        assertThat(lines.get(0).getPaymentDate()).isEqualTo(LocalDate.of(2019, 1, 1));
        assertThat(lines.get(0).getPresentValue()).isEqualByComparingTo(BigDecimal.ZERO.setScale(2));
        BigDecimal monthlyRate = new BigDecimal("0.12").divide(new BigDecimal("12"), new MathContext(20, RoundingMode.HALF_EVEN));
        BigDecimal expected = marchPayment
            .getPaymentAmount()
            .divide(BigDecimal.ONE.add(monthlyRate).pow(3, new MathContext(20, RoundingMode.HALF_EVEN)), 21, RoundingMode.HALF_EVEN)
            .setScale(2, RoundingMode.HALF_EVEN);
        assertThat(lines.get(2).getPresentValue()).isEqualByComparingTo(expected);
        assertThat(lines.get(2).getSequenceNumber()).isEqualTo(3);
    }

    @Test
    void filtersOutPaymentsBeforeAnchorDate() {
        LeasePayment dec2018 = new LeasePayment().paymentAmount(new BigDecimal("100"))
            .paymentDate(LocalDate.of(2018, 12, 15));
        LeasePayment feb2019 = new LeasePayment().paymentAmount(new BigDecimal("150"))
            .paymentDate(LocalDate.of(2019, 2, 15));

        List<PresentValueLine> lines = calculator.calculate(
            Arrays.asList(dec2018, feb2019),
            new BigDecimal("0.12"),
            LiabilityTimeGranularity.MONTHLY
        );

        assertThat(lines).hasSize(2);
        assertThat(lines.get(0).getPaymentDate()).isEqualTo(LocalDate.of(2019, 1, 1));
        assertThat(lines.stream().noneMatch(line -> line.getPaymentDate().isEqual(dec2018.getPaymentDate()))).isTrue();

        BigDecimal monthlyRate = new BigDecimal("0.12").divide(new BigDecimal("12"), new MathContext(20, RoundingMode.HALF_EVEN));
        BigDecimal febPresentValue = feb2019
            .getPaymentAmount()
            .divide(BigDecimal.ONE.add(monthlyRate).pow(2, new MathContext(20, RoundingMode.HALF_EVEN)), 21, RoundingMode.HALF_EVEN)
            .setScale(2, RoundingMode.HALF_EVEN);
        assertThat(lines.get(1).getPresentValue()).isEqualByComparingTo(febPresentValue);
    }

    @Test
    void failsWhenAllPaymentsPrecedeAnchorDate() {
        LeasePayment oldPayment = new LeasePayment().paymentAmount(new BigDecimal("200"))
            .paymentDate(LocalDate.of(2018, 5, 20));

        assertThatThrownBy(
            () -> calculator.calculate(Arrays.asList(oldPayment), new BigDecimal("0.05"), LiabilityTimeGranularity.MONTHLY)
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("anchor date 2019-01-01");
    }
}
