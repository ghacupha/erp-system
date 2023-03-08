package io.github.erp.internal.model.mapping;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 0.8.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
