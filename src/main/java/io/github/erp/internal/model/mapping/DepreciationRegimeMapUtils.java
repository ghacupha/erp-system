package io.github.erp.internal.model.mapping;

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
