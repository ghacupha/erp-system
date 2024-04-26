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
import io.github.erp.domain.AssetRevaluation;
import io.github.erp.service.dto.AssetRevaluationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetRevaluation} and its DTO {@link AssetRevaluationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        DealerMapper.class,
        ApplicationUserMapper.class,
        DepreciationPeriodMapper.class,
        AssetRegistrationMapper.class,
        PlaceholderMapper.class,
    }
)
public interface AssetRevaluationMapper extends EntityMapper<AssetRevaluationDTO, AssetRevaluation> {
    @Mapping(target = "revaluer", source = "revaluer", qualifiedByName = "dealerName")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "lastAccessedBy", source = "lastAccessedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "effectivePeriod", source = "effectivePeriod", qualifiedByName = "periodCode")
    @Mapping(target = "revaluedAsset", source = "revaluedAsset", qualifiedByName = "assetNumber")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    AssetRevaluationDTO toDto(AssetRevaluation s);

    @Mapping(target = "removePlaceholder", ignore = true)
    AssetRevaluation toEntity(AssetRevaluationDTO assetRevaluationDTO);
}
