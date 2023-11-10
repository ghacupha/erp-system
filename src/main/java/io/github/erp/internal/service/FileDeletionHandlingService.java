package io.github.erp.internal.service;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
