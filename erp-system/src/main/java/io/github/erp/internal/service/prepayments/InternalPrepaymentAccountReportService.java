package io.github.erp.internal.service.prepayments;

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
import io.github.erp.service.dto.PrepaymentAccountReportDTO;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PrepaymentAccountReport}.
 */
public interface InternalPrepaymentAccountReportService {
    /**
     * Save a prepaymentAccountReport.
     *
     * @param prepaymentAccountReportDTO the entity to save.
     * @return the persisted entity.
     */
    PrepaymentAccountReportDTO save(PrepaymentAccountReportDTO prepaymentAccountReportDTO);

    /**
     * Partially updates a prepaymentAccountReport.
     *
     * @param prepaymentAccountReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrepaymentAccountReportDTO> partialUpdate(PrepaymentAccountReportDTO prepaymentAccountReportDTO);

    /**
     * Get all the prepaymentAccountReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentAccountReportDTO> findAll(Pageable pageable);

    /**
     * Get the "id" prepaymentAccountReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrepaymentAccountReportDTO> findOne(Long id);

    /**
     * Delete the "id" prepaymentAccountReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the prepaymentAccountReport corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentAccountReportDTO> search(String query, Pageable pageable);

    /**
     * Fetch the prepayment account report items as at a given date
     * @param pageable the pagination information
     * @return list of prepayment report items
     */
    Page<PrepaymentAccountReportDTO> findAllByReportDate(LocalDate reportDate, Pageable pageable);

    /**
     * Fetch the account report as a single item value by specified date
     * @param reportDate date specified for account balance
     * @param id id of the specified transaction-account
     * @return account value for specified date
     */
    Optional<PrepaymentAccountReportDTO> findOneByReportDate(LocalDate reportDate, long id);
}
