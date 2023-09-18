package io.github.erp.service.impl;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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

import io.github.erp.domain.NatureOfCustomerComplaints;
import io.github.erp.repository.NatureOfCustomerComplaintsRepository;
import io.github.erp.repository.search.NatureOfCustomerComplaintsSearchRepository;
import io.github.erp.service.NatureOfCustomerComplaintsService;
import io.github.erp.service.dto.NatureOfCustomerComplaintsDTO;
import io.github.erp.service.mapper.NatureOfCustomerComplaintsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NatureOfCustomerComplaints}.
 */
@Service
@Transactional
public class NatureOfCustomerComplaintsServiceImpl implements NatureOfCustomerComplaintsService {

    private final Logger log = LoggerFactory.getLogger(NatureOfCustomerComplaintsServiceImpl.class);

    private final NatureOfCustomerComplaintsRepository natureOfCustomerComplaintsRepository;

    private final NatureOfCustomerComplaintsMapper natureOfCustomerComplaintsMapper;

    private final NatureOfCustomerComplaintsSearchRepository natureOfCustomerComplaintsSearchRepository;

    public NatureOfCustomerComplaintsServiceImpl(
        NatureOfCustomerComplaintsRepository natureOfCustomerComplaintsRepository,
        NatureOfCustomerComplaintsMapper natureOfCustomerComplaintsMapper,
        NatureOfCustomerComplaintsSearchRepository natureOfCustomerComplaintsSearchRepository
    ) {
        this.natureOfCustomerComplaintsRepository = natureOfCustomerComplaintsRepository;
        this.natureOfCustomerComplaintsMapper = natureOfCustomerComplaintsMapper;
        this.natureOfCustomerComplaintsSearchRepository = natureOfCustomerComplaintsSearchRepository;
    }

    @Override
    public NatureOfCustomerComplaintsDTO save(NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO) {
        log.debug("Request to save NatureOfCustomerComplaints : {}", natureOfCustomerComplaintsDTO);
        NatureOfCustomerComplaints natureOfCustomerComplaints = natureOfCustomerComplaintsMapper.toEntity(natureOfCustomerComplaintsDTO);
        natureOfCustomerComplaints = natureOfCustomerComplaintsRepository.save(natureOfCustomerComplaints);
        NatureOfCustomerComplaintsDTO result = natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaints);
        natureOfCustomerComplaintsSearchRepository.save(natureOfCustomerComplaints);
        return result;
    }

    @Override
    public Optional<NatureOfCustomerComplaintsDTO> partialUpdate(NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO) {
        log.debug("Request to partially update NatureOfCustomerComplaints : {}", natureOfCustomerComplaintsDTO);

        return natureOfCustomerComplaintsRepository
            .findById(natureOfCustomerComplaintsDTO.getId())
            .map(existingNatureOfCustomerComplaints -> {
                natureOfCustomerComplaintsMapper.partialUpdate(existingNatureOfCustomerComplaints, natureOfCustomerComplaintsDTO);

                return existingNatureOfCustomerComplaints;
            })
            .map(natureOfCustomerComplaintsRepository::save)
            .map(savedNatureOfCustomerComplaints -> {
                natureOfCustomerComplaintsSearchRepository.save(savedNatureOfCustomerComplaints);

                return savedNatureOfCustomerComplaints;
            })
            .map(natureOfCustomerComplaintsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NatureOfCustomerComplaintsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NatureOfCustomerComplaints");
        return natureOfCustomerComplaintsRepository.findAll(pageable).map(natureOfCustomerComplaintsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NatureOfCustomerComplaintsDTO> findOne(Long id) {
        log.debug("Request to get NatureOfCustomerComplaints : {}", id);
        return natureOfCustomerComplaintsRepository.findById(id).map(natureOfCustomerComplaintsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NatureOfCustomerComplaints : {}", id);
        natureOfCustomerComplaintsRepository.deleteById(id);
        natureOfCustomerComplaintsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NatureOfCustomerComplaintsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NatureOfCustomerComplaints for query {}", query);
        return natureOfCustomerComplaintsSearchRepository.search(query, pageable).map(natureOfCustomerComplaintsMapper::toDto);
    }
}
