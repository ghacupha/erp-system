package io.github.erp.service.impl;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.IsoCountryCode;
import io.github.erp.repository.IsoCountryCodeRepository;
import io.github.erp.repository.search.IsoCountryCodeSearchRepository;
import io.github.erp.service.IsoCountryCodeService;
import io.github.erp.service.dto.IsoCountryCodeDTO;
import io.github.erp.service.mapper.IsoCountryCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IsoCountryCode}.
 */
@Service
@Transactional
public class IsoCountryCodeServiceImpl implements IsoCountryCodeService {

    private final Logger log = LoggerFactory.getLogger(IsoCountryCodeServiceImpl.class);

    private final IsoCountryCodeRepository isoCountryCodeRepository;

    private final IsoCountryCodeMapper isoCountryCodeMapper;

    private final IsoCountryCodeSearchRepository isoCountryCodeSearchRepository;

    public IsoCountryCodeServiceImpl(
        IsoCountryCodeRepository isoCountryCodeRepository,
        IsoCountryCodeMapper isoCountryCodeMapper,
        IsoCountryCodeSearchRepository isoCountryCodeSearchRepository
    ) {
        this.isoCountryCodeRepository = isoCountryCodeRepository;
        this.isoCountryCodeMapper = isoCountryCodeMapper;
        this.isoCountryCodeSearchRepository = isoCountryCodeSearchRepository;
    }

    @Override
    public IsoCountryCodeDTO save(IsoCountryCodeDTO isoCountryCodeDTO) {
        log.debug("Request to save IsoCountryCode : {}", isoCountryCodeDTO);
        IsoCountryCode isoCountryCode = isoCountryCodeMapper.toEntity(isoCountryCodeDTO);
        isoCountryCode = isoCountryCodeRepository.save(isoCountryCode);
        IsoCountryCodeDTO result = isoCountryCodeMapper.toDto(isoCountryCode);
        isoCountryCodeSearchRepository.save(isoCountryCode);
        return result;
    }

    @Override
    public Optional<IsoCountryCodeDTO> partialUpdate(IsoCountryCodeDTO isoCountryCodeDTO) {
        log.debug("Request to partially update IsoCountryCode : {}", isoCountryCodeDTO);

        return isoCountryCodeRepository
            .findById(isoCountryCodeDTO.getId())
            .map(existingIsoCountryCode -> {
                isoCountryCodeMapper.partialUpdate(existingIsoCountryCode, isoCountryCodeDTO);

                return existingIsoCountryCode;
            })
            .map(isoCountryCodeRepository::save)
            .map(savedIsoCountryCode -> {
                isoCountryCodeSearchRepository.save(savedIsoCountryCode);

                return savedIsoCountryCode;
            })
            .map(isoCountryCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsoCountryCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IsoCountryCodes");
        return isoCountryCodeRepository.findAll(pageable).map(isoCountryCodeMapper::toDto);
    }

    public Page<IsoCountryCodeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return isoCountryCodeRepository.findAllWithEagerRelationships(pageable).map(isoCountryCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IsoCountryCodeDTO> findOne(Long id) {
        log.debug("Request to get IsoCountryCode : {}", id);
        return isoCountryCodeRepository.findOneWithEagerRelationships(id).map(isoCountryCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IsoCountryCode : {}", id);
        isoCountryCodeRepository.deleteById(id);
        isoCountryCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsoCountryCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IsoCountryCodes for query {}", query);
        return isoCountryCodeSearchRepository.search(query, pageable).map(isoCountryCodeMapper::toDto);
    }
}
