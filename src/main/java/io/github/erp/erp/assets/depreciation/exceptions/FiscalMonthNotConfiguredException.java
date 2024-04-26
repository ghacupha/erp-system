package io.github.erp.erp.assets.depreciation.exceptions;

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
import io.github.erp.domain.PrepaymentMarshalling;
import io.github.erp.erp.assets.depreciation.model.DepreciationBatchMessage;

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
