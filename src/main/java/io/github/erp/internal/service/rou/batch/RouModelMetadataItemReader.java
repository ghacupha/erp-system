package io.github.erp.internal.service.rou.batch;

import io.github.erp.internal.service.rou.InternalRouModelMetadataService;
import io.github.erp.service.dto.RouModelMetadataDTO;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class RouModelMetadataItemReader implements ItemReader<RouModelMetadataDTO> {

    private final InternalRouModelMetadataService rouModelMetadataService;

    private Iterator<RouModelMetadataDTO> metadataIterator;

    private final long rouDepreciationRequestId;

    public RouModelMetadataItemReader(InternalRouModelMetadataService rouModelMetadataService, long rouDepreciationRequestId) {
        this.rouModelMetadataService = rouModelMetadataService;
        this.rouDepreciationRequestId = rouDepreciationRequestId;
    }

    @Override
    public RouModelMetadataDTO read() {
        if (metadataIterator == null || !metadataIterator.hasNext()) {

            metadataIterator = rouModelMetadataService.getDepreciationAdjacentMetadataItems(rouDepreciationRequestId)
                .map(List::iterator)
                .orElse(null);

        }
        return metadataIterator.hasNext() ? metadataIterator.next() : null;
    }
}
