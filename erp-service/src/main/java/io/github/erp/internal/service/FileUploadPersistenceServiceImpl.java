package io.github.erp.internal.service;

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
import io.github.erp.internal.framework.service.FileUploadPersistenceService;
import io.github.erp.service.FileUploadService;
import io.github.erp.service.dto.FileUploadDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("fileUploadPersistenceService")
public class FileUploadPersistenceServiceImpl implements FileUploadPersistenceService<FileUploadDTO> {

    private final FileUploadService FileUploadService;

    public FileUploadPersistenceServiceImpl(FileUploadService FileUploadService) {
        this.FileUploadService = FileUploadService;
    }

    @Override
    public Optional<FileUploadDTO> findOne(long fileUploadDTOId) {
        return FileUploadService.findOne(fileUploadDTOId);
    }

    @Override
    public FileUploadDTO save(FileUploadDTO fileUpload) {
        return FileUploadService.save(fileUpload);
    }
}
