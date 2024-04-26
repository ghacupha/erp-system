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
import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.service.dto.NetBookValueEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NetBookValueEntry} and its DTO {@link NetBookValueEntryDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        ServiceOutletMapper.class,
        DepreciationPeriodMapper.class,
        FiscalMonthMapper.class,
        DepreciationMethodMapper.class,
        AssetRegistrationMapper.class,
        AssetCategoryMapper.class,
        PlaceholderMapper.class,
    }
)
public interface NetBookValueEntryMapper extends EntityMapper<NetBookValueEntryDTO, NetBookValueEntry> {
    @Mapping(target = "serviceOutlet", source = "serviceOutlet", qualifiedByName = "outletCode")
    @Mapping(target = "depreciationPeriod", source = "depreciationPeriod", qualifiedByName = "endDate")
    @Mapping(target = "fiscalMonth", source = "fiscalMonth", qualifiedByName = "fiscalMonthCode")
    @Mapping(target = "depreciationMethod", source = "depreciationMethod", qualifiedByName = "depreciationMethodName")
    @Mapping(target = "assetRegistration", source = "assetRegistration", qualifiedByName = "id")
    @Mapping(target = "assetCategory", source = "assetCategory", qualifiedByName = "assetCategoryName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    NetBookValueEntryDTO toDto(NetBookValueEntry s);

    @Mapping(target = "removePlaceholder", ignore = true)
    NetBookValueEntry toEntity(NetBookValueEntryDTO netBookValueEntryDTO);
}
