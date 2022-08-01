package io.github.erp.service.mapper;

import io.github.erp.domain.WorkInProgressTransfer;
import io.github.erp.service.dto.WorkInProgressTransferDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkInProgressTransfer} and its DTO {@link WorkInProgressTransferDTO}.
 */
@Mapper(componentModel = "spring", uses = { WorkInProgressRegistrationMapper.class, PlaceholderMapper.class })
public interface WorkInProgressTransferMapper extends EntityMapper<WorkInProgressTransferDTO, WorkInProgressTransfer> {
    @Mapping(target = "workInProgressRegistrations", source = "workInProgressRegistrations", qualifiedByName = "idSet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    WorkInProgressTransferDTO toDto(WorkInProgressTransfer s);

    @Mapping(target = "removeWorkInProgressRegistration", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    WorkInProgressTransfer toEntity(WorkInProgressTransferDTO workInProgressTransferDTO);
}
