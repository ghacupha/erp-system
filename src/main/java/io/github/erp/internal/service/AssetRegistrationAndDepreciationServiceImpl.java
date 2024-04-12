package io.github.erp.internal.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.internal.repository.InternalAssetRegistrationRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class AssetRegistrationAndDepreciationServiceImpl implements AssetRegistrationAndDepreciationService {


    private final Logger log = LoggerFactory.getLogger(AssetRegistrationServiceImpl.class);

    private final InternalAssetRegistrationRepository internalAssetRegistrationRepository;

    private final AssetRegistrationMapper assetRegistrationMapper;

    private final AssetRegistrationSearchRepository assetRegistrationSearchRepository;

    public AssetRegistrationAndDepreciationServiceImpl(
        InternalAssetRegistrationRepository internalAssetRegistrationRepository,
        AssetRegistrationMapper assetRegistrationMapper,
        AssetRegistrationSearchRepository assetRegistrationSearchRepository
    ) {
        this.internalAssetRegistrationRepository = internalAssetRegistrationRepository;
        this.assetRegistrationMapper = assetRegistrationMapper;
        this.assetRegistrationSearchRepository = assetRegistrationSearchRepository;
    }

    @Override
    public AssetRegistrationDTO save(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to save AssetRegistration : {}", assetRegistrationDTO);
        AssetRegistration assetRegistration = assetRegistrationMapper.toEntity(assetRegistrationDTO);
        assetRegistration = internalAssetRegistrationRepository.save(assetRegistration);
        AssetRegistrationDTO result = assetRegistrationMapper.toDto(assetRegistration);
        assetRegistrationSearchRepository.save(assetRegistration);
        return result;
    }

    @Override
    public Optional<AssetRegistrationDTO> partialUpdate(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to partially update AssetRegistration : {}", assetRegistrationDTO);

        return internalAssetRegistrationRepository
            .findById(assetRegistrationDTO.getId())
            .map(existingAssetRegistration -> {
                assetRegistrationMapper.partialUpdate(existingAssetRegistration, assetRegistrationDTO);

                return existingAssetRegistration;
            })
            .map(internalAssetRegistrationRepository::save)
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
        return internalAssetRegistrationRepository.findAll(pageable).map(assetRegistrationMapper::toDto);
    }

    public Page<AssetRegistrationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return internalAssetRegistrationRepository.findAllWithEagerRelationships(pageable).map(assetRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetRegistrationDTO> findOne(Long id) {
        log.debug("Request to get AssetRegistration : {}", id);
        return internalAssetRegistrationRepository.findOneWithEagerRelationships(id).map(assetRegistrationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetRegistration : {}", id);
        internalAssetRegistrationRepository.deleteById(id);
        assetRegistrationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetRegistrationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetRegistrations for query {}", query);
        return assetRegistrationSearchRepository.search(query, pageable).map(assetRegistrationMapper::toDto);
    }

    /**
     * @param capitalizationDate End date which is the cut off for new assets included in the list
     * @return List of assetIds
     */
    @Override
    public List<Long> getAssetIdsByCapitalizationDateBefore(LocalDate capitalizationDate) {

        return internalAssetRegistrationRepository.getAssetIdsByCapitalizationDateBefore(capitalizationDate);
    }

    /**
     * Initial Cost of the assets being depreciated
     *
     * @param capitalizationDate End date by which the newly acquired assets are included in the depreciation scope
     * @return The summation of asset costs of assets within the depreciation scope defined by capitalization date
     */
    @Override
    public BigDecimal getInitialAssetCostByCapitalizationDateBefore(LocalDate capitalizationDate) {

        return internalAssetRegistrationRepository.getInitialAssetCostByCapitalizationDateBefore(capitalizationDate);
    }
}
