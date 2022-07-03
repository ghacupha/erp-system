package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutletStatusMapperTest {

    private OutletStatusMapper outletStatusMapper;

    @BeforeEach
    public void setUp() {
        outletStatusMapper = new OutletStatusMapperImpl();
    }
}
