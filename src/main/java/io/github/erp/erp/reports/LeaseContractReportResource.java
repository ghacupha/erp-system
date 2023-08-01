package io.github.erp.erp.reports;

/*-
 * Erp System - Mark IV No 3 (Ehud Series) Server ver 1.3.3
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.service.LeaseContractQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import javax.validation.Valid;

// TODO Complete system reporting for this
@RestController("LeaseContractReportResource")
@RequestMapping("/api/leases")
public class LeaseContractReportResource {

    private static final Logger log = LoggerFactory.getLogger(LeaseContractReportResource.class);

    private final LeaseContractQueryService leaseContractQueryService;

    public LeaseContractReportResource(LeaseContractQueryService leaseContractQueryService) {
        this.leaseContractQueryService = leaseContractQueryService;
    }

    /**
     * {@code POST  /lease-contracts} : Create a new leaseContract.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseContracts in body.
     */
    @PostMapping("/contract-listing")
    public ResponseEntity<Page<LeaseContractReportDTO>> createContractList(@Valid @RequestBody LeaseContractReportRequisition reportRequisition) {
        log.debug("REST request to get Lease Contracts Listing by criteria: {}", reportRequisition);
        Page<LeaseContractReportDTO> page = Page.empty();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }
}
