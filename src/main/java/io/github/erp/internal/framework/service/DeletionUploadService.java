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
import java.util.List;
import java.util.Optional;

/**
 * This interface is used in batch deletion job instances to access data about the
 * file-upload entity's instance that is being deleted. It only need to provide the file itself
 * based on the file-id as well as the file based on a string token
 *
 * The method {@link DeletionUploadService::findAllByUploadToken()} uses the token to query the
 * repository find all instances of the DTO which have the same message-token. This method is specific to
 * the entity which is being configured for file-related batch deletion
 *
 * @param <DTO> This is the DTO containing the file-upload's instance message-token
 */
public interface DeletionUploadService<DTO> {

    /**
     * Return a list of persistent DTO entity instance whose file upload token is
     * equal to the stringToken provided in the args
     *
     * @param stringToken
     * @return
     */
    Optional<List<DTO>> findAllByUploadToken(String stringToken);
}
