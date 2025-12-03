package io.github.erp.erp.resources.leases;

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
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationProcessor;
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationRequest;
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationResponse;
import io.github.erp.domain.LiabilityEnumeration;
import io.github.erp.erp.leases.liability.enumeration.batch.LiabilityEnumerationBatchConfiguration;
import io.github.erp.erp.leases.liability.enumeration.batch.LiabilityEnumerationJobLauncher;
import io.github.erp.repository.LiabilityEnumerationRepository;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController("liabilityEnumerationResourceProd")
@RequestMapping("/api/leases")
public class LiabilityEnumerationResourceProd {

    private static final Logger log = LoggerFactory.getLogger(LiabilityEnumerationResourceProd.class);

    private final LiabilityEnumerationProcessor liabilityEnumerationProcessor;
    private final LiabilityEnumerationRepository liabilityEnumerationRepository;

    public LiabilityEnumerationResourceProd(
        LiabilityEnumerationRepository liabilityEnumerationRepository,
        JobLauncher jobLauncher,
        @Qualifier(LiabilityEnumerationBatchConfiguration.JOB_NAME) Job liabilityEnumerationJob
    ) {
        this.liabilityEnumerationProcessor =
            new LiabilityEnumerationProcessor(
                new LiabilityEnumerationJobLauncher(jobLauncher, liabilityEnumerationJob));
        this.liabilityEnumerationRepository = liabilityEnumerationRepository;
    }

    @PostMapping("/liability-enumerations")
    public ResponseEntity<LiabilityEnumerationResponse> enumerate(@Valid @RequestBody LiabilityEnumerationRequest request) {
        log.debug("REST request to enumerate present values for lease contract {}", request.getLeaseContractId());
        LiabilityEnumerationResponse response = liabilityEnumerationProcessor.enumerate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/liability-enumerations")
    public ResponseEntity<List<LiabilityEnumeration>> getAllLiabilityEnumerations(@PageableDefault(size = 10) Pageable pageable) {
        Page<LiabilityEnumeration> page = liabilityEnumerationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/liability-enumerations/{id}")
    public ResponseEntity<LiabilityEnumeration> getLiabilityEnumeration(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(liabilityEnumerationRepository.findById(id));
    }
}
