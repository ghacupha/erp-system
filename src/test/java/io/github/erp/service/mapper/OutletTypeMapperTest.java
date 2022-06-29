package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutletTypeMapperTest {

    private OutletTypeMapper outletTypeMapper;

    @BeforeEach
    public void setUp() {
        outletTypeMapper = new OutletTypeMapperImpl();
    }
}
