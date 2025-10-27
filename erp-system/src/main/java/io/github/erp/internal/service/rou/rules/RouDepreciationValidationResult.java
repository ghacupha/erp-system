package io.github.erp.internal.service.rou.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RouDepreciationValidationResult {

    private boolean valid = true;
    private final List<String> messages = new ArrayList<>();
    private UUID previousBatchJobIdentifier;

    public RouDepreciationValidationResult(UUID existingPreviousIdentifier) {
        this.previousBatchJobIdentifier = existingPreviousIdentifier;
    }

    public boolean isValid() {
        return valid;
    }

    public void reject(String message) {
        this.valid = false;
        if (message != null && !message.isBlank()) {
            messages.add(message);
        }
    }

    public void addMessage(String message) {
        if (message != null && !message.isBlank()) {
            messages.add(message);
        }
    }

    public void setPreviousBatchJobIdentifier(UUID previousBatchJobIdentifier) {
        this.previousBatchJobIdentifier = previousBatchJobIdentifier;
    }

    public Optional<UUID> getPreviousBatchJobIdentifier() {
        return Optional.ofNullable(previousBatchJobIdentifier);
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}
