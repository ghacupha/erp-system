package io.github.erp.internal.service.wip;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.service.dto.WIPTransferListReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.WIPTransferListReport}.
 */
public interface InternalWIPTransferListReportService {
    /**
     * Save a wIPTransferListReport.
     *
     * @param wIPTransferListReportDTO the entity to save.
     * @return the persisted entity.
     */
    WIPTransferListReportDTO save(WIPTransferListReportDTO wIPTransferListReportDTO);

    /**
     * Partially updates a wIPTransferListReport.
     *
     * @param wIPTransferListReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WIPTransferListReportDTO> partialUpdate(WIPTransferListReportDTO wIPTransferListReportDTO);

    /**
     * Get all the wIPTransferListReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WIPTransferListReportDTO> findAll(Pageable pageable);

    /**
     * Get the "id" wIPTransferListReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WIPTransferListReportDTO> findOne(Long id);

    /**
     * Delete the "id" wIPTransferListReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the wIPTransferListReport corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WIPTransferListReportDTO> search(String query, Pageable pageable);
}
