package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlgorithmMapperTest {

    private AlgorithmMapper algorithmMapper;

    @BeforeEach
    public void setUp() {
        algorithmMapper = new AlgorithmMapperImpl();
    }
}
