package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.InternalMemo;
import io.github.erp.service.dto.InternalMemoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InternalMemo} and its DTO {@link InternalMemoDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { MemoActionMapper.class, DealerMapper.class, BusinessDocumentMapper.class, PlaceholderMapper.class }
)
public interface InternalMemoMapper extends EntityMapper<InternalMemoDTO, InternalMemo> {
    @Mapping(target = "actionRequired", source = "actionRequired", qualifiedByName = "action")
    @Mapping(target = "addressedTo", source = "addressedTo", qualifiedByName = "dealerName")
    @Mapping(target = "from", source = "from", qualifiedByName = "dealerName")
    @Mapping(target = "preparedBies", source = "preparedBies", qualifiedByName = "dealerNameSet")
    @Mapping(target = "reviewedBies", source = "reviewedBies", qualifiedByName = "dealerNameSet")
    @Mapping(target = "approvedBies", source = "approvedBies", qualifiedByName = "dealerNameSet")
    @Mapping(target = "memoDocuments", source = "memoDocuments", qualifiedByName = "documentTitleSet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    InternalMemoDTO toDto(InternalMemo s);

    @Mapping(target = "removePreparedBy", ignore = true)
    @Mapping(target = "removeReviewedBy", ignore = true)
    @Mapping(target = "removeApprovedBy", ignore = true)
    @Mapping(target = "removeMemoDocument", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    InternalMemo toEntity(InternalMemoDTO internalMemoDTO);
}
