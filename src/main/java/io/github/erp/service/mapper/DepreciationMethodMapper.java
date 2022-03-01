package io.github.erp.service.mapper;

import io.github.erp.domain.DepreciationMethod;
import io.github.erp.service.dto.DepreciationMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepreciationMethod} and its DTO {@link DepreciationMethodDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface DepreciationMethodMapper extends EntityMapper<DepreciationMethodDTO, DepreciationMethod> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    DepreciationMethodDTO toDto(DepreciationMethod s);

    @Mapping(target = "removePlaceholder", ignore = true)
    DepreciationMethod toEntity(DepreciationMethodDTO depreciationMethodDTO);
}
