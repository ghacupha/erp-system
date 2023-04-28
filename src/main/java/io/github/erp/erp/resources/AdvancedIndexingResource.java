package io.github.erp.erp.resources;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;

@RestController("AdvancedIndexingResource")
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
    @PostMapping("/run-index")
    @Timed
    @ResponseStatus(HttpStatus.ACCEPTED)
    // @Secured(AuthoritiesConstants.PAYMENTS_USER)
    public void reindexAll() {
        log.info("REST request to reindex Elasticsearch by : {}", SecurityUtils.getCurrentUserLogin().orElse("user"));

        asynchronousIndexingService.startAsynchronousIndex();

    }
}
