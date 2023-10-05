package io.github.erp.service.impl;

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
            .map(existingFileUpload -> {
                fileUploadMapper.partialUpdate(existingFileUpload, fileUploadDTO);

                return existingFileUpload;
            })
            .map(fileUploadRepository::save)
            .map(savedFileUpload -> {
                fileUploadSearchRepository.save(savedFileUpload);

                return savedFileUpload;
            })
            .map(fileUploadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileUploadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FileUploads");
        return fileUploadRepository.findAll(pageable).map(fileUploadMapper::toDto);
    }

    public Page<FileUploadDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fileUploadRepository.findAllWithEagerRelationships(pageable).map(fileUploadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileUploadDTO> findOne(Long id) {
        log.debug("Request to get FileUpload : {}", id);
        return fileUploadRepository.findOneWithEagerRelationships(id).map(fileUploadMapper::toDto);
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
        return fileUploadSearchRepository.search(query, pageable).map(fileUploadMapper::toDto);
    }
}
