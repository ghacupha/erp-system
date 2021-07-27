package io.github.erp.service;

import io.github.erp.service.dto.FileUploadDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.FileUpload}.
 */
public interface FileUploadService {

    /**
     * Save a fileUpload.
     *
     * @param fileUploadDTO the entity to save.
     * @return the persisted entity.
     */
    FileUploadDTO save(FileUploadDTO fileUploadDTO);

    /**
     * Get all the fileUploads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FileUploadDTO> findAll(Pageable pageable);


    /**
     * Get the "id" fileUpload.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileUploadDTO> findOne(Long id);

    /**
     * Delete the "id" fileUpload.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the fileUpload corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FileUploadDTO> search(String query, Pageable pageable);
}
