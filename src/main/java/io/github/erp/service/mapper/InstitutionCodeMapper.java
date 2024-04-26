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
import io.github.erp.domain.InstitutionCode;
import io.github.erp.service.dto.InstitutionCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InstitutionCode} and its DTO {@link InstitutionCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface InstitutionCodeMapper extends EntityMapper<InstitutionCodeDTO, InstitutionCode> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    InstitutionCodeDTO toDto(InstitutionCode s);

    @Mapping(target = "removePlaceholder", ignore = true)
    InstitutionCode toEntity(InstitutionCodeDTO institutionCodeDTO);

    @Named("institutionCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "institutionCode", source = "institutionCode")
    InstitutionCodeDTO toDtoInstitutionCode(InstitutionCode institutionCode);

    @Named("institutionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "institutionName", source = "institutionName")
    InstitutionCodeDTO toDtoInstitutionName(InstitutionCode institutionCode);
}
