package io.github.erp.internal.service.rou.batch;

import io.github.erp.internal.service.rou.InternalRouDepreciationEntryService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

public class UpdateOutstandingAmountProcessor implements ItemProcessor<RouDepreciationEntryDTO, RouDepreciationEntryDTO> {

    private final InternalRouDepreciationEntryService depreciationEntryService;

    public UpdateOutstandingAmountProcessor(InternalRouDepreciationEntryService depreciationEntryService) {
        this.depreciationEntryService = depreciationEntryService;
    }

    @Override
    public RouDepreciationEntryDTO process(@NotNull RouDepreciationEntryDTO entry) {
        BigDecimal outstandingAmount = depreciationEntryService.calculateOutstandingAmount(entry);
        entry.setOutstandingAmount(outstandingAmount);
        return entry;
    }
}
