package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.WorkInProgressTransfer;
import io.github.erp.service.dto.WorkInProgressTransferDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkInProgressTransfer} and its DTO {@link WorkInProgressTransferDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PlaceholderMapper.class,
        BusinessDocumentMapper.class,
        AssetCategoryMapper.class,
        WorkInProgressRegistrationMapper.class,
        ServiceOutletMapper.class,
        SettlementMapper.class,
        WorkProjectRegisterMapper.class,
    }
)
public interface WorkInProgressTransferMapper extends EntityMapper<WorkInProgressTransferDTO, WorkInProgressTransfer> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    @Mapping(target = "assetCategory", source = "assetCategory", qualifiedByName = "assetCategoryName")
    @Mapping(target = "workInProgressRegistration", source = "workInProgressRegistration", qualifiedByName = "sequenceNumber")
    @Mapping(target = "serviceOutlet", source = "serviceOutlet", qualifiedByName = "outletCode")
    @Mapping(target = "transferSettlement", source = "transferSettlement", qualifiedByName = "paymentNumber")
    @Mapping(target = "originalSettlement", source = "originalSettlement", qualifiedByName = "paymentNumber")
    @Mapping(target = "workProjectRegister", source = "workProjectRegister", qualifiedByName = "projectTitle")
    WorkInProgressTransferDTO toDto(WorkInProgressTransfer s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    WorkInProgressTransfer toEntity(WorkInProgressTransferDTO workInProgressTransferDTO);
}
