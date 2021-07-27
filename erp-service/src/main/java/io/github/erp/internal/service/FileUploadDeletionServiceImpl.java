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

import io.github.erp.internal.framework.service.FileUploadDeletionService;
import io.github.erp.service.LeassetsFileUploadService;
import io.github.erp.service.dto.LeassetsFileUploadDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This is created to work with the file-deletion-listener for that is called to finally remove the file whose data
 * has been removed from the library
 */
@Service("fileUploadDeletionService")
public class FileUploadDeletionServiceImpl implements FileUploadDeletionService<LeassetsFileUploadDTO> {

    private final LeassetsFileUploadService leassetsFileUploadService;

    public FileUploadDeletionServiceImpl(LeassetsFileUploadService leassetsFileUploadService) {
        this.leassetsFileUploadService = leassetsFileUploadService;
    }

    @Override
    public Optional<LeassetsFileUploadDTO> findOne(long fileId) {
        return leassetsFileUploadService.findOne(fileId);
    }

    @Override
    public void delete(Long id) {

        leassetsFileUploadService.delete(id);
    }
}
