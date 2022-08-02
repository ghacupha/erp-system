package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AmortizationSequenceMapperTest {

    private AmortizationSequenceMapper amortizationSequenceMapper;

    @BeforeEach
    public void setUp() {
        amortizationSequenceMapper = new AmortizationSequenceMapperImpl();
    }
}
