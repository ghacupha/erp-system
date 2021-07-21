package io.github.erp.web.rest;

import io.github.erp.service.DealerService;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import io.github.erp.service.dto.DealerDTO;
import io.github.erp.service.dto.DealerCriteria;
import io.github.erp.service.DealerQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.github.erp.domain.Dealer}.
 */
@RestController
@RequestMapping("/api")
public class DealerResource {

    private final Logger log = LoggerFactory.getLogger(DealerResource.class);

    private static final String ENTITY_NAME = "erpServiceDealer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DealerService dealerService;

    private final DealerQueryService dealerQueryService;

    public DealerResource(DealerService dealerService, DealerQueryService dealerQueryService) {
        this.dealerService = dealerService;
        this.dealerQueryService = dealerQueryService;
    }

    /**
     * {@code POST  /dealers} : Create a new dealer.
     *
     * @param dealerDTO the dealerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dealerDTO, or with status {@code 400 (Bad Request)} if the dealer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dealers")
    public ResponseEntity<DealerDTO> createDealer(@Valid @RequestBody DealerDTO dealerDTO) throws URISyntaxException {
        log.debug("REST request to save Dealer : {}", dealerDTO);
        if (dealerDTO.getId() != null) {
            throw new BadRequestAlertException("A new dealer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DealerDTO result = dealerService.save(dealerDTO);
        return ResponseEntity.created(new URI("/api/dealers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dealers} : Updates an existing dealer.
     *
     * @param dealerDTO the dealerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dealerDTO,
     * or with status {@code 400 (Bad Request)} if the dealerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dealerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dealers")
    public ResponseEntity<DealerDTO> updateDealer(@Valid @RequestBody DealerDTO dealerDTO) throws URISyntaxException {
        log.debug("REST request to update Dealer : {}", dealerDTO);
        if (dealerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DealerDTO result = dealerService.save(dealerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dealerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dealers} : get all the dealers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dealers in body.
     */
    @GetMapping("/dealers")
    public ResponseEntity<List<DealerDTO>> getAllDealers(DealerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Dealers by criteria: {}", criteria);
        Page<DealerDTO> page = dealerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dealers/count} : count all the dealers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/dealers/count")
    public ResponseEntity<Long> countDealers(DealerCriteria criteria) {
        log.debug("REST request to count Dealers by criteria: {}", criteria);
        return ResponseEntity.ok().body(dealerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dealers/:id} : get the "id" dealer.
     *
     * @param id the id of the dealerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dealerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dealers/{id}")
    public ResponseEntity<DealerDTO> getDealer(@PathVariable Long id) {
        log.debug("REST request to get Dealer : {}", id);
        Optional<DealerDTO> dealerDTO = dealerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dealerDTO);
    }

    /**
     * {@code DELETE  /dealers/:id} : delete the "id" dealer.
     *
     * @param id the id of the dealerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dealers/{id}")
    public ResponseEntity<Void> deleteDealer(@PathVariable Long id) {
        log.debug("REST request to delete Dealer : {}", id);
        dealerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/dealers?query=:query} : search for the dealer corresponding
     * to the query.
     *
     * @param query the query of the dealer search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/dealers")
    public ResponseEntity<List<DealerDTO>> searchDealers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Dealers for query {}", query);
        Page<DealerDTO> page = dealerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
