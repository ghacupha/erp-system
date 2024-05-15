package io.github.erp.internal.service.rou.batch;

import io.github.erp.service.dto.RouModelMetadataDTO;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class RouModelMetadataItemReader implements ItemReader<RouModelMetadataDTO> {

    @Autowired
    private InternalRouModelMetadataService rouModelMetadataService;

    private Iterator<RouModelMetadataDTO> metadataIterator;

    @Override
    public RouModelMetadataDTO read() {
        if (metadataIterator == null || !metadataIterator.hasNext()) {
            List<RouModelMetadataDTO> metadataList = rouModelMetadataService.getAllMetadata();
            metadataIterator = metadataList.iterator();
        }
        return metadataIterator.hasNext() ? metadataIterator.next() : null;
    }
}
