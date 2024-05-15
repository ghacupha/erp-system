package io.github.erp.internal.service.rou.batch;

import io.github.erp.domain.RouDepreciationEntry;
import io.github.erp.internal.service.rou.InternalRouDepreciationEntryService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.mapper.RouDepreciationEntryMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

public class UpdateOutstandingAmountProcessor implements ItemProcessor<RouDepreciationEntry, RouDepreciationEntryDTO> {

    private final RouDepreciationEntryMapper rouDepreciationEntryMapper;
    private final InternalRouDepreciationEntryService depreciationEntryService;

    public UpdateOutstandingAmountProcessor(RouDepreciationEntryMapper rouDepreciationEntryMapper, InternalRouDepreciationEntryService depreciationEntryService) {
        this.rouDepreciationEntryMapper = rouDepreciationEntryMapper;
        this.depreciationEntryService = depreciationEntryService;
    }

    @Override
    public RouDepreciationEntryDTO process(@NotNull RouDepreciationEntry entry) {
        BigDecimal outstandingAmount = depreciationEntryService.calculateOutstandingAmount(rouDepreciationEntryMapper.toDto(entry));
        entry.setOutstandingAmount(outstandingAmount);
        return rouDepreciationEntryMapper.toDto(entry);
    }
}
