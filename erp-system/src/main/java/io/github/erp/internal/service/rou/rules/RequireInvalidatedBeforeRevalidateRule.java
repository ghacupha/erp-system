package io.github.erp.internal.service.rou.rules;

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

import io.github.erp.service.dto.RouDepreciationRequestDTO;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.BasicRule;

public class RequireInvalidatedBeforeRevalidateRule extends BasicRule {

    public RequireInvalidatedBeforeRevalidateRule() {
        super("requireInvalidatedBeforeRevalidate");
    }

    @Override
    public boolean evaluate(Facts facts) {
        RouDepreciationValidationOperation operation = facts.get(RouDepreciationValidationFacts.OPERATION);
        if (operation != RouDepreciationValidationOperation.REVALIDATE) {
            return false;
        }
        RouDepreciationRequestDTO request = facts.get(RouDepreciationValidationFacts.REQUEST);
        return !Boolean.TRUE.equals(request.getInvalidated());
    }

    @Override
    public void execute(Facts facts) {
        RouDepreciationValidationResult result = facts.get(RouDepreciationValidationFacts.RESULT);
        result.reject("ROU depreciation request must be in an invalidated state before it can be revalidated.");
    }
}
