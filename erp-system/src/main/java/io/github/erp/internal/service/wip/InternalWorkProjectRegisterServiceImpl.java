package io.github.erp.internal.service.wip;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.WorkProjectRegister;
import io.github.erp.internal.repository.InternalWorkProjectRegisterRepository;
import io.github.erp.internal.utilities.NextIntegerFiller;
import io.github.erp.repository.search.WorkProjectRegisterSearchRepository;
import io.github.erp.service.dto.WorkProjectRegisterDTO;
import io.github.erp.service.mapper.WorkProjectRegisterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link WorkProjectRegister}.
 */
@Service
@Transactional
public class InternalWorkProjectRegisterServiceImpl implements InternalWorkProjectRegisterService {

    private final Logger log = LoggerFactory.getLogger(InternalWorkProjectRegisterServiceImpl.class);

    private final InternalWorkProjectRegisterRepository workProjectRegisterRepository;

    private final WorkProjectRegisterMapper workProjectRegisterMapper;

    private final WorkProjectRegisterSearchRepository workProjectRegisterSearchRepository;

    public InternalWorkProjectRegisterServiceImpl(
        InternalWorkProjectRegisterRepository workProjectRegisterRepository,
        WorkProjectRegisterMapper workProjectRegisterMapper,
        WorkProjectRegisterSearchRepository workProjectRegisterSearchRepository
    ) {
        this.workProjectRegisterRepository = workProjectRegisterRepository;
        this.workProjectRegisterMapper = workProjectRegisterMapper;
        this.workProjectRegisterSearchRepository = workProjectRegisterSearchRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Long calculateNextCatalogueNumber() {
        log.debug("Request to get next catalogue number");
        return NextIntegerFiller.fillNext(workProjectRegisterRepository.findAllIds());
    }

    @Override
    public WorkProjectRegisterDTO save(WorkProjectRegisterDTO workProjectRegisterDTO) {
        log.debug("Request to save WorkProjectRegister : {}", workProjectRegisterDTO);
        WorkProjectRegister workProjectRegister = workProjectRegisterMapper.toEntity(workProjectRegisterDTO);
        workProjectRegister = workProjectRegisterRepository.save(workProjectRegister);
        WorkProjectRegisterDTO result = workProjectRegisterMapper.toDto(workProjectRegister);
        workProjectRegisterSearchRepository.save(workProjectRegister);
        return result;
    }

    @Override
    public Optional<WorkProjectRegisterDTO> partialUpdate(WorkProjectRegisterDTO workProjectRegisterDTO) {
        log.debug("Request to partially update WorkProjectRegister : {}", workProjectRegisterDTO);

        return workProjectRegisterRepository
            .findById(workProjectRegisterDTO.getId())
            .map(existingWorkProjectRegister -> {
                workProjectRegisterMapper.partialUpdate(existingWorkProjectRegister, workProjectRegisterDTO);

                return existingWorkProjectRegister;
            })
            .map(workProjectRegisterRepository::save)
            .map(savedWorkProjectRegister -> {
                workProjectRegisterSearchRepository.save(savedWorkProjectRegister);

                return savedWorkProjectRegister;
            })
            .map(workProjectRegisterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkProjectRegisterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkProjectRegisters");
        return workProjectRegisterRepository.findAll(pageable).map(workProjectRegisterMapper::toDto);
    }

    public Page<WorkProjectRegisterDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workProjectRegisterRepository.findAllWithEagerRelationships(pageable).map(workProjectRegisterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkProjectRegisterDTO> findOne(Long id) {
        log.debug("Request to get WorkProjectRegister : {}", id);
        return workProjectRegisterRepository.findOneWithEagerRelationships(id).map(workProjectRegisterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkProjectRegister : {}", id);
        workProjectRegisterRepository.deleteById(id);
        workProjectRegisterSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkProjectRegisterDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkProjectRegisters for query {}", query);
        return workProjectRegisterSearchRepository.search(query, pageable).map(workProjectRegisterMapper::toDto);
    }
}
