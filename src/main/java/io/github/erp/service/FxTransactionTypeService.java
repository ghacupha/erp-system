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
import io.github.erp.service.dto.FxTransactionTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.FxTransactionType}.
 */
public interface FxTransactionTypeService {
    /**
     * Save a fxTransactionType.
     *
     * @param fxTransactionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    FxTransactionTypeDTO save(FxTransactionTypeDTO fxTransactionTypeDTO);

    /**
     * Partially updates a fxTransactionType.
     *
     * @param fxTransactionTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FxTransactionTypeDTO> partialUpdate(FxTransactionTypeDTO fxTransactionTypeDTO);

    /**
     * Get all the fxTransactionTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FxTransactionTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fxTransactionType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FxTransactionTypeDTO> findOne(Long id);

    /**
     * Delete the "id" fxTransactionType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the fxTransactionType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FxTransactionTypeDTO> search(String query, Pageable pageable);
}
