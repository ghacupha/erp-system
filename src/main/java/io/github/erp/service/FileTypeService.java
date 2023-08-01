package io.github.erp.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.FileType;
import io.github.erp.repository.FileTypeRepository;
import io.github.erp.repository.search.FileTypeSearchRepository;
import io.github.erp.service.dto.FileTypeDTO;
import io.github.erp.service.mapper.FileTypeMapper;
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

    private final FileTypeMapper fileTypeMapper;

    private final FileTypeSearchRepository fileTypeSearchRepository;

    public FileTypeService(
        FileTypeRepository fileTypeRepository,
        FileTypeMapper fileTypeMapper,
        FileTypeSearchRepository fileTypeSearchRepository
    ) {
        this.fileTypeRepository = fileTypeRepository;
        this.fileTypeMapper = fileTypeMapper;
        this.fileTypeSearchRepository = fileTypeSearchRepository;
    }

    /**
     * Save a fileType.
     *
     * @param fileTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public FileTypeDTO save(FileTypeDTO fileTypeDTO) {
        log.debug("Request to save FileType : {}", fileTypeDTO);
        FileType fileType = fileTypeMapper.toEntity(fileTypeDTO);
        fileType = fileTypeRepository.save(fileType);
        FileTypeDTO result = fileTypeMapper.toDto(fileType);
        fileTypeSearchRepository.save(fileType);
        return result;
    }

    /**
     * Partially update a fileType.
     *
     * @param fileTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FileTypeDTO> partialUpdate(FileTypeDTO fileTypeDTO) {
        log.debug("Request to partially update FileType : {}", fileTypeDTO);

        return fileTypeRepository
            .findById(fileTypeDTO.getId())
            .map(existingFileType -> {
                fileTypeMapper.partialUpdate(existingFileType, fileTypeDTO);

                return existingFileType;
            })
            .map(fileTypeRepository::save)
            .map(savedFileType -> {
                fileTypeSearchRepository.save(savedFileType);

                return savedFileType;
            })
            .map(fileTypeMapper::toDto);
    }

    /**
     * Get all the fileTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FileTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FileTypes");
        return fileTypeRepository.findAll(pageable).map(fileTypeMapper::toDto);
    }

    /**
     * Get all the fileTypes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FileTypeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fileTypeRepository.findAllWithEagerRelationships(pageable).map(fileTypeMapper::toDto);
    }

    /**
     * Get one fileType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FileTypeDTO> findOne(Long id) {
        log.debug("Request to get FileType : {}", id);
        return fileTypeRepository.findOneWithEagerRelationships(id).map(fileTypeMapper::toDto);
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
    public Page<FileTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FileTypes for query {}", query);
        return fileTypeSearchRepository.search(query, pageable).map(fileTypeMapper::toDto);
    }
}
