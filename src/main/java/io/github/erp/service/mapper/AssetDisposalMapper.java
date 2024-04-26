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
import io.github.erp.domain.AssetDisposal;
import io.github.erp.service.dto.AssetDisposalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetDisposal} and its DTO {@link AssetDisposalDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { ApplicationUserMapper.class, DepreciationPeriodMapper.class, PlaceholderMapper.class, AssetRegistrationMapper.class }
)
public interface AssetDisposalMapper extends EntityMapper<AssetDisposalDTO, AssetDisposal> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "modifiedBy", source = "modifiedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "lastAccessedBy", source = "lastAccessedBy", qualifiedByName = "applicationIdentity")
    @Mapping(target = "effectivePeriod", source = "effectivePeriod", qualifiedByName = "startDate")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "assetDisposed", source = "assetDisposed", qualifiedByName = "assetNumber")
    AssetDisposalDTO toDto(AssetDisposal s);

    @Mapping(target = "removePlaceholder", ignore = true)
    AssetDisposal toEntity(AssetDisposalDTO assetDisposalDTO);
}
