package io.github.erp.internal.service.rou;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.RouDepreciationEntry;
import io.github.erp.internal.repository.InternalRouDepreciationEntryRepository;
import io.github.erp.repository.RouDepreciationEntryRepository;
import io.github.erp.repository.search.RouDepreciationEntrySearchRepository;
import io.github.erp.service.dto.LeasePeriodDTO;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.dto.RouModelMetadataDTO;
import io.github.erp.service.mapper.RouDepreciationEntryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.erp.erp.assets.depreciation.calculation.DepreciationConstants.ZERO;

/**
 * Service Implementation for managing {@link RouDepreciationEntry}.
 */
@Service
@Transactional
public class InternalRouDepreciationEntryServiceImpl implements InternalRouDepreciationEntryService {

    private final Logger log = LoggerFactory.getLogger(InternalRouDepreciationEntryServiceImpl.class);

    private final InternalRouDepreciationEntryRepository rouDepreciationEntryRepository;

    private final RouDepreciationEntryMapper rouDepreciationEntryMapper;

    private final RouDepreciationEntrySearchRepository rouDepreciationEntrySearchRepository;

    private final InternalLeasePeriodService internalLeasePeriodService;

    private final InternalRouModelMetadataService internalRouModelMetadataService;

    public InternalRouDepreciationEntryServiceImpl(
        InternalRouDepreciationEntryRepository rouDepreciationEntryRepository,
        RouDepreciationEntryMapper rouDepreciationEntryMapper,
        RouDepreciationEntrySearchRepository rouDepreciationEntrySearchRepository,
        InternalLeasePeriodService internalLeasePeriodService, InternalRouModelMetadataService internalRouModelMetadataService) {
        this.rouDepreciationEntryRepository = rouDepreciationEntryRepository;
        this.rouDepreciationEntryMapper = rouDepreciationEntryMapper;
        this.rouDepreciationEntrySearchRepository = rouDepreciationEntrySearchRepository;
        this.internalLeasePeriodService = internalLeasePeriodService;
        this.internalRouModelMetadataService = internalRouModelMetadataService;
    }

    @Override
    public RouDepreciationEntryDTO save(RouDepreciationEntryDTO rouDepreciationEntryDTO) {
        log.debug("Request to save RouDepreciationEntry : {}", rouDepreciationEntryDTO);
        RouDepreciationEntry rouDepreciationEntry = rouDepreciationEntryMapper.toEntity(rouDepreciationEntryDTO);
        rouDepreciationEntry = rouDepreciationEntryRepository.save(rouDepreciationEntry);
        RouDepreciationEntryDTO result = rouDepreciationEntryMapper.toDto(rouDepreciationEntry);
        rouDepreciationEntrySearchRepository.save(rouDepreciationEntry);
        return result;
    }

