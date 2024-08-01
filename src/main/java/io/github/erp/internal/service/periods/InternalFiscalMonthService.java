package io.github.erp.internal.service.periods;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import io.github.erp.service.dto.FiscalMonthDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.FiscalMonth}.
 */
public interface InternalFiscalMonthService {
    /**
     * Save a fiscalMonth.
     *
     * @param fiscalMonthDTO the entity to save.
     * @return the persisted entity.
     */
    FiscalMonthDTO save(FiscalMonthDTO fiscalMonthDTO);

    /**
     * Partially updates a fiscalMonth.
     *
     * @param fiscalMonthDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FiscalMonthDTO> partialUpdate(FiscalMonthDTO fiscalMonthDTO);

    /**
     * Get all the fiscalMonths.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FiscalMonthDTO> findAll(Pageable pageable);

    /**
     * Get all the fiscalMonths with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FiscalMonthDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" fiscalMonth.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FiscalMonthDTO> findOne(Long id);

    /**
     * Delete the "id" fiscalMonth.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the fiscalMonth corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FiscalMonthDTO> search(String query, Pageable pageable);

    /**
     * Return the adjacent fiscal-month after x number of periods have lapsed
     * @param currentFiscalMonthId of the current fiscal-month from whence we commence operation
     * @param fiscalPeriods number of periods lapsed
     * @return
     */
    Optional<FiscalMonthDTO> findOneAfterXPeriods(long currentFiscalMonthId, int fiscalPeriods);
}
