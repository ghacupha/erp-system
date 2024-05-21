package io.github.erp.internal.service.rou.batch;

import io.github.erp.internal.service.rou.InternalRouDepreciationEntryService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class RevalidateDepreciationEntryWriter implements ItemWriter<RouDepreciationEntryDTO> {

    private final InternalRouDepreciationEntryService internalRouDepreciationEntryService;

    public RevalidateDepreciationEntryWriter(InternalRouDepreciationEntryService internalRouDepreciationEntryService) {
        this.internalRouDepreciationEntryService = internalRouDepreciationEntryService;
    }

    /**
     * Process the supplied data element. Will not be called with any null items
     * in normal operation.
     *
     * @param items items to be written
     * @throws Exception if there are errors. The framework will catch the
     *                   exception and convert or rethrow it as appropriate.
     */
    @Override
    public void write(@NotNull List<? extends RouDepreciationEntryDTO> items) throws Exception {

        internalRouDepreciationEntryService.saveAll(items);
    }
}
