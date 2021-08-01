package io.github.erp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.FileType;
import io.github.erp.repository.FileTypeRepository;
import io.github.erp.service.FileTypeQueryService;
import io.github.erp.service.FileTypeService;
import io.github.erp.service.criteria.FileTypeCriteria;
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
 * REST controller for managing {@link io.github.erp.domain.FileType}.
 */
@RestController
@RequestMapping("/api")
public class FileTypeResource {

    private final Logger log = LoggerFactory.getLogger(FileTypeResource.class);

    private static final String ENTITY_NAME = "filesFileType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileTypeService fileTypeService;

    private final FileTypeRepository fileTypeRepository;

    private final FileTypeQueryService fileTypeQueryService;

    public FileTypeResource(
        FileTypeService fileTypeService,
        FileTypeRepository fileTypeRepository,
        FileTypeQueryService fileTypeQueryService
    ) {
        this.fileTypeService = fileTypeService;
        this.fileTypeRepository = fileTypeRepository;
        this.fileTypeQueryService = fileTypeQueryService;
    }

    /**
     * {@code POST  /file-types} : Create a new fileType.
     *
     * @param fileType the fileType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileType, or with status {@code 400 (Bad Request)} if the fileType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-types")
    public ResponseEntity<FileType> createFileType(@Valid @RequestBody FileType fileType) throws URISyntaxException {
        log.debug("REST request to save FileType : {}", fileType);
        if (fileType.getId() != null) {
            throw new BadRequestAlertException("A new fileType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileType result = fileTypeService.save(fileType);
        return ResponseEntity
            .created(new URI("/api/file-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-types/:id} : Updates an existing fileType.
     *
     * @param id the id of the fileType to save.
     * @param fileType the fileType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileType,
     * or with status {@code 400 (Bad Request)} if the fileType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-types/{id}")
    public ResponseEntity<FileType> updateFileType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FileType fileType
    ) throws URISyntaxException {
        log.debug("REST request to update FileType : {}, {}", id, fileType);
        if (fileType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FileType result = fileTypeService.save(fileType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fileType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /file-types/:id} : Partial updates given fields of an existing fileType, field will ignore if it is null
     *
     * @param id the id of the fileType to save.
     * @param fileType the fileType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileType,
     * or with status {@code 400 (Bad Request)} if the fileType is not valid,
     * or with status {@code 404 (Not Found)} if the fileType is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/file-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FileType> partialUpdateFileType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FileType fileType
    ) throws URISyntaxException {
        log.debug("REST request to partial update FileType partially : {}, {}", id, fileType);
        if (fileType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileType> result = fileTypeService.partialUpdate(fileType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fileType.getId().toString())
        );
    }

    /**
     * {@code GET  /file-types} : get all the fileTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileTypes in body.
     */
    @GetMapping("/file-types")
    public ResponseEntity<List<FileType>> getAllFileTypes(FileTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FileTypes by criteria: {}", criteria);
        Page<FileType> page = fileTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /file-types/count} : count all the fileTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/file-types/count")
    public ResponseEntity<Long> countFileTypes(FileTypeCriteria criteria) {
        log.debug("REST request to count FileTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(fileTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /file-types/:id} : get the "id" fileType.
     *
     * @param id the id of the fileType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-types/{id}")
    public ResponseEntity<FileType> getFileType(@PathVariable Long id) {
        log.debug("REST request to get FileType : {}", id);
        Optional<FileType> fileType = fileTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileType);
    }

    /**
     * {@code DELETE  /file-types/:id} : delete the "id" fileType.
     *
     * @param id the id of the fileType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-types/{id}")
    public ResponseEntity<Void> deleteFileType(@PathVariable Long id) {
        log.debug("REST request to delete FileType : {}", id);
        fileTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/file-types?query=:query} : search for the fileType corresponding
     * to the query.
     *
     * @param query the query of the fileType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/file-types")
    public ResponseEntity<List<FileType>> searchFileTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FileTypes for query {}", query);
        Page<FileType> page = fileTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
