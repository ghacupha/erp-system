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
import io.github.erp.domain.BusinessDocument;
import io.github.erp.service.dto.BusinessDocumentDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessDocument} and its DTO {@link BusinessDocumentDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        ApplicationUserMapper.class,
        DealerMapper.class,
        UniversallyUniqueMappingMapper.class,
        PlaceholderMapper.class,
        AlgorithmMapper.class,
        SecurityClearanceMapper.class,
    }
)
public interface BusinessDocumentMapper extends EntityMapper<BusinessDocumentDTO, BusinessDocument> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "originatingDepartment", source = "originatingDepartment", qualifiedByName = "dealerName")
    @Mapping(target = "applicationMappings", source = "applicationMappings", qualifiedByName = "universalKeySet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "fileChecksumAlgorithm", source = "fileChecksumAlgorithm", qualifiedByName = "name")
    @Mapping(target = "securityClearance", source = "securityClearance", qualifiedByName = "clearanceLevel")
    BusinessDocumentDTO toDto(BusinessDocument s);

    @Mapping(target = "removeApplicationMappings", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    BusinessDocument toEntity(BusinessDocumentDTO businessDocumentDTO);

    @Named("documentTitleSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentTitle", source = "documentTitle")
    Set<BusinessDocumentDTO> toDtoDocumentTitleSet(Set<BusinessDocument> businessDocument);

    @Named("documentTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentTitle", source = "documentTitle")
    BusinessDocumentDTO toDtoDocumentTitle(BusinessDocument businessDocument);
}
