package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.6
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

import io.github.erp.domain.BusinessDocument;
import io.github.erp.repository.BusinessDocumentRepository;
import io.github.erp.repository.search.BusinessDocumentSearchRepository;
import io.github.erp.service.BusinessDocumentService;
import io.github.erp.service.dto.BusinessDocumentDTO;
import io.github.erp.service.mapper.BusinessDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BusinessDocument}.
 */
@Service
@Transactional
public class BusinessDocumentServiceImpl implements BusinessDocumentService {

    private final Logger log = LoggerFactory.getLogger(BusinessDocumentServiceImpl.class);

    private final BusinessDocumentRepository businessDocumentRepository;

    private final BusinessDocumentMapper businessDocumentMapper;

    private final BusinessDocumentSearchRepository businessDocumentSearchRepository;

    public BusinessDocumentServiceImpl(
        BusinessDocumentRepository businessDocumentRepository,
        BusinessDocumentMapper businessDocumentMapper,
        BusinessDocumentSearchRepository businessDocumentSearchRepository
    ) {
        this.businessDocumentRepository = businessDocumentRepository;
        this.businessDocumentMapper = businessDocumentMapper;
        this.businessDocumentSearchRepository = businessDocumentSearchRepository;
    }

    @Override
    public BusinessDocumentDTO save(BusinessDocumentDTO businessDocumentDTO) {
        log.debug("Request to save BusinessDocument : {}", businessDocumentDTO);
        BusinessDocument businessDocument = businessDocumentMapper.toEntity(businessDocumentDTO);
        businessDocument = businessDocumentRepository.save(businessDocument);
        BusinessDocumentDTO result = businessDocumentMapper.toDto(businessDocument);
        businessDocumentSearchRepository.save(businessDocument);
        return result;
    }

    @Override
    public Optional<BusinessDocumentDTO> partialUpdate(BusinessDocumentDTO businessDocumentDTO) {
        log.debug("Request to partially update BusinessDocument : {}", businessDocumentDTO);

        return businessDocumentRepository
            .findById(businessDocumentDTO.getId())
            .map(existingBusinessDocument -> {
                businessDocumentMapper.partialUpdate(existingBusinessDocument, businessDocumentDTO);

                return existingBusinessDocument;
            })
            .map(businessDocumentRepository::save)
            .map(savedBusinessDocument -> {
                businessDocumentSearchRepository.save(savedBusinessDocument);

                return savedBusinessDocument;
            })
            .map(businessDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessDocuments");
        return businessDocumentRepository.findAll(pageable).map(businessDocumentMapper::toDto);
    }

    public Page<BusinessDocumentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return businessDocumentRepository.findAllWithEagerRelationships(pageable).map(businessDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusinessDocumentDTO> findOne(Long id) {
        log.debug("Request to get BusinessDocument : {}", id);
        return businessDocumentRepository.findOneWithEagerRelationships(id).map(businessDocumentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BusinessDocument : {}", id);
        businessDocumentRepository.deleteById(id);
        businessDocumentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessDocumentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BusinessDocuments for query {}", query);
        return businessDocumentSearchRepository.search(query, pageable).map(businessDocumentMapper::toDto);
    }
}
