package io.github.erp.internal.service;

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
    public Optional<String> getMapping(String universalKey) {
        AtomicReference<String> val = new AtomicReference<>("");
        internalUUMappingRepository.findByUniversalKeyEquals(universalKey).ifPresentOrElse(
            map -> { val.set(map.getMappedValue()); },
            () -> {throw new RuntimeException("Sorry, couldn't find mapping for " + universalKey + " Are sure you that configured?"); }
        );
        return Optional.of(val.get());
    }
}
