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

import io.github.erp.domain.MerchantType;
import io.github.erp.repository.MerchantTypeRepository;
import io.github.erp.repository.search.MerchantTypeSearchRepository;
import io.github.erp.service.MerchantTypeService;
import io.github.erp.service.dto.MerchantTypeDTO;
import io.github.erp.service.mapper.MerchantTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MerchantType}.
 */
@Service
@Transactional
public class MerchantTypeServiceImpl implements MerchantTypeService {

    private final Logger log = LoggerFactory.getLogger(MerchantTypeServiceImpl.class);

    private final MerchantTypeRepository merchantTypeRepository;

    private final MerchantTypeMapper merchantTypeMapper;

    private final MerchantTypeSearchRepository merchantTypeSearchRepository;

    public MerchantTypeServiceImpl(
        MerchantTypeRepository merchantTypeRepository,
        MerchantTypeMapper merchantTypeMapper,
        MerchantTypeSearchRepository merchantTypeSearchRepository
    ) {
        this.merchantTypeRepository = merchantTypeRepository;
        this.merchantTypeMapper = merchantTypeMapper;
        this.merchantTypeSearchRepository = merchantTypeSearchRepository;
    }

    @Override
    public MerchantTypeDTO save(MerchantTypeDTO merchantTypeDTO) {
        log.debug("Request to save MerchantType : {}", merchantTypeDTO);
        MerchantType merchantType = merchantTypeMapper.toEntity(merchantTypeDTO);
        merchantType = merchantTypeRepository.save(merchantType);
        MerchantTypeDTO result = merchantTypeMapper.toDto(merchantType);
        merchantTypeSearchRepository.save(merchantType);
        return result;
    }

    @Override
    public Optional<MerchantTypeDTO> partialUpdate(MerchantTypeDTO merchantTypeDTO) {
        log.debug("Request to partially update MerchantType : {}", merchantTypeDTO);

        return merchantTypeRepository
            .findById(merchantTypeDTO.getId())
            .map(existingMerchantType -> {
                merchantTypeMapper.partialUpdate(existingMerchantType, merchantTypeDTO);

                return existingMerchantType;
            })
            .map(merchantTypeRepository::save)
            .map(savedMerchantType -> {
                merchantTypeSearchRepository.save(savedMerchantType);

                return savedMerchantType;
            })
            .map(merchantTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MerchantTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MerchantTypes");
        return merchantTypeRepository.findAll(pageable).map(merchantTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MerchantTypeDTO> findOne(Long id) {
        log.debug("Request to get MerchantType : {}", id);
        return merchantTypeRepository.findById(id).map(merchantTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MerchantType : {}", id);
        merchantTypeRepository.deleteById(id);
        merchantTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MerchantTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MerchantTypes for query {}", query);
        return merchantTypeSearchRepository.search(query, pageable).map(merchantTypeMapper::toDto);
    }
}
