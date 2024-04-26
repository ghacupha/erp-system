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

import io.github.erp.domain.ShareHoldingFlag;
import io.github.erp.repository.ShareHoldingFlagRepository;
import io.github.erp.repository.search.ShareHoldingFlagSearchRepository;
import io.github.erp.service.ShareHoldingFlagService;
import io.github.erp.service.dto.ShareHoldingFlagDTO;
import io.github.erp.service.mapper.ShareHoldingFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShareHoldingFlag}.
 */
@Service
@Transactional
public class ShareHoldingFlagServiceImpl implements ShareHoldingFlagService {

    private final Logger log = LoggerFactory.getLogger(ShareHoldingFlagServiceImpl.class);

    private final ShareHoldingFlagRepository shareHoldingFlagRepository;

    private final ShareHoldingFlagMapper shareHoldingFlagMapper;

    private final ShareHoldingFlagSearchRepository shareHoldingFlagSearchRepository;

    public ShareHoldingFlagServiceImpl(
        ShareHoldingFlagRepository shareHoldingFlagRepository,
        ShareHoldingFlagMapper shareHoldingFlagMapper,
        ShareHoldingFlagSearchRepository shareHoldingFlagSearchRepository
    ) {
        this.shareHoldingFlagRepository = shareHoldingFlagRepository;
        this.shareHoldingFlagMapper = shareHoldingFlagMapper;
        this.shareHoldingFlagSearchRepository = shareHoldingFlagSearchRepository;
    }

    @Override
    public ShareHoldingFlagDTO save(ShareHoldingFlagDTO shareHoldingFlagDTO) {
        log.debug("Request to save ShareHoldingFlag : {}", shareHoldingFlagDTO);
        ShareHoldingFlag shareHoldingFlag = shareHoldingFlagMapper.toEntity(shareHoldingFlagDTO);
        shareHoldingFlag = shareHoldingFlagRepository.save(shareHoldingFlag);
        ShareHoldingFlagDTO result = shareHoldingFlagMapper.toDto(shareHoldingFlag);
        shareHoldingFlagSearchRepository.save(shareHoldingFlag);
        return result;
    }

    @Override
    public Optional<ShareHoldingFlagDTO> partialUpdate(ShareHoldingFlagDTO shareHoldingFlagDTO) {
        log.debug("Request to partially update ShareHoldingFlag : {}", shareHoldingFlagDTO);

        return shareHoldingFlagRepository
            .findById(shareHoldingFlagDTO.getId())
            .map(existingShareHoldingFlag -> {
                shareHoldingFlagMapper.partialUpdate(existingShareHoldingFlag, shareHoldingFlagDTO);

                return existingShareHoldingFlag;
            })
            .map(shareHoldingFlagRepository::save)
            .map(savedShareHoldingFlag -> {
                shareHoldingFlagSearchRepository.save(savedShareHoldingFlag);

                return savedShareHoldingFlag;
            })
            .map(shareHoldingFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShareHoldingFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShareHoldingFlags");
        return shareHoldingFlagRepository.findAll(pageable).map(shareHoldingFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShareHoldingFlagDTO> findOne(Long id) {
        log.debug("Request to get ShareHoldingFlag : {}", id);
        return shareHoldingFlagRepository.findById(id).map(shareHoldingFlagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShareHoldingFlag : {}", id);
        shareHoldingFlagRepository.deleteById(id);
        shareHoldingFlagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShareHoldingFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ShareHoldingFlags for query {}", query);
        return shareHoldingFlagSearchRepository.search(query, pageable).map(shareHoldingFlagMapper::toDto);
    }
}
