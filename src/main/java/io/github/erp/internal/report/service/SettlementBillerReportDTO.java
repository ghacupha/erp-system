package io.github.erp.internal.report.service;

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
import io.github.erp.domain.enumeration.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@XmlRootElement
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SettlementBillerReportDTO implements Serializable {

    private long id;
    private String biller;
    private String description;
    private String requisitionNumber;
    private String iso4217CurrencyCode;
    private BigDecimal paymentAmount;
    private PaymentStatus paymentStatus;
    private String currentOwner;
    private String nativeOwner;
    private String nativeDepartment;
    private ZonedDateTime timeOfRequisition;
}
