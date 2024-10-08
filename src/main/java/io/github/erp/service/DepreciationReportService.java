package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.service.dto.DepreciationReportDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.DepreciationReport}.
 */
public interface DepreciationReportService {
    /**
     * Save a depreciationReport.
     *
     * @param depreciationReportDTO the entity to save.
     * @return the persisted entity.
     */
    DepreciationReportDTO save(DepreciationReportDTO depreciationReportDTO);

    /**
     * Partially updates a depreciationReport.
     *
     * @param depreciationReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DepreciationReportDTO> partialUpdate(DepreciationReportDTO depreciationReportDTO);

    /**
     * Get all the depreciationReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationReportDTO> findAll(Pageable pageable);

    /**
     * Get the "id" depreciationReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepreciationReportDTO> findOne(Long id);

    /**
     * Delete the "id" depreciationReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the depreciationReport corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationReportDTO> search(String query, Pageable pageable);
}
