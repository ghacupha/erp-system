package io.github.erp.service;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.NatureOfCustomerComplaints;
import io.github.erp.repository.NatureOfCustomerComplaintsRepository;
import io.github.erp.repository.search.NatureOfCustomerComplaintsSearchRepository;
import io.github.erp.service.criteria.NatureOfCustomerComplaintsCriteria;
import io.github.erp.service.dto.NatureOfCustomerComplaintsDTO;
import io.github.erp.service.mapper.NatureOfCustomerComplaintsMapper;
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
 * Service for executing complex queries for {@link NatureOfCustomerComplaints} entities in the database.
 * The main input is a {@link NatureOfCustomerComplaintsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NatureOfCustomerComplaintsDTO} or a {@link Page} of {@link NatureOfCustomerComplaintsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NatureOfCustomerComplaintsQueryService extends QueryService<NatureOfCustomerComplaints> {

    private final Logger log = LoggerFactory.getLogger(NatureOfCustomerComplaintsQueryService.class);

    private final NatureOfCustomerComplaintsRepository natureOfCustomerComplaintsRepository;

    private final NatureOfCustomerComplaintsMapper natureOfCustomerComplaintsMapper;

    private final NatureOfCustomerComplaintsSearchRepository natureOfCustomerComplaintsSearchRepository;

    public NatureOfCustomerComplaintsQueryService(
        NatureOfCustomerComplaintsRepository natureOfCustomerComplaintsRepository,
        NatureOfCustomerComplaintsMapper natureOfCustomerComplaintsMapper,
        NatureOfCustomerComplaintsSearchRepository natureOfCustomerComplaintsSearchRepository
    ) {
        this.natureOfCustomerComplaintsRepository = natureOfCustomerComplaintsRepository;
        this.natureOfCustomerComplaintsMapper = natureOfCustomerComplaintsMapper;
        this.natureOfCustomerComplaintsSearchRepository = natureOfCustomerComplaintsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link NatureOfCustomerComplaintsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NatureOfCustomerComplaintsDTO> findByCriteria(NatureOfCustomerComplaintsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NatureOfCustomerComplaints> specification = createSpecification(criteria);
        return natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaintsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NatureOfCustomerComplaintsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NatureOfCustomerComplaintsDTO> findByCriteria(NatureOfCustomerComplaintsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NatureOfCustomerComplaints> specification = createSpecification(criteria);
        return natureOfCustomerComplaintsRepository.findAll(specification, page).map(natureOfCustomerComplaintsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NatureOfCustomerComplaintsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NatureOfCustomerComplaints> specification = createSpecification(criteria);
        return natureOfCustomerComplaintsRepository.count(specification);
    }

    /**
     * Function to convert {@link NatureOfCustomerComplaintsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NatureOfCustomerComplaints> createSpecification(NatureOfCustomerComplaintsCriteria criteria) {
        Specification<NatureOfCustomerComplaints> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NatureOfCustomerComplaints_.id));
            }
            if (criteria.getNatureOfComplaintTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getNatureOfComplaintTypeCode(),
                            NatureOfCustomerComplaints_.natureOfComplaintTypeCode
                        )
                    );
            }
            if (criteria.getNatureOfComplaintType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getNatureOfComplaintType(), NatureOfCustomerComplaints_.natureOfComplaintType)
                    );
            }
        }
        return specification;
    }
}
