package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrepaymentMappingMapperTest {

    private PrepaymentMappingMapper prepaymentMappingMapper;

    @BeforeEach
    public void setUp() {
        prepaymentMappingMapper = new PrepaymentMappingMapperImpl();
    }
}
