package io.github.erp.internal.service.rou;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.LeasePeriod;
import io.github.erp.internal.repository.InternalLeasePeriodRepository;
import io.github.erp.repository.search.LeasePeriodSearchRepository;
import io.github.erp.service.RouModelMetadataService;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.dto.LeasePeriodDTO;
import io.github.erp.service.dto.RouModelMetadataDTO;
import io.github.erp.service.mapper.LeasePeriodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link LeasePeriod}.
 */
@Service
@Transactional
public class InternalLeasePeriodServiceImpl implements InternalLeasePeriodService {

    private final Logger log = LoggerFactory.getLogger(InternalLeasePeriodServiceImpl.class);

    private final InternalLeasePeriodRepository leasePeriodRepository;

    private final RouModelMetadataService rouModelMetadataService;

    private final LeasePeriodMapper leasePeriodMapper;

    private final LeasePeriodSearchRepository leasePeriodSearchRepository;

    public InternalLeasePeriodServiceImpl(
        InternalLeasePeriodRepository leasePeriodRepository,
        RouModelMetadataService rouModelMetadataService, LeasePeriodMapper leasePeriodMapper,
        LeasePeriodSearchRepository leasePeriodSearchRepository
    ) {
        this.leasePeriodRepository = leasePeriodRepository;
        this.rouModelMetadataService = rouModelMetadataService;
        this.leasePeriodMapper = leasePeriodMapper;
        this.leasePeriodSearchRepository = leasePeriodSearchRepository;
    }

    @Override
    public LeasePeriodDTO save(LeasePeriodDTO leasePeriodDTO) {
        log.debug("Request to save LeasePeriod : {}", leasePeriodDTO);
        LeasePeriod leasePeriod = leasePeriodMapper.toEntity(leasePeriodDTO);
        leasePeriod = leasePeriodRepository.save(leasePeriod);
        LeasePeriodDTO result = leasePeriodMapper.toDto(leasePeriod);
        leasePeriodSearchRepository.save(leasePeriod);
        return result;
    }

    @Override
    public Optional<LeasePeriodDTO> partialUpdate(LeasePeriodDTO leasePeriodDTO) {
        log.debug("Request to partially update LeasePeriod : {}", leasePeriodDTO);

        return leasePeriodRepository
            .findById(leasePeriodDTO.getId())
            .map(existingLeasePeriod -> {
                leasePeriodMapper.partialUpdate(existingLeasePeriod, leasePeriodDTO);

                return existingLeasePeriod;
            })
            .map(leasePeriodRepository::save)
            .map(savedLeasePeriod -> {
                leasePeriodSearchRepository.save(savedLeasePeriod);

                return savedLeasePeriod;
            })
            .map(leasePeriodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeasePeriodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeasePeriods");
        return leasePeriodRepository.findAll(pageable).map(leasePeriodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeasePeriodDTO> findOne(Long id) {
        log.debug("Request to get LeasePeriod : {}", id);
        return leasePeriodRepository.findById(id).map(leasePeriodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeasePeriod : {}", id);
        leasePeriodRepository.deleteById(id);
        leasePeriodSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeasePeriodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeasePeriods for query {}", query);
        return leasePeriodSearchRepository.search(query, pageable).map(leasePeriodMapper::toDto);
    }

    /**
     * Get the initial leasePeriod in which the commencement-date belongs.
     * The appropriate initial leasePeriod is one in whose duration the
     * commencementDate is contained
     *
     * @param commencementDate of the leaseModelMetadata
     * @return the entity.
     */
    @Override
    public Optional<LeasePeriodDTO> findInitialPeriod(LocalDate commencementDate) {
        return leasePeriodRepository.findInitialPeriod(commencementDate)
            .map(leasePeriodMapper::toDto);
    }

    /**
     * Get the initial leasePeriod in which the commencement-date belongs.
     * The appropriate initial leasePeriod is one in whose duration the
     * commencementDate is contained. The query then fetches the subsequent
     * periods until the lease-term-periods are attained
     *
     * @param modelMetadata This is the lease item for which we need to obtain lease-periods
     * @return the entity.
     */
    @Override
    public Optional<List<LeasePeriodDTO>> findLeasePeriods(RouModelMetadataDTO modelMetadata) {

        return rouModelMetadataService.findOne(modelMetadata.getId())
            .flatMap(metadata -> leasePeriodRepository.findLeaseDepreciationPeriods(metadata.getCommencementDate(), metadata.getLeaseTermPeriods())
                .map(leasePeriodMapper::toDto));
    }

    /**
     * Get the initial leasePeriod in which the commencement-date belongs.
     * The appropriate initial leasePeriod is one in whose duration the
     * commencementDate is contained. The query then fetches the subsequent
     * periods until the lease-term-periods are attained
     *
     * @param leaseContract This is the lease item for which we need to obtain lease-periods
     * @return the entity.
     */
    @Override
    public Optional<List<LeasePeriodDTO>> findLeasePeriods(IFRS16LeaseContractDTO leaseContract) {

        var ref = new Object() {
            Optional<List<LeasePeriodDTO>> periodList = Optional.empty();
        };

        leasePeriodRepository.findInitialPeriod(leaseContract.getId()).ifPresent(commencementDate -> {

            leasePeriodRepository.findNumberOfLeaseTermPeriods(leaseContract.getId()).ifPresent(periods -> {

                ref.periodList = leasePeriodRepository.findLeaseAmortizationPeriods(commencementDate.getStartDate(), periods)
                    .map(leasePeriodMapper::toDto);
            });
        });

        return ref.periodList;
    }
}
