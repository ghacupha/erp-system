package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.LeaseContract;
import io.github.erp.repository.LeaseContractRepository;
import io.github.erp.repository.search.LeaseContractSearchRepository;
import io.github.erp.service.LeaseContractService;
import io.github.erp.service.dto.LeaseContractDTO;
import io.github.erp.service.mapper.LeaseContractMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseContract}.
 */
@Service
@Transactional
public class LeaseContractServiceImpl implements LeaseContractService {

    private final Logger log = LoggerFactory.getLogger(LeaseContractServiceImpl.class);

    private final LeaseContractRepository leaseContractRepository;

    private final LeaseContractMapper leaseContractMapper;

    private final LeaseContractSearchRepository leaseContractSearchRepository;

    public LeaseContractServiceImpl(
        LeaseContractRepository leaseContractRepository,
        LeaseContractMapper leaseContractMapper,
        LeaseContractSearchRepository leaseContractSearchRepository
    ) {
        this.leaseContractRepository = leaseContractRepository;
        this.leaseContractMapper = leaseContractMapper;
        this.leaseContractSearchRepository = leaseContractSearchRepository;
    }

    @Override
    public LeaseContractDTO save(LeaseContractDTO leaseContractDTO) {
        log.debug("Request to save LeaseContract : {}", leaseContractDTO);
        LeaseContract leaseContract = leaseContractMapper.toEntity(leaseContractDTO);
        leaseContract = leaseContractRepository.save(leaseContract);
        LeaseContractDTO result = leaseContractMapper.toDto(leaseContract);
        leaseContractSearchRepository.save(leaseContract);
        return result;
    }

    @Override
    public Optional<LeaseContractDTO> partialUpdate(LeaseContractDTO leaseContractDTO) {
        log.debug("Request to partially update LeaseContract : {}", leaseContractDTO);

        return leaseContractRepository
            .findById(leaseContractDTO.getId())
            .map(existingLeaseContract -> {
                leaseContractMapper.partialUpdate(existingLeaseContract, leaseContractDTO);

                return existingLeaseContract;
            })
            .map(leaseContractRepository::save)
            .map(savedLeaseContract -> {
                leaseContractSearchRepository.save(savedLeaseContract);

                return savedLeaseContract;
            })
            .map(leaseContractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseContracts");
        return leaseContractRepository.findAll(pageable).map(leaseContractMapper::toDto);
    }

    public Page<LeaseContractDTO> findAllWithEagerRelationships(Pageable pageable) {
        return leaseContractRepository.findAllWithEagerRelationships(pageable).map(leaseContractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseContractDTO> findOne(Long id) {
        log.debug("Request to get LeaseContract : {}", id);
        return leaseContractRepository.findOneWithEagerRelationships(id).map(leaseContractMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseContract : {}", id);
        leaseContractRepository.deleteById(id);
        leaseContractSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseContractDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseContracts for query {}", query);
        return leaseContractSearchRepository.search(query, pageable).map(leaseContractMapper::toDto);
    }
}