    @Override
    public Optional<RouDepreciationEntryDTO> partialUpdate(RouDepreciationEntryDTO rouDepreciationEntryDTO) {
        log.debug("Request to partially update RouDepreciationEntry : {}", rouDepreciationEntryDTO);

        return rouDepreciationEntryRepository
            .findById(rouDepreciationEntryDTO.getId())
            .map(existingRouDepreciationEntry -> {
                rouDepreciationEntryMapper.partialUpdate(existingRouDepreciationEntry, rouDepreciationEntryDTO);

                return existingRouDepreciationEntry;
            })
            .map(rouDepreciationEntryRepository::save)
            .map(savedRouDepreciationEntry -> {
                rouDepreciationEntrySearchRepository.save(savedRouDepreciationEntry);

                return savedRouDepreciationEntry;
            })
            .map(rouDepreciationEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouDepreciationEntries");
        return rouDepreciationEntryRepository.findAll(pageable).map(rouDepreciationEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouDepreciationEntryDTO> findOne(Long id) {
        log.debug("Request to get RouDepreciationEntry : {}", id);
        return rouDepreciationEntryRepository.findById(id).map(rouDepreciationEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouDepreciationEntry : {}", id);
        rouDepreciationEntryRepository.deleteById(id);
        rouDepreciationEntrySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouDepreciationEntries for query {}", query);
        return rouDepreciationEntrySearchRepository.search(query, pageable).map(rouDepreciationEntryMapper::toDto);
    }

    /**
     * Save a rouDepreciationEntry.
     *
     * @param rouDepreciationEntryDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public List<RouDepreciationEntryDTO> save(List<RouDepreciationEntryDTO> rouDepreciationEntryDTO) {

        List<RouDepreciationEntry> rouDepreciationEntry = rouDepreciationEntryMapper.toEntity(rouDepreciationEntryDTO);

        rouDepreciationEntry = rouDepreciationEntryRepository.saveAllAndFlush(rouDepreciationEntry);

        return rouDepreciationEntryMapper.toDto(rouDepreciationEntry);

    }

    /**
     * Calculate the outstanding amount for the current depreciation entry
     *
     * @param entry The instance for outstanding amount calculation
     * @return Calculated outstanding amount
     */
    @Override
    public BigDecimal calculateOutstandingAmount(RouDepreciationEntryDTO entry) {

        log.debug("Request to calculate outstanding amount on entry {}", entry);

        AtomicReference<BigDecimal> outstandingAmount = new AtomicReference<>(BigDecimal.ZERO);

            // Fetch from db with all details
        findOne(entry.getId()).ifPresent(depreciation -> {

            RouModelMetadataDTO modelMetadataDTO = internalRouModelMetadataService.findOne(depreciation.getRouMetadata().getId()).orElseThrow();

            BigDecimal depreciationPerPeriod = modelMetadataDTO.getLeaseAmount().divide(BigDecimal.valueOf(modelMetadataDTO.getLeaseTermPeriods()), RoundingMode.HALF_EVEN).setScale(2, RoundingMode.HALF_EVEN);

            LeasePeriodDTO initialLeasePeriod = internalLeasePeriodService.findInitialPeriod(modelMetadataDTO.getCommencementDate())
                .orElseThrow();

            LeasePeriodDTO entryLeasePeriod = internalLeasePeriodService.findOne(depreciation.getLeasePeriod().getId()).orElseThrow();

            long lapsedPeriods = entryLeasePeriod.getSequenceNumber() - initialLeasePeriod.getSequenceNumber() + 1;

            BigDecimal accruedDepreciation = depreciationPerPeriod.multiply(BigDecimal.valueOf(lapsedPeriods));

            outstandingAmount.set(modelMetadataDTO.getLeaseAmount().subtract(accruedDepreciation).max(ZERO));

        });

        return outstandingAmount.get();
    }

    /**
     * This query looks for processed items whose outstanding amount has not yet been
     * updated. It's used in the batch sequence to further update the outstanding amounts
     *
     * @return list of entities for processing
     */
    @Override
    public Optional<List<RouDepreciationEntryDTO>> getOutstandingAmountItems() {

        return rouDepreciationEntryRepository.getOutstandingAmountItems(BigDecimal.TEN)
            .map(rouDepreciationEntryMapper::toDto);
    }

    /**
     * Fetch items that contain the provided batch-job-processor. These are items that have
     * been processed in a particular way through a batch process whose identifier is the same
     * as the parameter
     *
     * @param batchJobIdentifier Identifier of the batch process
     * @return List of items from a particular batch process instance
     */
    @Override
    public Optional<List<RouDepreciationEntryDTO>> getProcessedItems(UUID batchJobIdentifier) {
        return rouDepreciationEntryRepository.getProcessedItems(batchJobIdentifier)
            .map(rouDepreciationEntryMapper::toDto);
    }

    /**
     * Typically applied in a batch process to save a collection
     *
     * @param items Collection for persistence
     */
    @Override
    public void saveAll(List<? extends RouDepreciationEntryDTO> items) {

        rouDepreciationEntryRepository.saveAll(rouDepreciationEntryMapper.toEntity(new ArrayList<>(items)));
        rouDepreciationEntrySearchRepository.saveAll(rouDepreciationEntryMapper.toEntity(new ArrayList<>(items)));
    }

    @Override
    public Optional<Integer> countProcessedItems(UUID batchJobIdentifier) {

        return rouDepreciationEntryRepository.countRouDepreciationEntriesByBatchJobIdentifierEquals(batchJobIdentifier);
    }
}
