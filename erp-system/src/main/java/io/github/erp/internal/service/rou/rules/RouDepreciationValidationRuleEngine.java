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
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.stereotype.Component;

@Component
public class RouDepreciationValidationRuleEngine {

    private final RulesEngine rulesEngine;
    private final Rules invalidationRules;
    private final Rules revalidationRules;

    public RouDepreciationValidationRuleEngine() {
        this.rulesEngine = new DefaultRulesEngine();
        this.invalidationRules = new Rules(
            new PreventInvalidationWhenAlreadyInvalidatedRule(),
            new BackfillPreviousBatchIdentifierForInvalidationRule()
        );
        this.revalidationRules = new Rules(
            new RequireInvalidatedBeforeRevalidateRule(),
            new RequirePreviousBatchIdentifierForRevalidationRule()
        );
    }

    public RouDepreciationValidationResult evaluateInvalidation(RouDepreciationRequestDTO requestDTO) {
        return fireRules(requestDTO, RouDepreciationValidationOperation.INVALIDATE, invalidationRules);
    }

    public RouDepreciationValidationResult evaluateRevalidation(RouDepreciationRequestDTO requestDTO) {
        return fireRules(requestDTO, RouDepreciationValidationOperation.REVALIDATE, revalidationRules);
    }

    private RouDepreciationValidationResult fireRules(
        RouDepreciationRequestDTO requestDTO,
        RouDepreciationValidationOperation operation,
        Rules rules
    ) {
        Facts facts = new Facts();
        RouDepreciationValidationResult result = new RouDepreciationValidationResult(requestDTO.getBatchJobIdentifier());
        facts.put(RouDepreciationValidationFacts.REQUEST, requestDTO);
        facts.put(RouDepreciationValidationFacts.OPERATION, operation);
        facts.put(RouDepreciationValidationFacts.RESULT, result);
        rulesEngine.fire(rules, facts);
        return result;
    }
}
