package io.github.erp.erp.depreciation.calculation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DepreciationConstants {

    public static final int DECIMAL_SCALE = 6;
    public static final int MONEY_SCALE = 2;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    public static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
}
