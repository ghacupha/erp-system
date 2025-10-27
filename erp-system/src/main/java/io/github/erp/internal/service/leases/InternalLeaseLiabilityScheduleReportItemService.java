package io.github.erp.internal.service.leases;

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
import io.github.erp.service.dto.LeaseLiabilityInterestExpenseSummaryDTO;
import io.github.erp.service.dto.LeaseLiabilityScheduleReportItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.LeaseLiabilityScheduleReportItem}.
 */
public interface InternalLeaseLiabilityScheduleReportItemService {
    /**
     * Save a leaseLiabilityScheduleReportItem.
     *
     * @param leaseLiabilityScheduleReportItemDTO the entity to save.
     * @return the persisted entity.
     */
    LeaseLiabilityScheduleReportItemDTO save(LeaseLiabilityScheduleReportItemDTO leaseLiabilityScheduleReportItemDTO);

    /**
     * Partially updates a leaseLiabilityScheduleReportItem.
     *
     * @param leaseLiabilityScheduleReportItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeaseLiabilityScheduleReportItemDTO> partialUpdate(LeaseLiabilityScheduleReportItemDTO leaseLiabilityScheduleReportItemDTO);

    /**
     * Get all the leaseLiabilityScheduleReportItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseLiabilityScheduleReportItemDTO> findAll(Pageable pageable);

    /**
     * Get the lease liability interest expense summary for the supplied lease period.
     *
     * @param leasePeriodId the lease period identifier driving the report window.
     * @return a fully mapped list of report rows.
     */
    List<LeaseLiabilityInterestExpenseSummaryDTO> getLeaseLiabilityInterestExpenseSummary(long leasePeriodId);

    /**
     * Get the "id" leaseLiabilityScheduleReportItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaseLiabilityScheduleReportItemDTO> findOne(Long id);

    /**
     * Delete the "id" leaseLiabilityScheduleReportItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the leaseLiabilityScheduleReportItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseLiabilityScheduleReportItemDTO> search(String query, Pageable pageable);
}
