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
import io.github.erp.service.dto.FxTransactionRateTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.FxTransactionRateType}.
 */
public interface FxTransactionRateTypeService {
    /**
     * Save a fxTransactionRateType.
     *
     * @param fxTransactionRateTypeDTO the entity to save.
     * @return the persisted entity.
     */
    FxTransactionRateTypeDTO save(FxTransactionRateTypeDTO fxTransactionRateTypeDTO);

    /**
     * Partially updates a fxTransactionRateType.
     *
     * @param fxTransactionRateTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FxTransactionRateTypeDTO> partialUpdate(FxTransactionRateTypeDTO fxTransactionRateTypeDTO);

    /**
     * Get all the fxTransactionRateTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FxTransactionRateTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fxTransactionRateType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FxTransactionRateTypeDTO> findOne(Long id);

    /**
     * Delete the "id" fxTransactionRateType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the fxTransactionRateType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FxTransactionRateTypeDTO> search(String query, Pageable pageable);
}
