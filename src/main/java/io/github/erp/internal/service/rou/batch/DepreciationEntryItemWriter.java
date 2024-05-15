package io.github.erp.internal.service.rou.batch;

import io.github.erp.internal.service.rou.InternalRouDepreciationEntryService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepreciationEntryItemWriter implements ItemWriter<List<RouDepreciationEntryDTO>> {

    @Autowired
    private InternalRouDepreciationEntryService rouDepreciationEntryService;

    @Override
    public void write(List<? extends List<RouDepreciationEntryDTO>> items) throws Exception {
        for (List<RouDepreciationEntryDTO> entries : items) {
            rouDepreciationEntryService.save(entries);
        }
    }
}
