package io.github.erp.internal.service.rou.batch;

import io.github.erp.internal.service.rou.InternalRouDepreciationEntryService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class UpdateOutstandingAmountItemWriter implements ItemWriter<RouDepreciationEntryDTO> {

    private final InternalRouDepreciationEntryService depreciationEntryService;

    public UpdateOutstandingAmountItemWriter(InternalRouDepreciationEntryService depreciationEntryService) {
        this.depreciationEntryService = depreciationEntryService;
    }

    @Override
    public void write(List<? extends RouDepreciationEntryDTO> items) {
        depreciationEntryService.save((RouDepreciationEntryDTO) items);
    }
}

