package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.FileUpload;
import io.github.erp.repository.FileUploadRepository;
import io.github.erp.repository.search.FileUploadSearchRepository;
import io.github.erp.service.FileUploadService;
import io.github.erp.service.dto.FileUploadDTO;
import io.github.erp.service.mapper.FileUploadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FileUpload}.
 */
@Service
@Transactional
public class FileUploadServiceImpl implements FileUploadService {

    private final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final FileUploadRepository fileUploadRepository;

    private final FileUploadMapper fileUploadMapper;

    private final FileUploadSearchRepository fileUploadSearchRepository;

    public FileUploadServiceImpl(
        FileUploadRepository fileUploadRepository,
        FileUploadMapper fileUploadMapper,
        FileUploadSearchRepository fileUploadSearchRepository
    ) {
        this.fileUploadRepository = fileUploadRepository;
        this.fileUploadMapper = fileUploadMapper;
        this.fileUploadSearchRepository = fileUploadSearchRepository;
    }

    @Override
    public FileUploadDTO save(FileUploadDTO fileUploadDTO) {
        log.debug("Request to save FileUpload : {}", fileUploadDTO);
        FileUpload fileUpload = fileUploadMapper.toEntity(fileUploadDTO);
        fileUpload = fileUploadRepository.save(fileUpload);
        FileUploadDTO result = fileUploadMapper.toDto(fileUpload);
        fileUploadSearchRepository.save(fileUpload);
        return result;
    }

    @Override
    public Optional<FileUploadDTO> partialUpdate(FileUploadDTO fileUploadDTO) {
        log.debug("Request to partially update FileUpload : {}", fileUploadDTO);

        return fileUploadRepository
            .findById(fileUploadDTO.getId())
            .map(
                existingFileUpload -> {
                    fileUploadMapper.partialUpdate(existingFileUpload, fileUploadDTO);

                    return existingFileUpload;
                }
            )
            .map(fileUploadRepository::save)
            .map(
                savedFileUpload -> {
                    fileUploadSearchRepository.save(savedFileUpload);

                    return savedFileUpload;
                }
            )
            .map(fileUploadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileUploadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FileUploads");
        return fileUploadRepository.findAll(pageable).map(fileUploadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileUploadDTO> findOne(Long id) {
        log.debug("Request to get FileUpload : {}", id);
        return fileUploadRepository.findById(id).map(fileUploadMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileUpload : {}", id);
        fileUploadRepository.deleteById(id);
        fileUploadSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileUploadDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FileUploads for query {}", query);
        return fileUploadSearchRepository.search(queryStringQuery(query), pageable).map(fileUploadMapper::toDto);
    }
}
