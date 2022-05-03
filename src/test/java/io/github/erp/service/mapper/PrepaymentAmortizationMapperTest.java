package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrepaymentAmortizationMapperTest {

    private PrepaymentAmortizationMapper prepaymentAmortizationMapper;

    @BeforeEach
    public void setUp() {
        prepaymentAmortizationMapper = new PrepaymentAmortizationMapperImpl();
    }
}
