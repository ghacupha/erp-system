package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.AgencyNotice;
import io.github.erp.repository.AgencyNoticeRepository;
import io.github.erp.repository.search.AgencyNoticeSearchRepository;
import io.github.erp.service.AgencyNoticeService;
import io.github.erp.service.dto.AgencyNoticeDTO;
import io.github.erp.service.mapper.AgencyNoticeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AgencyNotice}.
 */
@Service
@Transactional
public class AgencyNoticeServiceImpl implements AgencyNoticeService {

    private final Logger log = LoggerFactory.getLogger(AgencyNoticeServiceImpl.class);

    private final AgencyNoticeRepository agencyNoticeRepository;

    private final AgencyNoticeMapper agencyNoticeMapper;

    private final AgencyNoticeSearchRepository agencyNoticeSearchRepository;

    public AgencyNoticeServiceImpl(
        AgencyNoticeRepository agencyNoticeRepository,
        AgencyNoticeMapper agencyNoticeMapper,
        AgencyNoticeSearchRepository agencyNoticeSearchRepository
    ) {
        this.agencyNoticeRepository = agencyNoticeRepository;
        this.agencyNoticeMapper = agencyNoticeMapper;
        this.agencyNoticeSearchRepository = agencyNoticeSearchRepository;
    }

    @Override
    public AgencyNoticeDTO save(AgencyNoticeDTO agencyNoticeDTO) {
        log.debug("Request to save AgencyNotice : {}", agencyNoticeDTO);
        AgencyNotice agencyNotice = agencyNoticeMapper.toEntity(agencyNoticeDTO);
        agencyNotice = agencyNoticeRepository.save(agencyNotice);
        AgencyNoticeDTO result = agencyNoticeMapper.toDto(agencyNotice);
        agencyNoticeSearchRepository.save(agencyNotice);
        return result;
    }

    @Override
    public Optional<AgencyNoticeDTO> partialUpdate(AgencyNoticeDTO agencyNoticeDTO) {
        log.debug("Request to partially update AgencyNotice : {}", agencyNoticeDTO);

        return agencyNoticeRepository
            .findById(agencyNoticeDTO.getId())
            .map(existingAgencyNotice -> {
                agencyNoticeMapper.partialUpdate(existingAgencyNotice, agencyNoticeDTO);

                return existingAgencyNotice;
            })
            .map(agencyNoticeRepository::save)
            .map(savedAgencyNotice -> {
                agencyNoticeSearchRepository.save(savedAgencyNotice);

                return savedAgencyNotice;
            })
            .map(agencyNoticeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgencyNoticeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AgencyNotices");
        return agencyNoticeRepository.findAll(pageable).map(agencyNoticeMapper::toDto);
    }

    public Page<AgencyNoticeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return agencyNoticeRepository.findAllWithEagerRelationships(pageable).map(agencyNoticeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgencyNoticeDTO> findOne(Long id) {
        log.debug("Request to get AgencyNotice : {}", id);
        return agencyNoticeRepository.findOneWithEagerRelationships(id).map(agencyNoticeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgencyNotice : {}", id);
        agencyNoticeRepository.deleteById(id);
        agencyNoticeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgencyNoticeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AgencyNotices for query {}", query);
        return agencyNoticeSearchRepository.search(query, pageable).map(agencyNoticeMapper::toDto);
    }
}
