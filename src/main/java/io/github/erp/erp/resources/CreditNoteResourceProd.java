package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
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
import io.github.erp.repository.CreditNoteRepository;
import io.github.erp.service.CreditNoteQueryService;
import io.github.erp.service.CreditNoteService;
import io.github.erp.service.criteria.CreditNoteCriteria;
import io.github.erp.service.dto.CreditNoteDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.erp.domain.CreditNote}.
 */
@RestController("creditNoteResourceProd")
@RequestMapping("/api/payments")
public class CreditNoteResourceProd {

    private final Logger log = LoggerFactory.getLogger(CreditNoteResourceProd.class);

    private static final String ENTITY_NAME = "creditNote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CreditNoteService creditNoteService;

    private final CreditNoteRepository creditNoteRepository;

    private final CreditNoteQueryService creditNoteQueryService;

    public CreditNoteResourceProd(
        CreditNoteService creditNoteService,
        CreditNoteRepository creditNoteRepository,
        CreditNoteQueryService creditNoteQueryService
    ) {
        this.creditNoteService = creditNoteService;
        this.creditNoteRepository = creditNoteRepository;
        this.creditNoteQueryService = creditNoteQueryService;
    }

    /**
     * {@code POST  /credit-notes} : Create a new creditNote.
     *
     * @param creditNoteDTO the creditNoteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new creditNoteDTO, or with status {@code 400 (Bad Request)} if the creditNote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/credit-notes")
    public ResponseEntity<CreditNoteDTO> createCreditNote(@Valid @RequestBody CreditNoteDTO creditNoteDTO) throws URISyntaxException {
        log.debug("REST request to save CreditNote : {}", creditNoteDTO);
        if (creditNoteDTO.getId() != null) {
            throw new BadRequestAlertException("A new creditNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CreditNoteDTO result = creditNoteService.save(creditNoteDTO);
        return ResponseEntity
            .created(new URI("/api/credit-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /credit-notes/:id} : Updates an existing creditNote.
     *
     * @param id the id of the creditNoteDTO to save.
     * @param creditNoteDTO the creditNoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditNoteDTO,
     * or with status {@code 400 (Bad Request)} if the creditNoteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the creditNoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/credit-notes/{id}")
    public ResponseEntity<CreditNoteDTO> updateCreditNote(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CreditNoteDTO creditNoteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CreditNote : {}, {}", id, creditNoteDTO);
        if (creditNoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditNoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CreditNoteDTO result = creditNoteService.save(creditNoteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, creditNoteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /credit-notes/:id} : Partial updates given fields of an existing creditNote, field will ignore if it is null
     *
     * @param id the id of the creditNoteDTO to save.
     * @param creditNoteDTO the creditNoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditNoteDTO,
     * or with status {@code 400 (Bad Request)} if the creditNoteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the creditNoteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the creditNoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/credit-notes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CreditNoteDTO> partialUpdateCreditNote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CreditNoteDTO creditNoteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CreditNote partially : {}, {}", id, creditNoteDTO);
        if (creditNoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditNoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CreditNoteDTO> result = creditNoteService.partialUpdate(creditNoteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, creditNoteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /credit-notes} : get all the creditNotes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of creditNotes in body.
     */
    @GetMapping("/credit-notes")
    public ResponseEntity<List<CreditNoteDTO>> getAllCreditNotes(CreditNoteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CreditNotes by criteria: {}", criteria);
        Page<CreditNoteDTO> page = creditNoteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /credit-notes/count} : count all the creditNotes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/credit-notes/count")
    public ResponseEntity<Long> countCreditNotes(CreditNoteCriteria criteria) {
        log.debug("REST request to count CreditNotes by criteria: {}", criteria);
        return ResponseEntity.ok().body(creditNoteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /credit-notes/:id} : get the "id" creditNote.
     *
     * @param id the id of the creditNoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the creditNoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/credit-notes/{id}")
    public ResponseEntity<CreditNoteDTO> getCreditNote(@PathVariable Long id) {
        log.debug("REST request to get CreditNote : {}", id);
        Optional<CreditNoteDTO> creditNoteDTO = creditNoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(creditNoteDTO);
    }

    /**
     * {@code DELETE  /credit-notes/:id} : delete the "id" creditNote.
     *
     * @param id the id of the creditNoteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/credit-notes/{id}")
    public ResponseEntity<Void> deleteCreditNote(@PathVariable Long id) {
        log.debug("REST request to delete CreditNote : {}", id);
        creditNoteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/credit-notes?query=:query} : search for the creditNote corresponding
     * to the query.
     *
     * @param query the query of the creditNote search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/credit-notes")
    public ResponseEntity<List<CreditNoteDTO>> searchCreditNotes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CreditNotes for query {}", query);
        Page<CreditNoteDTO> page = creditNoteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
