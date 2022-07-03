package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UniversallyUniqueMappingMapperTest {

    private UniversallyUniqueMappingMapper universallyUniqueMappingMapper;

    @BeforeEach
    public void setUp() {
        universallyUniqueMappingMapper = new UniversallyUniqueMappingMapperImpl();
    }
}
