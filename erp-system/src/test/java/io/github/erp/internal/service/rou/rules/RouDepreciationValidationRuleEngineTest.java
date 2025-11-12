package io.github.erp.internal.service.rou.rules;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.service.dto.RouDepreciationRequestDTO;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RouDepreciationValidationRuleEngineTest {

    private final RouDepreciationValidationRuleEngine engine = new RouDepreciationValidationRuleEngine();

    @Test
    void invalidationShouldRejectWhenRequestAlreadyInvalidated() {
        RouDepreciationRequestDTO request = new RouDepreciationRequestDTO();
        request.setInvalidated(true);

        RouDepreciationValidationResult result = engine.evaluateInvalidation(request);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessages()).contains("ROU depreciation request is already invalidated and cannot be invalidated again.");
    }

    @Test
    void invalidationShouldBackfillBatchIdentifierWhenMissing() {
        RouDepreciationRequestDTO request = new RouDepreciationRequestDTO();
        request.setInvalidated(false);
        request.setBatchJobIdentifier(null);

        RouDepreciationValidationResult result = engine.evaluateInvalidation(request);

        assertThat(result.isValid()).isTrue();
        assertThat(result.getPreviousBatchJobIdentifier()).isPresent();
        assertThat(result.getMessages()).contains("Generated placeholder batch job identifier for first-time invalidation.");
    }

    @Test
    void invalidationShouldRetainExistingBatchIdentifier() {
        RouDepreciationRequestDTO request = new RouDepreciationRequestDTO();
        UUID existingIdentifier = UUID.randomUUID();
        request.setInvalidated(false);
        request.setBatchJobIdentifier(existingIdentifier);

        RouDepreciationValidationResult result = engine.evaluateInvalidation(request);

        assertThat(result.isValid()).isTrue();
        assertThat(result.getPreviousBatchJobIdentifier()).contains(existingIdentifier);
    }

    @Test
    void revalidationShouldRejectWhenRequestNotInvalidated() {
        RouDepreciationRequestDTO request = new RouDepreciationRequestDTO();
        request.setInvalidated(false);
        request.setBatchJobIdentifier(UUID.randomUUID());

        RouDepreciationValidationResult result = engine.evaluateRevalidation(request);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessages()).contains("ROU depreciation request must be in an invalidated state before it can be revalidated.");
    }

    @Test
    void revalidationShouldRejectWhenMissingPreviousBatchIdentifier() {
        RouDepreciationRequestDTO request = new RouDepreciationRequestDTO();
        request.setInvalidated(true);
        request.setBatchJobIdentifier(null);

        RouDepreciationValidationResult result = engine.evaluateRevalidation(request);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessages()).contains("Cannot revalidate ROU depreciation request without a prior batch job identifier.");
    }

    @Test
    void revalidationShouldSucceedWhenInvalidatedAndHasBatchIdentifier() {
        RouDepreciationRequestDTO request = new RouDepreciationRequestDTO();
        UUID identifier = UUID.randomUUID();
        request.setInvalidated(true);
        request.setBatchJobIdentifier(identifier);

        RouDepreciationValidationResult result = engine.evaluateRevalidation(request);

        assertThat(result.isValid()).isTrue();
        assertThat(result.getPreviousBatchJobIdentifier()).contains(identifier);
        assertThat(result.getMessages()).isEmpty();
    }
}
