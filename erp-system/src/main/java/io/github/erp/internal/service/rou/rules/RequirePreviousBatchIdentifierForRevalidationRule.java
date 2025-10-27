package io.github.erp.internal.service.rou.rules;

import io.github.erp.service.dto.RouDepreciationRequestDTO;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.BasicRule;

public class RequirePreviousBatchIdentifierForRevalidationRule extends BasicRule {

    public RequirePreviousBatchIdentifierForRevalidationRule() {
        super("requirePreviousBatchIdentifierForRevalidation");
    }

    @Override
    public boolean evaluate(Facts facts) {
        RouDepreciationValidationOperation operation = facts.get(RouDepreciationValidationFacts.OPERATION);
        if (operation != RouDepreciationValidationOperation.REVALIDATE) {
            return false;
        }
        RouDepreciationRequestDTO request = facts.get(RouDepreciationValidationFacts.REQUEST);
        return request.getBatchJobIdentifier() == null;
    }

    @Override
    public void execute(Facts facts) {
        RouDepreciationValidationResult result = facts.get(RouDepreciationValidationFacts.RESULT);
        result.reject("Cannot revalidate ROU depreciation request without a prior batch job identifier.");
    }
}
