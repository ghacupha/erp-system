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
import io.github.erp.internal.framework.service.FileUploadDeletionService;
import io.github.erp.internal.framework.model.FileUploadHasDataFile;
import io.github.erp.internal.model.mapping.FileUploadHasDataFileMapping;
import io.github.erp.service.FileUploadService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This is created to work with the file-deletion-listener for that is called to finally remove the file whose data
 * has been removed from the library
 */
@Service("fileUploadDeletionService")
public class FileUploadDeletionServiceImpl implements FileUploadDeletionService<FileUploadHasDataFile> {

    private final FileUploadHasDataFileMapping dataFileMapping;
    private final FileUploadService fileUploadService;

    public FileUploadDeletionServiceImpl(FileUploadHasDataFileMapping dataFileMapping, FileUploadService fileUploadService) {
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

    @Override
    public void delete(Long id) {

        fileUploadService.delete(id);
    }
}
