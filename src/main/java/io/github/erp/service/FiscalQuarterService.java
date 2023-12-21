package io.github.erp.service;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import io.github.erp.service.dto.FiscalQuarterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.FiscalQuarter}.
 */
public interface FiscalQuarterService {
    /**
     * Save a fiscalQuarter.
     *
     * @param fiscalQuarterDTO the entity to save.
     * @return the persisted entity.
     */
    FiscalQuarterDTO save(FiscalQuarterDTO fiscalQuarterDTO);

    /**
     * Partially updates a fiscalQuarter.
     *
     * @param fiscalQuarterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FiscalQuarterDTO> partialUpdate(FiscalQuarterDTO fiscalQuarterDTO);

    /**
     * Get all the fiscalQuarters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FiscalQuarterDTO> findAll(Pageable pageable);

    /**
     * Get all the fiscalQuarters with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FiscalQuarterDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" fiscalQuarter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FiscalQuarterDTO> findOne(Long id);

    /**
     * Delete the "id" fiscalQuarter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the fiscalQuarter corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FiscalQuarterDTO> search(String query, Pageable pageable);
}
