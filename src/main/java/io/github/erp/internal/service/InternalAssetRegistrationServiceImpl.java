package io.github.erp.internal.service;

import io.github.erp.domain.AssetRegistration;
import io.github.erp.internal.repository.InternalAssetRegistrationRepository;
import io.github.erp.repository.AssetRegistrationRepository;
import io.github.erp.repository.search.AssetRegistrationSearchRepository;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.impl.AssetRegistrationServiceImpl;
import io.github.erp.service.mapper.AssetRegistrationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class InternalAssetRegistrationServiceImpl implements InternalAssetRegistrationService{


    private final Logger log = LoggerFactory.getLogger(AssetRegistrationServiceImpl.class);

    private final InternalAssetRegistrationRepository assetRegistrationRepository;

    private final AssetRegistrationMapper assetRegistrationMapper;

    private final AssetRegistrationSearchRepository assetRegistrationSearchRepository;

    public InternalAssetRegistrationServiceImpl(InternalAssetRegistrationRepository assetRegistrationRepository, AssetRegistrationMapper assetRegistrationMapper, AssetRegistrationSearchRepository assetRegistrationSearchRepository) {
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.assetRegistrationMapper = assetRegistrationMapper;
        this.assetRegistrationSearchRepository = assetRegistrationSearchRepository;
    }

    @Override
    public AssetRegistrationDTO save(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to save AssetRegistration : {}", assetRegistrationDTO);
        AssetRegistration assetRegistration = assetRegistrationMapper.toEntity(assetRegistrationDTO);
        assetRegistration = assetRegistrationRepository.save(assetRegistration);
        AssetRegistrationDTO result = assetRegistrationMapper.toDto(assetRegistration);
        assetRegistrationSearchRepository.save(assetRegistration);
        return result;
    }

    @Override
    public Optional<AssetRegistrationDTO> partialUpdate(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to partially update AssetRegistration : {}", assetRegistrationDTO);

        return assetRegistrationRepository
            .findById(assetRegistrationDTO.getId())
            .map(existingAssetRegistration -> {
                assetRegistrationMapper.partialUpdate(existingAssetRegistration, assetRegistrationDTO);

                return existingAssetRegistration;
            })
            .map(assetRegistrationRepository::save)
            .map(savedAssetRegistration -> {
                assetRegistrationSearchRepository.save(savedAssetRegistration);

                return savedAssetRegistration;
            })
            .map(assetRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetRegistrationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetRegistrations");
        return assetRegistrationRepository.findAll(pageable).map(assetRegistrationMapper::toDto);
    }

    public Page<AssetRegistrationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assetRegistrationRepository.findAllWithEagerRelationships(pageable).map(assetRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetRegistrationDTO> findOne(Long id) {
        log.debug("Request to get AssetRegistration : {}", id);
        return assetRegistrationRepository.findOneWithEagerRelationships(id).map(assetRegistrationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetRegistration : {}", id);
        assetRegistrationRepository.deleteById(id);
        assetRegistrationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetRegistrationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetRegistrations for query {}", query);
        return assetRegistrationSearchRepository.search(query, pageable).map(assetRegistrationMapper::toDto);
    }
}
