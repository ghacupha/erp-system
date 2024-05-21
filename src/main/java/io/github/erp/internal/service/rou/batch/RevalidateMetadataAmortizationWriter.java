package io.github.erp.internal.service.rou.batch;

import io.github.erp.internal.service.rou.InternalRouModelMetadataService;
import io.github.erp.service.dto.RouModelMetadataDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;

public class RevalidateMetadataAmortizationWriter implements ItemWriter<RouModelMetadataDTO> {

    private final InternalRouModelMetadataService internalRouModelMetadataService;

    public RevalidateMetadataAmortizationWriter(InternalRouModelMetadataService internalRouModelMetadataService) {
        this.internalRouModelMetadataService = internalRouModelMetadataService;
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
    public void write(@NotNull List<? extends RouModelMetadataDTO> items) throws Exception {
        internalRouModelMetadataService.saveAllWithSearch(new ArrayList<>(items));
    }
}
