package io.github.erp.internal.framework.service;

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
import java.util.Optional;

/**
 * This class is intended for use with the deletion job listener. This is the listener called after file-related
 * data has been removed from the repository and the file can now safely be deleted as well.
 * The parameter FUDTO is the file-upload-DTO which contains the binary file array for be removed from the library.
 * To remain generic the listener has been configured to use the has-index interface for the FUDTO parameter, that
 * way the listener can then be configured with the project specific file-upload entity
 */
public interface FileUploadDeletionService<FUDTO> {

    Optional<FUDTO> findOne(long fileId);

    void delete(Long id);
}
