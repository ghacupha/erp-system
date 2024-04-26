package io.github.erp.internal.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.AssetDisposal;
import io.github.erp.erp.assets.nbv.buffer.BufferedSinkProcessor;
import io.github.erp.internal.repository.InternalAssetDisposalRepository;
import io.github.erp.internal.service.applicationUser.CurrentUserContext;
import io.github.erp.repository.search.AssetDisposalSearchRepository;
import io.github.erp.service.dto.AssetDisposalDTO;
import io.github.erp.service.mapper.AssetDisposalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AssetDisposal}.
 */
@Service
@Transactional
public class InternalAssetDisposalServiceImpl implements InternalAssetDisposalService {

    private final Logger log = LoggerFactory.getLogger(InternalAssetDisposalServiceImpl.class);

    private final InternalAssetDisposalRepository assetDisposalRepository;

    private final AssetDisposalMapper assetDisposalMapper;

    private final AssetDisposalSearchRepository assetDisposalSearchRepository;

    private final BufferedSinkProcessor<AssetDisposal> bufferedSinkProcessor;

    public InternalAssetDisposalServiceImpl(
        InternalAssetDisposalRepository assetDisposalRepository,
        AssetDisposalMapper assetDisposalMapper,
        AssetDisposalSearchRepository assetDisposalSearchRepository,
        BufferedSinkProcessor<AssetDisposal> bufferedSinkProcessor) {
        this.assetDisposalRepository = assetDisposalRepository;
        this.assetDisposalMapper = assetDisposalMapper;
        this.assetDisposalSearchRepository = assetDisposalSearchRepository;
        this.bufferedSinkProcessor = bufferedSinkProcessor;
    }

    @Override
    public AssetDisposalDTO save(AssetDisposalDTO assetDisposalDTO) {
        log.debug("Request to save AssetDisposal : {}", assetDisposalDTO);

        ApplicationUser creator = CurrentUserContext.getCurrentUser();

        AssetDisposal assetDisposal = assetDisposalMapper.toEntity(assetDisposalDTO);
        assetDisposal.createdBy(creator);
        assetDisposal = assetDisposalRepository.save(assetDisposal);
        AssetDisposalDTO result = assetDisposalMapper.toDto(assetDisposal);
        assetDisposalSearchRepository.save(assetDisposal);
        return result;
    }

    @Override
    public Optional<AssetDisposalDTO> partialUpdate(AssetDisposalDTO assetDisposalDTO) {
        log.debug("Request to partially update AssetDisposal : {}", assetDisposalDTO);

        ApplicationUser modifier = CurrentUserContext.getCurrentUser();

        return assetDisposalRepository
            .findById(assetDisposalDTO.getId())
            .map(existingAssetDisposal -> {

                assetDisposalMapper.partialUpdate(existingAssetDisposal, assetDisposalDTO);

                // Note modifying agent
                existingAssetDisposal.modifiedBy(modifier);

                return existingAssetDisposal;
            })
            .map(assetDisposalRepository::save)
            .map(savedAssetDisposal -> {
                assetDisposalSearchRepository.save(savedAssetDisposal);

                return savedAssetDisposal;
            })
            .map(assetDisposalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetDisposalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetDisposals");
        return assetDisposalRepository.findAll(pageable).map(assetDisposalMapper::toDto);
    }

    public Page<AssetDisposalDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assetDisposalRepository.findAllWithEagerRelationships(pageable).map(assetDisposalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetDisposalDTO> findOne(Long id) {
        log.debug("Request to get AssetDisposal : {}", id);
        ApplicationUser accessor = CurrentUserContext.getCurrentUser();
        return assetDisposalRepository.findOneWithEagerRelationships(id)
            .map((assetDisposal) -> {
                assetDisposal.setLastAccessedBy(accessor);
                return assetDisposalMapper.toDto(assetDisposalRepository.save(assetDisposal));
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<AssetDisposalDTO>> findDisposedItems (Long disposedAssetId, LocalDate depreciationPeriodStartDate) {
        log.debug("Request to get AssetDisposal for asset id: {}", disposedAssetId);

        return assetDisposalRepository.findAssetDisposal(disposedAssetId, depreciationPeriodStartDate)
            .map(assetDisposalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetDisposal : {}", id);
        assetDisposalRepository.deleteById(id);
        assetDisposalSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetDisposalDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetDisposals for query {}", query);
        return assetDisposalSearchRepository.search(query, pageable).map(assetDisposalMapper::toDto);
    }
}
