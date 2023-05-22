package io.github.erp.service;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.2
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

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.FileType;
import io.github.erp.repository.FileTypeRepository;
import io.github.erp.repository.search.FileTypeSearchRepository;
import io.github.erp.service.criteria.FileTypeCriteria;
import io.github.erp.service.dto.FileTypeDTO;
import io.github.erp.service.mapper.FileTypeMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FileType} entities in the database.
 * The main input is a {@link FileTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FileTypeDTO} or a {@link Page} of {@link FileTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FileTypeQueryService extends QueryService<FileType> {

    private final Logger log = LoggerFactory.getLogger(FileTypeQueryService.class);

    private final FileTypeRepository fileTypeRepository;

    private final FileTypeMapper fileTypeMapper;

    private final FileTypeSearchRepository fileTypeSearchRepository;

    public FileTypeQueryService(
        FileTypeRepository fileTypeRepository,
        FileTypeMapper fileTypeMapper,
        FileTypeSearchRepository fileTypeSearchRepository
    ) {
        this.fileTypeRepository = fileTypeRepository;
        this.fileTypeMapper = fileTypeMapper;
        this.fileTypeSearchRepository = fileTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FileTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FileTypeDTO> findByCriteria(FileTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FileType> specification = createSpecification(criteria);
        return fileTypeMapper.toDto(fileTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FileTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FileTypeDTO> findByCriteria(FileTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FileType> specification = createSpecification(criteria);
        return fileTypeRepository.findAll(specification, page).map(fileTypeMapper::toDto);
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
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
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
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(FileType_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
