package io.github.erp.internal.service.rou.rules;

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
