package io.github.erp.service.mapper;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
