package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkInProgressRegistrationMapperTest {

    private WorkInProgressRegistrationMapper workInProgressRegistrationMapper;

    @BeforeEach
    public void setUp() {
        workInProgressRegistrationMapper = new WorkInProgressRegistrationMapperImpl();
    }
}
