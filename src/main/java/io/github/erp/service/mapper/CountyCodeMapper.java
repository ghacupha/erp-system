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
import io.github.erp.domain.CountyCode;
import io.github.erp.service.dto.CountyCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CountyCode} and its DTO {@link CountyCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface CountyCodeMapper extends EntityMapper<CountyCodeDTO, CountyCode> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    CountyCodeDTO toDto(CountyCode s);

    @Mapping(target = "removePlaceholder", ignore = true)
    CountyCode toEntity(CountyCodeDTO countyCodeDTO);

    @Named("countyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "countyName", source = "countyName")
    CountyCodeDTO toDtoCountyName(CountyCode countyCode);

    @Named("subCountyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "subCountyName", source = "subCountyName")
    CountyCodeDTO toDtoSubCountyName(CountyCode countyCode);
}
