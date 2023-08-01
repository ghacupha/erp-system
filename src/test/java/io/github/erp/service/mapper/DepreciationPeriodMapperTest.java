package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepreciationPeriodMapperTest {

    private DepreciationPeriodMapper depreciationPeriodMapper;

    @BeforeEach
    public void setUp() {
        depreciationPeriodMapper = new DepreciationPeriodMapperImpl();
    }
}
