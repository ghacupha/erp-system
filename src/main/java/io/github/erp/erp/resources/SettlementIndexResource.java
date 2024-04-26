package io.github.erp.erp.resources;

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
import io.github.erp.erp.index.reindexer.SettlementReIndexerService;
import io.github.erp.security.SecurityUtils;
import io.github.erp.service.SettlementQueryService;
import io.github.erp.service.criteria.SettlementCriteria;
import io.github.erp.service.dto.SettlementDTO;
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
@RestController("settlementIndexResource")
@RequestMapping("/api/payments")
public class SettlementIndexResource {

    private static final Logger log = LoggerFactory.getLogger(SettlementIndexResource.class);

    private final SettlementQueryService settlementQueryService;
    private final SettlementReIndexerService reIndexerService;

    public SettlementIndexResource(SettlementQueryService settlementQueryService, SettlementReIndexerService reIndexerService) {
        this.settlementQueryService = settlementQueryService;
        this.reIndexerService = reIndexerService;
    }

    /**
     * GET /elasticsearch/re-index -> Reindex all Settlement documents
     */
    @GetMapping("/settlements/elasticsearch/re-index")
    @Timed
    // @Secured(AuthoritiesConstants.PAYMENTS_USER)
    public ResponseEntity<List<SettlementDTO>> reindexAll(SettlementCriteria criteria, Pageable pageable) {
        log.info("REST request to reindex Elasticsearch by : {}", SecurityUtils.getCurrentUserLogin().orElse("user"));

        reIndexerService.reIndex();

        Page<SettlementDTO> page = settlementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok()
            .headers(headers)
            .body(page.getContent());
    }
}
