package io.github.erp.domain;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface DepreciationEntryInternal {

    Long getId();

    String getAssetRegistrationDetails();

    String getPostedAt();

    String getAssetNumber();

    String getServiceOutlet();

    String getAssetCategory();

    String getDepreciationMethod();

    String getDepreciationPeriod();

    String getFiscalMonthCode();

    BigDecimal getAssetRegistrationCost();

    BigDecimal getDepreciationAmount();

    Long getElapsedMonths();

    Long getPriorMonths();

    BigDecimal getUsefulLifeYears();

    BigDecimal getPreviousNBV();

    BigDecimal getNetBookValue();

    LocalDate getDepreciationPeriodStartDate();

    LocalDate getDepreciationPeriodEndDate();

    LocalDate getCapitalizationDate();
}
