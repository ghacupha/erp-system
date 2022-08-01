package io.github.erp.service;

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.SystemModule;
import io.github.erp.repository.SystemModuleRepository;
import io.github.erp.repository.search.SystemModuleSearchRepository;
import io.github.erp.service.criteria.SystemModuleCriteria;
import io.github.erp.service.dto.SystemModuleDTO;
import io.github.erp.service.mapper.SystemModuleMapper;
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
 * Service for executing complex queries for {@link SystemModule} entities in the database.
 * The main input is a {@link SystemModuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SystemModuleDTO} or a {@link Page} of {@link SystemModuleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemModuleQueryService extends QueryService<SystemModule> {

    private final Logger log = LoggerFactory.getLogger(SystemModuleQueryService.class);

    private final SystemModuleRepository systemModuleRepository;

    private final SystemModuleMapper systemModuleMapper;

    private final SystemModuleSearchRepository systemModuleSearchRepository;

    public SystemModuleQueryService(
        SystemModuleRepository systemModuleRepository,
        SystemModuleMapper systemModuleMapper,
        SystemModuleSearchRepository systemModuleSearchRepository
    ) {
        this.systemModuleRepository = systemModuleRepository;
        this.systemModuleMapper = systemModuleMapper;
        this.systemModuleSearchRepository = systemModuleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SystemModuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SystemModuleDTO> findByCriteria(SystemModuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SystemModule> specification = createSpecification(criteria);
        return systemModuleMapper.toDto(systemModuleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SystemModuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemModuleDTO> findByCriteria(SystemModuleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SystemModule> specification = createSpecification(criteria);
        return systemModuleRepository.findAll(specification, page).map(systemModuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemModuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SystemModule> specification = createSpecification(criteria);
        return systemModuleRepository.count(specification);
    }

    /**
     * Function to convert {@link SystemModuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SystemModule> createSpecification(SystemModuleCriteria criteria) {
        Specification<SystemModule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SystemModule_.id));
            }
            if (criteria.getModuleName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModuleName(), SystemModule_.moduleName));
            }
        }
        return specification;
    }
}
