package io.github.erp.erp.resources.settlements;

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
import io.github.erp.erp.startUp.index.reindexer.PaymentInvoiceReIndexerService;
import io.github.erp.security.SecurityUtils;
import io.github.erp.service.PaymentInvoiceQueryService;
import io.github.erp.service.criteria.PaymentInvoiceCriteria;
import io.github.erp.service.dto.PaymentInvoiceDTO;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;

@Profile("!no-index")
@RestController("paymentInvoiceIndexResource")
@RequestMapping("/api/payments")
public class PaymentInvoiceIndexResource {

    private static final Logger log = LoggerFactory.getLogger(PaymentInvoiceIndexResource.class);

    private final PaymentInvoiceQueryService paymentInvoiceQueryService;

    private final PaymentInvoiceReIndexerService reIndexerService;

    public PaymentInvoiceIndexResource(PaymentInvoiceQueryService paymentInvoiceQueryService, PaymentInvoiceReIndexerService reIndexerService) {
        this.paymentInvoiceQueryService = paymentInvoiceQueryService;
        this.reIndexerService = reIndexerService;
    }

    /**
     * GET /elasticsearch/re-index -> Reindex all documents
     */
    @GetMapping("/payment-invoices/elasticsearch/re-index")
    @Timed
    // @Secured(AuthoritiesConstants.PAYMENTS_USER)
    public ResponseEntity<List<PaymentInvoiceDTO>> reindexAll(PaymentInvoiceCriteria criteria, Pageable pageable) {
        log.info("REST request to reindex Elasticsearch by : {}", SecurityUtils.getCurrentUserLogin().orElse("user"));

        reIndexerService.reIndex();

        Page<PaymentInvoiceDTO> page = paymentInvoiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok()
            .headers(headers)
            .body(page.getContent());
    }
}
