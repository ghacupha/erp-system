package io.github.erp.internal.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.AssetRegistration;
import io.github.erp.internal.repository.InternalAssetRegistrationRepository;
import io.github.erp.internal.utilities.NextIntegerFiller;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InternalAssetRegistrationServiceImpl implements InternalAssetRegistrationService{


    private final Logger log = LoggerFactory.getLogger(AssetRegistrationServiceImpl.class);

    private final InternalAssetRegistrationRepository assetRegistrationRepository;

    private final AssetRegistrationMapper assetRegistrationMapper;

    private final AssetRegistrationSearchRepository assetRegistrationSearchRepository;

    private final InternalAssetRegistrationRepository internalAssetRegistrationRepository;

    public InternalAssetRegistrationServiceImpl(InternalAssetRegistrationRepository assetRegistrationRepository, AssetRegistrationMapper assetRegistrationMapper, AssetRegistrationSearchRepository assetRegistrationSearchRepository, InternalAssetRegistrationRepository internalAssetRegistrationRepository) {
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.assetRegistrationMapper = assetRegistrationMapper;
        this.assetRegistrationSearchRepository = assetRegistrationSearchRepository;
        this.internalAssetRegistrationRepository = internalAssetRegistrationRepository;
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

    @Override
    @Transactional(readOnly = true)
    public List<AssetRegistrationDTO> findByCapitalizationDateBefore(LocalDate capitalizationDate) {

        return internalAssetRegistrationRepository.findAllByCapitalizationDateBefore(capitalizationDate)
            .stream().map(assetRegistrationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long calculateNextAssetNumber() {
        log.debug("Request to get next asset number");
        return NextIntegerFiller.fillNext(assetRegistrationRepository.findAllIds());
    }
}
