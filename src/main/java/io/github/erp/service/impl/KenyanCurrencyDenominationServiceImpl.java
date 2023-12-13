package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.KenyanCurrencyDenomination;
import io.github.erp.repository.KenyanCurrencyDenominationRepository;
import io.github.erp.repository.search.KenyanCurrencyDenominationSearchRepository;
import io.github.erp.service.KenyanCurrencyDenominationService;
import io.github.erp.service.dto.KenyanCurrencyDenominationDTO;
import io.github.erp.service.mapper.KenyanCurrencyDenominationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link KenyanCurrencyDenomination}.
 */
@Service
@Transactional
public class KenyanCurrencyDenominationServiceImpl implements KenyanCurrencyDenominationService {

    private final Logger log = LoggerFactory.getLogger(KenyanCurrencyDenominationServiceImpl.class);

    private final KenyanCurrencyDenominationRepository kenyanCurrencyDenominationRepository;

    private final KenyanCurrencyDenominationMapper kenyanCurrencyDenominationMapper;

    private final KenyanCurrencyDenominationSearchRepository kenyanCurrencyDenominationSearchRepository;

    public KenyanCurrencyDenominationServiceImpl(
        KenyanCurrencyDenominationRepository kenyanCurrencyDenominationRepository,
        KenyanCurrencyDenominationMapper kenyanCurrencyDenominationMapper,
        KenyanCurrencyDenominationSearchRepository kenyanCurrencyDenominationSearchRepository
    ) {
        this.kenyanCurrencyDenominationRepository = kenyanCurrencyDenominationRepository;
        this.kenyanCurrencyDenominationMapper = kenyanCurrencyDenominationMapper;
        this.kenyanCurrencyDenominationSearchRepository = kenyanCurrencyDenominationSearchRepository;
    }

    @Override
    public KenyanCurrencyDenominationDTO save(KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO) {
        log.debug("Request to save KenyanCurrencyDenomination : {}", kenyanCurrencyDenominationDTO);
        KenyanCurrencyDenomination kenyanCurrencyDenomination = kenyanCurrencyDenominationMapper.toEntity(kenyanCurrencyDenominationDTO);
        kenyanCurrencyDenomination = kenyanCurrencyDenominationRepository.save(kenyanCurrencyDenomination);
        KenyanCurrencyDenominationDTO result = kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenomination);
        kenyanCurrencyDenominationSearchRepository.save(kenyanCurrencyDenomination);
        return result;
    }

    @Override
    public Optional<KenyanCurrencyDenominationDTO> partialUpdate(KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO) {
        log.debug("Request to partially update KenyanCurrencyDenomination : {}", kenyanCurrencyDenominationDTO);

        return kenyanCurrencyDenominationRepository
            .findById(kenyanCurrencyDenominationDTO.getId())
            .map(existingKenyanCurrencyDenomination -> {
                kenyanCurrencyDenominationMapper.partialUpdate(existingKenyanCurrencyDenomination, kenyanCurrencyDenominationDTO);

                return existingKenyanCurrencyDenomination;
            })
            .map(kenyanCurrencyDenominationRepository::save)
            .map(savedKenyanCurrencyDenomination -> {
                kenyanCurrencyDenominationSearchRepository.save(savedKenyanCurrencyDenomination);

                return savedKenyanCurrencyDenomination;
            })
            .map(kenyanCurrencyDenominationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KenyanCurrencyDenominationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all KenyanCurrencyDenominations");
        return kenyanCurrencyDenominationRepository.findAll(pageable).map(kenyanCurrencyDenominationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<KenyanCurrencyDenominationDTO> findOne(Long id) {
        log.debug("Request to get KenyanCurrencyDenomination : {}", id);
        return kenyanCurrencyDenominationRepository.findById(id).map(kenyanCurrencyDenominationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete KenyanCurrencyDenomination : {}", id);
        kenyanCurrencyDenominationRepository.deleteById(id);
        kenyanCurrencyDenominationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KenyanCurrencyDenominationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of KenyanCurrencyDenominations for query {}", query);
        return kenyanCurrencyDenominationSearchRepository.search(query, pageable).map(kenyanCurrencyDenominationMapper::toDto);
    }
}
