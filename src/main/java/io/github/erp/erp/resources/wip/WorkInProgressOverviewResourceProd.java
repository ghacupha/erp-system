package io.github.erp.erp.resources.wip;

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
import io.github.erp.domain.WorkInProgressOverview;
import io.github.erp.internal.repository.InternalWorkInProgressOverviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

import java.time.LocalDate;
import java.util.Optional;

/**
 * REST controller for managing {@link WorkInProgressOverview}.
 */
@RestController("WorkInProgressOverviewResourceProd")
@RequestMapping("/api/fixed-asset")
@Transactional
public class WorkInProgressOverviewResourceProd {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressOverviewResourceProd.class);

    private final InternalWorkInProgressOverviewRepository workInProgressOverviewRepository;

    public WorkInProgressOverviewResourceProd(InternalWorkInProgressOverviewRepository workInProgressOverviewRepository) {
        this.workInProgressOverviewRepository = workInProgressOverviewRepository;
    }

    /**
     * {@code GET  /work-in-progress-overviews/:id} : get the "id" workInProgressOverview.
     *
     * @param reportDate the date of the report to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workInProgressOverview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-in-progress-overview")
    public ResponseEntity<WorkInProgressOverview> getWorkInProgressOverview(@RequestParam("reportDate") String reportDate) {
        log.debug("REST request to get WorkInProgressOverview for date: {}", reportDate);
        Optional<WorkInProgressOverview> workInProgressOverview = workInProgressOverviewRepository.findByReportDate(LocalDate.parse(reportDate))
            .map(workInProgressOverviewDTO -> new WorkInProgressOverview()
                .numberOfItems(workInProgressOverviewDTO.getNumberOfItems())
                .instalmentAmount(workInProgressOverviewDTO.getInstalmentAmount())
                .totalTransferAmount(workInProgressOverviewDTO.getTotalTransferAmount())
                .outstandingAmount(workInProgressOverviewDTO.getOutstandingAmount()));

        return ResponseUtil.wrapOrNotFound(workInProgressOverview);
    }
}
