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
import io.github.erp.domain.AssetRegistration;
import io.github.erp.erp.assets.registrationindex.queue.AssetRegistrationIndexProducer;
import io.github.erp.internal.repository.InternalAssetRegistrationRepository;
import io.github.erp.internal.utilities.NextIntegerFiller;
import io.github.erp.repository.search.AssetRegistrationIndexSearchRepository;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.impl.AssetRegistrationServiceImpl;
import io.github.erp.service.mapper.AssetRegistrationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InternalAssetRegistrationServiceImpl implements InternalAssetRegistrationService{


    private final Logger log = LoggerFactory.getLogger(AssetRegistrationServiceImpl.class);

    private final InternalAssetRegistrationRepository assetRegistrationRepository;

    private final AssetRegistrationMapper assetRegistrationMapper;

    private final InternalAssetRegistrationRepository internalAssetRegistrationRepository;

    private final AssetRegistrationIndexProducer assetRegistrationIndexProducer;

    private final AssetRegistrationIndexSearchRepository assetRegistrationIndexSearchRepository;

    public InternalAssetRegistrationServiceImpl(InternalAssetRegistrationRepository assetRegistrationRepository, AssetRegistrationMapper assetRegistrationMapper, InternalAssetRegistrationRepository internalAssetRegistrationRepository, AssetRegistrationIndexProducer assetRegistrationIndexProducer, AssetRegistrationIndexSearchRepository assetRegistrationIndexSearchRepository) {
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.assetRegistrationMapper = assetRegistrationMapper;
        this.internalAssetRegistrationRepository = internalAssetRegistrationRepository;
        this.assetRegistrationIndexProducer = assetRegistrationIndexProducer;
        this.assetRegistrationIndexSearchRepository = assetRegistrationIndexSearchRepository;
    }

    @Override
    public AssetRegistrationDTO save(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to save AssetRegistration : {}", assetRegistrationDTO);
        AssetRegistration assetRegistration = assetRegistrationMapper.toEntity(assetRegistrationDTO);
        assetRegistration = assetRegistrationRepository.save(assetRegistration);
        AssetRegistrationDTO result = assetRegistrationMapper.toDto(assetRegistration);
        // Indexing happens asynchronously off the back of this queued message (see
        // AssetRegistrationIndexProducer/Consumer) - a slow or unavailable Elasticsearch can no
        // longer fail or roll back this save, which is what used to happen when this called
        // assetRegistrationSearchRepository.save(assetRegistration) directly, in-transaction,
        // against AssetRegistration's own deeply-relational (and therefore mapping-fragile) doc.
        queueIndexMessageAfterCommit(assetRegistration.getId());
        return result;
    }

    /**
     * Defers the Kafka send until this method's own transaction actually commits. Sending
     * eagerly (still inside the transaction) let the consumer race the commit: it could - and,
     * verified live against this session's dev stack, did - re-fetch the entity before the
     * save's relationship rows were visible outside this transaction, indexing a stale (e.g.
     * accessory-less) snapshot moments after attaching accessories. Falls back to sending
     * immediately when no transaction is active (defensive - every caller here is
     * {@code @Transactional}, but this shouldn't silently drop the message if that ever isn't
     * true).
     */
    private void queueIndexMessageAfterCommit(Long assetRegistrationId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            assetRegistrationIndexProducer.sendIndexMessage(List.of(assetRegistrationId));
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    assetRegistrationIndexProducer.sendIndexMessage(List.of(assetRegistrationId));
                }
            }
        );
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
                queueIndexMessageAfterCommit(savedAssetRegistration.getId());

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
        queueDeleteMessageAfterCommit(id);
    }

    private void queueDeleteMessageAfterCommit(Long assetRegistrationId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            assetRegistrationIndexProducer.sendDeleteMessage(assetRegistrationId);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    assetRegistrationIndexProducer.sendDeleteMessage(assetRegistrationId);
                }
            }
        );
    }

    /**
     * Searches the flattened {@code AssetRegistrationIndex} document (matches on any related
     * entity's name/number, see AssetRegistrationIndexMapper) for matching ids, then re-fetches
     * the real {@link AssetRegistration} rows from Postgres before returning - a search result
     * is never handed back straight from Elasticsearch. This is what prevents a stale index
     * entry (one whose underlying row was since deleted or never actually committed - e.g. an ES
     * document left over from a message that was redelivered after a rollback) from ever
     * appearing as a real result: {@code findAllById} simply won't return a row that isn't there.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AssetRegistrationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetRegistrations for query {}", query);

        Page<io.github.erp.domain.AssetRegistrationIndex> indexPage = assetRegistrationIndexSearchRepository.search(query, pageable);
        List<Long> orderedIds = indexPage.getContent().stream().map(io.github.erp.domain.AssetRegistrationIndex::getId).collect(Collectors.toList());

        if (orderedIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, indexPage.getTotalElements());
        }

        Map<Long, AssetRegistration> byId = assetRegistrationRepository
            .findAllById(orderedIds)
            .stream()
            .collect(Collectors.toMap(AssetRegistration::getId, entity -> entity, (a, b) -> a, LinkedHashMap::new));

        List<AssetRegistrationDTO> results = orderedIds
            .stream()
            .map(byId::get)
            .filter(java.util.Objects::nonNull)
            .map(assetRegistrationMapper::toDto)
            .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, indexPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetRegistrationDTO> findByCapitalizationDateBefore(LocalDate capitalizationDate) {

        return internalAssetRegistrationRepository.findAllByCapitalizationDateLessThanEqual(capitalizationDate)
            .stream().map(assetRegistrationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long calculateNextAssetNumber() {
        log.debug("Request to get next asset number");
        return NextIntegerFiller.fillNext(assetRegistrationRepository.findAllAssetNumbers());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findAllIds() {
        log.debug("Request to get list of ids");
        return assetRegistrationRepository.findAllIds();
    }

    /**
     * List of asset ids of assets related to asset-general-adjustment
     *
     * @return List of ids
     */
    @Override
    public List<Long> findAdjacentAssetIds() {

        return assetRegistrationRepository.findAdjacentAssetIds();
    }

    /**
     * List of asset ids of assets related to asset-general-adjustment
     *
     * @return List of ids
     */
    @Override
    public List<Long> findDisposedAssetIds() {

        return assetRegistrationRepository.findDisposedAssetIds();
    }

    /**
     * List of asset ids of assets related to asset-write-off
     *
     * @return List of ids
     */
    @Override
    public List<Long> findWrittenOffAssetIds() {

        return assetRegistrationRepository.findWrittenOffAssetIds();
    }
}
