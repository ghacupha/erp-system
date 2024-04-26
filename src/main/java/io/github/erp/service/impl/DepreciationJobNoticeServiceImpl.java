package io.github.erp.service.impl;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.DepreciationJobNotice;
import io.github.erp.repository.DepreciationJobNoticeRepository;
import io.github.erp.repository.search.DepreciationJobNoticeSearchRepository;
import io.github.erp.service.DepreciationJobNoticeService;
import io.github.erp.service.dto.DepreciationJobNoticeDTO;
import io.github.erp.service.mapper.DepreciationJobNoticeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DepreciationJobNotice}.
 */
@Service
@Transactional
public class DepreciationJobNoticeServiceImpl implements DepreciationJobNoticeService {

    private final Logger log = LoggerFactory.getLogger(DepreciationJobNoticeServiceImpl.class);

    private final DepreciationJobNoticeRepository depreciationJobNoticeRepository;

    private final DepreciationJobNoticeMapper depreciationJobNoticeMapper;

    private final DepreciationJobNoticeSearchRepository depreciationJobNoticeSearchRepository;

    public DepreciationJobNoticeServiceImpl(
        DepreciationJobNoticeRepository depreciationJobNoticeRepository,
        DepreciationJobNoticeMapper depreciationJobNoticeMapper,
        DepreciationJobNoticeSearchRepository depreciationJobNoticeSearchRepository
    ) {
        this.depreciationJobNoticeRepository = depreciationJobNoticeRepository;
        this.depreciationJobNoticeMapper = depreciationJobNoticeMapper;
        this.depreciationJobNoticeSearchRepository = depreciationJobNoticeSearchRepository;
    }

    @Override
    public DepreciationJobNoticeDTO save(DepreciationJobNoticeDTO depreciationJobNoticeDTO) {
        log.debug("Request to save DepreciationJobNotice : {}", depreciationJobNoticeDTO);
        DepreciationJobNotice depreciationJobNotice = depreciationJobNoticeMapper.toEntity(depreciationJobNoticeDTO);
        depreciationJobNotice = depreciationJobNoticeRepository.save(depreciationJobNotice);
        DepreciationJobNoticeDTO result = depreciationJobNoticeMapper.toDto(depreciationJobNotice);
        depreciationJobNoticeSearchRepository.save(depreciationJobNotice);
        return result;
    }

    @Override
    public Optional<DepreciationJobNoticeDTO> partialUpdate(DepreciationJobNoticeDTO depreciationJobNoticeDTO) {
        log.debug("Request to partially update DepreciationJobNotice : {}", depreciationJobNoticeDTO);

        return depreciationJobNoticeRepository
            .findById(depreciationJobNoticeDTO.getId())
            .map(existingDepreciationJobNotice -> {
                depreciationJobNoticeMapper.partialUpdate(existingDepreciationJobNotice, depreciationJobNoticeDTO);

                return existingDepreciationJobNotice;
            })
            .map(depreciationJobNoticeRepository::save)
            .map(savedDepreciationJobNotice -> {
                depreciationJobNoticeSearchRepository.save(savedDepreciationJobNotice);

                return savedDepreciationJobNotice;
            })
            .map(depreciationJobNoticeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationJobNoticeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepreciationJobNotices");
        return depreciationJobNoticeRepository.findAll(pageable).map(depreciationJobNoticeMapper::toDto);
    }

    public Page<DepreciationJobNoticeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return depreciationJobNoticeRepository.findAllWithEagerRelationships(pageable).map(depreciationJobNoticeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepreciationJobNoticeDTO> findOne(Long id) {
        log.debug("Request to get DepreciationJobNotice : {}", id);
        return depreciationJobNoticeRepository.findOneWithEagerRelationships(id).map(depreciationJobNoticeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepreciationJobNotice : {}", id);
        depreciationJobNoticeRepository.deleteById(id);
        depreciationJobNoticeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationJobNoticeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepreciationJobNotices for query {}", query);
        return depreciationJobNoticeSearchRepository.search(query, pageable).map(depreciationJobNoticeMapper::toDto);
    }
}
