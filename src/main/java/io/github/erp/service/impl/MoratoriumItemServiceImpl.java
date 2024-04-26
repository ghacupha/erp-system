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

import io.github.erp.domain.MoratoriumItem;
import io.github.erp.repository.MoratoriumItemRepository;
import io.github.erp.repository.search.MoratoriumItemSearchRepository;
import io.github.erp.service.MoratoriumItemService;
import io.github.erp.service.dto.MoratoriumItemDTO;
import io.github.erp.service.mapper.MoratoriumItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MoratoriumItem}.
 */
@Service
@Transactional
public class MoratoriumItemServiceImpl implements MoratoriumItemService {

    private final Logger log = LoggerFactory.getLogger(MoratoriumItemServiceImpl.class);

    private final MoratoriumItemRepository moratoriumItemRepository;

    private final MoratoriumItemMapper moratoriumItemMapper;

    private final MoratoriumItemSearchRepository moratoriumItemSearchRepository;

    public MoratoriumItemServiceImpl(
        MoratoriumItemRepository moratoriumItemRepository,
        MoratoriumItemMapper moratoriumItemMapper,
        MoratoriumItemSearchRepository moratoriumItemSearchRepository
    ) {
        this.moratoriumItemRepository = moratoriumItemRepository;
        this.moratoriumItemMapper = moratoriumItemMapper;
        this.moratoriumItemSearchRepository = moratoriumItemSearchRepository;
    }

    @Override
    public MoratoriumItemDTO save(MoratoriumItemDTO moratoriumItemDTO) {
        log.debug("Request to save MoratoriumItem : {}", moratoriumItemDTO);
        MoratoriumItem moratoriumItem = moratoriumItemMapper.toEntity(moratoriumItemDTO);
        moratoriumItem = moratoriumItemRepository.save(moratoriumItem);
        MoratoriumItemDTO result = moratoriumItemMapper.toDto(moratoriumItem);
        moratoriumItemSearchRepository.save(moratoriumItem);
        return result;
    }

    @Override
    public Optional<MoratoriumItemDTO> partialUpdate(MoratoriumItemDTO moratoriumItemDTO) {
        log.debug("Request to partially update MoratoriumItem : {}", moratoriumItemDTO);

        return moratoriumItemRepository
            .findById(moratoriumItemDTO.getId())
            .map(existingMoratoriumItem -> {
                moratoriumItemMapper.partialUpdate(existingMoratoriumItem, moratoriumItemDTO);

                return existingMoratoriumItem;
            })
            .map(moratoriumItemRepository::save)
            .map(savedMoratoriumItem -> {
                moratoriumItemSearchRepository.save(savedMoratoriumItem);

                return savedMoratoriumItem;
            })
            .map(moratoriumItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoratoriumItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MoratoriumItems");
        return moratoriumItemRepository.findAll(pageable).map(moratoriumItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoratoriumItemDTO> findOne(Long id) {
        log.debug("Request to get MoratoriumItem : {}", id);
        return moratoriumItemRepository.findById(id).map(moratoriumItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MoratoriumItem : {}", id);
        moratoriumItemRepository.deleteById(id);
        moratoriumItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoratoriumItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MoratoriumItems for query {}", query);
        return moratoriumItemSearchRepository.search(query, pageable).map(moratoriumItemMapper::toDto);
    }
}
