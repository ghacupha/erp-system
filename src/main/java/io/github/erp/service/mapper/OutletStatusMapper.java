package io.github.erp.service.mapper;

import io.github.erp.domain.OutletStatus;
import io.github.erp.service.dto.OutletStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OutletStatus} and its DTO {@link OutletStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface OutletStatusMapper extends EntityMapper<OutletStatusDTO, OutletStatus> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    OutletStatusDTO toDto(OutletStatus s);

    @Mapping(target = "removePlaceholder", ignore = true)
    OutletStatus toEntity(OutletStatusDTO outletStatusDTO);

    @Named("branchStatusType")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "branchStatusType", source = "branchStatusType")
    OutletStatusDTO toDtoBranchStatusType(OutletStatus outletStatus);
}
