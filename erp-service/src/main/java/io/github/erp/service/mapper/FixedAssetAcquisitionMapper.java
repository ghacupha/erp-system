package io.github.erp.service.mapper;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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


import io.github.erp.domain.*;
import io.github.erp.service.dto.FixedAssetAcquisitionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FixedAssetAcquisition} and its DTO {@link FixedAssetAcquisitionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FixedAssetAcquisitionMapper extends EntityMapper<FixedAssetAcquisitionDTO, FixedAssetAcquisition> {



    default FixedAssetAcquisition fromId(Long id) {
        if (id == null) {
            return null;
        }
        FixedAssetAcquisition fixedAssetAcquisition = new FixedAssetAcquisition();
        fixedAssetAcquisition.setId(id);
        return fixedAssetAcquisition;
    }
}
