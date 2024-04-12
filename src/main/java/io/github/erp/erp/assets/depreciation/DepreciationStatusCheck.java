package io.github.erp.erp.assets.depreciation;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import org.springframework.stereotype.Service;

@Service
public class DepreciationStatusCheck {

    private final DepreciationEntrySinkProcessorImpl depreciationProcessor;

    public DepreciationStatusCheck(DepreciationEntrySinkProcessorImpl depreciationProcessor) {
        this.depreciationProcessor = depreciationProcessor;
    }

    public void checkProcessStates() {
        // Check process states here
        // If the process is complete, call the shutdown method
        if (isDepreciationProcessComplete()) {
            depreciationProcessor.shutdown();
        }
    }

    private boolean isDepreciationProcessComplete() {
        // Implement logic to check if the depreciation process is complete
        // For example, check if all assets have been processed
        // Return true if the process is complete, false otherwise

        return true;
    }
}
