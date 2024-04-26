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
import io.github.erp.service.dto.CollateralInformationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.CollateralInformation}.
 */
public interface CollateralInformationService {
    /**
     * Save a collateralInformation.
     *
     * @param collateralInformationDTO the entity to save.
     * @return the persisted entity.
     */
    CollateralInformationDTO save(CollateralInformationDTO collateralInformationDTO);

    /**
     * Partially updates a collateralInformation.
     *
     * @param collateralInformationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CollateralInformationDTO> partialUpdate(CollateralInformationDTO collateralInformationDTO);

    /**
     * Get all the collateralInformations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CollateralInformationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" collateralInformation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CollateralInformationDTO> findOne(Long id);

    /**
     * Delete the "id" collateralInformation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the collateralInformation corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CollateralInformationDTO> search(String query, Pageable pageable);
}
