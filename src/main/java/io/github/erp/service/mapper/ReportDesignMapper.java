package io.github.erp.service.mapper;

import io.github.erp.domain.ReportDesign;
import io.github.erp.service.dto.ReportDesignDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportDesign} and its DTO {@link ReportDesignDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        UniversallyUniqueMappingMapper.class,
        SecurityClearanceMapper.class,
        ApplicationUserMapper.class,
        DealerMapper.class,
        PlaceholderMapper.class,
        SystemModuleMapper.class,
    }
)
public interface ReportDesignMapper extends EntityMapper<ReportDesignDTO, ReportDesign> {
    @Mapping(target = "parameters", source = "parameters", qualifiedByName = "mappedValueSet")
    @Mapping(target = "securityClearance", source = "securityClearance", qualifiedByName = "clearanceLevel")
    @Mapping(target = "reportDesigner", source = "reportDesigner", qualifiedByName = "applicationIdentity")
    @Mapping(target = "organization", source = "organization", qualifiedByName = "dealerName")
    @Mapping(target = "department", source = "department", qualifiedByName = "dealerName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "systemModule", source = "systemModule", qualifiedByName = "moduleName")
    ReportDesignDTO toDto(ReportDesign s);

    @Mapping(target = "removeParameters", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    ReportDesign toEntity(ReportDesignDTO reportDesignDTO);

    @Named("designation")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designation", source = "designation")
    ReportDesignDTO toDtoDesignation(ReportDesign reportDesign);
}
