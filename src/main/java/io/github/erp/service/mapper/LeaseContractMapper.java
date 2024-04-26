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
import io.github.erp.domain.LeaseContract;
import io.github.erp.service.dto.LeaseContractDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaseContract} and its DTO {@link LeaseContractDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PlaceholderMapper.class, UniversallyUniqueMappingMapper.class, BusinessDocumentMapper.class, ContractMetadataMapper.class }
)
public interface LeaseContractMapper extends EntityMapper<LeaseContractDTO, LeaseContract> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "systemMappings", source = "systemMappings", qualifiedByName = "mappedValueSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    @Mapping(target = "contractMetadata", source = "contractMetadata", qualifiedByName = "contractTitleSet")
    LeaseContractDTO toDto(LeaseContract s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeSystemMappings", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    @Mapping(target = "removeContractMetadata", ignore = true)
    LeaseContract toEntity(LeaseContractDTO leaseContractDTO);

    @Named("bookingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingId", source = "bookingId")
    LeaseContractDTO toDtoBookingId(LeaseContract leaseContract);
}
