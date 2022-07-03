package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceOutletMapperTest {

    private ServiceOutletMapper serviceOutletMapper;

    @BeforeEach
    public void setUp() {
        serviceOutletMapper = new ServiceOutletMapperImpl();
    }
}
