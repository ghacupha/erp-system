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
import io.github.erp.domain.DepreciationJob;
import io.github.erp.service.dto.DepreciationJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepreciationJob} and its DTO {@link DepreciationJobDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicationUserMapper.class, DepreciationPeriodMapper.class })
public interface DepreciationJobMapper extends EntityMapper<DepreciationJobDTO, DepreciationJob> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "designation")
    @Mapping(target = "depreciationPeriod", source = "depreciationPeriod", qualifiedByName = "endDate")
    DepreciationJobDTO toDto(DepreciationJob s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepreciationJobDTO toDtoId(DepreciationJob depreciationJob);

    @Named("description")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    DepreciationJobDTO toDtoDescription(DepreciationJob depreciationJob);
}
