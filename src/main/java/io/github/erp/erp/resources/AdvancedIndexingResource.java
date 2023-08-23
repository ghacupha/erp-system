package io.github.erp.erp.resources;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
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
import io.github.erp.erp.index.engine_v1.AsynchronousIndexingService;
import io.github.erp.security.SecurityUtils;
import io.github.erp.service.criteria.DealerCriteria;
import io.github.erp.service.dto.DealerDTO;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;

@RestController("advancedIndexingResource")
@RequestMapping("/api/index")
public class AdvancedIndexingResource {

    private static final Logger log = LoggerFactory.getLogger(AdvancedIndexingResource.class);

    private final AsynchronousIndexingService asynchronousIndexingService;

    public AdvancedIndexingResource(AsynchronousIndexingService asynchronousIndexingService) {
        this.asynchronousIndexingService = asynchronousIndexingService;
    }

    /**
     * GET /elasticsearch/re-index -> Reindex all Dealer documents
     */
    @GetMapping("/run-index")
    @Timed
    @ResponseStatus(HttpStatus.ACCEPTED)
    // @Secured(AuthoritiesConstants.PAYMENTS_USER)
    public ResponseEntity<?> reindexAll() {
        log.info("REST request to reindex Elasticsearch by : {}", SecurityUtils.getCurrentUserLogin().orElse("user"));

        try {
            asynchronousIndexingService.startAsynchronousIndex();

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception ignored) {

            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
        }
    }
}
