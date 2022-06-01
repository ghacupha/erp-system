
/*-
 * Erp System - Mark II No 4 (Artaxerxes Series)
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
/*-
 *  Server - Leases and assets management platform
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
package io.github.erp.internal.framework.fileProcessing;

import com.google.common.collect.ImmutableList;
import io.github.erp.internal.model.FileNotification;
import io.github.erp.service.dto.FileUploadDTO;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The idea is to have all processor in a class and apply them to a file upload
 */
public class FileUploadProcessorChain {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileUploadProcessorChain.class);
    private final List<FileUploadProcessor<FileUploadDTO>> fileUploadProcessors;

    public FileUploadProcessorChain(final List<FileUploadProcessor<FileUploadDTO>> fileUploadProcessors) {
        this.fileUploadProcessors = fileUploadProcessors;
    }

    public FileUploadProcessorChain() {
        this.fileUploadProcessors = new CopyOnWriteArrayList<>();
    }

    public void addProcessor(FileUploadProcessor<FileUploadDTO> fileUploadProcessor) {
        log.info("Adding new file-upload processor {}", fileUploadProcessor);
        this.fileUploadProcessors.add(fileUploadProcessor);
    }

    public List<FileUploadDTO> apply(FileUploadDTO fileUploadDTO, FileNotification fileNotification) {
        log.debug("Applying {} file upload processors to file-upload {} with notification {}", this.fileUploadProcessors.size(), fileUploadDTO, fileNotification);
        return fileUploadProcessors.stream().map(processor -> processor.processFileUpload(fileUploadDTO, fileNotification)).collect(ImmutableList.toImmutableList());
    }
}
