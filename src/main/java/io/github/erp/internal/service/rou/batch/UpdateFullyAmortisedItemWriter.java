package io.github.erp.internal.service.rou.batch;

import io.github.erp.internal.service.rou.InternalRouModelMetadataService;
import io.github.erp.service.dto.RouModelMetadataDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;

public class UpdateFullyAmortisedItemWriter implements ItemWriter<RouModelMetadataDTO> {

    private final InternalRouModelMetadataService metadataService;

    public UpdateFullyAmortisedItemWriter(InternalRouModelMetadataService metadataService) {
        this.metadataService = metadataService;
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

        metadataService.saveAll(new ArrayList<>(items));
    }
}
