package io.github.erp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentCalculationMapperTest {

    private PaymentCalculationMapper paymentCalculationMapper;

    @BeforeEach
    public void setUp() {
        paymentCalculationMapper = new PaymentCalculationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(paymentCalculationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(paymentCalculationMapper.fromId(null)).isNull();
    }
}
