package io.github.erp.internal.model.mapping;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.6-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.DepreciationRegime;

public class DepreciationRegimeMapUtils {

    private static final String STRAIGHT_LINE_BASIS_STRING = "straight line basis";
    private static final String DECLINING_BALANCE_BASIS_STRING = "declining balance basis";

    public static DepreciationRegime depreciationRegime(String stringDesignation) {

        if (stringDesignation.equalsIgnoreCase(STRAIGHT_LINE_BASIS_STRING))
            return DepreciationRegime.STRAIGHT_LINE_BASIS;
        if (stringDesignation.equalsIgnoreCase(DECLINING_BALANCE_BASIS_STRING))
            return DepreciationRegime.DECLINING_BALANCE_BASIS;
        return null;
    }

    public static String depreciationRegime(DepreciationRegime depreciationRegime) {

        switch (depreciationRegime) {
            case STRAIGHT_LINE_BASIS:
                return STRAIGHT_LINE_BASIS_STRING;
            case DECLINING_BALANCE_BASIS:
                return DECLINING_BALANCE_BASIS_STRING;
            default: throw new IllegalArgumentException("You want to review your depreciation regime, just one more time!");
        }
    }
}
