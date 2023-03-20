package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProcessStatusMapperTest {

    private ProcessStatusMapper processStatusMapper;

    @BeforeEach
    public void setUp() {
        processStatusMapper = new ProcessStatusMapperImpl();
    }
}
