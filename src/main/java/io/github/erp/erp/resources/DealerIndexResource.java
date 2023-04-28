package io.github.erp.erp.resources;

import io.github.erp.erp.index.DealersReIndexerService;
import io.github.erp.security.SecurityUtils;
import io.github.erp.service.DealerQueryService;
import io.github.erp.service.criteria.DealerCriteria;
import io.github.erp.service.dto.DealerDTO;
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

@RestController("DealerIndexResource")
@RequestMapping("/api/app")
@Profile("!no-index")
public class DealerIndexResource {

    private final static Logger log = LoggerFactory.getLogger(DealerIndexResource.class);

    private final DealersReIndexerService reIndexerService;
    private final DealerQueryService dealerQueryService;

    public DealerIndexResource(DealersReIndexerService reIndexerService, DealerQueryService dealerQueryService) {
        this.reIndexerService = reIndexerService;
        this.dealerQueryService = dealerQueryService;
    }

    /**
     * GET /elasticsearch/re-index -> Reindex all Dealer documents
     */
    @GetMapping("/dealers/elasticsearch/re-index")
    @Timed
    // @Secured(AuthoritiesConstants.PAYMENTS_USER)
    public ResponseEntity<List<DealerDTO>> reindexAll(DealerCriteria criteria, Pageable pageable) {
        log.info("REST request to reindex Elasticsearch by : {}", SecurityUtils.getCurrentUserLogin().orElse("user"));

        reIndexerService.reIndex();

        Page<DealerDTO> page = dealerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok()
            .headers(headers)
            .body(page.getContent());
    }
}
