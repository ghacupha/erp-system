package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentCalculationMapperTest {

    private PaymentCalculationMapper paymentCalculationMapper;

    @BeforeEach
    public void setUp() {
        paymentCalculationMapper = new PaymentCalculationMapperImpl();
    }
}
