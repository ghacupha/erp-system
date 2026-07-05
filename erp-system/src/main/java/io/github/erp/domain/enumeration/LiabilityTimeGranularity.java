package io.github.erp.domain.enumeration;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

/**
 * Compounding frequency for lease liability present value calculations.
 */
public enum LiabilityTimeGranularity {
    MONTHLY(12, 1),
    QUARTERLY(4, 3),
    BI_ANNUAL(2, 6),
    YEARLY(1, 12);

    private final int compoundsPerYear;
    private final int monthsPerStep;

    LiabilityTimeGranularity(int compoundsPerYear, int monthsPerStep) {
        this.compoundsPerYear = compoundsPerYear;
        this.monthsPerStep = monthsPerStep;
    }

    public int getCompoundsPerYear() {
        return compoundsPerYear;
    }

    public int getMonthsPerStep() {
        return monthsPerStep;
    }

    public static LiabilityTimeGranularity fromCode(String code) {
        for (LiabilityTimeGranularity value : values()) {
            if (value.name().equalsIgnoreCase(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unsupported compounding granularity: " + code);
    }
}
