package io.github.erp.erp.assets.depreciation;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
