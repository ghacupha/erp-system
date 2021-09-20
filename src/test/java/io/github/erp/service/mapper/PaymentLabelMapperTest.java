package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentLabelMapperTest {

    private PaymentLabelMapper paymentLabelMapper;

    @BeforeEach
    public void setUp() {
        paymentLabelMapper = new PaymentLabelMapperImpl();
    }
}
