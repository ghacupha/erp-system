package io.github.erp.internal.service.rou.rules;

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
