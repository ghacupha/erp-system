package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
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

import io.github.erp.domain.TerminalsAndPOS;
import io.github.erp.repository.TerminalsAndPOSRepository;
import io.github.erp.repository.search.TerminalsAndPOSSearchRepository;
import io.github.erp.service.TerminalsAndPOSService;
import io.github.erp.service.dto.TerminalsAndPOSDTO;
import io.github.erp.service.mapper.TerminalsAndPOSMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TerminalsAndPOS}.
 */
@Service
@Transactional
public class TerminalsAndPOSServiceImpl implements TerminalsAndPOSService {

    private final Logger log = LoggerFactory.getLogger(TerminalsAndPOSServiceImpl.class);

    private final TerminalsAndPOSRepository terminalsAndPOSRepository;

    private final TerminalsAndPOSMapper terminalsAndPOSMapper;

    private final TerminalsAndPOSSearchRepository terminalsAndPOSSearchRepository;

    public TerminalsAndPOSServiceImpl(
        TerminalsAndPOSRepository terminalsAndPOSRepository,
        TerminalsAndPOSMapper terminalsAndPOSMapper,
        TerminalsAndPOSSearchRepository terminalsAndPOSSearchRepository
    ) {
        this.terminalsAndPOSRepository = terminalsAndPOSRepository;
        this.terminalsAndPOSMapper = terminalsAndPOSMapper;
        this.terminalsAndPOSSearchRepository = terminalsAndPOSSearchRepository;
    }

    @Override
    public TerminalsAndPOSDTO save(TerminalsAndPOSDTO terminalsAndPOSDTO) {
        log.debug("Request to save TerminalsAndPOS : {}", terminalsAndPOSDTO);
        TerminalsAndPOS terminalsAndPOS = terminalsAndPOSMapper.toEntity(terminalsAndPOSDTO);
        terminalsAndPOS = terminalsAndPOSRepository.save(terminalsAndPOS);
        TerminalsAndPOSDTO result = terminalsAndPOSMapper.toDto(terminalsAndPOS);
        terminalsAndPOSSearchRepository.save(terminalsAndPOS);
        return result;
    }

    @Override
    public Optional<TerminalsAndPOSDTO> partialUpdate(TerminalsAndPOSDTO terminalsAndPOSDTO) {
        log.debug("Request to partially update TerminalsAndPOS : {}", terminalsAndPOSDTO);

        return terminalsAndPOSRepository
            .findById(terminalsAndPOSDTO.getId())
            .map(existingTerminalsAndPOS -> {
                terminalsAndPOSMapper.partialUpdate(existingTerminalsAndPOS, terminalsAndPOSDTO);

                return existingTerminalsAndPOS;
            })
            .map(terminalsAndPOSRepository::save)
            .map(savedTerminalsAndPOS -> {
                terminalsAndPOSSearchRepository.save(savedTerminalsAndPOS);

                return savedTerminalsAndPOS;
            })
            .map(terminalsAndPOSMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TerminalsAndPOSDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TerminalsAndPOS");
        return terminalsAndPOSRepository.findAll(pageable).map(terminalsAndPOSMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TerminalsAndPOSDTO> findOne(Long id) {
        log.debug("Request to get TerminalsAndPOS : {}", id);
        return terminalsAndPOSRepository.findById(id).map(terminalsAndPOSMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TerminalsAndPOS : {}", id);
        terminalsAndPOSRepository.deleteById(id);
        terminalsAndPOSSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TerminalsAndPOSDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TerminalsAndPOS for query {}", query);
        return terminalsAndPOSSearchRepository.search(query, pageable).map(terminalsAndPOSMapper::toDto);
    }
}
