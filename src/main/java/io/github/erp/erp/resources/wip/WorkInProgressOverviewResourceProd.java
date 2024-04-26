package io.github.erp.erp.resources.wip;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
