package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.WorkInProgressRegistration;
import io.github.erp.repository.WorkInProgressRegistrationRepository;
import io.github.erp.repository.search.WorkInProgressRegistrationSearchRepository;
import io.github.erp.service.WorkInProgressRegistrationService;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import io.github.erp.service.mapper.WorkInProgressRegistrationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkInProgressRegistration}.
 */
@Service
@Transactional
public class WorkInProgressRegistrationServiceImpl implements WorkInProgressRegistrationService {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressRegistrationServiceImpl.class);

    private final WorkInProgressRegistrationRepository workInProgressRegistrationRepository;

    private final WorkInProgressRegistrationMapper workInProgressRegistrationMapper;

    private final WorkInProgressRegistrationSearchRepository workInProgressRegistrationSearchRepository;

    public WorkInProgressRegistrationServiceImpl(
        WorkInProgressRegistrationRepository workInProgressRegistrationRepository,
        WorkInProgressRegistrationMapper workInProgressRegistrationMapper,
        WorkInProgressRegistrationSearchRepository workInProgressRegistrationSearchRepository
    ) {
        this.workInProgressRegistrationRepository = workInProgressRegistrationRepository;
        this.workInProgressRegistrationMapper = workInProgressRegistrationMapper;
        this.workInProgressRegistrationSearchRepository = workInProgressRegistrationSearchRepository;
    }

    @Override
    public WorkInProgressRegistrationDTO save(WorkInProgressRegistrationDTO workInProgressRegistrationDTO) {
        log.debug("Request to save WorkInProgressRegistration : {}", workInProgressRegistrationDTO);
        WorkInProgressRegistration workInProgressRegistration = workInProgressRegistrationMapper.toEntity(workInProgressRegistrationDTO);
        workInProgressRegistration = workInProgressRegistrationRepository.save(workInProgressRegistration);
        WorkInProgressRegistrationDTO result = workInProgressRegistrationMapper.toDto(workInProgressRegistration);
        workInProgressRegistrationSearchRepository.save(workInProgressRegistration);
        return result;
    }

    @Override
    public Optional<WorkInProgressRegistrationDTO> partialUpdate(WorkInProgressRegistrationDTO workInProgressRegistrationDTO) {
        log.debug("Request to partially update WorkInProgressRegistration : {}", workInProgressRegistrationDTO);

        return workInProgressRegistrationRepository
            .findById(workInProgressRegistrationDTO.getId())
            .map(existingWorkInProgressRegistration -> {
                workInProgressRegistrationMapper.partialUpdate(existingWorkInProgressRegistration, workInProgressRegistrationDTO);

                return existingWorkInProgressRegistration;
            })
            .map(workInProgressRegistrationRepository::save)
            .map(savedWorkInProgressRegistration -> {
                workInProgressRegistrationSearchRepository.save(savedWorkInProgressRegistration);

                return savedWorkInProgressRegistration;
            })
            .map(workInProgressRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressRegistrationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkInProgressRegistrations");
        return workInProgressRegistrationRepository.findAll(pageable).map(workInProgressRegistrationMapper::toDto);
    }

    public Page<WorkInProgressRegistrationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workInProgressRegistrationRepository.findAllWithEagerRelationships(pageable).map(workInProgressRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkInProgressRegistrationDTO> findOne(Long id) {
        log.debug("Request to get WorkInProgressRegistration : {}", id);
        return workInProgressRegistrationRepository.findOneWithEagerRelationships(id).map(workInProgressRegistrationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkInProgressRegistration : {}", id);
        workInProgressRegistrationRepository.deleteById(id);
        workInProgressRegistrationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkInProgressRegistrationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkInProgressRegistrations for query {}", query);
        return workInProgressRegistrationSearchRepository.search(query, pageable).map(workInProgressRegistrationMapper::toDto);
    }
}
