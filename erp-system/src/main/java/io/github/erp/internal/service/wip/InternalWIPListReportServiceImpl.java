package io.github.erp.internal.service.wip;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.WIPListReport;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.repository.WIPListReportRepository;
import io.github.erp.repository.search.WIPListReportSearchRepository;
import io.github.erp.service.WIPListReportQueryService;
import io.github.erp.service.WIPListReportService;
import io.github.erp.service.criteria.WIPListReportCriteria;
import io.github.erp.service.dto.WIPListReportDTO;
import io.github.erp.service.mapper.WIPListReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link WIPListReport}.
 */
@Service("internalWIPListReportService")
@Transactional
public class InternalWIPListReportServiceImpl
    extends WIPListReportQueryService implements InternalWIPListReportService {

    private final Logger log = LoggerFactory.getLogger(InternalWIPListReportServiceImpl.class);

    private final WIPListReportRepository wIPListReportRepository;

    private final WIPListReportMapper wIPListReportMapper;

    private final WIPListReportSearchRepository wIPListReportSearchRepository;

    private final InternalApplicationUserDetailService internalApplicationUserDetailService;

    public InternalWIPListReportServiceImpl(
        WIPListReportRepository wIPListReportRepository,
        WIPListReportMapper wIPListReportMapper,
        WIPListReportSearchRepository wIPListReportSearchRepository, InternalApplicationUserDetailService internalApplicationUserDetailService
    ) {
        super(wIPListReportRepository, wIPListReportMapper, wIPListReportSearchRepository);
        this.wIPListReportRepository = wIPListReportRepository;
        this.wIPListReportMapper = wIPListReportMapper;
        this.wIPListReportSearchRepository = wIPListReportSearchRepository;
        this.internalApplicationUserDetailService = internalApplicationUserDetailService;
    }

    @Override
    public WIPListReportDTO save(WIPListReportDTO wIPListReportDTO) {
        log.debug("Request to save WIPListReport : {}", wIPListReportDTO);

        internalApplicationUserDetailService.getCurrentApplicationUser().ifPresent(wIPListReportDTO::setRequestedBy);

        WIPListReport wIPListReport = wIPListReportMapper.toEntity(wIPListReportDTO);
        wIPListReport = wIPListReportRepository.save(wIPListReport);
        WIPListReportDTO result = wIPListReportMapper.toDto(wIPListReport);
        wIPListReportSearchRepository.save(wIPListReport);
        return result;
    }

    @Override
    public Optional<WIPListReportDTO> partialUpdate(WIPListReportDTO wIPListReportDTO) {
        log.debug("Request to partially update WIPListReport : {}", wIPListReportDTO);

        return wIPListReportRepository
            .findById(wIPListReportDTO.getId())
            .map(existingWIPListReport -> {
                wIPListReportMapper.partialUpdate(existingWIPListReport, wIPListReportDTO);

                return existingWIPListReport;
            })
            .map(wIPListReportRepository::save)
            .map(savedWIPListReport -> {
                wIPListReportSearchRepository.save(savedWIPListReport);

                return savedWIPListReport;
            })
            .map(wIPListReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPListReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WIPListReports");
        return wIPListReportRepository.findAll(pageable).map(wIPListReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WIPListReportDTO> findOne(Long id) {
        log.debug("Request to get WIPListReport : {}", id);
        return wIPListReportRepository.findById(id).map(wIPListReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WIPListReport : {}", id);
        wIPListReportRepository.deleteById(id);
        wIPListReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WIPListReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WIPListReports for query {}", query);
        return wIPListReportSearchRepository.search(query, pageable).map(wIPListReportMapper::toDto);
    }

    /**
     * Return a {@link List} of {@link WIPListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WIPListReportDTO> findByCriteria(WIPListReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WIPListReport> specification = createSpecification(criteria);
        return wIPListReportMapper.toDto(wIPListReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WIPListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WIPListReportDTO> findByCriteria(WIPListReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WIPListReport> specification = createSpecification(criteria);
        return wIPListReportRepository.findAll(specification, page).map(wIPListReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WIPListReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WIPListReport> specification = createSpecification(criteria);
        return wIPListReportRepository.count(specification);
    }
}
