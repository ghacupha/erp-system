package io.github.erp.service;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.FileUpload;
import io.github.erp.repository.FileUploadRepository;
import io.github.erp.repository.search.FileUploadSearchRepository;
import io.github.erp.service.criteria.FileUploadCriteria;
import io.github.erp.service.dto.FileUploadDTO;
import io.github.erp.service.mapper.FileUploadMapper;
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
 * Service for executing complex queries for {@link FileUpload} entities in the database.
 * The main input is a {@link FileUploadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FileUploadDTO} or a {@link Page} of {@link FileUploadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FileUploadQueryService extends QueryService<FileUpload> {

    private final Logger log = LoggerFactory.getLogger(FileUploadQueryService.class);

    private final FileUploadRepository fileUploadRepository;

    private final FileUploadMapper fileUploadMapper;

    private final FileUploadSearchRepository fileUploadSearchRepository;

    public FileUploadQueryService(
        FileUploadRepository fileUploadRepository,
        FileUploadMapper fileUploadMapper,
        FileUploadSearchRepository fileUploadSearchRepository
    ) {
        this.fileUploadRepository = fileUploadRepository;
        this.fileUploadMapper = fileUploadMapper;
        this.fileUploadSearchRepository = fileUploadSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FileUploadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FileUploadDTO> findByCriteria(FileUploadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FileUpload> specification = createSpecification(criteria);
        return fileUploadMapper.toDto(fileUploadRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FileUploadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FileUploadDTO> findByCriteria(FileUploadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FileUpload> specification = createSpecification(criteria);
        return fileUploadRepository.findAll(specification, page).map(fileUploadMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FileUploadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FileUpload> specification = createSpecification(criteria);
        return fileUploadRepository.count(specification);
    }

    /**
     * Function to convert {@link FileUploadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FileUpload> createSpecification(FileUploadCriteria criteria) {
        Specification<FileUpload> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FileUpload_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FileUpload_.description));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), FileUpload_.fileName));
            }
            if (criteria.getPeriodFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodFrom(), FileUpload_.periodFrom));
            }
            if (criteria.getPeriodTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodTo(), FileUpload_.periodTo));
            }
            if (criteria.getFileTypeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileTypeId(), FileUpload_.fileTypeId));
            }
            if (criteria.getUploadSuccessful() != null) {
                specification = specification.and(buildSpecification(criteria.getUploadSuccessful(), FileUpload_.uploadSuccessful));
            }
            if (criteria.getUploadProcessed() != null) {
                specification = specification.and(buildSpecification(criteria.getUploadProcessed(), FileUpload_.uploadProcessed));
            }
            if (criteria.getUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUploadToken(), FileUpload_.uploadToken));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(FileUpload_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
