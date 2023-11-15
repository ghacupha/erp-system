package io.github.erp.erp.depreciation.calculation;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DepreciationConstants {

    public static final int DECIMAL_SCALE = 6;
    public static final int MONEY_SCALE = 2;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    public static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
    public static final BigDecimal TEN_THOUSAND = BigDecimal.valueOf(10000);
    public static final BigDecimal ZERO = new BigDecimal("0.00");
}
