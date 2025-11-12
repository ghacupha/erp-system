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
