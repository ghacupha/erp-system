package io.github.erp.service.mapper;

import io.github.erp.domain.SystemModule;
import io.github.erp.service.dto.SystemModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemModule} and its DTO {@link SystemModuleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SystemModuleMapper extends EntityMapper<SystemModuleDTO, SystemModule> {}
