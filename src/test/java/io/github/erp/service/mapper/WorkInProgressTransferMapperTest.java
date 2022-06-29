package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkInProgressTransferMapperTest {

    private WorkInProgressTransferMapper workInProgressTransferMapper;

    @BeforeEach
    public void setUp() {
        workInProgressTransferMapper = new WorkInProgressTransferMapperImpl();
    }
}
