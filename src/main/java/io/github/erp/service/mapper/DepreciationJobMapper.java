package io.github.erp.service.mapper;

import io.github.erp.domain.DepreciationJob;
import io.github.erp.service.dto.DepreciationJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepreciationJob} and its DTO {@link DepreciationJobDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicationUserMapper.class, DepreciationPeriodMapper.class })
public interface DepreciationJobMapper extends EntityMapper<DepreciationJobDTO, DepreciationJob> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "designation")
    @Mapping(target = "depreciationPeriod", source = "depreciationPeriod", qualifiedByName = "endDate")
    DepreciationJobDTO toDto(DepreciationJob s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepreciationJobDTO toDtoId(DepreciationJob depreciationJob);
}
