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
import java.math.BigDecimal;
import java.time.LocalDate;

public class PresentValueLine {

    private final Integer sequenceNumber;
    private final LocalDate paymentDate;
    private final BigDecimal paymentAmount;
    private final BigDecimal discountRate;
    private final BigDecimal presentValue;

    public PresentValueLine(Integer sequenceNumber, LocalDate paymentDate, BigDecimal paymentAmount, BigDecimal discountRate, BigDecimal presentValue) {
        this.sequenceNumber = sequenceNumber;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.discountRate = discountRate;
        this.presentValue = presentValue;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public BigDecimal getPresentValue() {
        return presentValue;
    }
}
