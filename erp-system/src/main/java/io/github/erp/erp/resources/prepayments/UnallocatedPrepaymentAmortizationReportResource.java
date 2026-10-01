package io.github.erp.erp.resources.prepayments;

/*-
 * Erp System - Mark X No 12 (Kadar Series) Server ver 1.9.0
 * Copyright © 2021 - 2026 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.erp.repository.prepayments.UnallocatedPrepaymentAmortizationReportRepository;
import io.github.erp.erp.reports.prepayments.UnallocatedPrepaymentAmortizationReportItem;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/prepayments")
public class UnallocatedPrepaymentAmortizationReportResource {

    private final Logger log = LoggerFactory.getLogger(UnallocatedPrepaymentAmortizationReportResource.class);

    private final UnallocatedPrepaymentAmortizationReportRepository repository;

    public UnallocatedPrepaymentAmortizationReportResource(UnallocatedPrepaymentAmortizationReportRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/unallocated-prepayment-amortizations")
    public ResponseEntity<List<UnallocatedPrepaymentAmortizationReportItem>> getUnallocatedPrepaymentAmortizations(Pageable pageable) {
        log.debug("REST request for unallocated prepayment amortizations");
        Page<UnallocatedPrepaymentAmortizationReportItem> page = repository.findUnallocated(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
