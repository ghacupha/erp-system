package io.github.erp.domain;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
