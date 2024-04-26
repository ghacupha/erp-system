package io.github.erp.service.mapper;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.FiscalYear;
import io.github.erp.service.dto.FiscalYearDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FiscalYear} and its DTO {@link FiscalYearDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class, ApplicationUserMapper.class })
public interface FiscalYearMapper extends EntityMapper<FiscalYearDTO, FiscalYear> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "lastUpdatedBy", source = "lastUpdatedBy", qualifiedByName = "applicationIdentity")
    FiscalYearDTO toDto(FiscalYear s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    FiscalYear toEntity(FiscalYearDTO fiscalYearDTO);

    @Named("fiscalYearCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fiscalYearCode", source = "fiscalYearCode")
    FiscalYearDTO toDtoFiscalYearCode(FiscalYear fiscalYear);
}
