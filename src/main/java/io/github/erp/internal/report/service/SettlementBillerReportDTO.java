package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.2
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
