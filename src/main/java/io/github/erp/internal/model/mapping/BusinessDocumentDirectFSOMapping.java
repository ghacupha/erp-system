package io.github.erp.internal.model.mapping;

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
import io.github.erp.internal.model.BusinessDocumentFSO;
import io.github.erp.service.mapper.AlgorithmMapper;
import io.github.erp.service.mapper.ApplicationUserMapper;
import io.github.erp.service.mapper.DealerMapper;
import io.github.erp.service.mapper.EntityMapper;
import io.github.erp.service.mapper.PlaceholderMapper;
import io.github.erp.service.mapper.SecurityClearanceMapper;
import io.github.erp.service.mapper.UniversallyUniqueMappingMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

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
public interface BusinessDocumentDirectFSOMapping extends EntityMapper<BusinessDocumentFSO, BusinessDocument> {

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "originatingDepartment", source = "originatingDepartment", qualifiedByName = "dealerName")
    @Mapping(target = "applicationMappings", source = "applicationMappings", qualifiedByName = "universalKeySet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "fileChecksumAlgorithm", source = "fileChecksumAlgorithm", qualifiedByName = "name")
    @Mapping(target = "securityClearance", source = "securityClearance", qualifiedByName = "clearanceLevel")
    BusinessDocumentFSO toDto(BusinessDocument s);

    @Mapping(target = "removeApplicationMappings", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    BusinessDocument toEntity(BusinessDocumentFSO businessDocumentDTO);

    @Named("documentTitleSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentTitle", source = "documentTitle")
    Set<BusinessDocumentFSO> toDtoDocumentTitleSet(Set<BusinessDocument> businessDocument);
}
