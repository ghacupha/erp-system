package io.github.erp.internal.service.leases;

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
import io.github.erp.internal.model.LeaseLiabilityReportItemREPO;
import io.github.erp.service.dto.LeaseLiabilityReportItemDTO;
import io.github.erp.service.dto.LeasePeriodDTO;
import io.github.erp.service.dto.LeaseRepaymentPeriodDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.LeaseLiabilityReportItem}.
 */
public interface InternalLeaseLiabilityReportItemService {
    /**
     * Save a leaseLiabilityReportItem.
     *
     * @param leaseLiabilityReportItemDTO the entity to save.
     * @return the persisted entity.
     */
    LeaseLiabilityReportItemDTO save(LeaseLiabilityReportItemDTO leaseLiabilityReportItemDTO);

    /**
     * Partially updates a leaseLiabilityReportItem.
     *
     * @param leaseLiabilityReportItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeaseLiabilityReportItemDTO> partialUpdate(LeaseLiabilityReportItemDTO leaseLiabilityReportItemDTO);

    /**
     * Get all the leaseLiabilityReportItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseLiabilityReportItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" leaseLiabilityReportItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaseLiabilityReportItemDTO> findOne(Long id);

    /**
     * Delete the "id" leaseLiabilityReportItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the leaseLiabilityReportItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseLiabilityReportItemDTO> search(String query, Pageable pageable);

    /**
     * Pageable lease-period-report items listed down for a user specified period
     *
     * @param leasePeriod Period for which the lease-liability item is current
     * @param pageable Pageable object for the lease liability listings
     * @return Lease liability items
     */
    Page<LeaseLiabilityReportItemDTO> leaseLiabilityReportItemsByLeasePeriod(LeaseRepaymentPeriodDTO leasePeriod, Pageable pageable);
}
