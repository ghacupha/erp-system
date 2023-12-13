package io.github.erp.internal.service;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.internal.model.mapping.AssetDepreciationBatchEntityDTOMapping;
import io.github.erp.internal.model.FixedAssetDepreciationBEO;
import tech.jhipster.service.filter.StringFilter;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.service.FixedAssetDepreciationQueryService;
import io.github.erp.service.criteria.FixedAssetDepreciationCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("fixedAssetDepreciationDeletionFileUploadService")
public class FixedAssetDepreciationDeletionFileUploadService implements DeletionUploadService<FixedAssetDepreciationBEO> {

    private final AssetDepreciationBatchEntityDTOMapping batchEntityDTOMapping;
    private final FixedAssetDepreciationQueryService fixedAssetDepreciationQueryService;

    public FixedAssetDepreciationDeletionFileUploadService(AssetDepreciationBatchEntityDTOMapping batchEntityDTOMapping, FixedAssetDepreciationQueryService fixedAssetDepreciationQueryService) {
        this.batchEntityDTOMapping = batchEntityDTOMapping;
        this.fixedAssetDepreciationQueryService = fixedAssetDepreciationQueryService;
    }

    @Override
    public Optional<List<FixedAssetDepreciationBEO>> findAllByUploadToken(String stringToken) {
        FixedAssetDepreciationCriteria criteria = new FixedAssetDepreciationCriteria();
        StringFilter uploadToken = new StringFilter();
        uploadToken.setEquals(stringToken);
        criteria.setFileUploadToken(uploadToken);
        return Optional.of(batchEntityDTOMapping.toValue1(fixedAssetDepreciationQueryService.findByCriteria(criteria)));
    }
}
