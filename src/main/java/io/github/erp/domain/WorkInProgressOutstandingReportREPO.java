package io.github.erp.domain;

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
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class WorkInProgressOutstandingReportREPO {

    private long id;

    private String sequenceNumber;

    private String particulars;

    private String dealerName;

    private String instalmentTransactionNumber;

    private LocalDate instalmentTransactionDate;

    private String iso4217Code;

    private BigDecimal instalmentAmount;

    private BigDecimal totalTransferAmount;

    private BigDecimal outstandingAmount;

    public WorkInProgressOutstandingReportREPO() {
    }

    public WorkInProgressOutstandingReportREPO(long id, String sequenceNumber, String particulars, String dealerName, String instalmentTransactionNumber, LocalDate instalmentTransactionDate, String iso4217Code, BigDecimal instalmentAmount, BigDecimal totalTransferAmount, BigDecimal outstandingAmount) {
        this.id = id;
        this.sequenceNumber = sequenceNumber;
        this.particulars = particulars;
        this.dealerName = dealerName;
        this.instalmentTransactionNumber = instalmentTransactionNumber;
        this.instalmentTransactionDate = instalmentTransactionDate;
        this.iso4217Code = iso4217Code;
        this.instalmentAmount = instalmentAmount;
        this.totalTransferAmount = totalTransferAmount;
        this.outstandingAmount = outstandingAmount;
    }
}
