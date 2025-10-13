package io.github.erp.internal.service.files;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.internal.framework.service.DataFileContainer;
import io.github.erp.internal.framework.model.FileUploadHasDataFile;
import io.github.erp.internal.model.mapping.FileUploadHasDataFileMapping;
import io.github.erp.service.FileUploadService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("fileUploadDataFileContainer")
public class FileUploadDataFileContainer implements DataFileContainer<FileUploadHasDataFile> {

    private final FileUploadHasDataFileMapping dataFileMapping;
    private final FileUploadService fileUploadService;

    public FileUploadDataFileContainer(FileUploadHasDataFileMapping dataFileMapping, FileUploadService fileUploadService) {
        this.dataFileMapping = dataFileMapping;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public Optional<FileUploadHasDataFile> findOne(long fileId) {

        final FileUploadHasDataFile[] file = new FileUploadHasDataFile[1];

        fileUploadService.findOne(fileId).ifPresent(fileUploadDTO -> {
            file[0] = dataFileMapping.toValue1(fileUploadDTO);
        });

        return Optional.of(file[0]);
    }
}
