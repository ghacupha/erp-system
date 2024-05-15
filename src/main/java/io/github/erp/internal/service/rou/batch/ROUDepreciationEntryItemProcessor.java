package io.github.erp.internal.service.rou.batch;

import io.github.erp.internal.service.rou.ROUDepreciationEntryCompilationService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.dto.RouModelMetadataDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ROUDepreciationEntryItemProcessor implements ItemProcessor<RouModelMetadataDTO, List<RouDepreciationEntryDTO>> {

    private final ROUDepreciationEntryCompilationService rouDepreciationEntryCompilationService;

    public ROUDepreciationEntryItemProcessor(ROUDepreciationEntryCompilationService rouDepreciationEntryCompilationService) {
        this.rouDepreciationEntryCompilationService = rouDepreciationEntryCompilationService;
    }

    @Override
    public List<RouDepreciationEntryDTO> process(@NotNull RouModelMetadataDTO model) {
        return rouDepreciationEntryCompilationService.compileDepreciationEntries(model);
    }
}
