package io.github.erp.erp.depreciation;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import io.github.erp.domain.PrepaymentMarshalling;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FiscalMonthNotConfiguredException extends IllegalStateException {
    public FiscalMonthNotConfiguredException(DepreciationBatchMessage message) {
    }

    public FiscalMonthNotConfiguredException(PrepaymentMarshalling marshalling, LocalDate startDate, LocalDate endDate) {

        super("Fiscal month not configured for start-date: " +
            startDate.format(DateTimeFormatter.ISO_DATE) + " and end-date: " +
            endDate.format(DateTimeFormatter.ISO_DATE) +
            " While marshalling item catalogued # " + marshalling.getPrepaymentAccount().getCatalogueNumber());
    }
}
