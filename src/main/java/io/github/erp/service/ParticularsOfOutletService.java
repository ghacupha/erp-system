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
import io.github.erp.service.dto.ParticularsOfOutletDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.ParticularsOfOutlet}.
 */
public interface ParticularsOfOutletService {
    /**
     * Save a particularsOfOutlet.
     *
     * @param particularsOfOutletDTO the entity to save.
     * @return the persisted entity.
     */
    ParticularsOfOutletDTO save(ParticularsOfOutletDTO particularsOfOutletDTO);

    /**
     * Partially updates a particularsOfOutlet.
     *
     * @param particularsOfOutletDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ParticularsOfOutletDTO> partialUpdate(ParticularsOfOutletDTO particularsOfOutletDTO);

    /**
     * Get all the particularsOfOutlets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParticularsOfOutletDTO> findAll(Pageable pageable);

    /**
     * Get the "id" particularsOfOutlet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ParticularsOfOutletDTO> findOne(Long id);

    /**
     * Delete the "id" particularsOfOutlet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the particularsOfOutlet corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParticularsOfOutletDTO> search(String query, Pageable pageable);
}
