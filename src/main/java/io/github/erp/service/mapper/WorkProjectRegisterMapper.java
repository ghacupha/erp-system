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
import io.github.erp.domain.WorkProjectRegister;
import io.github.erp.service.dto.WorkProjectRegisterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkProjectRegister} and its DTO {@link WorkProjectRegisterDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { DealerMapper.class, SettlementCurrencyMapper.class, PlaceholderMapper.class, BusinessDocumentMapper.class }
)
public interface WorkProjectRegisterMapper extends EntityMapper<WorkProjectRegisterDTO, WorkProjectRegister> {
    @Mapping(target = "dealers", source = "dealers", qualifiedByName = "dealerNameSet")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    WorkProjectRegisterDTO toDto(WorkProjectRegister s);

    @Mapping(target = "removeDealers", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    WorkProjectRegister toEntity(WorkProjectRegisterDTO workProjectRegisterDTO);

    @Named("catalogueNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "catalogueNumber", source = "catalogueNumber")
    WorkProjectRegisterDTO toDtoCatalogueNumber(WorkProjectRegister workProjectRegister);

    @Named("projectTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "projectTitle", source = "projectTitle")
    WorkProjectRegisterDTO toDtoProjectTitle(WorkProjectRegister workProjectRegister);
}
