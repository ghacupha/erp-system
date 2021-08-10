package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.TaxReferenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxReference} and its DTO {@link TaxReferenceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaxReferenceMapper extends EntityMapper<TaxReferenceDTO, TaxReference> {}
