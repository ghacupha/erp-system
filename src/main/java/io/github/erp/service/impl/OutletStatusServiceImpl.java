package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.OutletStatus;
import io.github.erp.repository.OutletStatusRepository;
import io.github.erp.repository.search.OutletStatusSearchRepository;
import io.github.erp.service.OutletStatusService;
import io.github.erp.service.dto.OutletStatusDTO;
import io.github.erp.service.mapper.OutletStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OutletStatus}.
 */
@Service
@Transactional
public class OutletStatusServiceImpl implements OutletStatusService {

    private final Logger log = LoggerFactory.getLogger(OutletStatusServiceImpl.class);

    private final OutletStatusRepository outletStatusRepository;

    private final OutletStatusMapper outletStatusMapper;

    private final OutletStatusSearchRepository outletStatusSearchRepository;

    public OutletStatusServiceImpl(
        OutletStatusRepository outletStatusRepository,
        OutletStatusMapper outletStatusMapper,
        OutletStatusSearchRepository outletStatusSearchRepository
    ) {
        this.outletStatusRepository = outletStatusRepository;
        this.outletStatusMapper = outletStatusMapper;
        this.outletStatusSearchRepository = outletStatusSearchRepository;
    }

    @Override
    public OutletStatusDTO save(OutletStatusDTO outletStatusDTO) {
        log.debug("Request to save OutletStatus : {}", outletStatusDTO);
        OutletStatus outletStatus = outletStatusMapper.toEntity(outletStatusDTO);
        outletStatus = outletStatusRepository.save(outletStatus);
        OutletStatusDTO result = outletStatusMapper.toDto(outletStatus);
        outletStatusSearchRepository.save(outletStatus);
        return result;
    }

    @Override
    public Optional<OutletStatusDTO> partialUpdate(OutletStatusDTO outletStatusDTO) {
        log.debug("Request to partially update OutletStatus : {}", outletStatusDTO);

        return outletStatusRepository
            .findById(outletStatusDTO.getId())
            .map(existingOutletStatus -> {
                outletStatusMapper.partialUpdate(existingOutletStatus, outletStatusDTO);

                return existingOutletStatus;
            })
            .map(outletStatusRepository::save)
            .map(savedOutletStatus -> {
                outletStatusSearchRepository.save(savedOutletStatus);

                return savedOutletStatus;
            })
            .map(outletStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutletStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OutletStatuses");
        return outletStatusRepository.findAll(pageable).map(outletStatusMapper::toDto);
    }

    public Page<OutletStatusDTO> findAllWithEagerRelationships(Pageable pageable) {
        return outletStatusRepository.findAllWithEagerRelationships(pageable).map(outletStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OutletStatusDTO> findOne(Long id) {
        log.debug("Request to get OutletStatus : {}", id);
        return outletStatusRepository.findOneWithEagerRelationships(id).map(outletStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OutletStatus : {}", id);
        outletStatusRepository.deleteById(id);
        outletStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutletStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OutletStatuses for query {}", query);
        return outletStatusSearchRepository.search(query, pageable).map(outletStatusMapper::toDto);
    }
}
