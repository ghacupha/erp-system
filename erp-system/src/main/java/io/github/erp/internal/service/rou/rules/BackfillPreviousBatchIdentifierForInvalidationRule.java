package io.github.erp.internal.service.rou.rules;

import io.github.erp.service.dto.RouDepreciationRequestDTO;
import java.util.UUID;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.BasicRule;

public class BackfillPreviousBatchIdentifierForInvalidationRule extends BasicRule {

    public BackfillPreviousBatchIdentifierForInvalidationRule() {
        super("backfillPreviousBatchIdentifierForInvalidation");
    }

    @Override
    public boolean evaluate(Facts facts) {
        RouDepreciationValidationOperation operation = facts.get(RouDepreciationValidationFacts.OPERATION);
        if (operation != RouDepreciationValidationOperation.INVALIDATE) {
            return false;
        }
        RouDepreciationRequestDTO request = facts.get(RouDepreciationValidationFacts.REQUEST);
        return request.getBatchJobIdentifier() == null;
    }

    @Override
    public void execute(Facts facts) {
        RouDepreciationValidationResult result = facts.get(RouDepreciationValidationFacts.RESULT);
        UUID generated = UUID.randomUUID();
        result.setPreviousBatchJobIdentifier(generated);
        result.addMessage("Generated placeholder batch job identifier for first-time invalidation.");
    }
}
