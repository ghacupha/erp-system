package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepreciationEntryMapperTest {

    private DepreciationEntryMapper depreciationEntryMapper;

    @BeforeEach
    public void setUp() {
        depreciationEntryMapper = new DepreciationEntryMapperImpl();
    }
}
