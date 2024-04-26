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

import io.github.erp.domain.ShareholderType;
import io.github.erp.repository.ShareholderTypeRepository;
import io.github.erp.repository.search.ShareholderTypeSearchRepository;
import io.github.erp.service.ShareholderTypeService;
import io.github.erp.service.dto.ShareholderTypeDTO;
import io.github.erp.service.mapper.ShareholderTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShareholderType}.
 */
@Service
@Transactional
public class ShareholderTypeServiceImpl implements ShareholderTypeService {

    private final Logger log = LoggerFactory.getLogger(ShareholderTypeServiceImpl.class);

    private final ShareholderTypeRepository shareholderTypeRepository;

    private final ShareholderTypeMapper shareholderTypeMapper;

    private final ShareholderTypeSearchRepository shareholderTypeSearchRepository;

    public ShareholderTypeServiceImpl(
        ShareholderTypeRepository shareholderTypeRepository,
        ShareholderTypeMapper shareholderTypeMapper,
        ShareholderTypeSearchRepository shareholderTypeSearchRepository
    ) {
        this.shareholderTypeRepository = shareholderTypeRepository;
        this.shareholderTypeMapper = shareholderTypeMapper;
        this.shareholderTypeSearchRepository = shareholderTypeSearchRepository;
    }

    @Override
    public ShareholderTypeDTO save(ShareholderTypeDTO shareholderTypeDTO) {
        log.debug("Request to save ShareholderType : {}", shareholderTypeDTO);
        ShareholderType shareholderType = shareholderTypeMapper.toEntity(shareholderTypeDTO);
        shareholderType = shareholderTypeRepository.save(shareholderType);
        ShareholderTypeDTO result = shareholderTypeMapper.toDto(shareholderType);
        shareholderTypeSearchRepository.save(shareholderType);
        return result;
    }

    @Override
    public Optional<ShareholderTypeDTO> partialUpdate(ShareholderTypeDTO shareholderTypeDTO) {
        log.debug("Request to partially update ShareholderType : {}", shareholderTypeDTO);

        return shareholderTypeRepository
            .findById(shareholderTypeDTO.getId())
            .map(existingShareholderType -> {
                shareholderTypeMapper.partialUpdate(existingShareholderType, shareholderTypeDTO);

                return existingShareholderType;
            })
            .map(shareholderTypeRepository::save)
            .map(savedShareholderType -> {
                shareholderTypeSearchRepository.save(savedShareholderType);

                return savedShareholderType;
            })
            .map(shareholderTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShareholderTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShareholderTypes");
        return shareholderTypeRepository.findAll(pageable).map(shareholderTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShareholderTypeDTO> findOne(Long id) {
        log.debug("Request to get ShareholderType : {}", id);
        return shareholderTypeRepository.findById(id).map(shareholderTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShareholderType : {}", id);
        shareholderTypeRepository.deleteById(id);
        shareholderTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShareholderTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ShareholderTypes for query {}", query);
        return shareholderTypeSearchRepository.search(query, pageable).map(shareholderTypeMapper::toDto);
    }
}
