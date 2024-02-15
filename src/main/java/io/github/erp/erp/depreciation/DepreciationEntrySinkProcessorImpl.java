package io.github.erp.erp.depreciation;

import io.github.erp.domain.DepreciationEntry;
import io.github.erp.internal.service.InternalDepreciationEntryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.erp.erp.depreciation.DepreciationJobSequenceServiceImpl.PREFERRED_BATCH_SIZE;

@Transactional
@Service
public class DepreciationEntrySinkProcessorImpl implements DepreciationEntrySinkProcessor {

    private final InternalDepreciationEntryService depreciationEntryService;

    private List<DepreciationEntry> buffer = new ArrayList<>();
    private final int batchSizeThreshold = PREFERRED_BATCH_SIZE;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public DepreciationEntrySinkProcessorImpl(InternalDepreciationEntryService depreciationEntryService) {
        this.depreciationEntryService = depreciationEntryService;
    }

    public void addDepreciationEntry(DepreciationEntry entry) {
        buffer.add(entry);
        if (buffer.size() >= batchSizeThreshold) {
            flushBuffer(); // Flush buffer if threshold is reached
        }
    }

    private void flushBuffer() {
        List<DepreciationEntry> entriesToPersist = new ArrayList<>(buffer);
        buffer.clear();

        executor.execute(() -> depreciationEntryService.saveAll(entriesToPersist));
    }

    public void shutdown() {
        executor.shutdown();
    }
}
