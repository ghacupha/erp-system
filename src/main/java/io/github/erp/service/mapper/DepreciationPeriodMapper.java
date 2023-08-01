package io.github.erp.service.mapper;

import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepreciationPeriod} and its DTO {@link DepreciationPeriodDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DepreciationPeriodMapper extends EntityMapper<DepreciationPeriodDTO, DepreciationPeriod> {
    @Mapping(target = "previousPeriod", source = "previousPeriod", qualifiedByName = "endDate")
    DepreciationPeriodDTO toDto(DepreciationPeriod s);

    @Named("endDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "endDate", source = "endDate")
    DepreciationPeriodDTO toDtoEndDate(DepreciationPeriod depreciationPeriod);
}
