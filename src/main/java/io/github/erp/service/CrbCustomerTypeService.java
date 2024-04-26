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
import io.github.erp.service.dto.CrbCustomerTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.CrbCustomerType}.
 */
public interface CrbCustomerTypeService {
    /**
     * Save a crbCustomerType.
     *
     * @param crbCustomerTypeDTO the entity to save.
     * @return the persisted entity.
     */
    CrbCustomerTypeDTO save(CrbCustomerTypeDTO crbCustomerTypeDTO);

    /**
     * Partially updates a crbCustomerType.
     *
     * @param crbCustomerTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CrbCustomerTypeDTO> partialUpdate(CrbCustomerTypeDTO crbCustomerTypeDTO);

    /**
     * Get all the crbCustomerTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CrbCustomerTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" crbCustomerType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CrbCustomerTypeDTO> findOne(Long id);

    /**
     * Delete the "id" crbCustomerType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the crbCustomerType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CrbCustomerTypeDTO> search(String query, Pageable pageable);
}
