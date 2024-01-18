package io.github.erp.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.service.dto.CreditCardFacilityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.CreditCardFacility}.
 */
public interface CreditCardFacilityService {
    /**
     * Save a creditCardFacility.
     *
     * @param creditCardFacilityDTO the entity to save.
     * @return the persisted entity.
     */
    CreditCardFacilityDTO save(CreditCardFacilityDTO creditCardFacilityDTO);

    /**
     * Partially updates a creditCardFacility.
     *
     * @param creditCardFacilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CreditCardFacilityDTO> partialUpdate(CreditCardFacilityDTO creditCardFacilityDTO);

    /**
     * Get all the creditCardFacilities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CreditCardFacilityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" creditCardFacility.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CreditCardFacilityDTO> findOne(Long id);

    /**
     * Delete the "id" creditCardFacility.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the creditCardFacility corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CreditCardFacilityDTO> search(String query, Pageable pageable);
}
