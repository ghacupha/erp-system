package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.QuestionBaseRepository;
import io.github.erp.service.QuestionBaseQueryService;
import io.github.erp.service.QuestionBaseService;
import io.github.erp.service.criteria.QuestionBaseCriteria;
import io.github.erp.service.dto.QuestionBaseDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.erp.domain.QuestionBase}.
 */
@RestController
@RequestMapping("/api")
public class QuestionBaseResource {

    private final Logger log = LoggerFactory.getLogger(QuestionBaseResource.class);

    private static final String ENTITY_NAME = "gdiDataQuestionBase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionBaseService questionBaseService;

    private final QuestionBaseRepository questionBaseRepository;

    private final QuestionBaseQueryService questionBaseQueryService;

    public QuestionBaseResource(
        QuestionBaseService questionBaseService,
        QuestionBaseRepository questionBaseRepository,
        QuestionBaseQueryService questionBaseQueryService
    ) {
        this.questionBaseService = questionBaseService;
        this.questionBaseRepository = questionBaseRepository;
        this.questionBaseQueryService = questionBaseQueryService;
    }

    /**
     * {@code POST  /question-bases} : Create a new questionBase.
     *
     * @param questionBaseDTO the questionBaseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionBaseDTO, or with status {@code 400 (Bad Request)} if the questionBase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/question-bases")
    public ResponseEntity<QuestionBaseDTO> createQuestionBase(@Valid @RequestBody QuestionBaseDTO questionBaseDTO)
        throws URISyntaxException {
        log.debug("REST request to save QuestionBase : {}", questionBaseDTO);
        if (questionBaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionBase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionBaseDTO result = questionBaseService.save(questionBaseDTO);
        return ResponseEntity
            .created(new URI("/api/question-bases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /question-bases/:id} : Updates an existing questionBase.
     *
     * @param id the id of the questionBaseDTO to save.
     * @param questionBaseDTO the questionBaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionBaseDTO,
     * or with status {@code 400 (Bad Request)} if the questionBaseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionBaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/question-bases/{id}")
    public ResponseEntity<QuestionBaseDTO> updateQuestionBase(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuestionBaseDTO questionBaseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuestionBase : {}, {}", id, questionBaseDTO);
        if (questionBaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionBaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionBaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionBaseDTO result = questionBaseService.save(questionBaseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionBaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /question-bases/:id} : Partial updates given fields of an existing questionBase, field will ignore if it is null
     *
     * @param id the id of the questionBaseDTO to save.
     * @param questionBaseDTO the questionBaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionBaseDTO,
     * or with status {@code 400 (Bad Request)} if the questionBaseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionBaseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionBaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/question-bases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionBaseDTO> partialUpdateQuestionBase(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuestionBaseDTO questionBaseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuestionBase partially : {}, {}", id, questionBaseDTO);
        if (questionBaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionBaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionBaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionBaseDTO> result = questionBaseService.partialUpdate(questionBaseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionBaseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /question-bases} : get all the questionBases.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionBases in body.
     */
    @GetMapping("/question-bases")
    public ResponseEntity<List<QuestionBaseDTO>> getAllQuestionBases(QuestionBaseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get QuestionBases by criteria: {}", criteria);
        Page<QuestionBaseDTO> page = questionBaseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /question-bases/count} : count all the questionBases.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/question-bases/count")
    public ResponseEntity<Long> countQuestionBases(QuestionBaseCriteria criteria) {
        log.debug("REST request to count QuestionBases by criteria: {}", criteria);
        return ResponseEntity.ok().body(questionBaseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /question-bases/:id} : get the "id" questionBase.
     *
     * @param id the id of the questionBaseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionBaseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/question-bases/{id}")
    public ResponseEntity<QuestionBaseDTO> getQuestionBase(@PathVariable Long id) {
        log.debug("REST request to get QuestionBase : {}", id);
        Optional<QuestionBaseDTO> questionBaseDTO = questionBaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionBaseDTO);
    }

    /**
     * {@code DELETE  /question-bases/:id} : delete the "id" questionBase.
     *
     * @param id the id of the questionBaseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/question-bases/{id}")
    public ResponseEntity<Void> deleteQuestionBase(@PathVariable Long id) {
        log.debug("REST request to delete QuestionBase : {}", id);
        questionBaseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/question-bases?query=:query} : search for the questionBase corresponding
     * to the query.
     *
     * @param query the query of the questionBase search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/question-bases")
    public ResponseEntity<List<QuestionBaseDTO>> searchQuestionBases(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of QuestionBases for query {}", query);
        Page<QuestionBaseDTO> page = questionBaseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
