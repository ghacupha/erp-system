package io.github.erp.internal.service;

/*-
 * Erp System - Mark III No 2 (Caleb Series) Server ver 0.1.2-SNAPSHOT
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
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.internal.repository.InternalUniversallyUniqueMappingRepository;
import io.github.erp.repository.UniversallyUniqueMappingRepository;
import io.github.erp.repository.search.UniversallyUniqueMappingSearchRepository;
import io.github.erp.service.impl.UniversallyUniqueMappingServiceImpl;
import io.github.erp.service.mapper.UniversallyUniqueMappingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service("internalUniversallyUniqueMappingService")
@Transactional
public class InternalUniversallyUniqueMappingServiceImpl
    extends UniversallyUniqueMappingServiceImpl
    implements InternalUniversallyUniqueMappingService {

    private final InternalUniversallyUniqueMappingRepository internalUUMappingRepository;

    public InternalUniversallyUniqueMappingServiceImpl(
        InternalUniversallyUniqueMappingRepository internalUUMappingRepository,
        UniversallyUniqueMappingRepository universallyUniqueMappingRepository,
        UniversallyUniqueMappingMapper universallyUniqueMappingMapper,
        UniversallyUniqueMappingSearchRepository universallyUniqueMappingSearchRepository) {
        super(universallyUniqueMappingRepository, universallyUniqueMappingMapper, universallyUniqueMappingSearchRepository);

        this.internalUUMappingRepository = internalUUMappingRepository;
    }

    @Override
    public Optional<UniversallyUniqueMapping> getMapping(String universalKey) {
        AtomicReference<UniversallyUniqueMapping> val = new AtomicReference<>();
        internalUUMappingRepository.findByUniversalKeyEquals(universalKey).ifPresentOrElse(
            val::set,
            () -> {throw new ConfigurationMappingNotFoundException("Sorry, couldn't find mapping for " + universalKey + " Are sure you had that configured?"); }
        );
        return Optional.of(val.get());
    }
}
