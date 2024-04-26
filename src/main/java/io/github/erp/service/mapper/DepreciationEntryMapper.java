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
import io.github.erp.domain.DepreciationEntry;
import io.github.erp.service.dto.DepreciationEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepreciationEntry} and its DTO {@link DepreciationEntryDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        ServiceOutletMapper.class,
        AssetCategoryMapper.class,
        DepreciationMethodMapper.class,
        AssetRegistrationMapper.class,
        DepreciationPeriodMapper.class,
        FiscalMonthMapper.class,
        FiscalQuarterMapper.class,
        FiscalYearMapper.class,
        DepreciationJobMapper.class,
        DepreciationBatchSequenceMapper.class,
    }
)
public interface DepreciationEntryMapper extends EntityMapper<DepreciationEntryDTO, DepreciationEntry> {
    @Mapping(target = "serviceOutlet", source = "serviceOutlet", qualifiedByName = "outletCode")
    @Mapping(target = "assetCategory", source = "assetCategory", qualifiedByName = "assetCategoryName")
    @Mapping(target = "depreciationMethod", source = "depreciationMethod", qualifiedByName = "depreciationMethodName")
    @Mapping(target = "assetRegistration", source = "assetRegistration", qualifiedByName = "assetNumber")
    @Mapping(target = "depreciationPeriod", source = "depreciationPeriod", qualifiedByName = "endDate")
    @Mapping(target = "fiscalMonth", source = "fiscalMonth", qualifiedByName = "fiscalMonthCode")
    @Mapping(target = "fiscalQuarter", source = "fiscalQuarter", qualifiedByName = "fiscalQuarterCode")
    @Mapping(target = "fiscalYear", source = "fiscalYear", qualifiedByName = "fiscalYearCode")
    @Mapping(target = "depreciationJob", source = "depreciationJob", qualifiedByName = "description")
    @Mapping(target = "depreciationBatchSequence", source = "depreciationBatchSequence", qualifiedByName = "id")
    DepreciationEntryDTO toDto(DepreciationEntry s);
}
