package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemModuleMapperTest {

    private SystemModuleMapper systemModuleMapper;

    @BeforeEach
    public void setUp() {
        systemModuleMapper = new SystemModuleMapperImpl();
    }
}
