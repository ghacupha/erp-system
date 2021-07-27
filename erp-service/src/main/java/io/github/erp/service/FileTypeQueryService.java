package io.github.erp.service;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.erp.domain.FileType;
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.repository.FileTypeRepository;
import io.github.erp.repository.search.FileTypeSearchRepository;
import io.github.erp.service.dto.FileTypeCriteria;

/**
 * Service for executing complex queries for {@link FileType} entities in the database.
 * The main input is a {@link FileTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FileType} or a {@link Page} of {@link FileType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FileTypeQueryService extends QueryService<FileType> {

    private final Logger log = LoggerFactory.getLogger(FileTypeQueryService.class);

    private final FileTypeRepository fileTypeRepository;

    private final FileTypeSearchRepository fileTypeSearchRepository;

    public FileTypeQueryService(FileTypeRepository fileTypeRepository, FileTypeSearchRepository fileTypeSearchRepository) {
        this.fileTypeRepository = fileTypeRepository;
        this.fileTypeSearchRepository = fileTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FileType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FileType> findByCriteria(FileTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FileType> specification = createSpecification(criteria);
        return fileTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FileType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FileType> findByCriteria(FileTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FileType> specification = createSpecification(criteria);
        return fileTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FileTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FileType> specification = createSpecification(criteria);
        return fileTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FileTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FileType> createSpecification(FileTypeCriteria criteria) {
        Specification<FileType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FileType_.id));
            }
            if (criteria.getFileTypeName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileTypeName(), FileType_.fileTypeName));
            }
            if (criteria.getFileMediumType() != null) {
                specification = specification.and(buildSpecification(criteria.getFileMediumType(), FileType_.fileMediumType));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FileType_.description));
            }
            if (criteria.getFileType() != null) {
                specification = specification.and(buildSpecification(criteria.getFileType(), FileType_.fileType));
            }
        }
        return specification;
    }
}
