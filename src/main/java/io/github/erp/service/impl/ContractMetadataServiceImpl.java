package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.ContractMetadata;
import io.github.erp.repository.ContractMetadataRepository;
import io.github.erp.repository.search.ContractMetadataSearchRepository;
import io.github.erp.service.ContractMetadataService;
import io.github.erp.service.dto.ContractMetadataDTO;
import io.github.erp.service.mapper.ContractMetadataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContractMetadata}.
 */
@Service
@Transactional
public class ContractMetadataServiceImpl implements ContractMetadataService {

    private final Logger log = LoggerFactory.getLogger(ContractMetadataServiceImpl.class);

    private final ContractMetadataRepository contractMetadataRepository;

    private final ContractMetadataMapper contractMetadataMapper;

    private final ContractMetadataSearchRepository contractMetadataSearchRepository;

    public ContractMetadataServiceImpl(
        ContractMetadataRepository contractMetadataRepository,
        ContractMetadataMapper contractMetadataMapper,
        ContractMetadataSearchRepository contractMetadataSearchRepository
    ) {
        this.contractMetadataRepository = contractMetadataRepository;
        this.contractMetadataMapper = contractMetadataMapper;
        this.contractMetadataSearchRepository = contractMetadataSearchRepository;
    }

    @Override
    public ContractMetadataDTO save(ContractMetadataDTO contractMetadataDTO) {
        log.debug("Request to save ContractMetadata : {}", contractMetadataDTO);
        ContractMetadata contractMetadata = contractMetadataMapper.toEntity(contractMetadataDTO);
        contractMetadata = contractMetadataRepository.save(contractMetadata);
        ContractMetadataDTO result = contractMetadataMapper.toDto(contractMetadata);
        contractMetadataSearchRepository.save(contractMetadata);
        return result;
    }

    @Override
    public Optional<ContractMetadataDTO> partialUpdate(ContractMetadataDTO contractMetadataDTO) {
        log.debug("Request to partially update ContractMetadata : {}", contractMetadataDTO);

        return contractMetadataRepository
            .findById(contractMetadataDTO.getId())
            .map(existingContractMetadata -> {
                contractMetadataMapper.partialUpdate(existingContractMetadata, contractMetadataDTO);

                return existingContractMetadata;
            })
            .map(contractMetadataRepository::save)
            .map(savedContractMetadata -> {
                contractMetadataSearchRepository.save(savedContractMetadata);

                return savedContractMetadata;
            })
            .map(contractMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractMetadataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContractMetadata");
        return contractMetadataRepository.findAll(pageable).map(contractMetadataMapper::toDto);
    }

    public Page<ContractMetadataDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contractMetadataRepository.findAllWithEagerRelationships(pageable).map(contractMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContractMetadataDTO> findOne(Long id) {
        log.debug("Request to get ContractMetadata : {}", id);
        return contractMetadataRepository.findOneWithEagerRelationships(id).map(contractMetadataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContractMetadata : {}", id);
        contractMetadataRepository.deleteById(id);
        contractMetadataSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractMetadataDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContractMetadata for query {}", query);
        return contractMetadataSearchRepository.search(query, pageable).map(contractMetadataMapper::toDto);
    }
}
