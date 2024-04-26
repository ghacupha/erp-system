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
    @Mapping(target = "settlement", source = "settlement", qualifiedByName = "paymentNumber")
    @Mapping(target = "workProjectRegister", source = "workProjectRegister", qualifiedByName = "projectTitle")
    WorkInProgressTransferDTO toDto(WorkInProgressTransfer s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    WorkInProgressTransfer toEntity(WorkInProgressTransferDTO workInProgressTransferDTO);
}
