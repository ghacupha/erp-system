package io.github.erp.erp.resources.gdi;

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
import io.github.erp.erp.index.engine_v1.AsynchronousIndexingService;
import io.github.erp.security.SecurityUtils;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
