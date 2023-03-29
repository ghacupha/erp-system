package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.repository.LeaseLiabilityScheduleItemRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleItemSearchRepository;
import io.github.erp.service.LeaseLiabilityScheduleItemService;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityScheduleItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaseLiabilityScheduleItem}.
 */
@Service
@Transactional
public class LeaseLiabilityScheduleItemServiceImpl implements LeaseLiabilityScheduleItemService {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleItemServiceImpl.class);

    private final LeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository;

    private final LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper;

    private final LeaseLiabilityScheduleItemSearchRepository leaseLiabilityScheduleItemSearchRepository;

    public LeaseLiabilityScheduleItemServiceImpl(
        LeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository,
        LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper,
        LeaseLiabilityScheduleItemSearchRepository leaseLiabilityScheduleItemSearchRepository
    ) {
        this.leaseLiabilityScheduleItemRepository = leaseLiabilityScheduleItemRepository;
        this.leaseLiabilityScheduleItemMapper = leaseLiabilityScheduleItemMapper;
        this.leaseLiabilityScheduleItemSearchRepository = leaseLiabilityScheduleItemSearchRepository;
    }

    @Override
    public LeaseLiabilityScheduleItemDTO save(LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO) {
        log.debug("Request to save LeaseLiabilityScheduleItem : {}", leaseLiabilityScheduleItemDTO);
        LeaseLiabilityScheduleItem leaseLiabilityScheduleItem = leaseLiabilityScheduleItemMapper.toEntity(leaseLiabilityScheduleItemDTO);
        leaseLiabilityScheduleItem = leaseLiabilityScheduleItemRepository.save(leaseLiabilityScheduleItem);
        LeaseLiabilityScheduleItemDTO result = leaseLiabilityScheduleItemMapper.toDto(leaseLiabilityScheduleItem);
        leaseLiabilityScheduleItemSearchRepository.save(leaseLiabilityScheduleItem);
        return result;
    }

    @Override
    public Optional<LeaseLiabilityScheduleItemDTO> partialUpdate(LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO) {
        log.debug("Request to partially update LeaseLiabilityScheduleItem : {}", leaseLiabilityScheduleItemDTO);

        return leaseLiabilityScheduleItemRepository
            .findById(leaseLiabilityScheduleItemDTO.getId())
            .map(existingLeaseLiabilityScheduleItem -> {
                leaseLiabilityScheduleItemMapper.partialUpdate(existingLeaseLiabilityScheduleItem, leaseLiabilityScheduleItemDTO);

                return existingLeaseLiabilityScheduleItem;
            })
            .map(leaseLiabilityScheduleItemRepository::save)
            .map(savedLeaseLiabilityScheduleItem -> {
                leaseLiabilityScheduleItemSearchRepository.save(savedLeaseLiabilityScheduleItem);

                return savedLeaseLiabilityScheduleItem;
            })
            .map(leaseLiabilityScheduleItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityScheduleItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilityScheduleItems");
        return leaseLiabilityScheduleItemRepository.findAll(pageable).map(leaseLiabilityScheduleItemMapper::toDto);
    }

    public Page<LeaseLiabilityScheduleItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return leaseLiabilityScheduleItemRepository.findAllWithEagerRelationships(pageable).map(leaseLiabilityScheduleItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityScheduleItemDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiabilityScheduleItem : {}", id);
        return leaseLiabilityScheduleItemRepository.findOneWithEagerRelationships(id).map(leaseLiabilityScheduleItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiabilityScheduleItem : {}", id);
        leaseLiabilityScheduleItemRepository.deleteById(id);
        leaseLiabilityScheduleItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityScheduleItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilityScheduleItems for query {}", query);
        return leaseLiabilityScheduleItemSearchRepository.search(query, pageable).map(leaseLiabilityScheduleItemMapper::toDto);
    }
}
