package io.github.erp.service.mapper;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.4
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
        AlgorithmMapper.class,
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
    @Mapping(target = "fileCheckSumAlgorithm", source = "fileCheckSumAlgorithm", qualifiedByName = "name")
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
