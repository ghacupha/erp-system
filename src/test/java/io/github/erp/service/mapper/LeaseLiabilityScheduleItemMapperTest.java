package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaseLiabilityScheduleItemMapperTest {

    private LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper;

    @BeforeEach
    public void setUp() {
        leaseLiabilityScheduleItemMapper = new LeaseLiabilityScheduleItemMapperImpl();
    }
}
