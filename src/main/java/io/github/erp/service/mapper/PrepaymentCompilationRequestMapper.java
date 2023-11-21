package io.github.erp.service.mapper;

import io.github.erp.domain.PrepaymentCompilationRequest;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrepaymentCompilationRequest} and its DTO {@link PrepaymentCompilationRequestDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PrepaymentCompilationRequestMapper extends EntityMapper<PrepaymentCompilationRequestDTO, PrepaymentCompilationRequest> {}
