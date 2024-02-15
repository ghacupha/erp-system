package io.github.erp.erp.depreciation;

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
