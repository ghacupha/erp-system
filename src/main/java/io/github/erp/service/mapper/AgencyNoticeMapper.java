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
import io.github.erp.domain.AgencyNotice;
import io.github.erp.service.dto.AgencyNoticeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgencyNotice} and its DTO {@link AgencyNoticeDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { DealerMapper.class, SettlementCurrencyMapper.class, PlaceholderMapper.class, BusinessDocumentMapper.class }
)
public interface AgencyNoticeMapper extends EntityMapper<AgencyNoticeDTO, AgencyNotice> {
    @Mapping(target = "correspondents", source = "correspondents", qualifiedByName = "dealerNameSet")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "assessor", source = "assessor", qualifiedByName = "dealerName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    AgencyNoticeDTO toDto(AgencyNotice s);

    @Mapping(target = "removeCorrespondents", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    AgencyNotice toEntity(AgencyNoticeDTO agencyNoticeDTO);
}
