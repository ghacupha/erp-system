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
import io.github.erp.domain.enumeration.FileModelType;
import io.github.erp.internal.framework.fileProcessing.FileUploadProcessorChain;
import io.github.erp.internal.framework.service.HandlingService;
import io.github.erp.internal.model.FileNotification;
import io.github.erp.service.FileTypeService;
import io.github.erp.service.FileUploadService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

// import java.util.concurrent.CompletableFuture;
// import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This handler takes a file-id and processes related workflows for deletion of file related data
 */
@Service("fileDeletionHandlingService")
public class FileDeletionHandlingService implements HandlingService<Long> {
    private final FileTypeService fileTypeService;
    private final FileUploadService fileUploadService;

    private final FileUploadProcessorChain fileUploadDeletionProcessorChain;

    public FileDeletionHandlingService(
        FileTypeService fileTypeService,
        FileUploadService fileUploadService,
        @Qualifier("fileUploadDeletionProcessorChain")
            FileUploadProcessorChain fileUploadDeletionProcessorChain ) {
        this.fileTypeService = fileTypeService;
        this.fileUploadService = fileUploadService;
        this.fileUploadDeletionProcessorChain = fileUploadDeletionProcessorChain;
    }

    @Override
    @Async
    public void handle(final Long payload) {

        // CompletableFuture<Boolean> future = new CompletableFuture<>();

        // taskExecutor.execute(() -> {
        //     deletionSequence(payload);
        //     future.complete(true);
        // });
        // return future;

        deletionSequence(payload);
    }

    private void deletionSequence(Long payload) {
        // commencement of deletion sequence
        fileUploadService
            .findOne(payload)
            .ifPresent(
                file -> {
                    final AtomicReference<FileModelType> type = new AtomicReference<>();
                    fileTypeService
                        .findOne(file.getFileTypeId())
                        .ifPresent(
                            fileType -> {
                                type.set(fileType.getFileType());

                                fileUploadDeletionProcessorChain.apply(
                                    file,
                                    FileNotification
                                        .builder()
                                        .fileId(String.valueOf(file.getId()))
                                        .filename(file.getFileName())
                                        .messageToken(file.getUploadToken())
                                        .timestamp(System.currentTimeMillis())
                                        .fileModelType(type.get())
                                        .build()
                                );
                            }
                        );
                }
            );
    }
}
