package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepreciationMethodMapperTest {

    private DepreciationMethodMapper depreciationMethodMapper;

    @BeforeEach
    public void setUp() {
        depreciationMethodMapper = new DepreciationMethodMapperImpl();
    }
}
