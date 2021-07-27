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

import io.github.erp.domain.enumeration.LeassetsFileModelType;
import io.github.erp.internal.framework.fileProcessing.FileUploadProcessorChain;
import io.github.erp.internal.framework.service.HandlingService;
import io.github.erp.internal.model.FileNotification;
import io.github.erp.service.LeassetsFileTypeService;
import io.github.erp.service.LeassetsFileUploadService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * This handler takes a file-id and processes related workflows for deletion of file related data
 */
@Service("fileDeletionHandlingService")
public class FileDeletionHandlingService implements HandlingService<Long> {
    private final LeassetsFileTypeService fileTypeService;
    private final LeassetsFileUploadService fileUploadService;

    private final FileUploadProcessorChain fileUploadDeletionProcessorChain;

    public FileDeletionHandlingService(
        final LeassetsFileTypeService fileTypeService,
        final LeassetsFileUploadService fileUploadService,
        final @Qualifier("fileUploadDeletionProcessorChain")
            FileUploadProcessorChain fileUploadDeletionProcessorChain
    ) {
        this.fileTypeService = fileTypeService;
        this.fileUploadService = fileUploadService;
        this.fileUploadDeletionProcessorChain = fileUploadDeletionProcessorChain;
    }

    /**
     * Returns an instance of this after handling the payload issued
     *
     * @param payload The item being handled
     */
    @Override
    @Async
    public void handle(final Long payload) {
        fileUploadService
            .findOne(payload)
            .ifPresent(
                file -> {
                    final AtomicReference<LeassetsFileModelType> type = new AtomicReference<>();
                    fileTypeService
                        .findOne(file.getLeassetsFileTypeId())
                        .ifPresent(
                            fileType -> {
                                type.set(fileType.getLeassetsfileType());

                                fileUploadDeletionProcessorChain.apply(
                                    file,
                                    FileNotification
                                        .builder()
                                        .fileId(String.valueOf(file.getId()))
                                        .filename(file.getFileName())
                                        .messageToken(file.getUploadToken())
                                        .timestamp(System.currentTimeMillis())
                                        .leassetsfileModelType(type.get())
                                        .build()
                                );
                            }
                        );
                }
            );
    }
}
