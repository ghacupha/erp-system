package io.github.erp.service;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.service.dto.MerchantTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.MerchantType}.
 */
public interface MerchantTypeService {
    /**
     * Save a merchantType.
     *
     * @param merchantTypeDTO the entity to save.
     * @return the persisted entity.
     */
    MerchantTypeDTO save(MerchantTypeDTO merchantTypeDTO);

    /**
     * Partially updates a merchantType.
     *
     * @param merchantTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MerchantTypeDTO> partialUpdate(MerchantTypeDTO merchantTypeDTO);

    /**
     * Get all the merchantTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MerchantTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" merchantType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MerchantTypeDTO> findOne(Long id);

    /**
     * Delete the "id" merchantType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the merchantType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MerchantTypeDTO> search(String query, Pageable pageable);
}
