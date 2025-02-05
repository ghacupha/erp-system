package io.github.erp.internal.service.wip;

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

import io.github.erp.domain.WIPListReport;
import io.github.erp.service.criteria.WIPListReportCriteria;
import io.github.erp.service.dto.WIPListReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.WIPListReport}.
 */
public interface InternalWIPListReportService {
    /**
     * Save a wIPListReport.
     *
     * @param wIPListReportDTO the entity to save.
     * @return the persisted entity.
     */
    WIPListReportDTO save(WIPListReportDTO wIPListReportDTO);

    /**
     * Partially updates a wIPListReport.
     *
     * @param wIPListReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WIPListReportDTO> partialUpdate(WIPListReportDTO wIPListReportDTO);

    /**
     * Get all the wIPListReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WIPListReportDTO> findAll(Pageable pageable);

    /**
     * Get the "id" wIPListReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WIPListReportDTO> findOne(Long id);

    /**
     * Delete the "id" wIPListReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the wIPListReport corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WIPListReportDTO> search(String query, Pageable pageable);


    /**
     * Return a {@link List} of {@link WIPListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    List<WIPListReportDTO> findByCriteria(WIPListReportCriteria criteria);

    /**
     * Return a {@link Page} of {@link WIPListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    Page<WIPListReportDTO> findByCriteria(WIPListReportCriteria criteria, Pageable page);

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    long countByCriteria(WIPListReportCriteria criteria);
}
