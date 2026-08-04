package io.github.erp.internal.service.assets;

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
import io.github.erp.domain.AssetAccessory;
import io.github.erp.domain.AssetAccessoryIndex;
import io.github.erp.erp.assets.accessoryindex.queue.AssetAccessoryIndexProducer;
import io.github.erp.repository.AssetAccessoryRepository;
import io.github.erp.repository.search.AssetAccessoryIndexSearchRepository;
import io.github.erp.service.dto.AssetAccessoryDTO;
import io.github.erp.service.mapper.AssetAccessoryMapper;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Replaces the generated {@code AssetAccessoryServiceImpl} as the implementation behind
 * {@code AssetAccessoryResourceProd} - same synchronous-ES-write problem, same fix, as
 * InternalAssetRegistrationServiceImpl (see its doc comment and the accompanying man_pages
 * write-up for the full rationale, including the send-before-commit race caught and fixed there
 * - this class starts with that fix already in place).
 */
@Service
@Transactional
public class InternalAssetAccessoryServiceImpl implements InternalAssetAccessoryService {

    private final Logger log = LoggerFactory.getLogger(InternalAssetAccessoryServiceImpl.class);

    private final AssetAccessoryRepository assetAccessoryRepository;
    private final AssetAccessoryMapper assetAccessoryMapper;
    private final AssetAccessoryIndexProducer assetAccessoryIndexProducer;
    private final AssetAccessoryIndexSearchRepository assetAccessoryIndexSearchRepository;

    public InternalAssetAccessoryServiceImpl(
        AssetAccessoryRepository assetAccessoryRepository,
        AssetAccessoryMapper assetAccessoryMapper,
        AssetAccessoryIndexProducer assetAccessoryIndexProducer,
        AssetAccessoryIndexSearchRepository assetAccessoryIndexSearchRepository
    ) {
        this.assetAccessoryRepository = assetAccessoryRepository;
        this.assetAccessoryMapper = assetAccessoryMapper;
        this.assetAccessoryIndexProducer = assetAccessoryIndexProducer;
        this.assetAccessoryIndexSearchRepository = assetAccessoryIndexSearchRepository;
    }

    @Override
    public AssetAccessoryDTO save(AssetAccessoryDTO assetAccessoryDTO) {
        log.debug("Request to save AssetAccessory : {}", assetAccessoryDTO);
        AssetAccessory assetAccessory = assetAccessoryMapper.toEntity(assetAccessoryDTO);
        assetAccessory = assetAccessoryRepository.save(assetAccessory);
        AssetAccessoryDTO result = assetAccessoryMapper.toDto(assetAccessory);
        queueIndexMessageAfterCommit(assetAccessory.getId());
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
                queueIndexMessageAfterCommit(savedAssetAccessory.getId());
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

    @Override
    @Transactional(readOnly = true)
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
        queueDeleteMessageAfterCommit(id);
    }

    /**
     * Matches, then hydrates from Postgres - see InternalAssetRegistrationServiceImpl#search's
     * doc comment for the full rationale (identical here).
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AssetAccessoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetAccessories for query {}", query);

        Page<AssetAccessoryIndex> indexPage = assetAccessoryIndexSearchRepository.search(query, pageable);
        List<Long> orderedIds = indexPage.getContent().stream().map(AssetAccessoryIndex::getId).collect(Collectors.toList());

        if (orderedIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, indexPage.getTotalElements());
        }

        Map<Long, AssetAccessory> byId = assetAccessoryRepository
            .findAllById(orderedIds)
            .stream()
            .collect(Collectors.toMap(AssetAccessory::getId, entity -> entity, (a, b) -> a, LinkedHashMap::new));

        List<AssetAccessoryDTO> results = orderedIds
            .stream()
            .map(byId::get)
            .filter(Objects::nonNull)
            .map(assetAccessoryMapper::toDto)
            .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, indexPage.getTotalElements());
    }

    private void queueIndexMessageAfterCommit(Long assetAccessoryId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            assetAccessoryIndexProducer.sendIndexMessage(List.of(assetAccessoryId));
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    assetAccessoryIndexProducer.sendIndexMessage(List.of(assetAccessoryId));
                }
            }
        );
    }

    private void queueDeleteMessageAfterCommit(Long assetAccessoryId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            assetAccessoryIndexProducer.sendDeleteMessage(assetAccessoryId);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    assetAccessoryIndexProducer.sendDeleteMessage(assetAccessoryId);
                }
            }
        );
    }
}
