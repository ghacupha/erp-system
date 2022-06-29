package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemContentTypeMapperTest {

    private SystemContentTypeMapper systemContentTypeMapper;

    @BeforeEach
    public void setUp() {
        systemContentTypeMapper = new SystemContentTypeMapperImpl();
    }
}
