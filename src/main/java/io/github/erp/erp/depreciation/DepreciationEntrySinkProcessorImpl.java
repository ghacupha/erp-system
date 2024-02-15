package io.github.erp.erp.depreciation;

import io.github.erp.domain.DepreciationEntry;
import io.github.erp.erp.depreciation.context.DepreciationJobContext;
import io.github.erp.internal.service.InternalDepreciationEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.erp.erp.depreciation.DepreciationJobSequenceServiceImpl.PREFERRED_BATCH_SIZE;

@Transactional
@Service
public class DepreciationEntrySinkProcessorImpl implements DepreciationEntrySinkProcessor {

    private final static Logger log = LoggerFactory.getLogger(DepreciationEntrySinkProcessorImpl.class);

    private final InternalDepreciationEntryService depreciationEntryService;

    private List<DepreciationEntry> buffer = new ArrayList<>();
    private final int batchSizeThreshold = PREFERRED_BATCH_SIZE;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private DepreciationJobContext contextManager = DepreciationJobContext.getInstance();

    public DepreciationEntrySinkProcessorImpl(InternalDepreciationEntryService depreciationEntryService) {
        this.depreciationEntryService = depreciationEntryService;
    }

    public void addDepreciationEntry(DepreciationEntry entry, UUID depreciationJobCountDownContextId) {
        buffer.add(entry);

        if (buffer.size() >= batchSizeThreshold) {

            flushBuffer(depreciationJobCountDownContextId);
        }
    }

    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public void flushRemainingItems(UUID depreciationJobCountDownContextId) {

        this.flushBuffer(depreciationJobCountDownContextId);
    }

    private void flushBuffer(UUID depreciationJobCountDownContextId) {

        log.info("Depreciation buffer flushing {} items to the sink", buffer.size());

        if (!buffer.isEmpty()) {

            int numberOfPersistedEntries = this.flushBuffer();

            int pendingItems = contextManager.getNumberOfProcessedItems(depreciationJobCountDownContextId);

            contextManager.updateNumberOfProcessedItems(depreciationJobCountDownContextId, -numberOfPersistedEntries);

            log.info("{} entries successfully saved to the data-sink. {} pending for persistence", numberOfPersistedEntries, pendingItems);
        }
    }

    private int flushBuffer() {

        int flushedItems = buffer.size();

        log.info("Depreciation buffer flushing {} items to the sink", flushedItems);

        this.saveEntries();

        return flushedItems;
    }

    private void saveEntries() {

        if (!buffer.isEmpty()) {

            List<DepreciationEntry> entriesToPersist = new ArrayList<>(buffer);

            log.info("Clearing buffer...");

            buffer.clear();

            log.info("Saving {} entries to the database", entriesToPersist.size());

            // depreciationEntryService.saveAll(entriesToPersist);

            executor.execute(() -> depreciationEntryService.saveAll(entriesToPersist));
        }
    }
}
