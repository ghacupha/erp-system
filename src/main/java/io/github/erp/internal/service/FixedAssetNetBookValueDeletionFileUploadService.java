package io.github.erp.internal.service;

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
import io.github.erp.internal.model.FixedAssetNetBookValueBEO;
import io.github.erp.internal.model.mapping.NetBookValueBatchEntityDTOMapping;
import tech.jhipster.service.filter.StringFilter;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.service.FixedAssetNetBookValueQueryService;
import io.github.erp.service.criteria.FixedAssetNetBookValueCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("fixedAssetNetBookValueDeletionFileUploadService")
public class FixedAssetNetBookValueDeletionFileUploadService  implements DeletionUploadService<FixedAssetNetBookValueBEO> {

    private final NetBookValueBatchEntityDTOMapping bookValueBatchEntityDTOMapping;
    private final FixedAssetNetBookValueQueryService fixedAssetNetBookValueQueryService;

    public FixedAssetNetBookValueDeletionFileUploadService(NetBookValueBatchEntityDTOMapping bookValueBatchEntityDTOMapping, FixedAssetNetBookValueQueryService fixedAssetNetBookValueQueryService) {
        this.bookValueBatchEntityDTOMapping = bookValueBatchEntityDTOMapping;
        this.fixedAssetNetBookValueQueryService = fixedAssetNetBookValueQueryService;
    }

    @Override
    public Optional<List<FixedAssetNetBookValueBEO>> findAllByUploadToken(String stringToken) {
        FixedAssetNetBookValueCriteria criteria = new FixedAssetNetBookValueCriteria();
        StringFilter uploadToken = new StringFilter();
        uploadToken.setEquals(stringToken);
        criteria.setFileUploadToken(uploadToken);
        return Optional.of(bookValueBatchEntityDTOMapping.toValue1(fixedAssetNetBookValueQueryService.findByCriteria(criteria)));
    }
}
