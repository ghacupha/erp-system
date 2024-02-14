package io.github.erp.erp.depreciation;

import io.github.erp.domain.DepreciationEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DepreciationEntrySinkProcessorImpl implements DepreciationEntrySinkProcessor {

    private List<DepreciationEntry> buffer = new ArrayList<>();
    private final int batchSizeThreshold = 100; // Example threshold for batch size
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void addDepreciationEntry(DepreciationEntry entry) {
        buffer.add(entry);
        if (buffer.size() >= batchSizeThreshold) {
            flushBuffer(); // Flush buffer if threshold is reached
        }
    }

    private void flushBuffer() {
        List<DepreciationEntry> entriesToPersist = new ArrayList<>(buffer);
        buffer.clear();

        executor.execute(() -> {
            // Code to persist entriesToPersist to the database
            // This could be your existing database persistence logic
            // Example:
            for (DepreciationEntry entry : entriesToPersist) {
                // Persist entry to the database
                // database.persist(entry);
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}
