package io.github.erp.service;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import io.github.erp.domain.InterbankSectorCode;
import io.github.erp.repository.InterbankSectorCodeRepository;
import io.github.erp.repository.search.InterbankSectorCodeSearchRepository;
import io.github.erp.service.criteria.InterbankSectorCodeCriteria;
import io.github.erp.service.dto.InterbankSectorCodeDTO;
import io.github.erp.service.mapper.InterbankSectorCodeMapper;
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
 * Service for executing complex queries for {@link InterbankSectorCode} entities in the database.
 * The main input is a {@link InterbankSectorCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InterbankSectorCodeDTO} or a {@link Page} of {@link InterbankSectorCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InterbankSectorCodeQueryService extends QueryService<InterbankSectorCode> {

    private final Logger log = LoggerFactory.getLogger(InterbankSectorCodeQueryService.class);

    private final InterbankSectorCodeRepository interbankSectorCodeRepository;

    private final InterbankSectorCodeMapper interbankSectorCodeMapper;

    private final InterbankSectorCodeSearchRepository interbankSectorCodeSearchRepository;

    public InterbankSectorCodeQueryService(
        InterbankSectorCodeRepository interbankSectorCodeRepository,
        InterbankSectorCodeMapper interbankSectorCodeMapper,
        InterbankSectorCodeSearchRepository interbankSectorCodeSearchRepository
    ) {
        this.interbankSectorCodeRepository = interbankSectorCodeRepository;
        this.interbankSectorCodeMapper = interbankSectorCodeMapper;
        this.interbankSectorCodeSearchRepository = interbankSectorCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InterbankSectorCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InterbankSectorCodeDTO> findByCriteria(InterbankSectorCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InterbankSectorCode> specification = createSpecification(criteria);
        return interbankSectorCodeMapper.toDto(interbankSectorCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InterbankSectorCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InterbankSectorCodeDTO> findByCriteria(InterbankSectorCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InterbankSectorCode> specification = createSpecification(criteria);
        return interbankSectorCodeRepository.findAll(specification, page).map(interbankSectorCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InterbankSectorCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InterbankSectorCode> specification = createSpecification(criteria);
        return interbankSectorCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link InterbankSectorCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InterbankSectorCode> createSpecification(InterbankSectorCodeCriteria criteria) {
        Specification<InterbankSectorCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InterbankSectorCode_.id));
            }
            if (criteria.getInterbankSectorCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getInterbankSectorCode(), InterbankSectorCode_.interbankSectorCode)
                    );
            }
        }
        return specification;
    }
}
