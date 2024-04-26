package io.github.erp.service;

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
import io.github.erp.service.dto.ShareholderTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.ShareholderType}.
 */
public interface ShareholderTypeService {
    /**
     * Save a shareholderType.
     *
     * @param shareholderTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ShareholderTypeDTO save(ShareholderTypeDTO shareholderTypeDTO);

    /**
     * Partially updates a shareholderType.
     *
     * @param shareholderTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShareholderTypeDTO> partialUpdate(ShareholderTypeDTO shareholderTypeDTO);

    /**
     * Get all the shareholderTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ShareholderTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" shareholderType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShareholderTypeDTO> findOne(Long id);

    /**
     * Delete the "id" shareholderType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the shareholderType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ShareholderTypeDTO> search(String query, Pageable pageable);
}
