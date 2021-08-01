package io.github.erp.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.FileType;
import io.github.erp.repository.FileTypeRepository;
import io.github.erp.repository.search.FileTypeSearchRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FileType}.
 */
@Service
@Transactional
public class FileTypeService {

    private final Logger log = LoggerFactory.getLogger(FileTypeService.class);

    private final FileTypeRepository fileTypeRepository;

    private final FileTypeSearchRepository fileTypeSearchRepository;

    public FileTypeService(FileTypeRepository fileTypeRepository, FileTypeSearchRepository fileTypeSearchRepository) {
        this.fileTypeRepository = fileTypeRepository;
        this.fileTypeSearchRepository = fileTypeSearchRepository;
    }

    /**
     * Save a fileType.
     *
     * @param fileType the entity to save.
     * @return the persisted entity.
     */
    public FileType save(FileType fileType) {
        log.debug("Request to save FileType : {}", fileType);
        FileType result = fileTypeRepository.save(fileType);
        fileTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Partially update a fileType.
     *
     * @param fileType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FileType> partialUpdate(FileType fileType) {
        log.debug("Request to partially update FileType : {}", fileType);

        return fileTypeRepository
            .findById(fileType.getId())
            .map(
                existingFileType -> {
                    if (fileType.getFileTypeName() != null) {
                        existingFileType.setFileTypeName(fileType.getFileTypeName());
                    }
                    if (fileType.getFileMediumType() != null) {
                        existingFileType.setFileMediumType(fileType.getFileMediumType());
                    }
                    if (fileType.getDescription() != null) {
                        existingFileType.setDescription(fileType.getDescription());
                    }
                    if (fileType.getFileTemplate() != null) {
                        existingFileType.setFileTemplate(fileType.getFileTemplate());
                    }
                    if (fileType.getFileTemplateContentType() != null) {
                        existingFileType.setFileTemplateContentType(fileType.getFileTemplateContentType());
                    }
                    if (fileType.getFileType() != null) {
                        existingFileType.setFileType(fileType.getFileType());
                    }

                    return existingFileType;
                }
            )
            .map(fileTypeRepository::save)
            .map(
                savedFileType -> {
                    fileTypeSearchRepository.save(savedFileType);

                    return savedFileType;
                }
            );
    }

    /**
     * Get all the fileTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FileType> findAll(Pageable pageable) {
        log.debug("Request to get all FileTypes");
        return fileTypeRepository.findAll(pageable);
    }

    /**
     * Get one fileType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FileType> findOne(Long id) {
        log.debug("Request to get FileType : {}", id);
        return fileTypeRepository.findById(id);
    }

    /**
     * Delete the fileType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FileType : {}", id);
        fileTypeRepository.deleteById(id);
        fileTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the fileType corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FileType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FileTypes for query {}", query);
        return fileTypeSearchRepository.search(queryStringQuery(query), pageable);
    }
}
