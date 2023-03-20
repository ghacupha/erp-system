package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AmortizationRecurrenceMapperTest {

    private AmortizationRecurrenceMapper amortizationRecurrenceMapper;

    @BeforeEach
    public void setUp() {
        amortizationRecurrenceMapper = new AmortizationRecurrenceMapperImpl();
    }
}
