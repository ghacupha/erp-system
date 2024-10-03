package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
