package io.github.erp.internal.service;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.8-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
