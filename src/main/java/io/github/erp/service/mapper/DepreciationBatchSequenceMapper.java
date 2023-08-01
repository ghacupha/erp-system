package io.github.erp.service.mapper;

import io.github.erp.domain.DepreciationBatchSequence;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepreciationBatchSequence} and its DTO {@link DepreciationBatchSequenceDTO}.
 */
@Mapper(componentModel = "spring", uses = { DepreciationJobMapper.class })
public interface DepreciationBatchSequenceMapper extends EntityMapper<DepreciationBatchSequenceDTO, DepreciationBatchSequence> {
    @Mapping(target = "depreciationJob", source = "depreciationJob", qualifiedByName = "id")
    DepreciationBatchSequenceDTO toDto(DepreciationBatchSequence s);
}
