package io.github.erp.internal.service.prepayments;

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
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.PrepaymentReportRequisition}.
 */
public interface InternalPrepaymentReportRequisitionService {
    /**
     * Save a prepaymentReportRequisition.
     *
     * @param prepaymentReportRequisitionDTO the entity to save.
     * @return the persisted entity.
     */
    PrepaymentReportRequisitionDTO save(PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO);

    /**
     * Partially updates a prepaymentReportRequisition.
     *
     * @param prepaymentReportRequisitionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrepaymentReportRequisitionDTO> partialUpdate(PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO);

    /**
     * Get all the prepaymentReportRequisitions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentReportRequisitionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" prepaymentReportRequisition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrepaymentReportRequisitionDTO> findOne(Long id);

    /**
     * Delete the "id" prepaymentReportRequisition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the prepaymentReportRequisition corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentReportRequisitionDTO> search(String query, Pageable pageable);
}
