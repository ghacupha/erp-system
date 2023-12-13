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

import io.github.erp.domain.ReasonsForBouncedCheque;
import io.github.erp.repository.ReasonsForBouncedChequeRepository;
import io.github.erp.repository.search.ReasonsForBouncedChequeSearchRepository;
import io.github.erp.service.ReasonsForBouncedChequeService;
import io.github.erp.service.dto.ReasonsForBouncedChequeDTO;
import io.github.erp.service.mapper.ReasonsForBouncedChequeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReasonsForBouncedCheque}.
 */
@Service
@Transactional
public class ReasonsForBouncedChequeServiceImpl implements ReasonsForBouncedChequeService {

    private final Logger log = LoggerFactory.getLogger(ReasonsForBouncedChequeServiceImpl.class);

    private final ReasonsForBouncedChequeRepository reasonsForBouncedChequeRepository;

    private final ReasonsForBouncedChequeMapper reasonsForBouncedChequeMapper;

    private final ReasonsForBouncedChequeSearchRepository reasonsForBouncedChequeSearchRepository;

    public ReasonsForBouncedChequeServiceImpl(
        ReasonsForBouncedChequeRepository reasonsForBouncedChequeRepository,
        ReasonsForBouncedChequeMapper reasonsForBouncedChequeMapper,
        ReasonsForBouncedChequeSearchRepository reasonsForBouncedChequeSearchRepository
    ) {
        this.reasonsForBouncedChequeRepository = reasonsForBouncedChequeRepository;
        this.reasonsForBouncedChequeMapper = reasonsForBouncedChequeMapper;
        this.reasonsForBouncedChequeSearchRepository = reasonsForBouncedChequeSearchRepository;
    }

    @Override
    public ReasonsForBouncedChequeDTO save(ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO) {
        log.debug("Request to save ReasonsForBouncedCheque : {}", reasonsForBouncedChequeDTO);
        ReasonsForBouncedCheque reasonsForBouncedCheque = reasonsForBouncedChequeMapper.toEntity(reasonsForBouncedChequeDTO);
        reasonsForBouncedCheque = reasonsForBouncedChequeRepository.save(reasonsForBouncedCheque);
        ReasonsForBouncedChequeDTO result = reasonsForBouncedChequeMapper.toDto(reasonsForBouncedCheque);
        reasonsForBouncedChequeSearchRepository.save(reasonsForBouncedCheque);
        return result;
    }

    @Override
    public Optional<ReasonsForBouncedChequeDTO> partialUpdate(ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO) {
        log.debug("Request to partially update ReasonsForBouncedCheque : {}", reasonsForBouncedChequeDTO);

        return reasonsForBouncedChequeRepository
            .findById(reasonsForBouncedChequeDTO.getId())
            .map(existingReasonsForBouncedCheque -> {
                reasonsForBouncedChequeMapper.partialUpdate(existingReasonsForBouncedCheque, reasonsForBouncedChequeDTO);

                return existingReasonsForBouncedCheque;
            })
            .map(reasonsForBouncedChequeRepository::save)
            .map(savedReasonsForBouncedCheque -> {
                reasonsForBouncedChequeSearchRepository.save(savedReasonsForBouncedCheque);

                return savedReasonsForBouncedCheque;
            })
            .map(reasonsForBouncedChequeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReasonsForBouncedChequeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReasonsForBouncedCheques");
        return reasonsForBouncedChequeRepository.findAll(pageable).map(reasonsForBouncedChequeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReasonsForBouncedChequeDTO> findOne(Long id) {
        log.debug("Request to get ReasonsForBouncedCheque : {}", id);
        return reasonsForBouncedChequeRepository.findById(id).map(reasonsForBouncedChequeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReasonsForBouncedCheque : {}", id);
        reasonsForBouncedChequeRepository.deleteById(id);
        reasonsForBouncedChequeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReasonsForBouncedChequeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReasonsForBouncedCheques for query {}", query);
        return reasonsForBouncedChequeSearchRepository.search(query, pageable).map(reasonsForBouncedChequeMapper::toDto);
    }
}
