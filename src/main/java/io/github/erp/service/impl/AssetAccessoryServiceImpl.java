package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.AssetAccessory;
import io.github.erp.repository.AssetAccessoryRepository;
import io.github.erp.repository.search.AssetAccessorySearchRepository;
import io.github.erp.service.AssetAccessoryService;
import io.github.erp.service.dto.AssetAccessoryDTO;
import io.github.erp.service.mapper.AssetAccessoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetAccessory}.
 */
@Service
@Transactional
public class AssetAccessoryServiceImpl implements AssetAccessoryService {

    private final Logger log = LoggerFactory.getLogger(AssetAccessoryServiceImpl.class);

    private final AssetAccessoryRepository assetAccessoryRepository;

    private final AssetAccessoryMapper assetAccessoryMapper;

    private final AssetAccessorySearchRepository assetAccessorySearchRepository;

    public AssetAccessoryServiceImpl(
        AssetAccessoryRepository assetAccessoryRepository,
        AssetAccessoryMapper assetAccessoryMapper,
        AssetAccessorySearchRepository assetAccessorySearchRepository
    ) {
        this.assetAccessoryRepository = assetAccessoryRepository;
        this.assetAccessoryMapper = assetAccessoryMapper;
        this.assetAccessorySearchRepository = assetAccessorySearchRepository;
    }

    @Override
    public AssetAccessoryDTO save(AssetAccessoryDTO assetAccessoryDTO) {
        log.debug("Request to save AssetAccessory : {}", assetAccessoryDTO);
        AssetAccessory assetAccessory = assetAccessoryMapper.toEntity(assetAccessoryDTO);
        assetAccessory = assetAccessoryRepository.save(assetAccessory);
        AssetAccessoryDTO result = assetAccessoryMapper.toDto(assetAccessory);
        assetAccessorySearchRepository.save(assetAccessory);
        return result;
    }

    @Override
    public Optional<AssetAccessoryDTO> partialUpdate(AssetAccessoryDTO assetAccessoryDTO) {
        log.debug("Request to partially update AssetAccessory : {}", assetAccessoryDTO);

        return assetAccessoryRepository
            .findById(assetAccessoryDTO.getId())
            .map(existingAssetAccessory -> {
                assetAccessoryMapper.partialUpdate(existingAssetAccessory, assetAccessoryDTO);

                return existingAssetAccessory;
            })
            .map(assetAccessoryRepository::save)
            .map(savedAssetAccessory -> {
                assetAccessorySearchRepository.save(savedAssetAccessory);

                return savedAssetAccessory;
            })
            .map(assetAccessoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetAccessoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetAccessories");
        return assetAccessoryRepository.findAll(pageable).map(assetAccessoryMapper::toDto);
    }

    public Page<AssetAccessoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assetAccessoryRepository.findAllWithEagerRelationships(pageable).map(assetAccessoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetAccessoryDTO> findOne(Long id) {
        log.debug("Request to get AssetAccessory : {}", id);
        return assetAccessoryRepository.findOneWithEagerRelationships(id).map(assetAccessoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetAccessory : {}", id);
        assetAccessoryRepository.deleteById(id);
        assetAccessorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetAccessoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetAccessories for query {}", query);
        return assetAccessorySearchRepository.search(query, pageable).map(assetAccessoryMapper::toDto);
    }
}
