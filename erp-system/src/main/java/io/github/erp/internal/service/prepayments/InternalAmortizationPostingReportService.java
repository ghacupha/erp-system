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
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.AmortizationPostingReport}.
 */
public interface InternalAmortizationPostingReportService {
    /**
     * Save a amortizationPostingReport.
     *
     * @param amortizationPostingReportDTO the entity to save.
     * @return the persisted entity.
     */
    AmortizationPostingReportDTO save(AmortizationPostingReportDTO amortizationPostingReportDTO);

    /**
     * Partially updates a amortizationPostingReport.
     *
     * @param amortizationPostingReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AmortizationPostingReportDTO> partialUpdate(AmortizationPostingReportDTO amortizationPostingReportDTO);

    /**
     * Get all the amortizationPostingReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AmortizationPostingReportDTO> findAll(Pageable pageable);

    /**
     * Get the "id" amortizationPostingReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AmortizationPostingReportDTO> findOne(Long id);

    /**
     * Delete the "id" amortizationPostingReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the amortizationPostingReport corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AmortizationPostingReportDTO> search(String query, Pageable pageable);

    /**
     * Seek to find a list of amortization posting entries due on a particular date and in
     * particular such a date is determined by the configured last date of the amortization
     * period.All items in the
     * @param reportDate This is the reportDate parameter on which date the system is to have
     *                   posted entries for amortization
     * @return List of instances whose end of amortization period aligns with the query date
     */
    Optional<List<AmortizationPostingReportDTO>> findAllByReportDate(LocalDate reportDate);
}
