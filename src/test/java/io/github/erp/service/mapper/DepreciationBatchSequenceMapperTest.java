package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepreciationBatchSequenceMapperTest {

    private DepreciationBatchSequenceMapper depreciationBatchSequenceMapper;

    @BeforeEach
    public void setUp() {
        depreciationBatchSequenceMapper = new DepreciationBatchSequenceMapperImpl();
    }
}
