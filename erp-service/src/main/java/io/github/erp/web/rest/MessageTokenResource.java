package io.github.erp.web.rest;

import io.github.erp.service.MessageTokenService;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import io.github.erp.service.dto.MessageTokenDTO;
import io.github.erp.service.dto.MessageTokenCriteria;
import io.github.erp.service.MessageTokenQueryService;

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
 * REST controller for managing {@link io.github.erp.domain.MessageToken}.
 */
@RestController
@RequestMapping("/api")
public class MessageTokenResource {

    private final Logger log = LoggerFactory.getLogger(MessageTokenResource.class);

    private static final String ENTITY_NAME = "filesMessageToken";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MessageTokenService messageTokenService;

    private final MessageTokenQueryService messageTokenQueryService;

    public MessageTokenResource(MessageTokenService messageTokenService, MessageTokenQueryService messageTokenQueryService) {
        this.messageTokenService = messageTokenService;
        this.messageTokenQueryService = messageTokenQueryService;
    }

    /**
     * {@code POST  /message-tokens} : Create a new messageToken.
     *
     * @param messageTokenDTO the messageTokenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new messageTokenDTO, or with status {@code 400 (Bad Request)} if the messageToken has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/message-tokens")
    public ResponseEntity<MessageTokenDTO> createMessageToken(@Valid @RequestBody MessageTokenDTO messageTokenDTO) throws URISyntaxException {
        log.debug("REST request to save MessageToken : {}", messageTokenDTO);
        if (messageTokenDTO.getId() != null) {
            throw new BadRequestAlertException("A new messageToken cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MessageTokenDTO result = messageTokenService.save(messageTokenDTO);
        return ResponseEntity.created(new URI("/api/message-tokens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /message-tokens} : Updates an existing messageToken.
     *
     * @param messageTokenDTO the messageTokenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageTokenDTO,
     * or with status {@code 400 (Bad Request)} if the messageTokenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the messageTokenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/message-tokens")
    public ResponseEntity<MessageTokenDTO> updateMessageToken(@Valid @RequestBody MessageTokenDTO messageTokenDTO) throws URISyntaxException {
        log.debug("REST request to update MessageToken : {}", messageTokenDTO);
        if (messageTokenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MessageTokenDTO result = messageTokenService.save(messageTokenDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, messageTokenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /message-tokens} : get all the messageTokens.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of messageTokens in body.
     */
    @GetMapping("/message-tokens")
    public ResponseEntity<List<MessageTokenDTO>> getAllMessageTokens(MessageTokenCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MessageTokens by criteria: {}", criteria);
        Page<MessageTokenDTO> page = messageTokenQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /message-tokens/count} : count all the messageTokens.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/message-tokens/count")
    public ResponseEntity<Long> countMessageTokens(MessageTokenCriteria criteria) {
        log.debug("REST request to count MessageTokens by criteria: {}", criteria);
        return ResponseEntity.ok().body(messageTokenQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /message-tokens/:id} : get the "id" messageToken.
     *
     * @param id the id of the messageTokenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the messageTokenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/message-tokens/{id}")
    public ResponseEntity<MessageTokenDTO> getMessageToken(@PathVariable Long id) {
        log.debug("REST request to get MessageToken : {}", id);
        Optional<MessageTokenDTO> messageTokenDTO = messageTokenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(messageTokenDTO);
    }

    /**
     * {@code DELETE  /message-tokens/:id} : delete the "id" messageToken.
     *
     * @param id the id of the messageTokenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/message-tokens/{id}")
    public ResponseEntity<Void> deleteMessageToken(@PathVariable Long id) {
        log.debug("REST request to delete MessageToken : {}", id);
        messageTokenService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/message-tokens?query=:query} : search for the messageToken corresponding
     * to the query.
     *
     * @param query the query of the messageToken search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/message-tokens")
    public ResponseEntity<List<MessageTokenDTO>> searchMessageTokens(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MessageTokens for query {}", query);
        Page<MessageTokenDTO> page = messageTokenService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
