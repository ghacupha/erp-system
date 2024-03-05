package io.github.erp.internal.report.service;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.FiscalMonth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NetBookValueEntryVM implements Serializable {

    Long id;

    String assetNumber;

    String assetTag;

    String assetDescription;

    UUID nbvIdentifier;

    UUID compilationJobIdentifier;

    UUID compilationBatchIdentifier;

    Integer elapsedMonths;

    Integer priorMonths;

    Integer usefulLifeYears;

    BigDecimal netBookValueAmount;

    BigDecimal previousNetBookValueAmount;

    BigDecimal historicalCost;

    LocalDate capitalizationDate;

    String serviceOutlet;

    String depreciationPeriod;

    String fiscalMonth;

    String depreciationMethod;

    String assetCategory;
}
