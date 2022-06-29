package io.github.erp.service;

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.ReportStatus;
import io.github.erp.repository.ReportStatusRepository;
import io.github.erp.repository.search.ReportStatusSearchRepository;
import io.github.erp.service.criteria.ReportStatusCriteria;
import io.github.erp.service.dto.ReportStatusDTO;
import io.github.erp.service.mapper.ReportStatusMapper;
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
 * Service for executing complex queries for {@link ReportStatus} entities in the database.
 * The main input is a {@link ReportStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReportStatusDTO} or a {@link Page} of {@link ReportStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportStatusQueryService extends QueryService<ReportStatus> {

    private final Logger log = LoggerFactory.getLogger(ReportStatusQueryService.class);

    private final ReportStatusRepository reportStatusRepository;

    private final ReportStatusMapper reportStatusMapper;

    private final ReportStatusSearchRepository reportStatusSearchRepository;

    public ReportStatusQueryService(
        ReportStatusRepository reportStatusRepository,
        ReportStatusMapper reportStatusMapper,
        ReportStatusSearchRepository reportStatusSearchRepository
    ) {
        this.reportStatusRepository = reportStatusRepository;
        this.reportStatusMapper = reportStatusMapper;
        this.reportStatusSearchRepository = reportStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReportStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReportStatusDTO> findByCriteria(ReportStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReportStatus> specification = createSpecification(criteria);
        return reportStatusMapper.toDto(reportStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReportStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportStatusDTO> findByCriteria(ReportStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportStatus> specification = createSpecification(criteria);
        return reportStatusRepository.findAll(specification, page).map(reportStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ReportStatus> specification = createSpecification(criteria);
        return reportStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportStatus> createSpecification(ReportStatusCriteria criteria) {
        Specification<ReportStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReportStatus_.id));
            }
            if (criteria.getReportName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportName(), ReportStatus_.reportName));
            }
            if (criteria.getReportId() != null) {
                specification = specification.and(buildSpecification(criteria.getReportId(), ReportStatus_.reportId));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(ReportStatus_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
