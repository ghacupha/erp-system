package io.github.erp.erp.reports;

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
