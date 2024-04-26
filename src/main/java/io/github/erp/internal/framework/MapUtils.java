package io.github.erp.internal.framework;

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
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static io.github.erp.internal.framework.AppConstants.DATETIME_FORMATTER;

/**
 * General utilities for mappings and conversion
 */
public class MapUtils {

    public static LocalDate dateStringToLocalDate(String dateString) {
        return LocalDate.parse(dateString, DATETIME_FORMATTER);
    }

    public static String localDateToDateString(LocalDate localDateValue) {
        return DATETIME_FORMATTER.format(localDateValue);
    }

    public static Double bigDecimalToDouble(BigDecimal bigDecimalValue) {
        return NumberUtils.toDouble(bigDecimalValue);
    }

    public static BigDecimal doubleToBigDecimal(Double doubleValue) {
        return NumberUtils.toScaledBigDecimal(doubleValue, 2, RoundingMode.HALF_EVEN);
    }
}
