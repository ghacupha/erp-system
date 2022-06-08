package io.github.erp.service.mapper;

import io.github.erp.domain.MfbBranchCode;
import io.github.erp.service.dto.MfbBranchCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MfbBranchCode} and its DTO {@link MfbBranchCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface MfbBranchCodeMapper extends EntityMapper<MfbBranchCodeDTO, MfbBranchCode> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    MfbBranchCodeDTO toDto(MfbBranchCode s);

    @Mapping(target = "removePlaceholder", ignore = true)
    MfbBranchCode toEntity(MfbBranchCodeDTO mfbBranchCodeDTO);
}
