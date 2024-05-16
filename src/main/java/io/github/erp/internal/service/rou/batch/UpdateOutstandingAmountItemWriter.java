package io.github.erp.internal.service.rou.batch;

import io.github.erp.internal.service.rou.InternalRouDepreciationEntryService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;

public class UpdateOutstandingAmountItemWriter implements ItemWriter<RouDepreciationEntryDTO> {

    private final InternalRouDepreciationEntryService depreciationEntryService;

    public UpdateOutstandingAmountItemWriter(InternalRouDepreciationEntryService depreciationEntryService) {
        this.depreciationEntryService = depreciationEntryService;
    }

    @Override
    public void write(@NotNull List<? extends RouDepreciationEntryDTO> items) {

        // TODO surprised this works, rigorous tests required
        depreciationEntryService.save(new ArrayList<>(items));
    }
}

