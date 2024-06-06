package io.github.erp.internal.service.assets;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.internal.model.mapping.AssetAcquisitionBatchEntityDTOMapping;
import io.github.erp.internal.model.FixedAssetAcquisitionBEO;
import tech.jhipster.service.filter.StringFilter;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.service.FixedAssetAcquisitionQueryService;
import io.github.erp.service.criteria.FixedAssetAcquisitionCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("fixedAssetAcquisitionDeletionFileUploadService")
public class FixedAssetAcquisitionDeletionFileUploadService implements DeletionUploadService<FixedAssetAcquisitionBEO> {

    private final AssetAcquisitionBatchEntityDTOMapping batchEntityDTOMapping;
    private final FixedAssetAcquisitionQueryService fixedAssetAcquisitionQueryService;

    public FixedAssetAcquisitionDeletionFileUploadService(AssetAcquisitionBatchEntityDTOMapping batchEntityDTOMapping, FixedAssetAcquisitionQueryService fixedAssetAcquisitionQueryService) {
        this.batchEntityDTOMapping = batchEntityDTOMapping;
        this.fixedAssetAcquisitionQueryService = fixedAssetAcquisitionQueryService;
    }

    @Override
    public Optional<List<FixedAssetAcquisitionBEO>> findAllByUploadToken(String stringToken) {
        FixedAssetAcquisitionCriteria criteria = new FixedAssetAcquisitionCriteria();
        StringFilter uploadToken = new StringFilter();
        uploadToken.setEquals(stringToken);
        criteria.setFileUploadToken(uploadToken);
        return Optional.of(batchEntityDTOMapping.toValue1(fixedAssetAcquisitionQueryService.findByCriteria(criteria)));
    }
}
