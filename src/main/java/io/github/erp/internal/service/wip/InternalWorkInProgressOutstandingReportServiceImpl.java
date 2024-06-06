package io.github.erp.internal.service.wip;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.WorkInProgressOutstandingReport;
import io.github.erp.domain.WorkInProgressOutstandingReportREPO;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalWIPOutstandingReportRepository;
import io.github.erp.repository.search.WorkInProgressOutstandingReportSearchRepository;
import io.github.erp.service.dto.WorkInProgressOutstandingReportDTO;
import io.github.erp.service.mapper.WorkInProgressOutstandingReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WorkInProgressOutstandingReport}.
 */
@Service
@Transactional
public class InternalWorkInProgressOutstandingReportServiceImpl implements InternalWorkInProgressOutstandingReportService {

    private final Logger log = LoggerFactory.getLogger(InternalWorkInProgressOutstandingReportServiceImpl.class);

    private final InternalWIPOutstandingReportRepository workInProgressOutstandingReportRepository;

    private final Mapping<WorkInProgressOutstandingReportREPO, WorkInProgressOutstandingReportDTO> workInProgressOutstandingReportDTOMapping;

    private final WorkInProgressOutstandingReportMapper workInProgressOutstandingReportMapper;

    private final WorkInProgressOutstandingReportSearchRepository workInProgressOutstandingReportSearchRepository;

    public InternalWorkInProgressOutstandingReportServiceImpl(
        InternalWIPOutstandingReportRepository workInProgressOutstandingReportRepository,
        Mapping<WorkInProgressOutstandingReportREPO, WorkInProgressOutstandingReportDTO> workInProgressOutstandingReportDTOMapping,
        WorkInProgressOutstandingReportMapper workInProgressOutstandingReportMapper,
        WorkInProgressOutstandingReportSearchRepository workInProgressOutstandingReportSearchRepository
    ) {
        this.workInProgressOutstandingReportRepository = workInProgressOutstandingReportRepository;
        this.workInProgressOutstandingReportDTOMapping = workInProgressOutstandingReportDTOMapping;
        this.workInProgressOutstandingReportMapper = workInProgressOutstandingReportMapper;
        this.workInProgressOutstandingReportSearchRepository = workInProgressOutstandingReportSearchRepository;
    }

    @Override
    public WorkInProgressOutstandingReportDTO save(WorkInProgressOutstandingReportDTO workInProgressOutstandingReportDTO) {
        log.debug("Request to save WorkInProgressOutstandingReport : {}", workInProgressOutstandingReportDTO);
        WorkInProgressOutstandingReport workInProgressOutstandingReport = workInProgressOutstandingReportMapper.toEntity(
            workInProgressOutstandingReportDTO
        );
        workInProgressOutstandingReport = workInProgressOutstandingReportRepository.save(workInProgressOutstandingReport);
        WorkInProgressOutstandingReportDTO result = workInProgressOutstandingReportMapper.toDto(workInProgressOutstandingReport);
        workInProgressOutstandingReportSearchRepository.save(workInProgressOutstandingReport);
        return result;
    }

    @Override
    public Optional<WorkInProgressOutstandingReportDTO> partialUpdate(
        WorkInProgressOutstandingReportDTO workInProgressOutstandingReportDTO
    ) {
        log.debug("Request to partially update WorkInProgressOutstandingReport : {}", workInProgressOutstandingReportDTO);

        return workInProgressOutstandingReportRepository
            .findById(workInProgressOutstandingReportDTO.getId())
            .map(existingWorkInProgressOutstandingReport -> {
                workInProgressOutstandingReportMapper.partialUpdate(
                    existingWorkInProgressOutstandingReport,
                    workInProgressOutstandingReportDTO
                );

                return existingWorkInProgressOutstandingReport;
            })
            .map(workInProgressOutstandingReportRepository::save)
            .map(savedWorkInProgressOutstandingReport -> {
                workInProgressOutstandingReportSearchRepository.save(savedWorkInProgressOutstandingReport);

                return savedWorkInProgressOutstandingReport;
            })
            .map(workInProgressOutstandingReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressOutstandingReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkInProgressOutstandingReports");
        return workInProgressOutstandingReportRepository.findAll(pageable).map(workInProgressOutstandingReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkInProgressOutstandingReportDTO> findOne(Long id) {
        log.debug("Request to get WorkInProgressOutstandingReport : {}", id);
        return workInProgressOutstandingReportRepository.findById(id).map(workInProgressOutstandingReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkInProgressOutstandingReport : {}", id);
        workInProgressOutstandingReportRepository.deleteById(id);
        workInProgressOutstandingReportSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressOutstandingReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkInProgressOutstandingReports for query {}", query);
        return workInProgressOutstandingReportSearchRepository.search(query, pageable).map(workInProgressOutstandingReportMapper::toDto);
    }

    /**
     * Generates a listing containing the list of items with the outstanding information on all
     * wip items
     *
     * @param reportDate Date of the report
     * @param pageable   the pagination information
     * @return list of reporting items
     */
    @Override
    public Page<WorkInProgressOutstandingReportDTO> findReportItemsByReportDate(LocalDate reportDate, Pageable pageable) {

        return workInProgressOutstandingReportRepository.findByReportDate(reportDate, pageable)
            .map(workInProgressOutstandingReportDTOMapping::toValue2);
    }

    /**
     * Generates a listing containing the list of items with the outstanding information on all
     * wip items
     *
     * @param reportDate Date of the report
     * @return list of reporting items
     */
    @Override
    public Optional<List<WorkInProgressOutstandingReportDTO>> findAllReportItemsByReportDate(LocalDate reportDate) {


        return Optional.of(workInProgressOutstandingReportRepository.findByReportDate(reportDate, Pageable.ofSize(Integer.MAX_VALUE))
            .getContent()
            .stream()
            .map(workInProgressOutstandingReportDTOMapping::toValue2)
            .collect(Collectors.toList()));
    }

    /**
     * Find the report value of an individual work-in-progress item
     *
     * @param reportDate Date of the report balance
     * @param id         id of the work-in-progress item registration information
     * @return Work-in-progress outstanding balance information
     */
    @Override
    public Optional<WorkInProgressOutstandingReportDTO> findByReportDate(LocalDate reportDate, long id) {

        return workInProgressOutstandingReportRepository.findByReportDate(reportDate, id)
            .map(workInProgressOutstandingReportDTOMapping::toValue2);
    }
}
