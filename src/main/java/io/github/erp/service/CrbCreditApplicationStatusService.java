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
import io.github.erp.service.dto.CrbCreditApplicationStatusDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.CrbCreditApplicationStatus}.
 */
public interface CrbCreditApplicationStatusService {
    /**
     * Save a crbCreditApplicationStatus.
     *
     * @param crbCreditApplicationStatusDTO the entity to save.
     * @return the persisted entity.
     */
    CrbCreditApplicationStatusDTO save(CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO);

    /**
     * Partially updates a crbCreditApplicationStatus.
     *
     * @param crbCreditApplicationStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CrbCreditApplicationStatusDTO> partialUpdate(CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO);

    /**
     * Get all the crbCreditApplicationStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CrbCreditApplicationStatusDTO> findAll(Pageable pageable);

    /**
     * Get the "id" crbCreditApplicationStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CrbCreditApplicationStatusDTO> findOne(Long id);

    /**
     * Delete the "id" crbCreditApplicationStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the crbCreditApplicationStatus corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CrbCreditApplicationStatusDTO> search(String query, Pageable pageable);
}
