package io.github.erp.service.mapper;

import io.github.erp.domain.PrepaymentCompilationRequest;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrepaymentCompilationRequest} and its DTO {@link PrepaymentCompilationRequestDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, ApplicationUserMapper.class })
public interface PrepaymentCompilationRequestMapper extends EntityMapper<PrepaymentCompilationRequestDTO, PrepaymentCompilationRequest> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "applicationIdentity")
    PrepaymentCompilationRequestDTO toDto(PrepaymentCompilationRequest s);

    @Mapping(target = "removePlaceholder", ignore = true)
    PrepaymentCompilationRequest toEntity(PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PrepaymentCompilationRequestDTO toDtoId(PrepaymentCompilationRequest prepaymentCompilationRequest);
}
