package io.github.erp.erp.assets.depreciation.calculation;

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
