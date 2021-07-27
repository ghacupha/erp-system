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
import io.github.erp.internal.framework.batch.BatchPersistentFileUploadService;
import io.github.erp.internal.framework.batch.HasDataFile;
import io.github.erp.service.FileUploadService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class BatchPersistentFileUploadServiceImpl implements BatchPersistentFileUploadService {

    private final FileUploadService fileUploadService;

    public BatchPersistentFileUploadServiceImpl(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @Override
    public Optional<HasDataFile> findOne(long fileId) {

        AtomicReference<Optional<HasDataFile>> fileUpload = new AtomicReference<>();

        fileUploadService.findOne(fileId).ifPresent(optionalFile -> fileUpload.set(Optional.of(optionalFile)));

        return fileUpload.get();
    }
}
