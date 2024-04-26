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
import io.github.erp.domain.DepreciationJobNotice;
import io.github.erp.service.dto.DepreciationJobNoticeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepreciationJobNotice} and its DTO {@link DepreciationJobNoticeDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        DepreciationJobMapper.class,
        DepreciationBatchSequenceMapper.class,
        DepreciationPeriodMapper.class,
        PlaceholderMapper.class,
        UniversallyUniqueMappingMapper.class,
    }
)
public interface DepreciationJobNoticeMapper extends EntityMapper<DepreciationJobNoticeDTO, DepreciationJobNotice> {
    @Mapping(target = "depreciationJob", source = "depreciationJob", qualifiedByName = "id")
    @Mapping(target = "depreciationBatchSequence", source = "depreciationBatchSequence", qualifiedByName = "id")
    @Mapping(target = "depreciationPeriod", source = "depreciationPeriod", qualifiedByName = "id")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "universallyUniqueMappings", source = "universallyUniqueMappings", qualifiedByName = "universalKeySet")
    DepreciationJobNoticeDTO toDto(DepreciationJobNotice s);

    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeUniversallyUniqueMapping", ignore = true)
    DepreciationJobNotice toEntity(DepreciationJobNoticeDTO depreciationJobNoticeDTO);
}
