package io.github.erp.internal.service;

/*-
 * Leassets Server - Leases and assets management platform
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.jhipster.service.filter.StringFilter;
import io.github.leassets.internal.framework.service.DeletionUploadService;
import io.github.leassets.service.FixedAssetAcquisitionQueryService;
import io.github.leassets.service.LeassetsFileUploadService;
import io.github.leassets.service.dto.FixedAssetAcquisitionCriteria;
import io.github.leassets.service.dto.FixedAssetAcquisitionDTO;
import io.github.leassets.service.dto.LeassetsFileUploadDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("fixedAssetAcquisitionDeletionFileUploadService")
public class FixedAssetAcquisitionDeletionFileUploadService implements DeletionUploadService<FixedAssetAcquisitionDTO> {

    private final LeassetsFileUploadService leassetsFileUploadService;
    private final FixedAssetAcquisitionQueryService fixedAssetAcquisitionQueryService;

    public FixedAssetAcquisitionDeletionFileUploadService(LeassetsFileUploadService leassetsFileUploadService, FixedAssetAcquisitionQueryService fixedAssetAcquisitionQueryService) {
        this.leassetsFileUploadService = leassetsFileUploadService;
        this.fixedAssetAcquisitionQueryService = fixedAssetAcquisitionQueryService;
    }

    @Override
    public Optional<LeassetsFileUploadDTO> findOne(long fileId) {

        return leassetsFileUploadService.findOne(fileId);
    }

    @Override
    public Optional<List<FixedAssetAcquisitionDTO>> findAllByUploadToken(String stringToken) {
        FixedAssetAcquisitionCriteria criteria = new FixedAssetAcquisitionCriteria();
        StringFilter uploadToken = new StringFilter();
        uploadToken.setEquals(stringToken);
        criteria.setFileUploadToken(uploadToken);
        return Optional.of(fixedAssetAcquisitionQueryService.findByCriteria(criteria));
    }
}
