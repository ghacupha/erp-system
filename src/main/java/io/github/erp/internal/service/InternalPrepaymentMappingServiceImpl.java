package io.github.erp.internal.service;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.erp.domain.PrepaymentMapping;
import io.github.erp.internal.repository.InternalPrepaymentMappingRepository;
import io.github.erp.repository.PrepaymentMappingRepository;
import io.github.erp.repository.search.PrepaymentMappingSearchRepository;
import io.github.erp.service.dto.PrepaymentMappingDTO;
import io.github.erp.service.impl.PrepaymentMappingServiceImpl;
import io.github.erp.service.mapper.PrepaymentMappingMapper;

/**
 * Custom service for prepayment-mapping with access to the internal-prepayment-mapping-repository
 */
@Service("internalPrepaymentMappingService")
@Transactional
public class InternalPrepaymentMappingServiceImpl
extends PrepaymentMappingServiceImpl
implements InternalPrepaymentMappingService {

    private final PrepaymentMappingMapper prepaymentMappingMapper;
    private final InternalPrepaymentMappingRepository internalPrepaymentMappingRepository;

    public InternalPrepaymentMappingServiceImpl(
        InternalPrepaymentMappingRepository internalPrepaymentMappingRepository,
        PrepaymentMappingRepository prepaymentMappingRepository,
        PrepaymentMappingMapper prepaymentMappingMapper,
        PrepaymentMappingSearchRepository prepaymentMappingSearchRepository) {
        super(prepaymentMappingRepository, prepaymentMappingMapper, prepaymentMappingSearchRepository);
       this.internalPrepaymentMappingRepository = internalPrepaymentMappingRepository;
       this.prepaymentMappingMapper = prepaymentMappingMapper;

    }

    @Override
    public Optional<PrepaymentMappingDTO> getMapping(String parameterKey) {
        AtomicReference<PrepaymentMapping> val = new AtomicReference<>();
        internalPrepaymentMappingRepository.findByParameterKeyEquals(parameterKey).ifPresentOrElse(
            val::set,
            () -> {throw new ConfigurationMappingNotFoundException("Sorry, couldn't find mapping for " + parameterKey + " Are sure you have that configured?"); }
        );
        return Optional.of(prepaymentMappingMapper.toDto(val.get()));
    }

}
